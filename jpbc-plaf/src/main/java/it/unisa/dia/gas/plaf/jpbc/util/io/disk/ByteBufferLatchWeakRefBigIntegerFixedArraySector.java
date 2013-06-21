package it.unisa.dia.gas.plaf.jpbc.util.io.disk;

import it.unisa.dia.gas.plaf.jpbc.util.io.ByteBufferDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.ByteBufferDataOutput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataOutput;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class ByteBufferLatchWeakRefBigIntegerFixedArraySector extends ByteBufferLatchWeakRefBigIntegerArraySector {

    public ByteBufferLatchWeakRefBigIntegerFixedArraySector(int recordSize, int numRecords) throws IOException {
        super(recordSize, numRecords);

        this.lengthInBytes = ((recordSize + 4) * numRecords);
        this.offset = 0;
    }

    public ByteBufferLatchWeakRefBigIntegerFixedArraySector(int recordSize, int numRecords, String... labels) throws IOException {
        super(recordSize, numRecords, labels);

        this.lengthInBytes = ((recordSize + 4) * numRecords);
        this.offset = 0;
    }


    public ArraySector<BigInteger> mapTo(Mode mode, ByteBuffer buffer) {
        this.buffer = buffer;
        this.in = new PairingDataInput(new ByteBufferDataInput(buffer));
        this.out = new PairingDataOutput(new ByteBufferDataOutput(buffer));

        return this;
    }

}
