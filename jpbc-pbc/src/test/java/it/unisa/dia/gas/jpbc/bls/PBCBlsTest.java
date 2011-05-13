package it.unisa.dia.gas.jpbc.bls;

import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCBlsTest extends BlsTest {

    @Override
    protected void setUp() throws Exception {
        if (WrapperLibraryProvider.isAvailable()) {
            pairing = new PBCPairing(PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_181_603.properties"));
        } else
            pairing = null;
    }

}