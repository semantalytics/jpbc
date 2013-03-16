package it.unisa.dia.gas.plaf.jpbc.pairing.mt;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.PairingCombiner;
import it.unisa.dia.gas.plaf.jpbc.AbstractJPBCTest;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public class MultiplierTest extends AbstractJPBCTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"},
        };

        return Arrays.asList(data);
    }


    public MultiplierTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testMultiplier() {
        System.out.println(Runtime.getRuntime().availableProcessors());

        int n = 50;
        Element in1s[] = new Element[n];
        Element in2s[] = new Element[n];

        for (int i=0; i <n;i++){
            in1s[i] = pairing.getG1().newRandomElement();
                    in2s[i] =         pairing.getG2().newRandomElement();
        }

        // Test default
        System.out.println("Default");
        PairingCombiner multiplier = new DefaultPairingMultiplier(pairing, pairing.getGT());
        long start = System.currentTimeMillis();
        for (int i=0; i <n;i++){
            multiplier.addPairing(in1s[i], in2s[i]);
        }
        Element result1 = multiplier.doFinal();
        long end = System.currentTimeMillis();

        System.out.println("result = " + result1);
        System.out.println("elapsed = " + (end - start));


        // Test multi thread
        System.out.println("MultiThread");
        multiplier = new MultiThreadPairingMultiplier(pairing, pairing.getGT());
        start = System.currentTimeMillis();
        for (int i=0; i <n;i++){
            multiplier.addPairing(in1s[i], in2s[i]);
        }
        Element result2 = multiplier.doFinal();
        end = System.currentTimeMillis();

        System.out.println("result = " + result2);
        System.out.println("elapsed = " + (end - start));

        System.out.println("Are they Equal? " + result1.isEqual(result2));
    }

}
