package it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters;

import it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing.CTL13MMField;
import it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils;

import java.math.BigInteger;
import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.getRandom;
import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.modNear;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMInstance {

    private SecureRandom random;
    private CTL13InstanceParameters parameters;

    private BigInteger x0;
    private BigInteger y;       // level-one random encoding of 1
    private BigInteger pzt;     // zero-tester
    private BigInteger z;       // random invertible internet modulo x0
    private BigInteger zInv;
    private BigInteger[] xsp;   // level-zero encoding using for samp
    private BigInteger[] crtCoefficients;
    private BigInteger[] xs;    // level-zero and level-one encoding for re-randomization
    private BigInteger[] gs, p;

    private long isZeroBound;

    private CTL13MMField[] fields;


    public CTL13MMInstance(SecureRandom random, CTL13InstanceParameters parameters) {
        this.random = random;
        this.parameters = parameters;
        this.fields = new CTL13MMField[parameters.kappa + 1];

        generate();
    }

    public CTL13InstanceParameters getParameters() {
        return parameters;
    }


    public CTL13MMField getFieldAt(int index) {
        if (fields[index] == null)
            fields[index] = new CTL13MMField(random, this, index);

        return fields[index];
    }


    public BigInteger reduce(BigInteger value) {
        return value.mod(x0);
    }

    public boolean isZero(BigInteger value, int index) {
        value = modNear(value.multiply(pzt), x0);
        for (long i = parameters.kappa - index; i > 0; i--)
            value = modNear(value.multiply(y), x0);

        return (value.bitLength() < isZeroBound);
    }


    public BigInteger sampleAtLevel(int index) {
        return encodeAt(sampleAtZero(), index);
    }

    public BigInteger sampleAtZero() {
        BigInteger c = BigInteger.ZERO;

        for (int i = 0; i < parameters.ell; i++)
            if (random.nextBoolean())
                c = c.add(xsp[i]);

        return c.mod(x0);

    }


    public BigInteger encodeAt(BigInteger value, int index) {
        for (int i = index; i > 0; i--)
            value = modNear(value.multiply(y), x0);
        return value;
    }

    public BigInteger encodeAt(int degree) {
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < parameters.n; i++) {
            res = res.add(
                    gs[i].multiply(getRandom(parameters.rho, random)).add(getRandom(parameters.alpha, random))
                            .multiply(crtCoefficients[i])
            );
        }

        res = res.mod(x0);
        for (int j = degree; j > 0; j--)
            res = res.multiply(zInv).mod(x0);

        return res;
    }

    public BigInteger encodeZero() {
        return encodeZeroAt(0);
    }

    public BigInteger encodeZeroAt(int index) {
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < parameters.n; i++)
            res = res.add(gs[i].multiply(getRandom(parameters.rho, random)).multiply(crtCoefficients[i]));

        res = res.mod(x0);
        for (int j = index; j > 0; j--)
            res = res.multiply(zInv).mod(x0);

        return res;
    }

    public BigInteger encodeOne() {
        return encodeOneAt(0);
    }

    public BigInteger encodeOneAt(int index) {
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < parameters.n; i++) {
            res = res.add(
                    gs[i].multiply(getRandom(parameters.rho, random)).add(BigInteger.ONE)
                            .multiply(crtCoefficients[i])
            );
        }

        res = res.mod(x0);
        for (int j = index; j > 0; j--)
            res = res.multiply(zInv).mod(x0);

        return res;
    }

    public BigInteger reRandomize(BigInteger value, int index) {
        if (index != 1)
            throw new IllegalArgumentException("index must be 1");

        // Re-randomize.
        for (int i = 0; i < parameters.theta; i++) {
            // TODO : Ensure no duplicates are used.
            int pos = random.nextInt(parameters.deltaSquare);
            value = value.add(xs[pos % parameters.delta]
                    .multiply(xs[parameters.delta + pos / parameters.delta])
            );
        }

        return value.mod(x0);
    }


    protected void generate() {
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

    }

    protected BigInteger[] decodeAtLevel(BigInteger value, int degree) {
        for (int i = degree; i > 0; i--)
            value = value.multiply(z).mod(x0);

        BigInteger m[] = new BigInteger[parameters.n];
        for (int i = 0; i < parameters.n; i++) {
            m[i] = modNear(modNear(value, p[i]), gs[i]);
        }

        return m;
    }

}
