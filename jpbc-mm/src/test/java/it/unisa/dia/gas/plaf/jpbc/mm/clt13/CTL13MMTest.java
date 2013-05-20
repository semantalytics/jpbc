package it.unisa.dia.gas.plaf.jpbc.mm.clt13;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators.CTL13MMInstanceGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing.CTL13MMField;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.AbstractCTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13InstanceParameters;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
@RunWith(value = Parameterized.class)
public class CTL13MMTest {

    static SecureRandom random;
    static {
        random = new SecureRandom();

    }

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {CTL13InstanceParameters.TOY}
        };

        return Arrays.asList(data);
    }


    protected CTL13InstanceParameters instanceParameters;


    public CTL13MMTest(CTL13InstanceParameters instanceParameters) {
        this.instanceParameters = instanceParameters;
    }


    @org.junit.Test
    public void testEngine() {
        System.out.printf("Sampling the instance...\n");
        AbstractCTL13MMInstance instance = new CTL13MMInstanceGenerator(random, instanceParameters).generateInstance();

        int kappa = instanceParameters.getKappa();

        // Sample level-0 elements and computer their sum
        System.out.printf("Sample level-0 elements and computer their sum...\n");
        CTL13MMField fieldAtZero = instance.getFieldAt(0);

        Element[] as = new Element[kappa];
        for (int i = 0; i < kappa; i++)
            as[i] = fieldAtZero.newRandomElement();

        Element prod = as[0].duplicate();
        for (int i = 1; i < kappa; i++)
            prod.mul(as[i]);

        // Compute level-1 elements
        System.out.printf("Compute level-1 elements...\n");
        CTL13MMField fieldAtOne = instance.getFieldAt(1);

        Element[] gas = new Element[kappa];
        for (int i = 0; i < kappa; i++) {
            gas[i] = fieldAtOne.newElement().powZn(as[i]);
        }

        // Compute level-kappa element from gas
        System.out.printf("Compute level-kappa element from gas...\n");

        Element mul1 = gas[0].duplicate();
        for (int i = 1; i < kappa; i++)
            mul1.mul(gas[i]);

        System.out.println("mul1 = " + mul1);

        // Compute level-kappa element from sum
        System.out.printf("Compute level-kappa element from sum...\n");
        Element mul2 = instance.getFieldAt(kappa).newElement().powZn(prod);

        System.out.println("mul2 = " + mul2);

        System.out.printf("Compare...\n");
        assertEquals(true, mul1.isEqual(mul2));
    }

}
