package it.unisa.dia.gas.plaf.jpbc.pbc;

import com.sun.jna.Pointer;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.MPZElementType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.PBCElementPPType;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElementPowPreProcessing implements ElementPowPreProcessing {
    protected Field field;
    protected PBCElementPPType elementPPType;


    public PBCElementPowPreProcessing(Field field, Pointer element) {
        this.field = field;
        this.elementPPType = new PBCElementPPType(element);
    }


    public Element pow(BigInteger n) {
        PBCElement result = (PBCElement) field.newElement();
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_pow(result.value, MPZElementType.fromBigInteger(n), elementPPType);

        return result;
    }

    public Element powZn(Element n) {
        PBCElement result = (PBCElement) field.newElement();
        WrapperLibraryProvider.getWrapperLibrary().pbc_element_pp_pow_zn(result.value, ((PBCElement) n).value, elementPPType);

        return result;
    }
}
