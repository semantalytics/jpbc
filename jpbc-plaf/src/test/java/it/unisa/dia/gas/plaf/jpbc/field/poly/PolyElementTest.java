package it.unisa.dia.gas.plaf.jpbc.field.poly;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.naive.NaiveField;
import junit.framework.TestCase;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PolyElementTest extends TestCase {

/*
    public void testGDC() {
        PolyField field = new PolyField(new NaiveField(BigInteger.valueOf(17)));

        PolyElement r = field.newElement();
        r.setFromCoefficientMonic(new BigInteger[]{BigInteger.valueOf(3), BigInteger.valueOf(1)});

        PolyElement g = field.newElement();
        g.setFromCoefficientMonic(new BigInteger[]{BigInteger.valueOf(14), BigInteger.valueOf(8),BigInteger.valueOf(1)});

        Element fac = field.newElement().set(r).gcd(g);
        System.out.println("fac = " + fac);
    }
*/


    public void testFindRoot() {
        PolyField field = new PolyField(new NaiveField(BigInteger.valueOf(17)));
        PolyElement element = field.newElement();

        BigInteger[] coeff = new BigInteger[]{
                BigInteger.valueOf(5),
                BigInteger.valueOf(12),
                BigInteger.valueOf(12),
                BigInteger.valueOf(1),
        };
        element.setFromCoefficientMonic(coeff);
        System.out.println("poly = " + element);

        Element root = element.findRoot();
        System.out.println("root = " + root);

        BigInteger rootInt = root.toBigInteger();

        if (!BigInteger.valueOf(2).equals(rootInt) &&
            !BigInteger.valueOf(7).equals(rootInt) &&
            !BigInteger.valueOf(13).equals(rootInt))
            fail("Invalid root!");
    }

}
