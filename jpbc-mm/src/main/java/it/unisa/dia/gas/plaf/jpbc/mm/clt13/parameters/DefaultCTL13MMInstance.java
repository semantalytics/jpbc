package it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters;

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
public class DefaultCTL13MMInstance extends AbstractCTL13MMInstance {

    public DefaultCTL13MMInstance(SecureRandom random, CTL13InstanceParameters parameters) {
        super(random, parameters);
    }

    protected void generate() {
        if (load()) {
            isZeroBound = (x0.bitLength() - parameters.bound);

            return;
        }

        // Generate CRT modulo x0
        x0 = BigInteger.ONE;
        p = new BigInteger[parameters.n];
        for (int i = 0; i < parameters.n; i++) {
            p[i] = BigInteger.probablePrime(parameters.eta, random);
            x0 = x0.multiply(p[i]);
        }
        isZeroBound = (x0.bitLength() - parameters.bound);

        // Generate CRT Coefficients
        crtCoefficients = new BigInteger[parameters.n];
        for (int i = 0; i < parameters.n; i++) {
            BigInteger temp = x0.divide(p[i]);
            crtCoefficients[i] = temp.modInverse(p[i]).multiply(temp);
        }

        // Generate g_i's
        gs = new BigInteger[parameters.n];
        for (int i = 0; i < parameters.n; i++) {
            gs[i] = BigInteger.probablePrime(parameters.alpha, random);
        }

        // Generate z
        do {
            z = BigIntegerUtils.getRandom(x0, random);
            zInv = z.modInverse(x0);
        } while (zInv.equals(BigInteger.ZERO));

        // Generate xp_i's
        xsp = new BigInteger[parameters.ell];
        for (int i = 0; i < parameters.ell; i++)
            xsp[i] = encodeAt(0);

        // Generate y
        y = encodeOneAt(1);

        // Generate zero-tester pzt
        BigInteger zPowKappa = z.modPow(BigInteger.valueOf(parameters.kappa), x0);
        pzt = BigInteger.ZERO;
        for (int i = 0; i < parameters.n; i++) {
            pzt = pzt.add(
                    getRandom(parameters.beta, random).multiply(
                            gs[i].modInverse(p[i]).multiply(zPowKappa).mod(p[i])
                    ).multiply(x0.divide(p[i]))
            );
        }
        pzt = pzt.mod(x0);

        // Quadratic re-randomization stuff
        xs = new BigInteger[2 * parameters.delta];
        for (int i = 0; i < parameters.delta; i++) {
            xs[i] = encodeZero();
            xs[parameters.delta + i] = encodeAt(1);
        }

        store();
    }

    protected void store() {
        try {
            // file name
            String fileName = String.format(
                    "CTL13IP_eta_%d_n_%d_alpha_%d_ell_%d_rho_%d_delta_%d_kappa_%d_beta_%d_theta_%d_bound_%d.dat",
                    parameters.eta, parameters.n, parameters.alpha, parameters.ell, parameters.rho, parameters.delta,
                    parameters.kappa, parameters.beta, parameters.theta, parameters.bound);

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

    protected boolean load() {
        try {
            // file name
            String fileName = String.format(
                    "CTL13IP_eta_%d_n_%d_alpha_%d_ell_%d_rho_%d_delta_%d_kappa_%d_beta_%d_theta_%d_bound_%d.dat",
                    parameters.eta, parameters.n, parameters.alpha, parameters.ell, parameters.rho, parameters.delta,
                    parameters.kappa, parameters.beta, parameters.theta, parameters.bound);

            if (!new File(fileName).exists())
                return false;

            PairingObjectInput dos = new PairingObjectInput(fileName);
            x0 = dos.readBigInteger();
            y = dos.readBigInteger();
            pzt = dos.readBigInteger();
            z = dos.readBigInteger();
            zInv = dos.readBigInteger();

            xsp =dos.readBigIntegers();
            crtCoefficients = dos.readBigIntegers();
            xs = dos.readBigIntegers();
            gs = dos.readBigIntegers();
            p = dos.readBigIntegers();

            dos.close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

}
