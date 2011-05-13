package it.unisa.dia.gas.plaf.jpbc.util;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @todo: make this test reasonable!
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ElementUtilsTest extends TestCase {

    public void testTranspose() throws Exception {
        Pairing pairing = PairingFactory.getPairing("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties");

        int dim = 4;
        Element[][] A = new Element[dim][dim];
        A[0][0] = pairing.getZr().newElement(15);
        A[0][1] = pairing.getZr().newElement(21);
        A[0][2] = pairing.getZr().newElement(21);
        A[0][3] = pairing.getZr().newElement(15);

        A[1][0] = pairing.getZr().newElement(11);
        A[1][1] = pairing.getZr().newElement(25);
        A[1][2] = pairing.getZr().newElement(23);
        A[1][3] = pairing.getZr().newElement(20);

        A[2][0] = pairing.getZr().newElement(4);
        A[2][1] = pairing.getZr().newElement(19);
        A[2][2] = pairing.getZr().newElement(12);
        A[2][3] = pairing.getZr().newElement(1);

        A[3][0] = pairing.getZr().newElement(28);
        A[3][1] = pairing.getZr().newElement(25);
        A[3][2] = pairing.getZr().newElement(25);
        A[3][3] = pairing.getZr().newElement(8);

        ElementUtils.transpose(A);
    }

    public void testInvert() throws Exception {
        Pairing pairing = PairingFactory.getPairing("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties");

        SecureRandom SRNG = SecureRandom.getInstance("SHA1PRNG");
        boolean invertiable = true;
        int dim = 4;

        // init the matrix
        Element[][] A = new Element[dim][dim];
        for (int i = 0; i < dim; i++) {
            // generate random numbers
            for (int j = 0; j < dim; j++) {
                A[i][j] = pairing.getZr().newElement().set(new BigInteger(5, SRNG));
            }
            // generate dependent vector
            if (!invertiable)
                A[dim - 1] = A[2];
        }

        Element[][] inverted = ElementUtils.invert(A);

        Element[][] identity = ElementUtils.multiply(A, inverted);
    }
}
