package it.unisa.dia.gas.plaf.jpbc.field.curve;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import junit.framework.TestCase;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class CurveFieldTest extends TestCase {

    protected Pairing pairing;

    @Override
    protected void setUp() throws Exception {
        pairing = new TypeAPairing(getCurveParams());

        assertNotNull(pairing.getG1());
        assertNotNull(pairing.getG2());
        assertNotNull(pairing.getGT());
        assertNotNull(pairing.getZr());
    }

    protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"));
        return curveParams;
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


}
