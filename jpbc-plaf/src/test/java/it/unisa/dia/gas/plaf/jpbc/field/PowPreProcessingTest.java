package it.unisa.dia.gas.plaf.jpbc.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PowPreProcessingTest extends TestCase {

    protected Pairing pairing;


    public void testPowPreProcessing() {
        if (pairing == null)
            return;

        Element e1 = pairing.getZr().newElement().setToRandom();
        Element e2 = e1.duplicate();
        e1.initPowPreProcessing();
        BigInteger n = pairing.getZr().newElement().setToRandom().toBigInteger();

        Element r1 = e1.duplicate().powPreProcessing(n);
        Element r2 = e2.duplicate().pow(n);

        assertEquals(0, r1.compareTo(r2));
    }

    public void testPowPreProcessingZn() {
        if (pairing == null)
            return;

        Element e1 = pairing.getZr().newElement().setToRandom();
        Element e2 = e1.duplicate();
        e1.initPowPreProcessing();
        Element n = pairing.getZr().newElement().setToRandom();

        Element r1 = e1.duplicate().powZnPreProcessing(n);
        Element r2 = e2.duplicate().powZn(n);

        assertEquals(0, r1.compareTo(r2));
    }


    @Override
    protected void setUp() throws Exception {
        pairing = PairingFactory.getPairing(getCurveParams());
    }

    protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_603_181.properties"));
        return curveParams;
    }

}
