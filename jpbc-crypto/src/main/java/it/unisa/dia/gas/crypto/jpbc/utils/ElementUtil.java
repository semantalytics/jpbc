package it.unisa.dia.gas.crypto.jpbc.utils;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ElementUtil {
    
    public static Element[] cloneImmutably(Element[] source) {
        Element[] target = Arrays.copyOf(source, source.length);

        for (int i = 0; i < target.length; i++) {
            Element uElement = target[i];
            if (uElement != null && !uElement.isImmutable())
                target[i] = target[i].getImmutable();
        }

        return target;
    }
    
}
