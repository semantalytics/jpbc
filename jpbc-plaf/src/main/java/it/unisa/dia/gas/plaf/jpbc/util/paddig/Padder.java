package it.unisa.dia.gas.plaf.jpbc.util.paddig;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface Padder {

    void init(SecureRandom random);

    int addPadding(byte[] in, int inOff);

    int padCount(byte[] in);

}
