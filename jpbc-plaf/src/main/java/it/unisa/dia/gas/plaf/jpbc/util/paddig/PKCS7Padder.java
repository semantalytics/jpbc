package it.unisa.dia.gas.plaf.jpbc.util.paddig;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PKCS7Padder implements Padder {

    public void init(SecureRandom random) {
    }

    public int addPadding(byte[] in, int inOff) {
        byte code = (byte) (in.length - inOff);

        while (inOff < in.length) {
            in[inOff] = code;
            inOff++;
        }

        return code;
    }

    public int padCount(byte[] in) {
        int count = in[in.length - 1] & 0xff;

        if (count > in.length || count == 0) {
            throw new IllegalArgumentException("pad block corrupted");
        }

        for (int i = 1; i <= count; i++) {
            if (in[in.length - i] != count) {
                throw new IllegalArgumentException("pad block corrupted");
            }
        }

        return count;
    }
}
