package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPowPreProcessingTest extends TestCase {

    protected Pairing pairing;


    public void testPowPreProcessing() {
        Element e1 = pairing.getZr().newElement().setToRandom();
        Element e2 = e1.duplicate();

        System.out.println("e1 = " + e1);
        System.out.println("e2 = " + e2);

        SecureRandom random = new SecureRandom();
        e1.initPowPreProcessing();

        long t1 = 0;
        long t2 = 0;

        for (int i = 0; i < 10000; i++) {
            BigInteger n = new BigInteger(e1.getField().getOrder().bitLength(), random);

            long start = System.currentTimeMillis();
            Element r1 = e1.duplicate().powPreProcessing(n);
            long end = System.currentTimeMillis();
            t1+=(end-start);

            start = System.currentTimeMillis();
            Element r2 = e2.duplicate().pow(n);
            end = System.currentTimeMillis();
            t2+=(end-start);

            assertEquals(0, r1.compareTo(r2));
        }

        System.out.println("t1 = " + t1);
        System.out.println("t2 = " + t2);
    }



    @Override
    protected void setUp() throws Exception {
        pairing = new PBCPairing(getCurveParams());
    }

    protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(PBCPairingTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_603_181.properties"));
        return curveParams;
    }

}