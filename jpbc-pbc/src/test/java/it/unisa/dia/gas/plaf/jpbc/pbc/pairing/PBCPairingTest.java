package it.unisa.dia.gas.plaf.jpbc.pbc.pairing;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

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

    public void testProdPairing() {
        doProdPairing();
    }

    
    protected void setUp() throws Exception {
        if (WrapperLibraryProvider.isAvailable()) {
            pairing = new PBCPairing(getCurveParameters());

            assertNotNull(pairing.getG1());
            assertNotNull(pairing.getG2());
            assertNotNull(pairing.getGT());
            assertNotNull(pairing.getZr());
        } else
            pairing = null;
    }

    protected CurveParameters getCurveParameters() {
        return PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_181_603.properties");
    }
}
