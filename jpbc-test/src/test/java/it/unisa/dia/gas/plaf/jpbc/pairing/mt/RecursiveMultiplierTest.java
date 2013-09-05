package it.unisa.dia.gas.plaf.jpbc.pairing.mt;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.AbstractJPBCTest;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.recursive.RecursiveMultiplier;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ForkJoinPool;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 1.0.0
 */
public class RecursiveMultiplierTest extends AbstractJPBCTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"},
        };

        return Arrays.asList(data);
    }


    public RecursiveMultiplierTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testRecursiveMultiplier() {
        System.out.println(Runtime.getRuntime().availableProcessors());

        int n = 10000;
        Element elements[] = new Element[n];

        for (int i = 0; i < n; i++) {
            elements[i] = pairing.getGT().newRandomElement();
        }

        long start = System.currentTimeMillis();
        Element product = pairing.getGT().newOneElement();
        for (int i = 0; i < n; i++) {
            product.mul(elements[i]);
        }
        long end = System.currentTimeMillis();
        System.out.println("elapsed = " + (end - start));

        // Test default
        start = System.currentTimeMillis();
        ForkJoinPool pool = new ForkJoinPool();
        Element result = pool.invoke(new RecursiveMultiplier(elements, 0, n - 1));
        end = System.currentTimeMillis();

        System.out.println("elapsed = " + (end - start));
        System.out.println("result = " + result);
        System.out.println("isEqual = " + product.isEqual(result));
    }

}
