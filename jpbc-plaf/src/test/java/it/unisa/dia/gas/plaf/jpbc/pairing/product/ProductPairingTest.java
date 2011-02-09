package it.unisa.dia.gas.plaf.jpbc.pairing.product;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import junit.framework.TestCase;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ProductPairingTest extends TestCase {


    public void testOne() {
        Pairing pairing = new ProductPairing(
                new SecureRandom(),
                new TypeAPairing(
                        new CurveParams().load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"))
                ),
                5
        );

        Element g = pairing.getG1().newRandomElement();
        Element h = pairing.getG2().newRandomElement();

        Element p = pairing.pairing(g, h);

        System.out.println("p = " + p);
    }

}
