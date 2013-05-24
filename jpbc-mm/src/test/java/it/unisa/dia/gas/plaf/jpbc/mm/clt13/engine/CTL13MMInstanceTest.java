package it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine;

import it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators.CTL13MMInstanceGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstanceParameters;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
@RunWith(value = Parameterized.class)
public class CTL13MMInstanceTest {

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


    public CTL13MMInstanceTest(CTL13MMInstanceParameters instanceParameters) {
        this.instanceParameters = instanceParameters;
    }

    @Before
    public void before() {
        instance = new DefaultCTL13MMInstance(
                random,
                new CTL13MMInstanceGenerator(random, instanceParameters).generate()
        );
    }


    @org.junit.Test
    public void test() {
        for (int i = 0; i < instance.getParameters().getKappa() + 1; i++) {
            System.out.printf("Check level %d...\n", i);
            Assert.assertTrue(instance.isZero(instance.encodeZeroAt(i), i));
            Assert.assertFalse(instance.isZero(instance.encodeOneAt(i), i));
        }
    }

    @org.junit.Test
    public void testIsZero() {
        BigInteger a = instance.encodeAt(0);

        BigInteger b = instance.encodeAt(instance.encodeAt(a, 0, 2), 2, 5);
        BigInteger c = instance.encodeAt(a, 0, 5);

        BigInteger diff = instance.reduce(b.subtract(c));

        Assert.assertEquals(true, instance.isZero(diff, 5));
    }


}
