package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Memory;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElementType extends Memory {

    public PBCElementType() {
        super(PBCLibrary.INSTANCE.pbc_element_sizeof());
    }

    @Override
    protected void finalize() {
        System.out.println("PBCElementType S: finalize!!");

        PBCLibrary.INSTANCE.pbc_element_clear(this);
        super.finalize();

        System.out.println("PBCElementType E: finalize!!");
    }
}
