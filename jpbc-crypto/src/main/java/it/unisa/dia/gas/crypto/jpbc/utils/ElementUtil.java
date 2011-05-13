package it.unisa.dia.gas.crypto.jpbc.utils;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPow;
import it.unisa.dia.gas.jpbc.Pairing;

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

    public static ElementPow[] cloneToElementPow(Element[] source) {
        ElementPow[] target = new ElementPow[source.length];

        for (int i = 0; i < target.length; i++) {
            target[i] = source[i].pow();
        }

        return target;
    }

    public static Element randomIn(Pairing pairing, Element generator) {
        return generator.powZn(pairing.getZr().newRandomElement());
    }

    public static byte[] toBytes(Element[] array) {
        byte[] result = new byte[array[0].getLengthInBytes() * array.length];

        int offset = 0;
        for (Element element : array) {
            byte[] temp = element.toBytes();
            System.arraycopy(temp, 0, result, offset, temp.length);
            offset+=temp.length;
        }

        return result;
    }


}
