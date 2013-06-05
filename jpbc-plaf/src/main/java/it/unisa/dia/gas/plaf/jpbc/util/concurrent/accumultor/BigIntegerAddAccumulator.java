package it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class BigIntegerAddAccumulator extends AbstractAccumulator<BigInteger> {


    public BigIntegerAddAccumulator() {
        this.result = BigInteger.ZERO;
    }


    protected void reduce(BigInteger value) {
        result = result.add(value);
    }

}
