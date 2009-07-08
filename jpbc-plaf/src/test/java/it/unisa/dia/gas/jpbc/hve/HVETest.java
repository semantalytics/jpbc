package it.unisa.dia.gas.jpbc.hve;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import junit.framework.TestCase;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVETest extends TestCase {


    public void testHVE() {
        // init pairing
        CurveParams curveParams = new CurveParams();
        curveParams.load(HVETest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_311_289.properties"));
        Pairing pairing = new TypeAPairing(curveParams);

        // load public information
        Pub pub = new Pub(pairing, 5);
        pub.setup();

        Key key = pub.keyGen(new byte[]{0, 0, 0, 0, -1});

        String message = "Hello World!!!";

        //Element m = pairing.getGT().newElement().setFromHash("Hello World!!!".getBytes());
//            Element m = pairing.getGT().newElement().setToRandom();
        Element m = pairing.getGT().newElement();
        m.setEncoding(message.getBytes());

        // create the engine
        Engine engine = new Engine(pub.pk, key);

        // enc
        CypherText ct = engine.enc(m, new byte[]{0, 0, 0, 0, 0});

        // dec
        Element m1 = engine.dec(ct);

        // compare m and m1
        assertEquals(0, m.compareTo(m1));
        assertEquals(message, new String(m1.getDecoding()));
    }


}
