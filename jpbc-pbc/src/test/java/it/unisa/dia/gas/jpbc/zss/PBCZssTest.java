package it.unisa.dia.gas.jpbc.zss;

import it.unisa.dia.gas.plaf.jpbc.pbc.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCZssTest extends ZssTest {

    @Override
    protected void setUp() throws Exception {
        if (WrapperLibraryProvider.isAvailable()) {
            pairing = PairingFactory.getPairing("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_181_603.properties");
        } else
            pairing = null;
    }

}