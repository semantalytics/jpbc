package it.unisa.dia.gas.plaf.jpbc.pbc.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCElement;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCField;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCElementType;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCPairingType;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCGTField extends PBCField {


    public PBCGTField(PBCPairingType pairing) {
        super(pairing);
    }


    public Element newElement() {
        PBCElementType element = new PBCElementType();
        PBCLibraryProvider.getPbcLibrary().pbc_element_init_GT(element, pairing);

        return new PBCElement(element, this);
    }

}