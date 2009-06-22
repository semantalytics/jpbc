package it.unisa.dia.gas.jpbc.zss;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeAPairing;
import junit.framework.TestCase;

import java.io.IOException;
import java.util.Properties;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ZssTest extends TestCase {

    public void testZss() {
        // Load pairing

        try {
            Properties properties = new Properties();
            properties.load(ZssTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_311_289.properties"));

            Pairing pairing = new TypeAPairing(properties);

            long time1, time2;

            Element P, Ppub, x, S, H, t1, t2, t3, t4;

/*
            element_init_Zr(x, pairing);
            element_init_Zr(H, pairing);
            element_init_Zr(t1, pairing);

            element_init_G1(S, pairing);
            element_init_G1(P, pairing);
            element_init_G1(Ppub, pairing);
            element_init_G1(t2, pairing);

            element_init_GT(t3, pairing);
            element_init_GT(t4, pairing);

            if (!pairing_is_symmetric(pairing)) {
                fprintf(stderr, "only works with symmetric pairing\n");
                exit(1);
            }
*/

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
            t1 = pairing.getZr().newElement().set(H).add(x).invert();
            S = pairing.getG1().newElement().set(P).mulZn(t1);

            System.out.printf("Signature of message \"Message\" is:\n");
            System.out.println("S = " + S);

            System.out.printf("VERIFY\n");
            H.setFromHash("Message".getBytes());
            t2 = pairing.getG1().newElement().set(P).mulZn(H).add(Ppub);

            time1 = System.currentTimeMillis();
            t3 = pairing.pairing(t2, S);
            t4 = pairing.pairing(P, P);
            time2 = System.currentTimeMillis();

            System.out.println("e(H(m)P + Ppub, S) = " + t3);
            System.out.println("e(P, P) = " + t4);

            assertEquals(0, t3.compareTo(t4));


            System.out.printf("All time = %d\n", time2 - time1);
        } catch (IOException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
    }

}
