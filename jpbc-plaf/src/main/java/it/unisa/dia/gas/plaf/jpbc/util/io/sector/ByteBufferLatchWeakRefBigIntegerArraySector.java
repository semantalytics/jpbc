package it.unisa.dia.gas.plaf.jpbc.util.io.sector;

import it.unisa.dia.gas.plaf.jpbc.util.io.ByteBufferDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.ByteBufferDataOutput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataOutput;

import java.io.IOException;
import java.lang.ref.SoftReference;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class ByteBufferLatchWeakRefBigIntegerArraySector implements ArraySector<BigInteger> {

    protected ByteBuffer buffer;
    protected int offset;
    protected int recordSize;

    protected int lengthInBytes;

    protected PairingDataInput in;
    protected PairingDataOutput out;

    protected Map<Integer, FlagLatch> latches;
    protected Map<Integer, SoftReference<BigInteger>> cache;


    public ByteBufferLatchWeakRefBigIntegerArraySector(ByteBuffer buffer, int offset, int recordSize) {
        this.buffer = buffer;
        this.offset = offset + 4;
        this.recordSize = recordSize + 4;
        this.lengthInBytes = buffer.capacity();

        this.in = new PairingDataInput(new ByteBufferDataInput(buffer));
        this.out = new PairingDataOutput(new ByteBufferDataOutput(buffer));
        this.latches = new FlagLatchMap<Integer>();
        this.cache = new ConcurrentHashMap<Integer, SoftReference<BigInteger>>();
    }

    public ByteBufferLatchWeakRefBigIntegerArraySector(ByteBuffer buffer, int recordSize) {
        this(buffer, 0, recordSize);
    }

    public ByteBufferLatchWeakRefBigIntegerArraySector(FileChannel channel, int offset, int recordSize, int numRecords) throws IOException {
        this(channel.map(FileChannel.MapMode.READ_ONLY, offset, 4 + ((recordSize + 4) * numRecords)), 0, recordSize);
    }


    public int getLengthInBytes() {
        return lengthInBytes;
    }

    public int getSize() {
        throw new IllegalStateException();
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
                    buffer.position(offset + (index * recordSize));
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
                buffer.position(offset + (index * recordSize));
                out.writeBigInteger(value);
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
