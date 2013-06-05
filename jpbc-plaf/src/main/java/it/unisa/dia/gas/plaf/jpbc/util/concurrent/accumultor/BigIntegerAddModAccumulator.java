package it.unisa.dia.gas.plaf.jpbc.util.concurrent.accumultor;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class BigIntegerAddModAccumulator extends AbstractAccumulator<BigInteger> {

    private BigInteger modulo;


    public BigIntegerAddModAccumulator(BigInteger modulo) {
        this.result = BigInteger.ZERO;
        this.modulo = modulo;
    }

    protected void reduce(BigInteger value) {
        result = result.add(value).mod(modulo);
    }

}
