package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.math.BigInteger;
import java.util.concurrent.RecursiveTask;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class RecursiveBigIntegerMultiplier extends RecursiveTask<BigInteger> {
    static final int SEQUENTIAL_THRESHOLD = 2;

    BigInteger[] value;
    int low;
    int high;

    public RecursiveBigIntegerMultiplier(BigInteger[] values, int lo, int hi) {
        this.value = values;
        this.low = lo;
        this.high = hi;
    }

    protected BigInteger compute() {
        if (high == low) {
          return value[low];
        }

        if (high - low < SEQUENTIAL_THRESHOLD) {
            return value[low].multiply(value[high]);
        } else {
            int mid = low + (high - low) / 2;

            RecursiveBigIntegerMultiplier left = new RecursiveBigIntegerMultiplier(value, low, mid);
            RecursiveBigIntegerMultiplier right = new RecursiveBigIntegerMultiplier(value, mid + 1, high);
            left.fork();

            BigInteger rightAns = right.compute();
            BigInteger leftAns = left.join();
            return rightAns.multiply(leftAns);
        }
    }

}

