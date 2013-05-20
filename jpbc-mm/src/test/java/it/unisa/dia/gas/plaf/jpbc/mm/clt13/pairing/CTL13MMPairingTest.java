package it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.AbstractCTL13MMInstance;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13InstanceParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.DefaultCTL13MMInstance;
import junit.framework.Assert;
import org.junit.Before;
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
public class CTL13MMPairingTest {

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
    protected AbstractCTL13MMInstance instance;
    protected Pairing pairing;


    public CTL13MMPairingTest(CTL13InstanceParameters instanceParameters) {
        this.instanceParameters = instanceParameters;
    }

    @Before
    public void before() {
        SecureRandom random = new SecureRandom();
        instance = new DefaultCTL13MMInstance(random, instanceParameters);
        pairing = new CTL13MMPairing(instance);
    }


    @org.junit.Test
    public void test0() {
        Element r = pairing.getFieldAt(0).newRandomElement().getImmutable();
        Element s = pairing.getFieldAt(0).newRandomElement().getImmutable();
        Element z = pairing.getFieldAt(0).newRandomElement();
        Element h = pairing.getFieldAt(0).newRandomElement().getImmutable();

        Element gs = pairing.getFieldAt(1).newElement().powZn(s).getImmutable();
//        Element H = pairing.getFieldAt(1).newElement().powZn(h).getImmutable();
        Element H = pairing.getFieldAt(1).newRandomElement().getImmutable();
        Element H0s = H.powZn(s);

        Element e1 = pairing.getFieldAt(1).newElement().powZn(r).mul(H.powZn(z));
        Element e2 = pairing.getFieldAt(1).newElement().powZn(z.negate());


        Element t1 = pairing.pairing(e1, gs);
        Element t2 = pairing.pairing(e2, H0s);
        Element left = t1.mul(t2);

        Element right = pairing.getFieldAt(2).newElement().powZn(s.duplicate().mul(r));

        System.out.println("left = " + left);
        System.out.println("right = " + right);

        Assert.assertEquals(true, left.isEqual(right));


//        Element s = pairing.getFieldAt(0).newRandomElement();
//        Element r = pairing.getFieldAt(0).newRandomElement();
//        Element h = pairing.getFieldAt(1).newRandomElement();
//        Element z = pairing.getZr().newRandomElement();
//
//        Element e1 = pairing.getFieldAt(1).newElement().powZn(r).mul(h.duplicate().powZn(z));
//        Element e2 = pairing.getFieldAt(1).newElement().powZn(z.duplicate().negate());
//
//        Element t1 = pairing.pairing(e1, pairing.getFieldAt(1).newElement().powZn(s));
//        Element t2 = pairing.pairing(e2, h.duplicate().powZn(s));
//
//        Assert.assertEquals(true,
//                t1.duplicate().mul(t2).isEqual(
//                        pairing.getFieldAt(2).newElement().powZn(
//                                s.duplicate().mul(r)
//                        )
//                ));

    }

}

