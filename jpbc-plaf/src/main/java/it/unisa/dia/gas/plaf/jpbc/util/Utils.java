package it.unisa.dia.gas.plaf.jpbc.util;

import java.util.Properties;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class Utils {

    public static String getProperty(Properties properties, String key) {
        String value = properties.getProperty(key);
        if (value == null)
            throw new IllegalArgumentException("Cannot find value for the following key : " + key);

        return value;
    }

    public static byte[] copyOf(byte[] original, int newLength) {
        int len = Math.min(original.length, newLength);
        byte[] copy = new byte[len];
        System.arraycopy(original, 0, copy, 0, len);
        return copy;
    }

}
