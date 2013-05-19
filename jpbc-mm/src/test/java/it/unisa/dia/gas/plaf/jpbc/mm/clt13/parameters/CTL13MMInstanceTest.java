package it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters;

import junit.framework.Assert;
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


    public CTL13MMInstanceTest(CTL13InstanceParameters instanceParameters) {
        this.instanceParameters = instanceParameters;
    }


    @org.junit.Test
    public void test() {
        SecureRandom random = new SecureRandom();
        CTL13MMInstance instance = new CTL13MMInstance(random, instanceParameters);

        Assert.assertTrue(instance.isZero(instance.encodeZero(), 0));
        Assert.assertFalse(instance.isZero(instance.encodeOne(), 0));

        Assert.assertTrue(instance.isZero(
                instance.reduce(
                        instance.encodeZero().multiply(instance.encodeAt(1)).add(
                                instance.encodeZero().multiply(instance.encodeAt(1))
                        ).add(
                                instance.encodeZero().multiply(instance.encodeAt(1))
                        ).add(
                                instance.encodeZero().multiply(instance.encodeAt(1))
                        ).add(
                                instance.encodeZero().multiply(instance.encodeAt(1))
                        )
                ), 1
        ));

    }

}
