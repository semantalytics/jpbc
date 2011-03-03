package it.unisa.dia.gas.jpbc.zss;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import junit.framework.TestCase;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ZssTest extends TestCase {

    protected Pairing pairing;

    @Override
    protected void setUp() throws Exception {
        // Load pairing
        CurveParams curveParams = new CurveParams();
        curveParams.load(ZssTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"));

        pairing = new TypeAPairing(curveParams);
    }


    public void testZss() {
        if (pairing == null)
            return;
        
        Element P, Ppub, x, S, H, t1, t2, t3, t4;

        x = pairing.getZr().newRandomElement();
        P = pairing.getG1().newRandomElement();

        Ppub = P.duplicate().mulZn(x);

        byte[] source = "Message".getBytes();

        H = pairing.getZr().newElement().setFromHash(source, 0, source.length);
        t1 = pairing.getZr().newElement().set(H).add(x).invert();
        S = pairing.getG1().newElement().set(P).mulZn(t1);

        t2 = pairing.getG1().newElement().set(P).mulZn(H).add(Ppub);

        t3 = pairing.pairing(t2, S);
        t4 = pairing.pairing(P, P);

        assertTrue(t3.isEqual(t4));
     }

}
