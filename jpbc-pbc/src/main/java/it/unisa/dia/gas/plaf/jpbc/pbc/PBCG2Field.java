package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCElementType;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCPairingType;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCG2Field extends PBCField {


    public PBCG2Field(PBCPairingType pairing) {
        super(pairing);
    }


    public Element newElement() {
        PBCElementType element = new PBCElementType();
        PBCLibraryProvider.getPbcLibrary().pbc_element_init_G2(element, pairing);

        return new PBCCurvePointElement(element, this);
    }

}