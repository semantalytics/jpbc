package it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.util.collection.LatchHashMap;
import it.unisa.dia.gas.plaf.jpbc.util.io.sector.*;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMLazyMapParameters extends CTL13MMMapParameters {

    protected Disk<ArraySector<BigInteger>> disk;


    public CTL13MMLazyMapParameters(CTL13MMInstanceParameters parameters) {
        super(new LatchHashMap<String, Object>(), parameters);
    }

    public CTL13MMLazyMapParameters(PairingParameters parameters) {
        super(new LatchHashMap<String, Object>(), new CTL13MMInstanceParameters(parameters));
    }


    public BigInteger getBigInteger(String key) {
        System.out.println("GET key = [" + key + "]");

        return disk.getSector("header").getAt(key);
    }

    public BigInteger getBigIntegerAt(String key, int index) {
        System.out.println("GET key = [" + key + "], index = [" + index + "]");
        return disk.getSector(key).getAt(index);
    }

    public void putBigInteger(String key, BigInteger value) {
        System.out.println("PUT key = [" + key + "]");

        disk.getSector("header").setAt(key, value);
    }

    public void putBigIntegerAt(String key, int index, BigInteger value) {
        System.out.println("PUT key = [" + key + "], index = [" + index + "]");

        disk.getSector(key).setAt(index, value);
    }


    public void init() {
        try {
            int x0Length = (parameters.getEta() * parameters.getN() + 7) / 8;
            int gLength = (parameters.getAlpha() + 7) / 8;
            int pLength = (parameters.getEta() + 7) / 8;

            FileChannelDisk<ArraySector<BigInteger>> fileChannelDisk = new FileChannelDisk<ArraySector<BigInteger>>();
            fileChannelDisk.addSector("header", new ByteBufferLatchWeakRefBigIntegerFixedArraySector(x0Length, 5, "x0", "y", "pzt", "z", "zInv"))
                    .addSector("xsp", new ByteBufferLatchWeakRefBigIntegerArraySector(x0Length, parameters.getEll()))
                    .addSector("crtCoefficients", new ByteBufferLatchWeakRefBigIntegerArraySector(x0Length, parameters.getN()))
                    .addSector("xs", new ByteBufferLatchWeakRefBigIntegerArraySector(x0Length, parameters.getDelta() * 2))
                    .addSector("gs", new ByteBufferLatchWeakRefBigIntegerArraySector(gLength, parameters.getN()))
                    .addSector("ps", new ByteBufferLatchWeakRefBigIntegerArraySector(pLength, parameters.getN()))
                    .mapTo(String.format(
                            "CTL13IP_eta_%d_n_%d_alpha_%d_ell_%d_rho_%d_delta_%d_kappa_%d_beta_%d_theta_%d_bound_%d.dat",
                            parameters.getEta(), parameters.getN(), parameters.getAlpha(), parameters.getEll(),
                            parameters.getRho(), parameters.getDelta(),
                            parameters.getKappa(), parameters.getBeta(), parameters.getTheta(), parameters.getBound())
                    );

            this.disk = fileChannelDisk;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void store(String fileName) {
        try {
            disk.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean load(String path) {
        try {
            if (!new File(path).exists())
                return false;

            int x0Length = (parameters.getEta() * parameters.getN() + 7) / 8;
            int gLength = (parameters.getAlpha() + 7) / 8;
            int pLength = (parameters.getEta() + 7) / 8;

            FileChannelDisk<ArraySector<BigInteger>> fileChannelDisk = new FileChannelDisk<ArraySector<BigInteger>>();
            fileChannelDisk.addSector("header", new ByteBufferWeakRefBigIntegerFixedArraySector(x0Length, 5, "x0", "y", "pzt", "z", "zInv"))
                    .addSector("xsp", new ByteBufferWeakRefBigIntegerArraySector(x0Length, parameters.getEll()))
                    .addSector("crtCoefficients", new ByteBufferWeakRefBigIntegerArraySector(x0Length, parameters.getN()))
                    .addSector("xs", new ByteBufferWeakRefBigIntegerArraySector(x0Length, parameters.getDelta() * 2))
                    .addSector("gs", new ByteBufferWeakRefBigIntegerArraySector(gLength, parameters.getN()))
                    .addSector("ps", new ByteBufferWeakRefBigIntegerArraySector(pLength, parameters.getN()))
                    .mapTo(new FileInputStream(path).getChannel());

            putObject("params", parameters);
            putObject("x0", fileChannelDisk.getSector("header").getAt(0));
            putObject("y", fileChannelDisk.getSector("header").getAt(1));
            putObject("pzt", fileChannelDisk.getSector("header").getAt(2));
            putObject("z", fileChannelDisk.getSector("header").getAt(3));
            putObject("zInv", fileChannelDisk.getSector("header").getAt(4));

            this.disk = fileChannelDisk;

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}