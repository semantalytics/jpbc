package it.unisa.dia.gas.plaf.jpbc.field.generic;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingObjectInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingObjectOutput;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class GenericElementPowPreProcessing implements ElementPowPreProcessing {
    public static final int DEFAULT_K = 5;

    protected Field field;

    protected int k;
    protected int bits;
    protected int numLookups;
    protected Element table[][];

    public GenericElementPowPreProcessing() {
    }

    public GenericElementPowPreProcessing(Element g, int k) {
        this.field = g.getField();
        this.bits = field.getOrder().bitLength();
        this.k = k;

        initTable(g);
    }

    public GenericElementPowPreProcessing(Field field, int k, byte[] source, int offset) {
        this.field = field;
        this.bits = field.getOrder().bitLength();
        this.k = k;

        initTableFromBytes(source, offset);
    }

    public Element pow(BigInteger n) {
        return powBaseTable(n);
    }

    public Element powZn(Element n) {
        return pow(n.toBigInteger());
    }

    public byte[] toBytes() {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream(
                    field.getLengthInBytes() * table.length * table[0].length
            );
            for (Element[] row : table) {
                for (Element element : row) {
                    out.write(element.toBytes());
                }
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected void initTableFromBytes(byte[] source, int offset) {
        int lookupSize = 1 << k;
        numLookups = bits / k + 1;
        table = new Element[numLookups][lookupSize];

        for (int i = 0; i < numLookups; i++) {
            for (int j = 0; j < lookupSize; j++) {
                table[i][j] = field.newElement();
                offset += table[i][j].setFromBytes(source, offset);
            }
        }
    }

    /**
     * build k-bit base table for n-bit exponentiation w/ base a
     *
     * @param g an element
     */
    protected void initTable(Element g) {
        int lookupSize = 1 << k;

        numLookups = bits / k + 1;
        table = new Element[numLookups][lookupSize];

        Element multiplier = g.duplicate();

        for (int i = 0; i < numLookups; i++) {
            table[i][0] = field.newOneElement();

            for (int j = 1; j < lookupSize; j++) {
                table[i][j] = multiplier.duplicate().mul(table[i][j - 1]);
            }
            multiplier.mul(table[i][lookupSize - 1]);
        }
    }

    protected Element powBaseTable(BigInteger n) {
        /* early abort if raising to power 0 */
        if (n.signum() == 0) {
            return field.newOneElement();
        }

        if (n.compareTo(field.getOrder()) > 0)
            n = n.mod(field.getOrder());

        Element result = field.newOneElement();
        int numLookups = n.bitLength() / k + 1;

        for (int row = 0; row < numLookups; row++) {
            int word = 0;
            for (int s = 0; s < k; s++) {
                word |= (n.testBit(k * row + s) ? 1 : 0) << s;
            }

            if (word > 0) {
                result.mul(table[row][word]);
            }
        }

        return result;
    }


    public void writeExternal(ObjectOutput out) throws IOException {
        PairingObjectOutput pout = (PairingObjectOutput) out;

        pout.writeFieldIdentifier(field);
        pout.writeInt(bits);
        pout.writeInt(k);
        pout.writeBytes(toBytes());
    }

    public void readExternal(ObjectInput in) throws IOException, ClassNotFoundException {
        PairingObjectInput pin = (PairingObjectInput) in;

        this.field = pin.readField();
        this.bits = pin.readInt();
        this.k = pin.readInt();
        byte[] buffer = pin.readBytes();

        initTableFromBytes(buffer, 0);
    }


}
