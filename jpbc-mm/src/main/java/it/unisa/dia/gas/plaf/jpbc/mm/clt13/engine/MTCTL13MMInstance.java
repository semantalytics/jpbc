package it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceValues;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.Accumulator;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor.BigIntegerAddAccumulator;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.concurrent.Callable;

import static it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils.IndexCallable;
import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.getRandom;
import static it.unisa.dia.gas.plaf.jpbc.util.math.BigIntegerUtils.modNear;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class MTCTL13MMInstance implements CTL13MMInstance {

    protected SecureRandom random;
    protected CTL13MMInstanceValues values;
    protected CTL13MMInstanceParameters parameters;

    protected BigInteger x0;
    protected BigInteger y;       // level-one random encoding of 1
    protected BigInteger z;       // random invertible internet modulo x0
    protected BigInteger zInv;
    protected BigInteger pzt;

    protected BigInteger[] zInvPows;
    protected BigInteger[] yPows;

    protected long isZeroBound;

    public MTCTL13MMInstance(SecureRandom random, PairingParameters parameters) {
        this.random = random;
        this.values = new CTL13MMInstanceValues(parameters);

        this.parameters = values.getInstanceParameters();
        this.x0 = values.getX0();
        this.y = values.getY();
        this.z = values.getZ();
        this.zInv = values.getZInv();
        this.pzt = values.getPzt();

        this.isZeroBound = (x0.bitLength() - this.parameters.getBound());

        zInvPows = new BigInteger[this.parameters.getKappa()];
        yPows = new BigInteger[this.parameters.getKappa() + 1];

        BigInteger temp = BigInteger.ONE;
        for (int i = 0; i < this.parameters.getKappa(); i++) {
            temp = temp.multiply(zInv).mod(x0);
            zInvPows[i] = temp;
        }

        yPows[0] = BigInteger.ONE;
        for (int i = 1; i <= this.parameters.getKappa(); i++)
            yPows[i] = yPows[i - 1].multiply(y).mod(x0);
    }


    public CTL13MMInstanceParameters getParameters() {
        return parameters;
    }


    public BigInteger reduce(BigInteger value) {
        return value.mod(x0);
    }

    public boolean isZero(BigInteger value, int index) {
        value = modNear(value.multiply(pzt).multiply(yPows[parameters.getKappa() - index]), x0);

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
        return modNear(value.multiply(yPows[endIndex - startIndex]), x0);
    }

    public BigInteger encodeAt(int degree) {
        Accumulator<BigInteger> accumulator = new BigIntegerAddAccumulator();
        for (int i = 0; i < parameters.getN(); i++) {
            accumulator.accumulate(new IndexCallable<BigInteger>(i) {
                public BigInteger call() throws Exception {
                    return values.getGsAt(i).multiply(getRandom(parameters.getRho(), random))
                            .add(getRandom(parameters.getAlpha(), random))
                            .multiply(values.getCrtCoefficientAt(i));
                }
            });
        }

        BigInteger res = accumulator.awaitResult().mod(x0);
        if (degree > 0)
            res = res.multiply(zInvPows[degree - 1]).mod(x0);

        return res;
    }

    public BigInteger encodeZero() {
        return encodeZeroAt(0);
    }

    public BigInteger encodeZeroAt(int index) {
        Accumulator<BigInteger> accumulator = new BigIntegerAddAccumulator();
        for (int i = 0; i < parameters.getN(); i++) {
            accumulator.accumulate(new IndexCallable<BigInteger>(i) {
                public BigInteger call() throws Exception {
                    return values.getGsAt(i).multiply(getRandom(parameters.getRho(), random))
                            .multiply(values.getCrtCoefficientAt(i));
                }
            });
        }

        BigInteger res = accumulator.awaitResult().mod(x0);
        if (index > 0)
            res = res.multiply(zInvPows[index - 1]).mod(x0);

        return res;
    }

    public BigInteger encodeOne() {
        return encodeOneAt(0);
    }

    public BigInteger encodeOneAt(int index) {
        Accumulator<BigInteger> accumulator = new BigIntegerAddAccumulator();
        for (int i = 0; i < parameters.getN(); i++) {
            accumulator.accumulate(new IndexCallable<BigInteger>(i) {
                public BigInteger call() throws Exception {
                    return values.getGsAt(i).multiply(getRandom(parameters.getRho(), random))
                            .add(BigInteger.ONE)
                            .multiply(values.getCrtCoefficientAt(i));
                }
            });
        }

        BigInteger res = accumulator.awaitResult().mod(x0);
        if (index > 0)
            res = res.multiply(zInvPows[index - 1]).mod(x0);

        return res;
    }

    public BigInteger reRandomize(BigInteger value, int index) {
        if (index != 1)
            throw new IllegalArgumentException("index must be 1");

        // Re-randomize.
        Accumulator<BigInteger> accumulator = new BigIntegerAddAccumulator();
        for (int i = 0; i < parameters.getTheta(); i++) {
            accumulator.accumulate(new Callable<BigInteger>() {
                public BigInteger call() throws Exception {
                    // TODO : Ensure no duplicates are used.
                    int pos = random.nextInt(parameters.getDeltaSquare());
                    return values.getXsAt(pos % parameters.getDelta())
                            .multiply(values.getXsAt(parameters.getDelta() + pos / parameters.getDelta()));
                }
            });
        }
        return accumulator.awaitResult().add(value).mod(x0);
    }


    public BigInteger extract(BigInteger value, int index) {
        value = modNear(value.multiply(pzt).multiply(yPows[parameters.getKappa() - index]), x0);
//
        return value.shiftRight(
                x0.bitLength() - parameters.getBound()
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
