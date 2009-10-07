package it.unisa.dia.gas.plaf.jpbc.pbc.element;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.pbc.pairing.PBCPairingTest;
import junit.framework.TestCase;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPowPreProcessingTest extends TestCase {

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


    protected void setUp() throws Exception {
        if (PBCLibraryProvider.isAvailable())
            pairing = new PBCPairing(getCurveParams());
        else
            pairing = null;
    }

    protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(PBCPairingTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_181_603.properties"));
        return curveParams;
    }

}