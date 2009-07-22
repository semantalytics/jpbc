package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import junit.framework.TestCase;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingTest extends TestCase {
    protected Pairing pairing;


    public void testPairing() {
        if (pairing == null)
            return;

        Element g, h;
        Element x1, x2;
        Element zg, zh, z;

        g = pairing.getG1().newElement().setToRandom();
        h = pairing.getG2().newElement().setToRandom();

        System.out.println("g = " + g);
        System.out.println("h = " + h);

        x1 = pairing.pairing(g, h);

        System.out.println("f(g, h) = " + x1);

        z = pairing.getZr().newElement().setToRandom();
        System.out.println("z = " + z);
        x1.powZn(z);
        System.out.println("f(g, h)^z = " + x1);

        zg = g.duplicate().powZn(z);

        System.out.println("g^z = " + zg);

        x2 = pairing.pairing(zg, h);
        System.out.println("f(g^z, h) = " + x2);

        assertEquals(0, x1.compareTo(x2));

        zh = h.duplicate().powZn(z);

        System.out.println("h^z = " + zh);

        x2 = pairing.pairing(g, zh);
        System.out.println("f(g, h^z) = " + x2);

        assertEquals(0, x1.compareTo(x2));
    }

    public void testPairingSymmetric() {
        if (pairing == null)
            return;

        if (pairing.isSymmetric()) {
            Element g;
            Element x1, x2;
            Element zg, zh, z;

            g = pairing.getG1().newElement().setToRandom();

            System.out.println("g = " + g);

            x1 = pairing.pairing(g, g);

            System.out.println("f(g, g) = " + x1);

            z = pairing.getZr().newElement().setToRandom();
            System.out.println("z = " + z);
            x1.powZn(z);
            System.out.println("f(g, g)^z = " + x1);

            zg = g.duplicate().powZn(z);

            System.out.println("g^z = " + zg);

            x2 = pairing.pairing(zg, g);
            System.out.println("f(g^z, g) = " + x2);

            assertEquals(0, x1.compareTo(x2));

            zh = g.duplicate().powZn(z);

            System.out.println("g^z = " + zh);

            x2 = pairing.pairing(g, zh);
            System.out.println("f(g, g^z) = " + x2);

            assertEquals(0, x1.compareTo(x2));
        }
    }

    public void testPairingPreProcessing() {
        if (pairing == null)
            return;

        Element g, h;

        g = pairing.getG1().newElement().setToRandom();

        pairing.initPairingPreProcessing(g);

        long t1 = 0;
        long t2 = 0;

        for (int i = 0; i < 100; i++) {
            h = pairing.getG2().newElement().setToRandom();

            long start = System.currentTimeMillis();
            Element r1 = pairing.pairing(h);
            long end = System.currentTimeMillis();
            t1+=(end-start);

            start = System.currentTimeMillis();
            Element r2 = pairing.pairing(g, h);
            end = System.currentTimeMillis();
            t2+=(end-start);

            assertEquals(0, r1.compareTo(r2));
        }

        System.out.println("t1 = " + t1);
        System.out.println("t2 = " + t2);

    }

    
    public void pairingBenchmark() {
        Element g1, g2, x1, z;

        int count = 0;
        for (int i = 0; i < 100; i++) {

            g1 = pairing.getG1().newElement().setToRandom();
            g2 = pairing.getG2().newElement().setToRandom();

            long s = System.currentTimeMillis();
            x1 = pairing.pairing(g1, g2);
            long e = System.currentTimeMillis();

            count += (e - s);
        }

        System.out.println("count = " + count);


//        count = 0;
//        for (int i = 0; i < 1000; i++) {
//            g = pairing.getG1().newElement().setToRandom();
//            z = pairing.getZr().newElement().setToRandom();
//
//            long s = System.currentTimeMillis();
//            g.powZn(z);
//            long e = System.currentTimeMillis();
//
//            count += (e - s);
//        }
//
//        System.out.println("count = " + count);
    }

}
