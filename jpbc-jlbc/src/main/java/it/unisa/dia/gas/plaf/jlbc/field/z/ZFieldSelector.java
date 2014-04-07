package it.unisa.dia.gas.plaf.jlbc.field.z;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.z.SymmetricZrField;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ZFieldSelector {

    private static ZFieldSelector INSTANCE = new ZFieldSelector();

    public static ZFieldSelector getInstance() {
        return INSTANCE;
    }

    private ZFieldSelector() {
    }


    public Field getSymmetricZrFieldPowerOfTwo(SecureRandom random, int k) {
        if (k <= 30) {
            return new SymmetricLongZrField(random, 1 << k);
        } else
            return new SymmetricZrField(random, BigInteger.ONE.shiftLeft(k));
    }
}
