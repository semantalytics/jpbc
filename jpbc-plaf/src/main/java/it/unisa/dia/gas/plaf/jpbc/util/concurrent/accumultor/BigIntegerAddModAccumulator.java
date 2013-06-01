package it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor;

import it.unisa.dia.gas.plaf.jpbc.util.concurrent.PoolExecutor;

import java.math.BigInteger;
import java.util.concurrent.Callable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public class BigIntegerAddModAccumulator extends PoolExecutor<BigInteger> implements Accumulator<BigInteger> {

    private BigInteger result;
    private BigInteger modulo;


    public BigIntegerAddModAccumulator(BigInteger modulo) {
        this.result = BigInteger.ZERO;
        this.modulo = modulo;
    }


    public Accumulator<BigInteger> accumulate(Callable<BigInteger> callable) {
        submit(callable);

        return this;
    }

    public BigInteger getResult() {
        return result;
    }

    public BigInteger doFinal() {
        return process().getResult();
    }

    public Accumulator<BigInteger> process() {
        super.process();

        return this;
    }

    public void reduce(BigInteger value) {
        result = result.add(value).mod(modulo);
    }

}
