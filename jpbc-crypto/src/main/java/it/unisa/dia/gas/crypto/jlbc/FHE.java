package it.unisa.dia.gas.crypto.jlbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Polynomial;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyModField;
import it.unisa.dia.gas.plaf.jpbc.field.z.SymmetricZrField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;

import java.math.BigInteger;
import java.util.Random;

public class FHE {

    static Random random = new Random();

    static int n, k;
    static BigInteger q, tt;
    static int sigma;
    static double sigmaSquare;
    static int t, lub, L;
    static Element te;

    static PolyModField R, Rq, Rt;
    static Field Zq, Zqas;
    static FieldSampler sampler;

    static {
        n = 128;
        k = 8;
//        q = BigInteger.valueOf(1061093377);
        q = new BigInteger("802239503293259863326950493533761069");
        t = 1024;
        tt = BigInteger.valueOf(t);
        L = 1;
        sigma = 8;

        sampler = new FieldSampler(random, sigma);

        lub = (int) (Math.log(q.doubleValue()) / Math.log(t)) + 1;

        random.setSeed(1000);
    }

    public static void setup() {
        R = new PolyModField(random, new ZField(random), n);
        Rq = new PolyModField(random, (Zq = new SymmetricZrField(random, q)), n);

        Zqas = new ZrField(random, q);
        te = Zqas.newElement(t).invert();

    }

    static Element s, s1, a0, a1;

    static Element as[][][];
    static Element bs[][][];

    public static void keygen() {
        System.out.println("FHE.keygen");
        s = sampler.sampleDFromField(Rq);
        Element e = sampler.sampleDFromField(Rq);
        System.out.println("e = " + e);
        a1 = Rq.newRandomElement();
//        a1 = R.newElement().set(Rnq.newRandomElement());

        // a0 = -(a1 * s + t * e)
        a0 = a1.duplicate().mul(s).add(e.mul(t)).negate();
//        a0 = a1.duplicate().mul(s).negate();

//        System.out.println(a0.duplicate().add(a1.duplicate().mul(s)));

        // Keys for relinearization
        /*
        BigInteger bigT = BigInteger.valueOf(t);
        BigInteger tt = BigInteger.ONE;
        Element sSquare = s.duplicate().square();
        as = new Element[lub];
        bs = new Element[lub];
        for (int i = 0; i < lub; i++) {
            as[i] = Rnq.newRandomElement();

            e = sampleDRq();
            bs[i] = as[i].duplicate().mul(s).add(e.mul(t)).negate().add(
                    sSquare.duplicate().mul(tt)
            );
            tt = tt.multiply(bigT);
        }
        */
        
        as = new Element[L][3][lub];
        bs = new Element[L][3][lub];

        Element sPrev = s.getField().newOneElement();
        Element sNext = s1 = sampler.sampleDFromField(Rq);
//        Element sNext = s1 = s;

        for (int l = 0; l < L; l++) {

            for (int i = 0; i < 3; i++) {
            
                BigInteger tPower = BigInteger.ONE;

                for (int j = 0; j < lub; j++) {
                    as[l][i][j] = Rq.newRandomElement();
                    e = sampler.sampleDFromField(Rq);
                    bs[l][i][j] = as[l][i][j].duplicate().mul(sNext).add(e.mul(t)).negate().add(sPrev.duplicate().mul(tPower));
                    tPower = tPower.multiply(tt);
                }

                sPrev.mul(s);
            }

        }
        
        System.out.println("s = " + s);
        System.out.println("e*t = " + e);
        System.out.println("a0 = " + a0);
        System.out.println("a1 = " + a1);
    }

    public static Element[] enc() {
        System.out.println("FHE.enc");

        Element u = sampler.sampleDFromField(Rq);
        Element f = sampler.sampleDFromField(Rq).mul(t);
        Element g = sampler.sampleDFromField(Rq).mul(t);
        System.out.println("g = " + g);

        Element m = Rq.newOneElement();
        System.out.println("m = " + m);
        System.out.println("m.isOne() = " + m.isOne());

        // c0 = a0 * u + g + m
        Element c0 = a0.duplicate().mul(u).add(g).add(m);
//        c0 = a0.duplicate().mul(u).add(m);

        // c1 = a1 * u + f
        Element c1 = a1.duplicate().mul(u).add(f);
//        c1 = a1.duplicate().mul(u);

//        System.out.println(c0.duplicate().add(c1.duplicate().mul(s)));

        System.out.println("c0 = " + c0);
        System.out.println("c1 = " + c1);

        System.out.println("c0(Rnq) = " + Rq.newElement().set(c0));
        System.out.println("c1(Rnq) = " + Rq.newElement().set(c1));

        return new Element[]{c0, c1};
    }

    public static Polynomial<Element> mod(Polynomial<Element> m, BigInteger order) {
//        BigInteger halfOrder = order.divide(BigInteger.valueOf(2));
        for (Element element : m.getCoefficients()) {
            BigInteger value = element.toBigInteger();
            
            value = value.mod(order);
//            if (value.compareTo(halfOrder) > 0)
//                value = value.subtract(order);
//
            element.set(value);
        }
        
        return m;
    }
    
    public static void dec(Element[] elements, Element key) {
        System.out.println("FHE.dec");
//        Polynomial<Element> m = (Polynomial<Element>) c0.duplicate().add(c1.duplicate().mul(s));
        
//        Polynomial<Element> c0 = (Polynomial<Element>) elements[0];
//        Polynomial<Element> c1 = (Polynomial<Element>) elements[1];
//        Polynomial<Element> m = (Polynomial<Element>) c0.duplicate().add(c1.duplicate().mul(s));
        
        Polynomial<Element> m = (Polynomial<Element>) elements[0].duplicate();
        System.out.println("c[0] = " + m);
        Element ss = key.getField().newOneElement();
        for (int i = 1; i < elements.length; i++) {
            System.out.println("c["+i+"] = " + elements[i]);

            m.add(elements[i].duplicate().mul(ss.mul(key)));
            System.out.println("ss = " + ss);
        }

        Element mm = m.duplicate();
        
        System.out.println("m = " + m);
        System.out.println("m(Rnq) = " + Rq.newElement().set(m));
        System.out.println("m.isOne() = " + m.isOne());
        mod(m, BigInteger.valueOf(t));
        System.out.println("m = " + m);
        System.out.println("m(Rnq) = " + Rq.newElement().set(mm));
        System.out.println("m(Rnq) mod t = " + mod(Rq.newElement().set(mm), BigInteger.valueOf(t)));
        System.out.println("m.isOne() = " + m.isOne());
    }
    
    
    public static Element[] multiply(Element[] left, Element[] right) {
        Element c0 = left[0].duplicate().mul(right[0]);

        Element c1 = left[0].duplicate().mul(right[1]).add(
                left[1].duplicate().mul(right[0])
        );

        Element c2 = left[1].duplicate().mul(right[1]);

        System.out.println("c0 = " + c0);
        System.out.println("c1 = " + c1);
        System.out.println("c2 = " + c2);

        System.out.println("c0(Rnq) = " + Rq.newElement().set(c0));
        System.out.println("c1(Rnq) = " + Rq.newElement().set(c1));
        System.out.println("c2(Rnq) = " + Rq.newElement().set(c2));
        
        // relinearization

        Element base = Zqas.newElement(t).invert();
        BigInteger baseBig = BigInteger.valueOf(t);
        System.out.println("base = " + base);

        Element[] c0s = moveRepresentation(c0);
        Element[] c1s = moveRepresentation(c1);
        Element[] c2s = moveRepresentation(c2);

        Element c1lin = c1.getField().newZeroElement();
        Element c0lin = c1.getField().newZeroElement();

        for (int j = 0; j < lub; j++) {
            c0lin.add(c0s[j].duplicate().mul(bs[0][0][j]));
            c1lin.add(c0s[j].duplicate().mul(as[0][0][j]));
        }

        for (int j = 0; j < lub; j++) {
            c0lin.add(c1s[j].duplicate().mul(bs[0][1][j]));
            c1lin.add(c1s[j].duplicate().mul(as[0][1][j]));
        }

        for (int j = 0; j < lub; j++) {
            c0lin.add(c2s[j].duplicate().mul(bs[0][2][j]));
            c1lin.add(c2s[j].duplicate().mul(as[0][2][j]));
        }
        return new Element[]{c0lin, c1lin};


//        Element[] c2s = moveRepresentation(c2);
//
//        Element c1lin = c1.duplicate();
//        Element c0lin = c0.duplicate();
//        for (int i = 0; i < lub; i++) {
//            c1lin.add(c2s[i].duplicate().mul(as[i]));
//            c0lin.add(c2s[i].duplicate().mul(bs[i]));
//        }
//        return new Element[]{c0lin, c1lin};

//        return new Element[]{c0, c1, c2};
    }
    
    
    public static Element[] moveRepresentation(Element e) {
        Element[] es = new Element[lub];
        for (int i = 0; i < lub; i++) {
            es[i] = e.getField().newElement();
        }
              
        int i =0;
        for (Element coeff : ((Polynomial<Element>) e).getCoefficients()) {
//            System.out.println("coeff = " + coeff);

            Element cursor = Zqas.newElement().set(coeff.duplicate());
            int j = 0;
            while (true) {
//                System.out.println("cursor = " + cursor);
                BigInteger reminder = cursor.toBigInteger().mod(tt);

                ((Polynomial<Element>) es[j++]).getCoefficient(i).set(reminder);

//                System.out.println("reminder = " + reminder);
                cursor = cursor.sub(cursor.getField().newElement(reminder)).mul(te);
                if (cursor.isZero())
                    break;
            }
            
            i++;
        }

        return es;
    }
    

    public static void main(String[] args) {
        Zq = new ZrField(random, BigInteger.valueOf(7));

        BigInteger Base = BigInteger.valueOf(2);

        Element base = Zq.newElement(2).invert();
        Element cursor = Zq.newElement(2);
        while (true) {
            System.out.println("cursor = " + cursor);
            
            BigInteger reminder = cursor.toBigInteger().mod(Base);
            System.out.println("reminder = " + reminder);
            
            Element quotient = cursor.sub(Zq.newElement(reminder)).mul(base) ;
            System.out.println("quotient = " + quotient);

            cursor = quotient;
            if (cursor.isZero())
                break;
        }
        
        
        
        
        setup();
        keygen();
        Element[] ciphertext1 = enc();
        dec(ciphertext1, s);

        Element[] ciphertext2 = enc();
        dec(ciphertext2, s);


        // Left is 2
        Element[] left = new Element[]{
                ciphertext2[0].duplicate(),
                ciphertext2[1].duplicate()
        };
        for (int  i = 0; i < 1; i++) {
            left[0].add(ciphertext1[0]);
            left[1].add(ciphertext1[1]);
        }
        dec(left, s);

        // right is 2
        Element[] right = new Element[]{
                ciphertext2[0].duplicate(),
                ciphertext2[1].duplicate()
        };
        for (int  i = 0; i < 1; i++) {
            right[0].add(ciphertext1[0]);
            right[1].add(ciphertext1[1]);
        }
        dec(right, s);

        // compute left * right
        System.out.println("left[0] = " + left[0]);
        System.out.println("left[1] = " + left[1]);
        System.out.println("right[0] = " + right[0]);
        System.out.println("right[1] = " + right[1]);

        
        Element[] mul = multiply(left, right);

        dec(mul, s1);
    }

}
