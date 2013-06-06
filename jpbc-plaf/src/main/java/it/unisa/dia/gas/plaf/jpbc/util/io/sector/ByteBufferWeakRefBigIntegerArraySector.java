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
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class ByteBufferWeakRefBigIntegerArraySector implements ArraySector<BigInteger> {

    private ByteBuffer buffer;
    private int offset;
    private int recordSize;

    private int lengthInBytes;

    private PairingDataInput in;
    private PairingDataOutput out;

    private Map<Integer, SoftReference<BigInteger>> cache;


    public ByteBufferWeakRefBigIntegerArraySector(ByteBuffer buffer, int offset, int recordSize) {
        this.buffer = buffer;
        this.offset = offset + 4;
        this.recordSize = recordSize + 4;
        this.lengthInBytes = buffer.capacity();

        this.in = new PairingDataInput(new ByteBufferDataInput(buffer));
        this.out = new PairingDataOutput(new ByteBufferDataOutput(buffer));
        this.cache = new ConcurrentHashMap<Integer, SoftReference<BigInteger>>();
    }

    public ByteBufferWeakRefBigIntegerArraySector(ByteBuffer buffer, int recordSize) {
        this(buffer, 0, recordSize);
    }

    public ByteBufferWeakRefBigIntegerArraySector(FileChannel channel, int offset, int recordSize, int numRecords) throws IOException {
        this(channel.map(FileChannel.MapMode.READ_ONLY, offset, 4 + ((recordSize + 4) * numRecords)), 0, recordSize);
    }


    public int getLengthInBytes() {
        return lengthInBytes;
    }

    public int getSize() {
        throw new IllegalStateException();
    }

    public synchronized BigInteger getAt(int index) {
        BigInteger result = null;
        SoftReference<BigInteger> sr = cache.get(index);

        if (sr != null)
            result = sr.get();

        if (result == null) {
            try {
                buffer.position(offset + (index * recordSize));
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
            buffer.position(offset + (index * recordSize));
            out.writeBigInteger(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
