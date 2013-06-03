package it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.jpbc.PairingParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMMapParameters;
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
        if (storeGeneratedInstance) {
            PairingParameters params = new CTL13MMMapParameters(parameters).load();
            if (params != null)
                return params;
        }

        CTL13MMMapParameters params = generateInternal();

        if (storeGeneratedInstance)
            params.store();

        return params;
    }

    protected CTL13MMMapParameters generateInternal() {
        long start = System.currentTimeMillis();

        // Generate CRT modulo x0
        BigInteger x0 = BigInteger.ONE;
        BigInteger[] ps = new BigInteger[parameters.getN()];
        for (int i = 0; i < parameters.getN(); i++) {
            ps[i] = BigInteger.probablePrime(parameters.getEta(), random);
            x0 = x0.multiply(ps[i]);
        }

        // Generate CRT Coefficients
        BigInteger[] crtCoefficients = new BigInteger[parameters.getN()];
        for (int i = 0; i < parameters.getN(); i++) {
            BigInteger temp = x0.divide(ps[i]);
            crtCoefficients[i] = temp.modInverse(ps[i]).multiply(temp);
        }

        // Generate g_i's
        BigInteger[] gs = new BigInteger[parameters.getN()];
        for (int i = 0; i < parameters.getN(); i++) {
            gs[i] = BigInteger.probablePrime(parameters.getAlpha(), random);
        }

        // Generate z
        BigInteger z, zInv;
        do {
            z = BigIntegerUtils.getRandom(x0, random);
            zInv = z.modInverse(x0);
        } while (zInv.equals(BigInteger.ZERO));

        // Generate xp_i's
        BigInteger[] xsp = new BigInteger[parameters.getEll()];
        for (int i = 0; i < parameters.getEll(); i++) {
            // xsp[i] = encodeAt(0);
            xsp[i] = BigInteger.ZERO;
            for (int j = 0; j < parameters.getN(); j++) {
                xsp[i] = xsp[i].add(
                        gs[j].multiply(getRandom(parameters.getRho(), random))
                                .add(getRandom(parameters.getAlpha(), random))
                                .multiply(crtCoefficients[j])
                );
            }
            xsp[i] = xsp[i].mod(x0);
        }

        // Generate y = encodeOneAt(1)
        BigInteger y = BigInteger.ZERO;
        for (int i = 0; i < parameters.getN(); i++) {
            y = y.add(
                    gs[i].multiply(getRandom(parameters.getRho(), random))
                            .add(BigInteger.ONE)
                            .multiply(crtCoefficients[i])
            );
        }
        y = y.multiply(zInv).mod(x0);

        // Generate zero-tester pzt
        BigInteger zPowKappa = z.modPow(BigInteger.valueOf(parameters.getKappa()), x0);
        BigInteger pzt = BigInteger.ZERO;
        for (int i = 0; i < parameters.getN(); i++) {
            pzt = pzt.add(
                    getRandom(parameters.getBeta(), random)
                            .multiply(gs[i].modInverse(ps[i]).multiply(zPowKappa).mod(ps[i]))
                            .multiply(x0.divide(ps[i]))
            );
        }
        pzt = pzt.mod(x0);

        // Quadratic re-randomization stuff
        BigInteger[] xs = new BigInteger[2 * parameters.getDelta()];
        for (int i = 0; i < parameters.getDelta(); i++) {
//            xs[i] = encodeZero();
            xs[i] = BigInteger.ZERO;
            for (int j = 0; j < parameters.getN(); j++)
                xs[i] = xs[i].add(
                        gs[j].multiply(getRandom(parameters.getRho(), random))
                                .multiply(crtCoefficients[j])
                );
            xs[i] = xs[i].mod(x0);

//            xs[parameters.getDelta() + i] = encodeAt(1);
            int index = parameters.getDelta() + i;
            xs[index] = BigInteger.ZERO;
            for (int j = 0; j < parameters.getN(); j++) {
                xs[index] = xs[index].add(
                        gs[j].multiply(getRandom(parameters.getRho(), random))
                                .add(getRandom(parameters.getAlpha(), random))
                                .multiply(crtCoefficients[j])
                );
            }
            xs[index] = xs[index].multiply(zInv).mod(x0);
        }

        long end = System.currentTimeMillis();

        System.out.println("end = " + (end-start));

        CTL13MMMapParameters mapParameters = new CTL13MMMapParameters(parameters);
        mapParameters.put("params", parameters);
        mapParameters.put("x0", x0);
        mapParameters.put("y", y);
        mapParameters.put("pzt", pzt);
        mapParameters.put("z", z);
        mapParameters.put("zInv", zInv);
        mapParameters.put("xsp", xsp);
        mapParameters.put("crtCoefficients", crtCoefficients);
        mapParameters.put("xs", xs);
        mapParameters.put("gs", gs);
        mapParameters.put("ps", ps);

        return mapParameters;
    }


    public static void main(String[] args) {
        CTL13MMInstanceGenerator gen = new CTL13MMInstanceGenerator(new SecureRandom(), CTL13MMInstanceParameters.SMALL);
        gen.generate();
    }


}
