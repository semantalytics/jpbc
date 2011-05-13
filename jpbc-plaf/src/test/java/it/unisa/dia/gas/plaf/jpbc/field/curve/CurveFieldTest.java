package it.unisa.dia.gas.plaf.jpbc.field.curve;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import junit.framework.TestCase;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class CurveFieldTest extends TestCase {

    protected Pairing pairing;

    @Override
    protected void setUp() throws Exception {
        pairing = new TypeAPairing(getCurveParameters());

        assertNotNull(pairing.getG1());
        assertNotNull(pairing.getG2());
        assertNotNull(pairing.getGT());
        assertNotNull(pairing.getZr());
    }

    protected CurveParameters getCurveParameters() {
        return PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties");
    }


    public void testOne() {
        if (pairing == null)
            return;

        Element g = pairing.getG1().newElement().setToRandom();
        Element a = pairing.getZr().newElement().setToRandom();
        Element r = pairing.getZr().newElement().setToRandom();

        Element egg = pairing.pairing(g, g);
        Element egga = egg.duplicate().powZn(a);

        Element gar = g.duplicate().powZn(a.duplicate().div(r));
        Element gr = g.duplicate().powZn(r);

        Element egargr = pairing.pairing(gar, gr);

        assertTrue(egga.isEqual(egargr));

        Element eggMinusa = egg.duplicate().powZn(a.duplicate().negate());
        Element one = egargr.duplicate().mul(eggMinusa);

        assertEquals(true, one.isOne());
    }

    /* TODO: reactive asap, PBC produces an invalid access memory...
    public void testTwice() {
        if (pairing == null)
            return;

        Element a = pairing.getG1().newElement().setToRandom();
        Element b = pairing.getG1().newElement().setToRandom();

        Element a_c = a.duplicate().twice();
        Element b_c = b.duplicate().twice();

        Element _a = a.duplicate();
        Element _b = b.duplicate();

        a.getField().twice(new Element[]{_a,_b});

        assertEquals(true, a_c.isEqual(_a));
        assertEquals(true, b_c.isEqual(_b));
    }

    public void testAdd() {
        if (pairing == null)
            return;

        Element a = pairing.getG1().newElement().setToRandom();
        Element b = pairing.getG1().newElement().setToRandom();
        Element a1 = pairing.getG1().newElement().setToRandom();
        Element b1 = pairing.getG1().newElement().setToRandom();

        Element c = a.duplicate().add(b);
        Element c1 = a1.duplicate().add(b1);

        Element _c = a.duplicate();
        Element _c1 = a1.duplicate();

        a.getField().add(new Element[]{_c,_c1}, new Element[]{b,b1});

        assertEquals(true, c.isEqual(_c));
        assertEquals(true, c1.isEqual(_c1));
    }

    */
}
