package it.unisa.dia.gas.jpbc.zss;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;
import junit.framework.TestCase;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCZssTest extends TestCase {

    public void testZss() {
        // Load pairing

        CurveParams curveParams = new CurveParams();
        curveParams.load(PBCZssTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_311_289.param"));

        Pairing pairing = new PBCPairing(curveParams);

        long time1, time2;

        Element P, Ppub, x, S, H, t1, t2, t3, t4;

        System.out.printf("ZSS short signature schema\n");
        System.out.printf("KEYGEN\n");

        x = pairing.getZr().newElement().setToRandom();
        P = pairing.getG1().newElement().setToRandom();
        Ppub = P.duplicate().mulZn(x);

        System.out.println("P = " + P);
        System.out.println("x = " + x);
        System.out.println("Ppub = " + Ppub);

        System.out.printf("SIGN\n");
        H = pairing.getZr().newElement().setFromHash("Message".getBytes());
//            H = pairing.getZr().newElement().setToRandom();
        t1 = pairing.getZr().newElement().set(H).add(x).invert();
        S = pairing.getG1().newElement().set(P).mulZn(t1);

        System.out.printf("Signature of message \"Message\" is:\n");
        System.out.println("S = " + S);

        System.out.printf("VERIFY\n");
//            H.setFromHash("Message".getBytes());
//            H = pairing.getZr().newElement().setToOne();
        t2 = pairing.getG1().newElement().set(P).mulZn(H).add(Ppub);

        time1 = System.currentTimeMillis();
        t3 = pairing.pairing(t2, S);
        t4 = pairing.pairing(P, P);
        time2 = System.currentTimeMillis();

        System.out.println("e(H(m)P + Ppub, S) = " + t3);
        System.out.println("e(P, P) = " + t4);

        assertEquals(0, t3.compareTo(t4));

        System.out.printf("All time = %d\n", time2 - time1);
    }

}