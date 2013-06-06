package it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.util.io.ByteBufferDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataOutput;
import it.unisa.dia.gas.plaf.jpbc.util.io.sector.ArraySector;
import it.unisa.dia.gas.plaf.jpbc.util.io.sector.ByteBufferWeakRefBigIntegerArraySector;

import java.io.*;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.HashMap;
import java.util.Map;

import static java.nio.channels.FileChannel.MapMode.READ_ONLY;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMLazyMapParameters extends CTL13MMMapParameters {

    protected Map<String, ArraySector<BigInteger>> sectors;


    public CTL13MMLazyMapParameters(CTL13MMInstanceParameters parameters) {
        super(parameters);

        this.sectors = new HashMap<String, ArraySector<BigInteger>>();
    }

    public CTL13MMLazyMapParameters(PairingParameters parameters) {
        this(new CTL13MMInstanceParameters(parameters));
    }


    public BigInteger getBigIntegerAt(String key, int index) {
        return sectors.get(key).getAt(index);
    }

    public void init() {
        try {
            RandomAccessFile f = new RandomAccessFile("t", "rw");
            f.setLength(1024 * 1024 * 1024);


        } catch (Exception e) {
            System.err.println(e);
        }
    }

    public void store(String fileName) {
        try {
            DataOutputStream os = new DataOutputStream(new FileOutputStream(fileName));
            PairingDataOutput pdo = new PairingDataOutput(os);

            int x0NumBytes = getBigInteger("x0").toByteArray().length;

            pdo.writeBigInteger(getBigInteger("x0"));
            pdo.writeBigInteger(getBigInteger("y"), x0NumBytes);
            pdo.writeBigInteger(getBigInteger("pzt"), x0NumBytes);
            pdo.writeBigInteger(getBigInteger("z"), x0NumBytes);
            pdo.writeBigInteger(getBigInteger("zInv"), x0NumBytes);

            pdo.writeBigIntegers((BigInteger[]) getObject("xsp"), x0NumBytes);
            pdo.writeBigIntegers((BigInteger[]) getObject("crtCoefficients"), x0NumBytes);
            pdo.writeBigIntegers((BigInteger[]) getObject("xs"), x0NumBytes);
            pdo.writeBigIntegers((BigInteger[]) getObject("gs"), (parameters.getAlpha() + 7) / 8);
            pdo.writeBigIntegers((BigInteger[]) getObject("ps"), (parameters.getEta() + 7) / 8);

            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean load(String path) {
        try {
            if (!new File(path).exists())
                return false;

            FileChannel channel = new FileInputStream(path).getChannel();
            ByteBuffer vector = channel.map(READ_ONLY, 0, channel.size());
            PairingDataInput dos = new PairingDataInput(new ByteBufferDataInput(vector));

            BigInteger x0 = dos.readBigInteger();
            BigInteger y = dos.readBigInteger();
            BigInteger pzt = dos.readBigInteger();
            BigInteger z = dos.readBigInteger();
            BigInteger zInv = dos.readBigInteger();

            put("params", parameters);

            put("x0", x0);
            put("y", y);
            put("pzt", pzt);
            put("z", z);
            put("zInv", zInv);

            int x0Length = x0.toByteArray().length;
            int cursor = (x0Length + 4) * 5;

            ArraySector<BigInteger> sector = new ByteBufferWeakRefBigIntegerArraySector(channel, cursor, x0Length, parameters.getEll());
            cursor += sector.getLengthInBytes();
            sectors.put("xsp", sector);

            sector = new ByteBufferWeakRefBigIntegerArraySector(channel, cursor, x0Length, parameters.getN());
            cursor += sector.getLengthInBytes();
            sectors.put("crtCoefficients", sector);

            sector = new ByteBufferWeakRefBigIntegerArraySector(channel, cursor, x0Length, parameters.getDelta() * 2);
            cursor += sector.getLengthInBytes();
            sectors.put("xs", sector);

            sector = new ByteBufferWeakRefBigIntegerArraySector(channel, cursor, (parameters.getAlpha() + 7) / 8, parameters.getN());
            cursor += sector.getLengthInBytes();
            sectors.put("gs", sector);

            sector = new ByteBufferWeakRefBigIntegerArraySector(channel, cursor, (parameters.getEta() + 7) / 8, parameters.getN());
            sectors.put("gs", sector);

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}