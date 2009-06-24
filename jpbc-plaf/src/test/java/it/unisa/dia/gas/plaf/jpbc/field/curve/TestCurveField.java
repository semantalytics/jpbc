package it.unisa.dia.gas.plaf.jpbc.field.curve;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import junit.framework.TestCase;

import java.util.Properties;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TestCurveField extends TestCase {

    protected Pairing pairing;

    @Override
    protected void setUp() throws Exception {
        pairing = new TypeAPairing(typeAProperties());

        assertNotNull(pairing.getG1());
        assertNotNull(pairing.getG2());
        assertNotNull(pairing.getGT());
        assertNotNull(pairing.getZr());
    }

    protected Properties typeAProperties() {
        Properties properties = new Properties();

        properties.put("type", "a");

        properties.put("q", "389517483806764372162075727451538192950200087543273118390202621592813077775963376258032864387");
        properties.put("h", "783228");
        properties.put("r", "497323236409786642155382248146820840100456173098092915971087118428877769660894881513471");
        properties.put("exp2", "288");
        properties.put("exp1", "144");
        properties.put("sign1", "1");
        properties.put("sign0", "-1");

        return properties;
    }


    public void testOne() {
        Element g = pairing.getG1().newElement().setToRandom();
        Element a = pairing.getZr().newElement().setToRandom();
        Element r = pairing.getZr().newElement().setToRandom();

        Element egg = pairing.pairing(g, g);
//        System.out.println("egg = " + egg);
        Element egga = egg.duplicate().powZn(a);

        Element gar = g.duplicate().powZn(
                a.duplicate().div(r)
        );
        Element gr = g.duplicate().powZn(r);

        Element egargr = pairing.pairing(gar, gr);

        assertEquals(0, egga.compareTo(egargr));

        Element eggMinusa = egg.duplicate().powZn(a.duplicate().negate());

//        System.out.println("egargr = " + egargr);
//        System.out.println("eggMinusa = " + eggMinusa);

        Element one = egargr.duplicate().mul(eggMinusa);

//        System.out.println("egg = " + egg);
//        System.out.println("one = " + one);

        assertTrue(one.isOne());
    }



}