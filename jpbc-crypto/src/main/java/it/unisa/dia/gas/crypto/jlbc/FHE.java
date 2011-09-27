package it.unisa.dia.gas.crypto.jlbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Polynomial;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyModElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyModField;
import it.unisa.dia.gas.plaf.jpbc.field.z.SymmetricZrField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FHE {

    static BigInteger tofk = BigInteger.valueOf(Math.round(Math.log(128)));
    static Random random = new Random();

    static int n;
    static BigInteger q;
    static int sigma;
    static double sigmaSquare;
    static int t, lub;

    
    
    static PolyModField R, Rq, Rt;
    static Field Zq, Zqas;

    static {
        n = 16;
//        q = BigInteger.valueOf(1061093377);
        q = new BigInteger("144115188076060673");
        t = 1024;
        sigma = 8;
        sigmaSquare = Math.pow(sigma, 2);
        
        lub = (int) (Math.log(144115188076060673d) / Math.log(t)) + 1;

        random.setSeed(1000);
    }

    public static BigInteger sampleZ(int sigma, BigInteger c) {
        while (true) {
            BigInteger offset = tofk.multiply(BigInteger.valueOf(sigma));

            BigInteger left = c.subtract(offset);
            BigInteger right = c.add(offset);

            BigInteger x = left.add(BigInteger.valueOf((long) (random.nextDouble() * (right.subtract(left)).longValue())));

            double rhoS = Math.exp(-Math.PI * Math.pow(x.subtract(c).longValue(), 2) / sigmaSquare);

            if (random.nextDouble() < rhoS)
                return x;
        }
    }

    public static BigInteger[] sampleD(int n, int sigma) {
        BigInteger[] v = new BigInteger[n];

        BigInteger c = BigInteger.ZERO;
        v[n - 1] = BigInteger.ZERO;
        for (int i = n - 2; i >= 0; i--) {
            BigInteger z = sampleZ(sigma, c);
            c = c.subtract(z);
            v[i] = v[i + 1].add(z);
        }

        return v;
    }


    public static void setup() {
        // Define R
        PolyField polyField = new PolyField(random, new ZField(random));
        PolyElement<Element> xToNPlusOne = polyField.newElement();

        List<Element> coefficients = xToNPlusOne.getCoefficients();
        coefficients.add(polyField.getTargetField().newElement().setToOne());
        for (int i = 1; i < n; i++) {
            coefficients.add(polyField.getTargetField().newZeroElement());
        }
        coefficients.add(polyField.getTargetField().newElement().setToOne());

        R = new PolyModField(random, xToNPlusOne);

        // Define Rq
        Zqas = new ZrField(random, q); 
        polyField = new PolyField(random, (Zq = new SymmetricZrField(random, q)));
//        polyField = new PolyField(random, (Zq = new ZrField(random, q)));
        xToNPlusOne = polyField.newElement();

        coefficients = xToNPlusOne.getCoefficients();
        coefficients.add(polyField.getTargetField().newElement().setToOne());
        for (int i = 1; i < n; i++) {
            coefficients.add(polyField.getTargetField().newZeroElement());
        }
        coefficients.add(polyField.getTargetField().newElement().setToOne());

        Rq = new PolyModField(random, xToNPlusOne);
    }

    static Element s, a0, a1;

    public static Element sampleDRq() {
        System.out.println("FHE.sampleDRq");

        BigInteger[] coeff = sampleD(n, sigma);
        System.out.println("coeff = " + Arrays.toString(coeff));

        PolyModElement result = Rq.newElement();
//        PolyModElement result = R.newElement();
        for (int i = 0; i < result.getDegree(); i++) {
            result.getCoefficient(i).set(coeff[i]);
        }

        System.out.println("result = " + result);
        return result;
    }

    static Element as[];
    static Element bs[];

    public static void keygen() {
        System.out.println("FHE.keygen");
        s = sampleDRq();
        Element e = sampleDRq();
        System.out.println("e = " + e);
        a1 = Rq.newRandomElement();
//        a1 = R.newElement().set(Rq.newRandomElement());

        // a0 = -(a1 * s + t * e)
        a0 = a1.duplicate().mul(s).add(e.mul(t)).negate();
//        a0 = a1.duplicate().mul(s).negate();

//        System.out.println(a0.duplicate().add(a1.duplicate().mul(s)));

        // Keys for relinearization
        BigInteger bigT = BigInteger.valueOf(t);
        BigInteger tt = BigInteger.ONE;
        Element sSquare = s.duplicate().square();
        as = new Element[lub];
        bs = new Element[lub];
        for (int i = 0; i < lub; i++) {
            as[i] = Rq.newRandomElement();

            e = sampleDRq();
            bs[i] = as[i].duplicate().mul(s).add(e.mul(t)).negate().add(
                    sSquare.duplicate().mul(tt)
            );
            tt = tt.multiply(bigT);
        }
        
        System.out.println("s = " + s);
        System.out.println("e*t = " + e);
        System.out.println("a0 = " + a0);
        System.out.println("a1 = " + a1);
    }

    public static Element[] enc() {
        System.out.println("FHE.enc");

        Element u = sampleDRq();
        Element f = sampleDRq().mul(t);
        Element g = sampleDRq().mul(t);
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

        System.out.println("c0(Rq) = " + Rq.newElement().set(c0));
        System.out.println("c1(Rq) = " + Rq.newElement().set(c1));

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
    
    public static void dec(Element[] elements) {
        System.out.println("FHE.dec");
//        Polynomial<Element> m = (Polynomial<Element>) c0.duplicate().add(c1.duplicate().mul(s));
        
//        Polynomial<Element> c0 = (Polynomial<Element>) elements[0];
//        Polynomial<Element> c1 = (Polynomial<Element>) elements[1];
//        Polynomial<Element> m = (Polynomial<Element>) c0.duplicate().add(c1.duplicate().mul(s));
        
        Polynomial<Element> m = (Polynomial<Element>) elements[0].duplicate();
        System.out.println("c[0] = " + m);
        Element ss = s.getField().newOneElement();
        for (int i = 1; i < elements.length; i++) {
            System.out.println("c["+i+"] = " + elements[i]);

            m.add(elements[i].duplicate().mul(ss.mul(s)));
            System.out.println("ss = " + ss);
        }

        Element mm = m.duplicate();
        
        System.out.println("m = " + m);
        System.out.println("m(Rq) = " + Rq.newElement().set(m));
        System.out.println("m.isOne() = " + m.isOne());
        mod(m, BigInteger.valueOf(t));
        System.out.println("m = " + m);
        System.out.println("m(Rq) = " + Rq.newElement().set(mm));
        System.out.println("m(Rq) mod t = " + mod(Rq.newElement().set(mm), BigInteger.valueOf(t)));
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

        System.out.println("c0(Rq) = " + Rq.newElement().set(c0));
        System.out.println("c1(Rq) = " + Rq.newElement().set(c1));
        System.out.println("c2(Rq) = " + Rq.newElement().set(c2));
        
        // relinearization

        Element base = Zqas.newElement(t).invert();
        BigInteger baseBig = BigInteger.valueOf(t);
        System.out.println("base = " + base);

        Element[] c2i = new Element[lub];
        for (int i = 0; i < lub; i++) {
            c2i[i] = c2.getField().newElement();
        }
              
        int i =0;
        for (Element coeff : ((Polynomial<Element>) c2).getCoefficients()) {
//            System.out.println("coeff = " + coeff);

            Element cursor = Zqas.newElement().set(coeff.duplicate());
            int j = 0;
            while (true) {
//                System.out.println("cursor = " + cursor);
                BigInteger reminder = cursor.toBigInteger().mod(baseBig);

                ((Polynomial<Element>) c2i[j++]).getCoefficient(i).set(reminder);

//                System.out.println("reminder = " + reminder);
                cursor = cursor.sub(cursor.getField().newElement(reminder)).mul(base);
                if (cursor.isZero())
                    break;
            }
            
            i++;
        }

        Element c1lin = c1.duplicate();
        Element c0lin = c0.duplicate();
        for (i = 0; i < lub; i++) {
            c1lin.add(c2i[i].duplicate().mul(as[i]));
            c0lin.add(c2i[i].duplicate().mul(bs[i]));
        }

        return new Element[]{c0lin, c1lin};

//        return new Element[]{c0, c1, c2};
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
        dec(ciphertext1);

        Element[] ciphertext2 = enc();
        dec(ciphertext2);


        // Left is 2
        Element[] left = new Element[]{
                ciphertext2[0].duplicate(),
                ciphertext2[1].duplicate()
        };
        for (int  i = 0; i < 1; i++) {
            left[0].add(ciphertext1[0]);
            left[1].add(ciphertext1[1]);
        }
        dec(left);

        // right is 2
        Element[] right = new Element[]{
                ciphertext2[0].duplicate(),
                ciphertext2[1].duplicate()
        };
        for (int  i = 0; i < 99; i++) {
            right[0].add(ciphertext1[0]);
            right[1].add(ciphertext1[1]);
        }
        dec(right);

        // compute left * right
        System.out.println("left[0] = " + left[0]);
        System.out.println("left[1] = " + left[1]);
        System.out.println("right[0] = " + right[0]);
        System.out.println("right[1] = " + right[1]);

        
        Element[] mul = multiply(left, right);

//        System.out.println(c2.div(c2.getField().newElement(t)));
        
        dec(mul);

//        SymmetricZrField field = new SymmetricZrField(BigInteger.valueOf(7));
//        System.out.println(field.isOrderOdd());
//        Element e = field.newElement(-1);
//        System.out.println("e = " + e);
    }

}
