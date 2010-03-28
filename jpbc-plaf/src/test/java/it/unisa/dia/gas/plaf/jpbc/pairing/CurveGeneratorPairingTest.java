package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.e.TypeECurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.f.TypeFCurveGenerator;
import junit.framework.TestCase;

import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class CurveGeneratorPairingTest extends TestCase {
    protected Pairing pairing;


    public void testTypeA() {
        CurveGenerator curveGenerator = new TypeACurveGenerator(181, 603);
        initPairing(curveGenerator.generate());
        doTest();
    }

    public void testTypeA1() {
        // Generate TypeA1 curve
        CurveGenerator curveGenerator = new TypeA1CurveGenerator();
        initPairing(curveGenerator.generate());
        doTest();
    }

/*
    public void testTypeD() {
        // Generate TypeD curve
        CurveGenerator curveGenerator = new TypeDCurveGenerator(9563);
        initPairing(curveGenerator.generate());
        doTest();
    }
*/

    public void testTypeE() {
        // Generate TypeE curve
        CurveGenerator curveGenerator = new TypeECurveGenerator(160, 1024);
        initPairing(curveGenerator.generate());
        doTest();
    }

    public void testTypeF() {
        // Generate TypeE curve
        CurveGenerator curveGenerator = new TypeFCurveGenerator(160);
        initPairing(curveGenerator.generate());
        doTest();
    }


    protected void initPairing(Map curve) {
        pairing = PairingFactory.getPairing((CurveParams) curve);

        assertNotNull(pairing.getG1());
        assertNotNull(pairing.getG2());
        assertNotNull(pairing.getGT());
        assertNotNull(pairing.getZr());
    }

    protected void doTest() {
        doPairing();
        doPairingPreProcessing();
        doPairingSymmetric();
    }

    protected void doPairing() {
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

        assertTrue(x1.isEqual(x2));

        zh = h.duplicate().powZn(z);

        System.out.println("h^z = " + zh);

        x2 = pairing.pairing(g, zh);
        System.out.println("f(g, h^z) = " + x2);

        assertTrue(x1.isEqual(x2));
    }

    protected void doPairingPreProcessing() {
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

        assertTrue(x1.isEqual(x2));
    }

    protected void doPairingSymmetric() {
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

            assertTrue(x1.isEqual(x2));

            zh = g.duplicate().powZn(z);

            System.out.println("g^z = " + zh);

            x2 = pairing.pairing(g, zh);
            System.out.println("f(g, g^z) = " + x2);

            assertTrue(x1.isEqual(x2));
        }
    }

}
