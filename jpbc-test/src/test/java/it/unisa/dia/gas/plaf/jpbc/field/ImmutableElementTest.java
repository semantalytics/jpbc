package it.unisa.dia.gas.plaf.jpbc.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.quadratic.QuadraticField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

import static junit.framework.Assert.assertEquals;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
@RunWith(value = Parameterized.class)
public class ImmutableElementTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        SecureRandom secureRandom = new SecureRandom();

        Object[][] data = {
                {new ZrField(BigInteger.probablePrime(128, secureRandom))},
                {new QuadraticField(secureRandom, new ZrField(BigInteger.probablePrime(128, secureRandom)))},
        };

        return Arrays.asList(data);
    }


    protected Field field;


    public ImmutableElementTest(Field field) {
        this.field = field;
    }

    @Test
    public void testImmutable() {
        Element e = field.newRandomElement();
        Element immutableE = e.duplicate();

        assertEquals(true, e.equals(immutableE));
    }

}
