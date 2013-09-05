package it.unisa.dia.gas.plaf.jpbc.pairing.mt;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.AbstractJPBCTest;
import it.unisa.dia.gas.plaf.jpbc.pairing.accumulator.MultiThreadMulPairingAccumulator;
import it.unisa.dia.gas.plaf.jpbc.pairing.accumulator.PairingAccumulator;
import it.unisa.dia.gas.plaf.jpbc.pairing.accumulator.SequentialMulPairingAccumulator;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 1.0.0
 */
public class MultiThreadPairingMultiplierTest extends AbstractJPBCTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"},
        };

        return Arrays.asList(data);
    }


    public MultiThreadPairingMultiplierTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testMultiplier() {
        System.out.println(Runtime.getRuntime().availableProcessors());

        int n = 1000;
        Element in1s[] = new Element[n];
        Element in2s[] = new Element[n];

        for (int i=0; i <n;i++){
            in1s[i] = pairing.getG1().newRandomElement();
                    in2s[i] =         pairing.getG2().newRandomElement();
        }

        // Test default
        System.out.println("Default");
        PairingAccumulator multiplier = new SequentialMulPairingAccumulator(pairing);
        long start = System.currentTimeMillis();
        for (int i=0; i <n;i++){
            multiplier.addPairing(in1s[i], in2s[i]);
        }
        Element result1 = multiplier.awaitResult();
        long end = System.currentTimeMillis();

        System.out.println("result = " + result1);
        System.out.println("elapsed = " + (end - start));


        // Test multi thread
        System.out.println("MultiThread");
        multiplier = new MultiThreadMulPairingAccumulator(pairing);
        start = System.currentTimeMillis();
        for (int i=0; i <n;i++){
            multiplier.addPairing(in1s[i], in2s[i]);
        }
        Element result2 = multiplier.awaitResult();
        end = System.currentTimeMillis();

        System.out.println("result = " + result2);
        System.out.println("elapsed = " + (end - start));

        System.out.println("Are they Equal? " + result1.isEqual(result2));
    }

}
