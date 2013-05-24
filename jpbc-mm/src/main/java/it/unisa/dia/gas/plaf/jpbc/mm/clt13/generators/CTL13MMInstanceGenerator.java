package it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators;

import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.CTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.DefaultCTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceParameters;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingObjectInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingObjectOutput;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.io.File;
import java.math.BigInteger;
import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.getRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMInstanceGenerator {

    private SecureRandom random;
    private CTL13MMInstanceParameters parameters;
    private boolean storeGeneratedInstance;


    public CTL13MMInstanceGenerator(SecureRandom random, CTL13MMInstanceParameters parameters) {
        this(random, parameters, true);
    }

    public CTL13MMInstanceGenerator(SecureRandom random, CTL13MMInstanceParameters parameters, boolean storeGeneratedInstance) {
        this.random = random;
        this.parameters = parameters;
        this.storeGeneratedInstance = storeGeneratedInstance;
    }


    public CTL13MMInstance generateInstance() {
        if (storeGeneratedInstance) {
            try {
                CTL13MMInstance instance = load();
                if (instance != null)
                    return instance;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

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


        if (storeGeneratedInstance)
            store(x0, y, pzt, z, zInv, xsp, crtCoefficients, xs, gs, ps);

        return new DefaultCTL13MMInstance(random, parameters, x0, y, pzt, z, zInv, xsp, crtCoefficients, xs, gs, ps);
    }


    protected void store(BigInteger x0, BigInteger y, BigInteger pzt, BigInteger z, BigInteger zInv,
                         BigInteger[] xsp, BigInteger[] crtCoefficients, BigInteger[] xs,
                         BigInteger[] gs, BigInteger[] p) {
        try {
            // file name
            String fileName = String.format(
                    "CTL13IP_eta_%d_n_%d_alpha_%d_ell_%d_rho_%d_delta_%d_kappa_%d_beta_%d_theta_%d_bound_%d.dat",
                    parameters.getEta(), parameters.getN(), parameters.getAlpha(), parameters.getEll(),
                    parameters.getRho(), parameters.getDelta(),
                    parameters.getKappa(), parameters.getBeta(), parameters.getTheta(), parameters.getBound());

            PairingObjectOutput dos = new PairingObjectOutput(fileName);

            dos.writeBigInteger(x0);
            dos.writeBigInteger(y);
            dos.writeBigInteger(pzt);
            dos.writeBigInteger(z);
            dos.writeBigInteger(zInv);

            dos.writeBigIntegers(xsp);
            dos.writeBigIntegers(crtCoefficients);
            dos.writeBigIntegers(xs);
            dos.writeBigIntegers(gs);
            dos.writeBigIntegers(p);

            dos.flush();
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected CTL13MMInstance load() {
        try {
            // file name
            String fileName = String.format(
                    "CTL13IP_eta_%d_n_%d_alpha_%d_ell_%d_rho_%d_delta_%d_kappa_%d_beta_%d_theta_%d_bound_%d.dat",
                    parameters.getEta(), parameters.getN(), parameters.getAlpha(), parameters.getEll(),
                    parameters.getRho(), parameters.getDelta(),
                    parameters.getKappa(), parameters.getBeta(), parameters.getTheta(), parameters.getBound());

            if (!new File(fileName).exists())
                return null;

            PairingObjectInput dos = new PairingObjectInput(fileName);

            BigInteger x0 = dos.readBigInteger();
            BigInteger y = dos.readBigInteger();
            BigInteger pzt = dos.readBigInteger();
            BigInteger z = dos.readBigInteger();
            BigInteger zInv = dos.readBigInteger();

            BigInteger[] xsp = dos.readBigIntegers();
            BigInteger[] crtCoefficients = dos.readBigIntegers();
            BigInteger[] xs = dos.readBigIntegers();
            BigInteger[] gs = dos.readBigIntegers();
            BigInteger[] p = dos.readBigIntegers();

            dos.close();

            return new DefaultCTL13MMInstance(random, parameters,
                    x0, y, pzt, z, zInv, xsp, crtCoefficients, xs, gs, p);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        CTL13MMInstanceGenerator gen = new CTL13MMInstanceGenerator(new SecureRandom(), CTL13MMInstanceParameters.TOY);
        gen.generateInstance();
    }


}
