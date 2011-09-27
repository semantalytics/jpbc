package it.unisa.dia.gas.crypto.jlbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Polynomial;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyField;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyModElement;
import it.unisa.dia.gas.plaf.jpbc.field.poly.PolyModField;
import it.unisa.dia.gas.plaf.jpbc.field.z.NaiveSymmetricZrField;
import it.unisa.dia.gas.plaf.jpbc.field.z.NaiveZField;
import it.unisa.dia.gas.plaf.jpbc.field.z.NaiveZrField;

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
    static int t;

    static PolyModField R, Rq, Rt;
    static Field Zq;

    static {
        n = 16;
        q = BigInteger.valueOf(1061093377);
        t = 1024;
        sigma = 8;
        sigmaSquare = Math.pow(sigma, 2);
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

//        for (int i = 0; i < v.length; i++) {
//            v[i] = v[i].abs();
//        }

        return v;
    }


    public static void setup() {
        // Define R
        PolyField polyField = new PolyField(random, new NaiveZField(random));
        PolyElement<Element> xToNPlusOne = polyField.newElement();

        List<Element> coefficients = xToNPlusOne.getCoefficients();
        coefficients.add(polyField.getTargetField().newElement().setToOne());
        for (int i = 1; i < n; i++) {
            coefficients.add(polyField.getTargetField().newZeroElement());
        }
        coefficients.add(polyField.getTargetField().newElement().setToOne());

        R = new PolyModField(random, xToNPlusOne);

        // Define Rq
        polyField = new PolyField(random, (Zq = new NaiveZrField(random, q)));
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

        PolyModElement result = R.newElement();
        for (int i = 0; i < result.getDegree(); i++) {
            result.getCoefficient(i).set(coeff[i]);
        }

        System.out.println("result = " + result);
        return result;
    }

    static Element as[];
    static Element bs[];

    public static void keygen() {
        s = sampleDRq();
        Element e = sampleDRq();
        a1 = R.newElement().set(Rq.newRandomElement());

        // a0 = -(a1 * s + t * e)
        a0 = a1.duplicate().mul(s).add(e.mul(t)).negate();
//        a0 = a1.duplicate().mul(s).negate();

//        System.out.println(a0.duplicate().add(a1.duplicate().mul(s)));

        // Keys for relinearization
//        BigInteger bigT = BigInteger.valueOf(t);
//        BigInteger tt = BigInteger.ONE;
//        Element sSquare = s.duplicate().square();
//        as = new Element[2];
//        bs = new Element[2];
//        for (int i = 0; i < 2; i++) {
//            as[i] = R.newElement().set(Rq.newRandomElement());
//            e = sampleDRq();
//            bs[i] = as[i].duplicate().mul(s).add(e.mul(t)).negate().add(
//                    sSquare.duplicate().mul(tt)
//            );
//            tt = tt.multiply(bigT);
//        }
        
        System.out.println("FHE.keygen");
        System.out.println("s = " + s);
        System.out.println("e = " + e);
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
        return new Element[]{c0, c1};
    }

    public static Polynomial<Element> mod(Polynomial<Element> m, BigInteger mod) {
        for (Element element : m.getCoefficients()) {
            element.set(element.toBigInteger().mod(mod));
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
        Element ss = s.getField().newOneElement();
        for (int i = 1; i < elements.length; i++) {
            m.add(elements[i].duplicate().mul(ss.mul(s)));
        }

        System.out.println("m = " + m);
        System.out.println("m.isOne() = " + m.isOne());
        mod(m, BigInteger.valueOf(t));
        System.out.println("m = " + m);
        System.out.println("m.isOne() = " + m.isOne());
    }

    public static void main(String[] args) {
        /*setup();
        keygen();
        Element[] ciphertext1 = enc();
        dec(ciphertext1);

        Element[] ciphertext2 = enc();
        dec(ciphertext2);


        Element[] left = new Element[]{
                ciphertext2[0].duplicate(),
                ciphertext2[1].duplicate()
        };
        for (int  i = 0; i < 99; i++) {
            left[0].add(ciphertext1[0]);
            left[1].add(ciphertext1[1]);
        }
        dec(left);

        Element[] right = new Element[]{
                ciphertext2[0].duplicate(),
                ciphertext2[1].duplicate()
        };
        for (int  i = 0; i < 4; i++) {
            right[0].add(ciphertext1[0]);
            right[1].add(ciphertext1[1]);
        }
        dec(right);

        // compute left * right
        Element c0 = left[0].duplicate().mul(right[0]);
        
        Element c1 = left[0].duplicate().mul(right[1]).add(
                left[1].duplicate().mul(right[0])
        );

        Element c2 = left[1].duplicate().mul(right[1]);

//        System.out.println(c2.div(c2.getField().newElement(t)));
        
        dec(new Element[]{c0, c1, c2});
        */

        NaiveSymmetricZrField field = new NaiveSymmetricZrField(BigInteger.valueOf(7));
        Element e = field.newElement(-1);
        System.out.println("e = " + e);
    }

}
