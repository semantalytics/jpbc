package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCElementType;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibrary;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCPairingType;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCZrField extends PBCField {


    public PBCZrField(PBCPairingType pairing) {
        super(pairing);
    }


    public Element newElement() {
        PBCElementType element = new PBCElementType();
        PBCLibrary.INSTANCE.pbc_element_init_Zr(element, pairing);

        return new PBCElement(element, this);
    }

}