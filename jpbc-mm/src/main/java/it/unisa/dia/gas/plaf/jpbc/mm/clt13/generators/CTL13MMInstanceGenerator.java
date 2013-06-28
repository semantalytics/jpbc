package it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMMapParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.MutablePairingParameters;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.getRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMInstanceGenerator implements PairingParametersGenerator {

    protected SecureRandom random;
    protected CTL13MMInstanceParameters parameters;
    protected boolean storeGeneratedInstance;


    public CTL13MMInstanceGenerator(SecureRandom random, CTL13MMInstanceParameters parameters) {
        this(random, parameters, true);
    }

    public CTL13MMInstanceGenerator(SecureRandom random, PairingParameters parameters) {
        this(random, new CTL13MMInstanceParameters(parameters), true);
    }

    public CTL13MMInstanceGenerator(SecureRandom random, CTL13MMInstanceParameters parameters, boolean storeGeneratedInstance) {
        this.random = random;
        this.parameters = parameters;
        this.storeGeneratedInstance = storeGeneratedInstance;
    }

    public CTL13MMInstanceGenerator(SecureRandom random, PairingParameters parameters, boolean storeGeneratedInstance) {
        this(random, new CTL13MMInstanceParameters(parameters), storeGeneratedInstance);
    }


    public PairingParameters generate() {
        CTL13MMMapParameters mapParameters = newCTL13MMMapParameters();
        if (storeGeneratedInstance) {
            if (mapParameters.load())
                return mapParameters;
        }

        mapParameters.init();
        generateInternal(mapParameters);

        if (storeGeneratedInstance)
            mapParameters.store();

        return mapParameters;
    }

    protected CTL13MMMapParameters newCTL13MMMapParameters() {
        return new CTL13MMMapParameters(parameters);
    }

    protected void generateInternal(MutablePairingParameters mapParameters) {
        long start = System.currentTimeMillis();

        // Generate CRT modulo x0
        BigInteger x0 = BigInteger.ONE;
        for (int i = 0; i < parameters.getN(); i++) {
            BigInteger ps = BigInteger.probablePrime(parameters.getEta(), random);
            mapParameters.putBigIntegerAt("ps", i, ps);
            x0 = x0.multiply(ps);
        }
        mapParameters.putBigInteger("x0", x0);

        // Generate CRT Coefficients
        for (int i = 0; i < parameters.getN(); i++) {
            BigInteger temp = x0.divide(mapParameters.getBigIntegerAt("ps", i));
            temp = temp.modInverse(mapParameters.getBigIntegerAt("ps", i)).multiply(temp);
            mapParameters.putBigIntegerAt("crtCoefficients", i, temp);
        }

        // Generate g_i's
        for (int i = 0; i < parameters.getN(); i++) {
            BigInteger gs = BigInteger.probablePrime(parameters.getAlpha(), random);
            mapParameters.putBigIntegerAt("gs", i, gs);
        }

        // Generate z
        BigInteger z, zInv;
        do {
            z = BigIntegerUtils.getRandom(x0, random);
            zInv = z.modInverse(x0);
        } while (zInv.equals(BigInteger.ZERO));
        mapParameters.putBigInteger("z", z);
        mapParameters.putBigInteger("zInv", zInv);

        BigInteger temp = BigInteger.ONE;
        for (int i = 0; i < parameters.getKappa(); i++) {
            temp = temp.multiply(zInv).mod(x0);
            mapParameters.putBigIntegerAt("zInvPow", i, temp);
        }

        // Generate xp_i's
        for (int i = 0; i < parameters.getEll(); i++) {
            // xsp[i] = encodeAt(0);
            BigInteger xsp = BigInteger.ZERO;
            for (int j = 0; j < parameters.getN(); j++) {
                xsp = xsp.add(
                        mapParameters.getBigIntegerAt("gs", j)
                                .multiply(getRandom(parameters.getRho(), random))
                                .add(getRandom(parameters.getAlpha(), random))
                                .multiply(mapParameters.getBigIntegerAt("crtCoefficients", j))
                );
            }
            xsp = xsp.mod(x0);
            mapParameters.putBigIntegerAt("xsp", i, xsp);
        }

        // Generate y = encodeOneAt(1)
        BigInteger y = BigInteger.ZERO;
        for (int i = 0; i < parameters.getN(); i++) {
            y = y.add(
                    mapParameters.getBigIntegerAt("gs", i).multiply(getRandom(parameters.getRho(), random))
                            .add(BigInteger.ONE)
                            .multiply(mapParameters.getBigIntegerAt("crtCoefficients", i))
            );
        }
        y = y.multiply(zInv).mod(x0);
        mapParameters.putBigInteger("y", y);

        BigInteger yPow = BigInteger.ONE;
        mapParameters.putBigIntegerAt("yPow", 0, yPow);
        for (int i = 1; i <= parameters.getKappa(); i++) {
            yPow = yPow.multiply(y).mod(x0);
            mapParameters.putBigIntegerAt("yPow", i, yPow);
        }

        // Generate zero-tester pzt
        BigInteger zPowKappa = z.modPow(BigInteger.valueOf(parameters.getKappa()), x0);
        BigInteger pzt = BigInteger.ZERO;
        for (int i = 0; i < parameters.getN(); i++) {
            BigInteger psi = mapParameters.getBigIntegerAt("ps", i);
            pzt = pzt.add(
                    getRandom(parameters.getBeta(), random)
                            .multiply(mapParameters.getBigIntegerAt("gs", i).modInverse(psi).multiply(zPowKappa).mod(psi))
                            .multiply(x0.divide(psi))
            );
        }
        pzt = pzt.mod(x0);
        mapParameters.putBigInteger("pzt", pzt);

        for (int level = 1; level <= parameters.getKappa(); level++) {

            String xsLevel = "xs" + level;

            // Quadratic re-randomization stuff
            for (int i = 0; i < parameters.getDelta(); i++) {
                //            xs[i] = encodeZero();
                BigInteger xs = BigInteger.ZERO;
                for (int j = 0; j < parameters.getN(); j++)
                    xs = xs.add(
                            mapParameters.getBigIntegerAt("gs", j).multiply(getRandom(parameters.getRho(), random))
                                    .multiply(mapParameters.getBigIntegerAt("crtCoefficients", j))
                    );
                xs = xs.mod(x0);
                mapParameters.putBigIntegerAt(xsLevel, i, xs);

                //            xs[parameters.getDelta() + i] = encodeAt(1);
                int index = parameters.getDelta() + i;
                xs = BigInteger.ZERO;
                for (int j = 0; j < parameters.getN(); j++) {
                    xs = xs.add(
                            mapParameters.getBigIntegerAt("gs", j).multiply(getRandom(parameters.getRho(), random))
                                    .add(getRandom(parameters.getAlpha(), random))
                                    .multiply(mapParameters.getBigIntegerAt("crtCoefficients", j))
                    );
                }
                xs = xs.mod(x0);
                for (int j = level; j > 0; j--)
                    xs = xs.multiply(zInv).mod(x0);

                mapParameters.putBigIntegerAt(xsLevel, index, xs);
            }

        }

        long end = System.currentTimeMillis();

        System.out.println("end = " + (end - start));
    }


    public static void main(String[] args) {
        CTL13MMInstanceGenerator gen = new CTL13MMInstanceGenerator(new SecureRandom(), CTL13MMInstanceParameters.TOY);
        gen.generate();
    }

}