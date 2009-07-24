package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingTest;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairingTest extends PairingTest {

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

    @Override
    protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();
//            curveParams.load(PBCPairingTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_603_181.properties"));
        curveParams.load(PBCPairingTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/d_9563.properties"));
        return curveParams;
    }
}
