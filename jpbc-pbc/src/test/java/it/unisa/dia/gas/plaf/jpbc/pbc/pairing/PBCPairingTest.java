package it.unisa.dia.gas.plaf.jpbc.pbc.pairing;

import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairingTest extends PairingTest {

    public void testPairing() {
        doPairing();
    }

    public void testPairingPreProcessing() {
        doPairingPreProcessing();
    }

    public void testPairingSymmetric() {
        doPairingSymmetric();
    }


    protected void setUp() throws Exception {
        if (PBCLibraryProvider.isAvailable()) {
            pairing = new PBCPairing(getCurveParams());

            assertNotNull(pairing.getG1());
            assertNotNull(pairing.getG2());
            assertNotNull(pairing.getGT());
            assertNotNull(pairing.getZr());
        } else
            pairing = null;
    }

    protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(PBCPairingTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/d_9563.properties"));
        return curveParams;
    }
}
