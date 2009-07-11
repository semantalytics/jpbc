package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Memory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairingType extends Memory {

    public PBCPairingType() {
        super(PBCLibraryProvider.getPbcLibrary().pbc_pairing_sizeof());
    }


    @Override
    protected void finalize() {
        PBCLibraryProvider.getPbcLibrary().pbc_pairing_clear(this);
        super.finalize();
    }
}