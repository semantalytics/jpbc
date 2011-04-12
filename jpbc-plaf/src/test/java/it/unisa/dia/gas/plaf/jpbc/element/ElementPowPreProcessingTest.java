package it.unisa.dia.gas.plaf.jpbc.element;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public abstract class ElementPowPreProcessingTest extends TestCase {

    protected Pairing pairing;


    public void testPowPreProcessing() {
        if (pairing == null)
            return;

        Element e1 = pairing.getZr().newElement().setToRandom();
        Element e2 = e1.duplicate();
        ElementPowPreProcessing ppp = e1.pow();
        BigInteger n = pairing.getZr().newElement().setToRandom().toBigInteger();

        Element r1 = ppp.pow(n);
        Element r2 = e2.duplicate().pow(n);

        assertTrue(r1.isEqual(r2));
    }

    public void testPowPreProcessingZn() {
        if (pairing == null)
            return;

        Element e1 = pairing.getZr().newElement().setToRandom();
        Element e2 = e1.duplicate();
        ElementPowPreProcessing ppp = e1.pow();
        Element n = pairing.getZr().newElement().setToRandom();

        Element r1 = ppp.powZn(n);
        Element r2 = e2.duplicate().powZn(n);

        assertTrue(r1.isEqual(r2));
    }

    public void testPowPreProcessingBytes() {
        if (pairing == null)
            return;

        Element e1 = pairing.getG1().newElement().setToRandom();

        ElementPowPreProcessing ppp1 = e1.pow();
        ElementPowPreProcessing ppp2 = e1.getField().pow(ppp1.toBytes());

        Element n = pairing.getZr().newElement().setToRandom();

        Element r1 = ppp1.powZn(n);
        Element r2 = ppp2.powZn(n);

        assertTrue(r1.isEqual(r2));
    }

    @Override
    protected void setUp() throws Exception {
        pairing = PairingFactory.getPairing(getCurveParameters());
    }

    protected abstract CurveParameters getCurveParameters();

}
