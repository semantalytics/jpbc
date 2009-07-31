package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Memory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairingType extends Memory {

    public PBCPairingType(String buf) {
        super(PBCLibraryProvider.getPbcLibrary().pbc_pairing_sizeof());
        PBCLibraryProvider.getPbcLibrary().pbc_pairing_init_inp_buf(this, buf, buf.length());
    }


    @Override
    protected void finalize() {
        PBCLibraryProvider.getPbcLibrary().pbc_pairing_clear(this);
        super.finalize();
    }
}