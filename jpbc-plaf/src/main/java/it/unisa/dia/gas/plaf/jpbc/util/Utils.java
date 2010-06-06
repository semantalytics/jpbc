package it.unisa.dia.gas.plaf.jpbc.util;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class Utils {

    public static byte[] copyOf(byte[] original, int newLength) {
        int len = Math.min(original.length, newLength);
        byte[] copy = new byte[len];
        System.arraycopy(original, 0, copy, 0, len);
        return copy;
    }

    public static byte[] copyOf(byte[] original, int offset, int newLength) {
        int len = Math.min(original.length - offset, newLength);
        byte[] copy = new byte[len];
        System.arraycopy(original, offset, copy, 0, len);
        return copy;
    }

}
