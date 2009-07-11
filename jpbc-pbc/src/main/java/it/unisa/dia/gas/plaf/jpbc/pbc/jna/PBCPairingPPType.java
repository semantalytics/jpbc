package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Memory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairingPPType extends Memory {

    public PBCPairingPPType() {
        super(PBCLibraryProvider.getPbcLibrary().pbc_pairing_pp_sizeof());
    }

}