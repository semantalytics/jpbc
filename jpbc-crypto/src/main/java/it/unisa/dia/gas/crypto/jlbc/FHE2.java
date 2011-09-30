package it.unisa.dia.gas.crypto.jlbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Polynomial;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyModField;
import it.unisa.dia.gas.plaf.jpbc.field.z.SymmetricZrField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Random;

public class FHE2 {

    static Random random = new Random();

    static int n, k;
    static BigInteger p, q, tt;
    static int sigma;
    static int t, lub, L, lup;
    static Element te;

    static Field<Polynomial> R, Rnq, Rkp, Rt;
    static Field Zq, Zp, Zqas;
    static FieldSampler sampler;

    static {
        t = 128;
        n = 2048;
//        k = 8;
        k = n;
        L = 3;

//        q = new BigInteger("144115188076060673");
//        q = new BigInteger("802239503293259863326950493533761069");
        q = new BigInteger(82, 12, random);

        System.out.println("q = " + q);

//        p = new BigInteger("8610900033770000").nextProbablePrime();
//        p = new BigInteger("144115188076060673");
        p = q;
//        p = new BigInteger("144115188075060677");
//        p = q.subtract(new BigInteger("99990000000000000")).nextProbablePrime();
//        p = q.subtract(new BigInteger("10")).nextProbablePrime();
//        p = new BigInteger(63, 12, random);

        System.out.println("p = " + p);

        System.out.println(q.divide(p).multiply(p).compareTo(q));
        System.out.println("q.subtract(p) = " + q.subtract(p));


        tt = BigInteger.valueOf(t);
        sigma = 8;

        sampler = new FieldSampler(random, sigma);

        lub = (int) (Math.log(q.doubleValue()) / Math.log(t)) + 1;
        lup = (int) (Math.log(p.doubleValue()) / Math.log(t)) + 1;

        System.out.println("lub = " + lub);
        System.out.println("lup = " + lup);

        random.setSeed(1000);
    }

    public static void setup() {
        R = new PolyModField(random, new ZField(random), n);
        Rnq = new PolyModField(random, (Zq = new SymmetricZrField(random, q)), n);
        Rkp = new PolyModField(random, (Zp = new SymmetricZrField(random, p)), k);

        Zqas = new ZrField(random, q);
        te = Zqas.newElement(t).invert();

    }

    // SK
    static Element s[];
    static Element shortS;

    // PK
    static Element a0, a1;
    static Element as[][][];
    static Element bs[][][];

    static Element shortAs[][];
    static Element shortBs[][];


    public static void keygen() {
        System.out.println("FHE.keygen");
        s = new Element[L+1];
        s[0] = sampler.sampleDFromField(Rnq);

        a1 = Rnq.newRandomElement();
        a0 = a1.duplicate().mul(s[0]).add(sampler.sampleDFromField(Rnq).mul(t)).negate();

        // Keys for relinearization
        as = new Element[L][3][lub];
        bs = new Element[L][3][lub];

        for (int l = 0; l < L; l++) {
            s[l+1] = s[0];//sampler.sampleDFromField(Rnq);

            Element secret = s[0].getField().newOneElement();
            for (int i = 0; i < 3; i++) {
                BigInteger tPower = BigInteger.ONE;
                for (int j = 0; j < lub; j++) {
                    as[l][i][j] = Rnq.newRandomElement();
                    bs[l][i][j] = as[l][i][j].duplicate().mul(s[l + 1]).add(sampler.sampleDFromField(Rnq).mul(t)).negate().add(
                            secret.duplicate().mul(tPower)
                    );

                    tPower = tPower.multiply(tt);
                }
                secret.mul(s[l]);
            }
        }

        // Keys for modulo-dimension reduction
        shortS = sampler.sampleDFromField(Rkp);
        shortAs = new Element[2][lub];
        shortBs = new Element[2][lub];

        Element secret = shortS.getField().newOneElement();
        Element key = shortS;
        for (int i = 0; i < 2; i++) {
            BigInteger tPower = BigInteger.ONE;
            for (int j = 0; j < lup; j++) {
                shortAs[i][j] = Rkp.newRandomElement();
                
                Element msg = muldiv2(secret.duplicate().mul(tPower), p, q);
                Element error = sampler.sampleDFromField(Rkp);

                shortBs[i][j] = shortAs[i][j].duplicate().mul(key).add(error).negate().add(msg);

                tPower = tPower.multiply(tt);
            }

            secret = s[L];
        }
    }
    
    public static Element muldiv(Polynomial<Element> a, BigInteger mul, BigInteger div) {
        for (Element element : a.getCoefficients()) {
            element.set(element.toBigInteger().multiply(mul).divide(div));
        }
        return a;
    } 
    

    public static Element[] enc() {
        Element u = sampler.sampleDFromField(Rnq);
        Element f = sampler.sampleDFromField(Rnq).mul(t);
        Element g = sampler.sampleDFromField(Rnq).mul(t);

        Element m = Rnq.newOneElement();

        // c0 = a0 * u + g + m
        Element c0 = a0.duplicate().mul(u).add(g).add(m);
        // c1 = a1 * u + f
        Element c1 = a1.duplicate().mul(u).add(f);

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
        Polynomial<Element> m = (Polynomial<Element>) elements[0].duplicate();
        Element ss = key.getField().newOneElement();
        for (int i = 1; i < elements.length; i++) {
            m.add(elements[i].duplicate().mul(ss.mul(key)));
        }

        System.out.println("m = " + m);
        System.out.println("m = " + mod(m, BigInteger.valueOf(t)));
    }
    
    
    public static Element[] multiply(Element[] left, Element[] right) {
        Element c0 = left[0].duplicate().mul(right[0]);
        Element c1 = left[0].duplicate().mul(right[1]).add(left[1].duplicate().mul(right[0]));
        Element c2 = left[1].duplicate().mul(right[1]);

        // relinearization
        Element[] c0s = moveRepresentation(c0);
        Element[] c1s = moveRepresentation(c1);
        Element[] c2s = moveRepresentation(c2);

        Element c1lin = c1.getField().newZeroElement();
        Element c0lin = c1.getField().newZeroElement();

        for (int j = 0; j < lub; j++) {
            c0lin.add(c0s[j].duplicate().mul(bs[0][0][j]));
            c1lin.add(c0s[j].duplicate().mul(as[0][0][j]));

            c0lin.add(c1s[j].duplicate().mul(bs[0][1][j]));
            c1lin.add(c1s[j].duplicate().mul(as[0][1][j]));

            c0lin.add(c2s[j].duplicate().mul(bs[0][2][j]));
            c1lin.add(c2s[j].duplicate().mul(as[0][2][j]));
        }

        return new Element[]{c0lin, c1lin};
    }
    
    public static Element[] add(Element[] left, Element[] right) {
        return null;
    }
    
    
    public static Element[] reduce(Element[] ct) {
        Element h0 = ct[0].duplicate().mul(q.add(BigInteger.ONE)).mul(Zp.newElement(tt).invert().toBigInteger());
        Element h1 = ct[1].duplicate().mul(q.add(BigInteger.ONE)).mul(Zp.newElement(tt).invert().toBigInteger());

        Element[] h0s = moveRepresentation(h0);
        Element[] h1s = moveRepresentation(h1);

        Element c1red = Rkp.newZeroElement();
        Element c0red = Rkp.newZeroElement();

        for (int j = 0; j < lup; j++) {
            c0red.add(shortBs[0][j].duplicate().mul(h0s[j]));
            c0red.add(shortBs[1][j].duplicate().mul(h1s[j]));

            c1red.add(shortAs[0][j].duplicate().mul(h0s[j]));
            c1red.add(shortAs[1][j].duplicate().mul(h1s[j]));
        }

        return new Element[]{
                c0red.mul(t),
                c1red.mul(t)
        };
    }
    
    
    
    public static Element muldiv2(Element a, BigInteger num, BigInteger den) {
//        System.out.println("FHE2.muldiv2");
//        System.out.println("a = " + a);
        Polynomial<Element> pa = (Polynomial<Element>) a;

        MathContext mc = new MathContext(1024, RoundingMode.HALF_UP);

        for (Element element : pa.getCoefficients()) {
//            System.out.println("element = " + element);
            
            BigDecimal ratio = new BigDecimal(element.toBigInteger()).divide(new BigDecimal(q), mc);
//            System.out.println("ratio = " + ratio);
            BigDecimal dest = ratio.multiply(new BigDecimal(p), mc);

//            System.out.println("dest = " + dest);
//            System.out.println("-dest = " + dest);
//            System.out.println("ddest = " + dest.doubleValue());
            BigInteger destI = new BigInteger(String.valueOf(Math.round(dest.doubleValue())));
//            System.out.println("destI = " + destI);
            
            element.set(destI);
        }
        return pa;
    }
    
    public static Element[] moveRepresentation(Element source) {
        Element[] es = new Element[lub];
        for (int i = 0; i < lub; i++) {
            es[i] = source.getField().newElement();
        }
              
        int i = 0;
        for (Element coeff : ((Polynomial<Element>) source).getCoefficients()) {
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
        long start = System.currentTimeMillis();
        setup();
        keygen();
        Element[] ciphertext1 = enc();
        dec(ciphertext1, s[0]);

        Element[] ciphertext2 = enc();
        dec(ciphertext2, s[0]);


        // Left is 2
        Element[] left = new Element[]{
                ciphertext2[0].duplicate(),
                ciphertext2[1].duplicate()
        };
        for (int  i = 0; i < 1; i++) {
            left[0].add(ciphertext1[0]);
            left[1].add(ciphertext1[1]);
        }
        dec(left, s[0]);

        // right is 2
        Element[] right = new Element[]{
                ciphertext2[0].duplicate(),
                ciphertext2[1].duplicate()
        };
        for (int  i = 0; i < 1; i++) {
            right[0].add(ciphertext1[0]);
            right[1].add(ciphertext1[1]);
        }
        dec(right, s[0]);

        // compute left * right
        Element[] mul = left;
        for (int i = 0; i < L-1; i++) {
            mul = multiply(mul, right);
        }
        
        dec(mul, s[L]);

        long end = System.currentTimeMillis();
        System.out.println("elapsed = " + (end-start));
        
//        Element[] reduced = reduce(mul);
//        dec(reduced, shortS);
    }

}
