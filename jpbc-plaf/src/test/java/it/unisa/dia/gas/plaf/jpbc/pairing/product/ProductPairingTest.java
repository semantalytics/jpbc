package it.unisa.dia.gas.plaf.jpbc.pairing.product;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingTest;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ProductPairingTest extends PairingTest {


    @Override
    protected void setUp() throws Exception {
        this.pairing = new ProductPairing(
                new SecureRandom(),
                new TypeAPairing(
                        new CurveParams().load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"))
                ),
                5
        );
    }

    @Override
    protected CurveParameters getCurveParameters() {
        return null;
    }

    public void testOne() {
        Element g = pairing.getG1().newRandomElement();
        Element h = pairing.getG2().newRandomElement();

        Element p = pairing.pairing(g, h);

        System.out.println("p = " + p);
    }

}
