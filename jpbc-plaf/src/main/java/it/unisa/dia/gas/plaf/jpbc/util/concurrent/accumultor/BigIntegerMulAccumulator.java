package it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class BigIntegerMulAccumulator extends AbstractAccumulator<BigInteger> {

    public BigIntegerMulAccumulator() {
        this.result = BigInteger.ONE;
    }


    protected void reduce(BigInteger value) {
        result = result.multiply(value);
    }

}