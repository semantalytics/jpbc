package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Memory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairingType extends Memory {

    public PBCPairingType() {
        super(PBCLibrary.INSTANCE.pbc_pairing_sizeof());
    }

}