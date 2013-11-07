package it.unisa.dia.gas.plaf.jpbc.pairing.immutable;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.AbstractJPBCTest;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 2.0.0
 */
public class ImmutablePairingTest extends AbstractJPBCTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties"},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties"},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/e/e.properties"},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties"},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/g/g149.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/a1/a1_3primes.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/e/e.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties"},
                {true, "it/unisa/dia/gas/plaf/jpbc/pairing/g/g149.properties"},
                {false, "it/unisa/dia/gas/plaf/jpbc/pairing/mm/ctl13/toy.properties"}
        };

        return Arrays.asList(data);
    }


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
        int n = pairing.getDegree();
        if (n == 2)
            n = 4;

        for (int i = 0; i < n; i++) {
            Field field = pairing.getFieldAt(i);
            assertEquals(true, field instanceof ImmutableField);

            assertEquals(true, field.newElement().isImmutable());
            assertEquals(true, field.newElement(1).isImmutable());
            assertEquals(true, field.newElement(BigInteger.ONE).isImmutable());
            assertEquals(true, field.newElement(field.newElement(1)).isImmutable());
            assertEquals(true, field.newOneElement().isImmutable());
            assertEquals(true, field.newZeroElement().isImmutable());
            assertEquals(true, field.newRandomElement().isImmutable());
            assertEquals(true, field.newElementFromBytes(field.newOneElement().toBytes()).isImmutable());
            assertEquals(true, field.newElementFromBytes(field.newOneElement().toBytes(), 0).isImmutable());

            verifySetMethods(field);

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

    private void verifySetMethods(Field field) {
        Element a = field.newRandomElement();
        try {
            a.set(5);
            fail("Cannot set an immutable element.");
        } catch (Exception e) {
            assertEquals("Invalid call on an immutable element", e.getMessage());
        }

        try {
            a.set(BigInteger.ONE);
            fail("Cannot set an immutable element.");
        } catch (Exception e) {

        }

        try {
            a.set(field.newRandomElement());
            fail("Cannot set an immutable element.");
        } catch (Exception e) {
            assertEquals("Invalid call on an immutable element", e.getMessage());
        }

        try {
            a.setToZero();
            fail("Cannot set an immutable element.");
        } catch (Exception e) {
            assertEquals("Invalid call on an immutable element", e.getMessage());
        }

        try {
            a.setToOne();
            fail("Cannot set an immutable element.");
        } catch (Exception e) {
            assertEquals("Invalid call on an immutable element", e.getMessage());
        }

        try {
            a.setToRandom();
            fail("Cannot set an immutable element.");
        } catch (Exception e) {
            assertEquals("Invalid call on an immutable element", e.getMessage());
        }

        try {
            a.setFromBytes(a.toBytes());
            fail("Cannot set an immutable element.");
        } catch (Exception e) {
            assertEquals("Invalid call on an immutable element", e.getMessage());
        }

        try {
            a.setFromBytes(a.toBytes(), 0);
            fail("Cannot set an immutable element.");
        } catch (Exception e) {
            assertEquals("Invalid call on an immutable element", e.getMessage());
        }

        try {
            a.setFromHash(a.toBytes(), 0, 80);
            fail("Cannot set an immutable element.");
        } catch (Exception e) {
            assertEquals("Invalid call on an immutable element", e.getMessage());
        }

        if (a instanceof Point) {
            Point p = (Point) a;
            try {
                p.setFromBytesCompressed(p.toBytesCompressed());
                fail("Cannot set an immutable element.");
            } catch (Exception e) {
                assertEquals("Invalid call on an immutable element", e.getMessage());
            }

            try {
                p.setFromBytesCompressed(p.toBytesCompressed(), 0);
                fail("Cannot set an immutable element.");
            } catch (Exception e) {
                assertEquals("Invalid call on an immutable element", e.getMessage());
            }

            try {
                p.setFromBytesX(p.toBytesX());
                fail("Cannot set an immutable element.");
            } catch (Exception e) {
                assertEquals("Invalid call on an immutable element", e.getMessage());
            }

            try {
                p.setFromBytesX(p.toBytesX(), 0);
                fail("Cannot set an immutable element.");
            } catch (Exception e) {
                assertEquals("Invalid call on an immutable element", e.getMessage());
            }
        }


    }

}
