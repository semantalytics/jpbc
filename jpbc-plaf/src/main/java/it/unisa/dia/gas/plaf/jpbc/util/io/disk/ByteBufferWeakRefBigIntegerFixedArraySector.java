package it.unisa.dia.gas.plaf.jpbc.util.io.disk;

import java.io.IOException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class ByteBufferWeakRefBigIntegerFixedArraySector extends ByteBufferWeakRefBigIntegerArraySector {


    public ByteBufferWeakRefBigIntegerFixedArraySector(int recordSize, int numRecords) throws IOException {
        super(recordSize, numRecords);

        this.lengthInBytes = ((recordSize + 4) * numRecords);
        this.offset = 0;
    }

    public ByteBufferWeakRefBigIntegerFixedArraySector(int recordSize, int numRecords, String... labels) throws IOException {
        super(recordSize, numRecords, labels);

        this.lengthInBytes = ((recordSize + 4) * numRecords);
        this.offset = 0;
    }

}
