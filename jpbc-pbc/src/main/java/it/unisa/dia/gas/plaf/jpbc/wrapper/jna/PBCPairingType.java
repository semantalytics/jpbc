package it.unisa.dia.gas.plaf.jpbc.wrapper.jna;

import com.sun.jna.Memory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairingType extends Memory {

    public PBCPairingType(String buf) {
        super(WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_sizeof());
        WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_init_inp_buf(this, buf, buf.length());
    }


    @Override
    protected void finalize() {
        if (valid()) {
            WrapperLibraryProvider.getWrapperLibrary().pbc_pairing_clear(this);
        }
//        super.finalize();
    }
}