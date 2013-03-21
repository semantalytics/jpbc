package it.unisa.dia.gas.plaf.jpbc.pairing.mt;


import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.AbstractJPBCTest;
import it.unisa.dia.gas.plaf.jpbc.pairing.combiner.DefaultPairingMultiplier;
import it.unisa.dia.gas.plaf.jpbc.pairing.combiner.PairingCombiner;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public class DefaultMultiplierTest extends AbstractJPBCTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"},
        };

        return Arrays.asList(data);
    }


    public DefaultMultiplierTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testBls() {


        PairingCombiner multiplier = new DefaultPairingMultiplier(pairing);

        for (int i=0; i <1000;i++){
            multiplier.addPairing(pairing.getG1().newRandomElement(),
                    pairing.getG2().newRandomElement()
            );
        }

        Element result = multiplier.doFinal();

        System.out.println(result);
    }

}
