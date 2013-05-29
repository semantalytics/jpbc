package it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceValues;

import java.math.BigInteger;
import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.getRandom;
import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.modNear;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class DefaultCTL13MMInstance implements CTL13MMInstance {

    protected SecureRandom random;
    protected CTL13MMInstanceValues values;
    protected CTL13MMInstanceParameters parameters;

    protected BigInteger x0;
    protected BigInteger y;       // level-one random encoding of 1
    protected BigInteger pzt;     // zero-tester
    protected BigInteger z;       // random invertible internet modulo x0
    protected BigInteger zInv;

    protected long isZeroBound;


    public DefaultCTL13MMInstance(SecureRandom random, PairingParameters parameters) {
        this.random = random;
        this.values = new CTL13MMInstanceValues(parameters);

        this.parameters = values.getInstanceParameters();
        this.x0 = values.getX0();
        this.y = values.getY();
        this.z = values.getZ();
        this.zInv = values.getZInv();
        this.pzt = values.getPzt();

        this.isZeroBound = (x0.bitLength() - this.parameters.getBound());
    }


    public CTL13MMInstanceParameters getParameters() {
        return parameters;
    }


    public BigInteger reduce(BigInteger value) {
        return value.mod(x0);
    }

    public boolean isZero(BigInteger value, int index) {
        value = modNear(value.multiply(pzt), x0);
        for (long i = parameters.getKappa() - index; i > 0; i--)
            value = modNear(value.multiply(y), x0);

        return (value.bitLength() < isZeroBound);
    }


    public BigInteger sampleAtLevel(int index) {
        return encodeAt(sampleAtZero(), 0, index);
    }

    public BigInteger sampleAtZero() {
        BigInteger c = BigInteger.ZERO;

        for (int i = 0; i < parameters.getEll(); i++)
            if (random.nextBoolean())
                c = c.add(values.getXspAt(i));

        return c.mod(x0);

    }


    public BigInteger encodeAt(BigInteger value, int startIndex, int endIndex) {
        for (int i = endIndex; i > startIndex; i--)
            value = modNear(value.multiply(y), x0);
        return value;
    }

    public BigInteger encodeAt(int degree) {
        BigInteger res = BigInteger.ZERO;
        for (int i = 0; i < parameters.getN(); i++) {
            res = res.add(
                    values.getGsAt(i).multiply(getRandom(parameters.getRho(), random)).add(getRandom(parameters.getAlpha(), random))
                            .multiply(values.getCrtCoefficientAt(i))
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
        for (int i = 0; i < parameters.getN(); i++)
            res = res.add(
                    values.getGsAt(i).multiply(getRandom(parameters.getRho(), random))
                            .multiply(values.getCrtCoefficientAt(i))
            );

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
        for (int i = 0; i < parameters.getN(); i++) {
            res = res.add(
                    values.getGsAt(i).multiply(getRandom(parameters.getRho(), random))
                            .add(BigInteger.ONE)
                            .multiply(values.getCrtCoefficientAt(i))
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
        for (int i = 0; i < parameters.getTheta(); i++) {
            // TODO : Ensure no duplicates are used.
            int pos = random.nextInt(parameters.getDeltaSquare());
            value = value.add(
                    values.getXsAt(pos % parameters.getDelta())
                            .multiply(values.getXsAt(parameters.getDelta() + pos / parameters.getDelta()))
            );
        }

        return value.mod(x0);
    }


    public BigInteger extract(BigInteger value, int index) {
        value = modNear(value.multiply(pzt), x0);
        for (long i = parameters.getKappa() - index; i > 0; i--)
            value = modNear(value.multiply(y), x0);

        return value.shiftRight(
                x0.bitLength()-parameters.getBound()
        );
    }


    protected BigInteger[] decodeAtLevel(BigInteger value, int degree) {
        for (int i = degree; i > 0; i--)
            value = value.multiply(z).mod(x0);

        BigInteger m[] = new BigInteger[parameters.getN()];
        for (int i = 0; i < parameters.getN(); i++) {
            m[i] = modNear(modNear(value, values.getPsAt(i)), values.getGsAt(i));
        }

        return m;
    }

}
