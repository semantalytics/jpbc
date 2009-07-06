package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import junit.framework.TestCase;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairingTest extends TestCase {
    protected Pairing pairing;
    

    @Override
    protected void setUp() throws Exception {
        pairing = new PBCPairing(PBCPairingTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_311_289.param"));

        assertNotNull(pairing.getG1());
        assertNotNull(pairing.getG2());
        assertNotNull(pairing.getGT());
        assertNotNull(pairing.getZr());
    }


    public void testPairing1() {
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

}
