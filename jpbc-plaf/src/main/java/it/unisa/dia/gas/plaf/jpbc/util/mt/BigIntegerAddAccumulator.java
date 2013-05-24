package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.math.BigInteger;
import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public class BigIntegerAddAccumulator extends MultiThreadExecutor<BigInteger> implements Accumulator<BigInteger> {

    private BigInteger result;


    public BigIntegerAddAccumulator() {
        this.result = BigInteger.ZERO;
    }


    public Accumulator<BigInteger> accumulate(Callable<BigInteger> callable) {
        submit(callable);

        return this;
    }

    public BigInteger getResult() {
        return result;
    }

    public Accumulator<BigInteger> process() {
        super.process();

        return this;
    }

    public void reduce(BigInteger value) {
        result = result.add(value);
    }

}
