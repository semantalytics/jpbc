package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEAttributes {

    public static byte[] attributesPatternToByte(int... attributesPattern) {
        byte[] result = new byte[attributesPattern.length];
        for (int i = 0; i < attributesPattern.length; i++) {
            result[i] = (byte) attributesPattern[i];
        }

        return result;
    }

}
