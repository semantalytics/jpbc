package it.unisa.dia.gas.plaf.jpbc.util.io.sector;

import it.unisa.dia.gas.plaf.jpbc.util.collection.FlagMap;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class ByteBufferLatchWeakRefBigIntegerArraySector extends ByteBufferWeakRefBigIntegerArraySector {

    protected FlagMap<Integer> flags;


    public ByteBufferLatchWeakRefBigIntegerArraySector(int recordSize, int numRecords) throws IOException {
        super(recordSize, numRecords);

        this.flags = new FlagMap<Integer>();
    }

    public ByteBufferLatchWeakRefBigIntegerArraySector(int recordSize, int numRecords, String... labels) throws IOException {
        super(recordSize, numRecords, labels);

        this.flags = new FlagMap<Integer>();
    }


    public BigInteger getAt(int index) {
        flags.get(index);

        BigInteger result = null;
        synchronized (this) {
            SoftReference<BigInteger> sr = cache.get(index);

            if (sr != null)
                result = sr.get();

            if (result == null) {
                try {
                    System.out.printf("GET index %d, offset %d, recordSize %d, limit %d \n", index, offset + (index * recordLength), recordLength, buffer.limit());
                    buffer.position(offset + (index * recordLength));
                    result = in.readBigInteger();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                cache.put(index, new SoftReference<BigInteger>(result));
            } else
                System.out.printf("GET SUCCESS index %d, offset %d, recordSize %d, limit %d \n", index, offset + (index * recordLength), recordLength, buffer.limit());
        }

        return result;
    }

    public void setAt(int index, BigInteger value) {
        synchronized (this) {
            cache.put(index, new SoftReference<BigInteger>(value));
            try {
                System.out.printf("SET index %d, offset %d, recordSize %d, limit %d \n", index, offset + (index * recordLength), recordLength, buffer.limit());
                buffer.position(offset + (index * recordLength));
                out.writeBigInteger(value, recordSize);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        flags.set(index);
    }

}
