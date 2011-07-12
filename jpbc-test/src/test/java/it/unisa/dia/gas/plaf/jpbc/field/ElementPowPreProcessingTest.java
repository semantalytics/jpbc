package it.unisa.dia.gas.plaf.jpbc.field;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.plaf.jpbc.AbstractJPBCTest;
import org.junit.Test;

import java.math.BigInteger;

import static org.junit.Assert.assertTrue;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ElementPowPreProcessingTest extends AbstractJPBCTest {


    public ElementPowPreProcessingTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testPowPreProcessing() {
        Element e1 = pairing.getZr().newElement().setToRandom();
        Element e2 = e1.duplicate();
        ElementPowPreProcessing ppp = e1.pow();
        BigInteger n = pairing.getZr().newElement().setToRandom().toBigInteger();

        Element r1 = ppp.pow(n);
        Element r2 = e2.duplicate().pow(n);

        assertTrue(r1.isEqual(r2));
    }

    @Test
    public void testPowPreProcessingZn() {
        Element e1 = pairing.getZr().newElement().setToRandom();
        Element e2 = e1.duplicate();
        ElementPowPreProcessing ppp = e1.pow();
        Element n = pairing.getZr().newElement().setToRandom();

        Element r1 = ppp.powZn(n);
        Element r2 = e2.duplicate().powZn(n);

        assertTrue(r1.isEqual(r2));
    }

    @Test
    public void testPowPreProcessingBytes() {
        Element e1 = pairing.getG1().newElement().setToRandom();

        ElementPowPreProcessing ppp1 = e1.pow();
        ElementPowPreProcessing ppp2 = e1.getField().pow(ppp1.toBytes());

        Element n = pairing.getZr().newElement().setToRandom();

        Element r1 = ppp1.powZn(n);
        Element r2 = ppp2.powZn(n);

        assertTrue(r1.isEqual(r2));
    }

}
