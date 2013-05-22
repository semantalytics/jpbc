package it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine.CTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators.CTL13MMInstanceGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceParameters;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
@RunWith(value = Parameterized.class)
public class CTL13MMPairingTest {

    static SecureRandom random;

    static {
        random = new SecureRandom();
    }

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {CTL13MMInstanceParameters.TOY}
        };

        return Arrays.asList(data);
    }


    protected CTL13MMInstanceParameters instanceParameters;
    protected CTL13MMInstance instance;
    protected Pairing pairing;


    public CTL13MMPairingTest(CTL13MMInstanceParameters instanceParameters) {
        this.instanceParameters = instanceParameters;
    }

    @Before
    public void before() {
        SecureRandom random = new SecureRandom();
        instance = new CTL13MMInstanceGenerator(random, instanceParameters).generateInstance();
        pairing = new CTL13MMPairing(random, instance);
    }


    @org.junit.Test
    public void testMultilinearity() {
        for (int num = 2; num < instance.getParameters().getKappa() + 1; num++) {
            System.out.printf("Checking level %d...\n", num);

            Element[] as = new Element[num];
            Element[] gas = new Element[num];
            Element prod = pairing.getFieldAt(0).newOneElement();
            for (int i = 0; i < as.length; i++) {
                as[i] = pairing.getFieldAt(0).newRandomElement();
                prod.mul(as[i]);
                System.out.println("as[i] = " + as[i]);

                gas[i] = pairing.getFieldAt(1).newElement().powZn(as[i]);
            }

            // compute left expression
            Element left = pairing.getFieldAt(num).newElement().powZn(prod);

            // compute right expression
            Element right = gas[0];
            for (int i = 1; i < as.length; i++) {
                right = pairing.pairing(right, gas[i]);
            }

            System.out.println("left  = " + left);
            System.out.println("right = " + right);

            Assert.assertEquals(true, left.isEqual(right));
        }
    }


    @org.junit.Test
    public void testIsZero() {
        Element a = pairing.getFieldAt(0).newRandomElement();

        Element b = pairing.getFieldAt(2).newElement().powZn(a);
        Element c = pairing.getFieldAt(5).newElement().powZn(a);

        Assert.assertEquals(true, b.isEqual(c));
    }


    @org.junit.Test
    public void testToBytes() {
        for (int index = 0; index < instance.getParameters().getKappa() + 1; index++) {
            System.out.printf("Checking level %d...\n", index);
            Element a = pairing.getFieldAt(index).newRandomElement();
            System.out.println("a = " + a);
            byte[] bytes = a.toBytes();

            Element b = pairing.getFieldAt(0).newElement();
            b.setFromBytes(bytes);
            System.out.println("b = " + b);

            Assert.assertEquals(true, a.isEqual(b));
        }
    }
}

