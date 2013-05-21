package it.unisa.dia.gas.plaf.jpbc.mm.clt13.engine;

import it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators.CTL13MMInstanceGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13InstanceParameters;
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
public class CTL13MMInstanceTest {

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
    protected CTL13MMInstance instance;


    public CTL13MMInstanceTest(CTL13InstanceParameters instanceParameters) {
        this.instanceParameters = instanceParameters;
    }

    @Before
    public void before() {
        SecureRandom random = new SecureRandom();
        instance = new CTL13MMInstanceGenerator(random, instanceParameters).generateInstance();
    }


    @org.junit.Test
    public void test() {
        for (int i = 0; i < instance.getParameters().getKappa() + 1; i++) {
            System.out.printf("Check level %d...\n", i);
            Assert.assertTrue(instance.isZero(instance.encodeZeroAt(i), i));
            Assert.assertFalse(instance.isZero(instance.encodeOneAt(i), i));
        }
    }

}
