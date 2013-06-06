package it.unisa.dia.gas.plaf.jpbc.util.io.sector;

import it.unisa.dia.gas.plaf.jpbc.util.io.ByteBufferDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.ByteBufferDataOutput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataOutput;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class ByteBufferBigIntegerArraySector implements ArraySector<BigInteger> {

    private ByteBuffer buffer;
    private int offset;
    private int recordSize;

    private int lengthInBytes;

    private PairingDataInput in;
    private PairingDataOutput out;

    public ByteBufferBigIntegerArraySector(ByteBuffer buffer, int offset, int recordSize) {
        this.buffer = buffer;
        this.offset = offset + 4;
        this.recordSize = recordSize + 4;
        this.lengthInBytes = buffer.capacity();

        this.in = new PairingDataInput(new ByteBufferDataInput(buffer));
        this.out = new PairingDataOutput(new ByteBufferDataOutput(buffer));
    }

    public ByteBufferBigIntegerArraySector(ByteBuffer buffer, int recordSize) {
        this(buffer, 0, recordSize);
    }

    public ByteBufferBigIntegerArraySector(FileChannel channel, int offset, int recordSize, int numRecords) throws IOException {
        this.lengthInBytes = 4 + ((recordSize + 4) * numRecords);

        this.buffer = channel.map(FileChannel.MapMode.READ_ONLY, offset, lengthInBytes);
        this.offset = 4;
        this.recordSize = recordSize + 4;

        this.in = new PairingDataInput(new ByteBufferDataInput(buffer));
        this.out = new PairingDataOutput(new ByteBufferDataOutput(buffer));
    }


    public int getLengthInBytes() {
        return lengthInBytes;
    }

    public int getSize() {
        throw new IllegalStateException();
    }

    public synchronized BigInteger getAt(int index) {
        try {
            buffer.position(offset + (index * recordSize));
            return in.readBigInteger();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public synchronized void setAt(int index, BigInteger value) {
        try {
            buffer.position(offset + (index * recordSize));
            out.writeBigInteger(value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
