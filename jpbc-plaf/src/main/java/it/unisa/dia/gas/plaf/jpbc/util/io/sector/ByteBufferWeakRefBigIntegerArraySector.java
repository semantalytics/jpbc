package it.unisa.dia.gas.plaf.jpbc.util.io.sector;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class ByteBufferWeakRefBigIntegerArraySector extends ByteBufferBigIntegerArraySector {

    protected Map<Integer, SoftReference<BigInteger>> cache;


    public ByteBufferWeakRefBigIntegerArraySector(int recordSize, int numRecords) throws IOException {
        super(recordSize, numRecords);

        this.cache = new ConcurrentHashMap<Integer, SoftReference<BigInteger>>();
    }

    public ByteBufferWeakRefBigIntegerArraySector(int recordSize, int numRecords, String... labels) throws IOException {
        super(recordSize, numRecords, labels);

        this.cache = new ConcurrentHashMap<Integer, SoftReference<BigInteger>>();
    }


    public synchronized BigInteger getAt(int index) {
        BigInteger result = null;
        SoftReference<BigInteger> sr = cache.get(index);

        if (sr != null)
            result = sr.get();

        if (result == null) {
            try {
                buffer.position(offset + (index * recordLength));
                result = in.readBigInteger();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            cache.put(index, new SoftReference<BigInteger>(result));
        }

        return result;
    }

    public synchronized void setAt(int index, BigInteger value) {
        cache.put(index, new SoftReference<BigInteger>(value));

        try {
            buffer.position(offset + (index * recordLength));
            out.writeBigInteger(value, recordSize);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}