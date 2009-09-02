package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.naive.NaiveField;
import junit.framework.TestCase;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HilbertPolyTest extends TestCase {

    public void testHilbertPolyRoot() {
        HilbertPolyGenerator hilbertPolyGenerator = new HilbertPolyGenerator(59);
        BigInteger[] hilbertPolyCoeffs = hilbertPolyGenerator.getHilbertPoly();

        PolyField field = new PolyField(new NaiveField(BigInteger.valueOf(17)));
        PolyElement hilbertPoly = field.newElement();
        hilbertPoly.setFromCoefficientMonic(hilbertPolyCoeffs);

        System.out.println("hilbertPoly = " + hilbertPoly);

        Element root = hilbertPoly.findRoot();

        System.out.println("root = " + root);
    }

}
