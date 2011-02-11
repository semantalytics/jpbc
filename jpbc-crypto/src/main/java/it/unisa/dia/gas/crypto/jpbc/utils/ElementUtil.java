package it.unisa.dia.gas.crypto.jpbc.utils;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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

    public static byte[] aggregateBytes(byte[]... bytesArray) {
        // TODO: precompute the length
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            for (byte[] bytes : bytesArray) {
                out.write(bytes);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return out.toByteArray();
    }


}
