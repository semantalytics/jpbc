package it.unisa.dia.gas.plaf.jpbc.pairing.immutable;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.AbstractJPBCTest;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 2.0.0
 */
public class ImmutablePairingTest extends AbstractJPBCTest {


    public ImmutablePairingTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Override
    public void before() throws Exception {
        super.before();
        this.pairing = new ImmutableParing(pairing);
    }

    @Test
    public void testImmutablePairing() throws Exception {
        assumeTrue(pairing.getDegree() == 2);

        for (int i = 0; i < 4; i++) {
            Field field = pairing.getFieldAt(i);
            assertEquals(true,  field instanceof ImmutableField);

            assertEquals(true, field.newElement().isImmutable());
            assertEquals(true, field.newElement(1).isImmutable());
            assertEquals(true, field.newElement(BigInteger.ONE).isImmutable());
            assertEquals(true, field.newElement(field.newElement(1)).isImmutable());
            assertEquals(true, field.newOneElement().isImmutable());
            assertEquals(true, field.newZeroElement().isImmutable());
            assertEquals(true, field.newRandomElement().isImmutable());
            assertEquals(true, field.newElementFromBytes(field.newOneElement().toBytes()).isImmutable());
            assertEquals(true, field.newElementFromBytes(field.newOneElement().toBytes(), 0).isImmutable());

//            check(field, "add", "sub", "mulZn", "div", "powZn");
//            check2(field, "twice", "square", "invert", "halve", "negate", "sqrt");
        }
    }


    private void check(Field field, String... methods) {
        try {
            for (String method : methods) {
                Element a = field.newRandomElement();
                Element b = (Element) a.getClass().getMethod(method, Element.class).invoke(a, field.newOneElement());
                assertEquals(true, b.isImmutable());
                assertEquals(true, b != a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void check2(Field field, String... methods) {
        try {
            for (String method : methods) {
                Element a = field.newRandomElement();
                Element b = (Element) a.getClass().getMethod(method).invoke(a);
                assertEquals(true, b.isImmutable());
                assertEquals(true, b != a);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
