package it.unisa.dia.gas.plaf.jpbc.pbc.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import junit.framework.TestCase;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingTest extends TestCase {

    protected Pairing pairing;


    public void doPairing() {
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

    public void doPairingPreProcessing() {
        if (pairing == null)
            return;

        Element g, h;
        Element x1, x2;

        g = pairing.getG1().newElement().setToRandom();
        h = pairing.getG2().newElement().setToRandom();

        System.out.println("g = " + g);
        System.out.println("h = " + h);

        x1 = pairing.pairing(g, h);

        PairingPreProcessing ppp = pairing.pairing(g);
        x2 = ppp.pairing(h);

        assertEquals(0, x1.compareTo(x2));
    }

    public void doPairingSymmetric() {
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

}
