package it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.AbstractElementCipher;
import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorElement;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.math.BigInteger;
import java.util.BitSet;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2ErrorTolerantOneTimePad extends AbstractElementCipher {

    protected VectorElement key;

    @Override
    public ElementCipher init(Element key) {
        this.key = (VectorElement) key;

        return this;
    }

    @Override
    public Element processBytes(byte[] buffer) {
        BigInteger order = key.getField().getTargetField().getOrder();
        Element halfOrder = key.getField().getTargetField().newElement(order.divide(BigIntegerUtils.TWO));
        VectorElement r = key.duplicate();

        BitSet bits = BitSet.valueOf(buffer);
        for (int i = 0; i < key.getSize(); i++) {
            if (bits.get(i))
                r.getAt(i).add(halfOrder);
        }

        return r;
    }

    @Override
    public byte[] processElementsToBytes(Element... input) {
        BigInteger order = key.getField().getTargetField().getOrder();
        BigInteger oneFourthOrder = order.divide(BigIntegerUtils.FOUR);

        VectorElement r = (VectorElement) input[0].duplicate().sub(key);

        BitSet bits = new BitSet(key.getSize());
        for (int i = 0; i < key.getSize(); i++) {
            bits.set(i, r.getAt(i).toBigInteger().abs().compareTo(oneFourthOrder) >= 0 );
        }

        return bits.toByteArray();
    }
}
