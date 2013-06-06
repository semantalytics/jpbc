package it.unisa.dia.gas.plaf.jpbc.util.io.sector;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class ByteBufferLatchWeakRefBigIntegerFixedArraySector extends ByteBufferLatchWeakRefBigIntegerArraySector {

    public ByteBufferLatchWeakRefBigIntegerFixedArraySector(ByteBuffer buffer, int offset, int recordSize) {
        super(buffer, offset, recordSize);

        this.offset = offset;
    }

    public ByteBufferLatchWeakRefBigIntegerFixedArraySector(ByteBuffer buffer, int recordSize) {
        this(buffer, 0, recordSize);
    }

    public ByteBufferLatchWeakRefBigIntegerFixedArraySector(FileChannel channel, int offset, int recordSize, int numRecords) throws IOException {
        this(channel.map(FileChannel.MapMode.READ_ONLY, offset, ((recordSize + 4) * numRecords)), 0, recordSize);
    }

}
