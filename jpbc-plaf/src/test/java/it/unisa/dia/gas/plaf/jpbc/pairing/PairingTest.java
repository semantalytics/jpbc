package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.PairingPreProcessing;
import junit.framework.TestCase;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class PairingTest extends TestCase {
    protected Pairing pairing;


    public void testPairing() {
        if (pairing == null)
            return;

        Element g, h;
        Element x1, x2;
        Element zg, zh, z;

        g = pairing.getG1().newElement().setToRandom();
        h = pairing.getG2().newElement().setToRandom();

        x1 = pairing.pairing(g, h);

        z = pairing.getZr().newElement().setToRandom();
        x1.powZn(z);

        zg = g.duplicate().powZn(z);

        x2 = pairing.pairing(zg, h);

        assertEquals(true, x1.isEqual(x2));

        zh = h.duplicate().powZn(z);

        x2 = pairing.pairing(g, zh);

        assertEquals(true, x1.isEqual(x2));
    }

    public void testPairingPreProcessing() {
        if (pairing == null)
            return;

        Element g, h;
        Element x1, x2;

        g = pairing.getG1().newElement().setToRandom();
        h = pairing.getG2().newElement().setToRandom();

        x1 = pairing.pairing(g, h);

        PairingPreProcessing ppp = pairing.pairing(g);
        x2 = ppp.pairing(h);
        
        assertTrue(x1.isEqual(x2));
    }

    public void testPairingPreProcessingBytes() {
        if (pairing == null)
            return;

        Element g, h;
        Element x1, x2;

        g = pairing.getG1().newElement().setToRandom();
        h = pairing.getG2().newElement().setToRandom();

        PairingPreProcessing ppp1 = pairing.pairing(g);
        PairingPreProcessing ppp2 = pairing.pairing(ppp1.toBytes());

        x1 = ppp1.pairing(h);
        x2 = ppp2.pairing(h);

        assertEquals(true, x1.isEqual(x2));
    }



    public void testPairingSymmetric() {
        if (pairing == null)
            return;

        if (pairing.isSymmetric()) {
            Element g;
            Element x1, x2;
            Element zg, zh, z;

            g = pairing.getG1().newElement().setToRandom();

            x1 = pairing.pairing(g, g);

            z = pairing.getZr().newElement().setToRandom();
            x1.powZn(z);

            zg = g.duplicate().powZn(z);

            x2 = pairing.pairing(zg, g);

            assertTrue(x1.isEqual(x2));

            zh = g.duplicate().powZn(z);

            x2 = pairing.pairing(g, zh);

            assertTrue(x1.isEqual(x2));
        }
    }

    public void testProdPairing() {
        if (pairing == null)
            return;

        Element g1, h1;
        Element g2, h2;

        Element out1, out2;

        g1 = pairing.getG1().newElement().setToRandom();
        h1 = pairing.getG2().newElement().setToRandom();

        g2 = pairing.getG1().newElement().setToRandom();
        h2 = pairing.getG2().newElement().setToRandom();

        out1 = pairing.pairing(g1, h1).mul(pairing.pairing(g2, h2));
        out2 = pairing.pairing(new Element[]{g1, g2}, new Element[]{h1, h2});

        assertEquals(true, out1.isEqual(out2));
    }

    @Override
    protected void setUp() throws Exception {
        CurveParameters curveParameters = getCurveParameters();
        if (curveParameters != null) {
            pairing = PairingFactory.getPairing(curveParameters);

            assertNotNull(pairing.getG1());
            assertNotNull(pairing.getG2());
            assertNotNull(pairing.getGT());
            assertNotNull(pairing.getZr());
        } else
            pairing = null;
    }

    protected abstract CurveParameters getCurveParameters();

}
