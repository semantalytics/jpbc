package it.unisa.dia.gas.plaf.jpbc.pairing;

import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;
import junit.framework.TestCase;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PairingFactoryTest extends TestCase {

    public void testUsePBCWhenPossible() {
        PairingFactory.getInstance().setUsePBCWhenPossible(true);

        Pairing pairing1 = PairingFactory.getPairing("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties");
        Pairing pairing2 = PairingFactory.getPairing("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties");

        assertEquals(WrapperLibraryProvider.isAvailable(), pairing1 instanceof PBCPairing);
        assertEquals(WrapperLibraryProvider.isAvailable(), pairing2 instanceof PBCPairing);
    }

}
