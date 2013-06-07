package it.unisa.dia.gas.plaf.jpbc.util.io.sector;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class ByteBufferLatchWeakRefBigIntegerArraySector extends ByteBufferWeakRefBigIntegerArraySector {

    protected Map<Integer, FlagLatch> latches;


    public ByteBufferLatchWeakRefBigIntegerArraySector(int recordSize, int numRecords) throws IOException {
        super(recordSize, numRecords);

        this.latches = new FlagLatchMap<Integer>();
    }

    public ByteBufferLatchWeakRefBigIntegerArraySector(int recordSize, int numRecords, String... labels) throws IOException {
        super(recordSize, numRecords, labels);

        this.latches = new FlagLatchMap<Integer>();
    }


    public BigInteger getAt(int index) {
        latches.get(index).get();

        BigInteger result = null;
        SoftReference<BigInteger> sr = cache.get(index);

        if (sr != null)
            result = sr.get();

        if (result == null) {
            synchronized (this) {
                try {
                    buffer.position(offset + (index * recordLength));
                    result = in.readBigInteger();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            cache.put(index, new SoftReference<BigInteger>(result));
        }

        return result;
    }

    public void setAt(int index, BigInteger value) {
        cache.put(index, new SoftReference<BigInteger>(value));

        synchronized (this) {
            try {
                buffer.position(offset + (index * recordLength));
                out.writeBigInteger(value, recordSize);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        latches.get(index).set();
    }


    class FlagLatchMap<K> extends HashMap<K, FlagLatch> {

        @Override
        public FlagLatch get(Object key) {
            if (containsKey(key))
                return super.get(key);

            FlagLatch latch = new FlagLatch(key);
            put((K) key, latch);
            return latch;
        }
    }

    class FlagLatch extends CountDownLatch {

        Object K;

        FlagLatch(Object K) {
            super(1);
            this.K = K;
        }

        void set() {
            countDown();
        }

        void get() {
            try {
                await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
