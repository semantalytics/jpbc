package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingTest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairingTest extends PairingTest {

    protected void setUp() throws Exception {
        CurveParams curveParams = new CurveParams();
        curveParams.load(PBCPairingTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_311_289.param"));

        pairing = new PBCPairing(curveParams);

        assertNotNull(pairing.getG1());
        assertNotNull(pairing.getG2());
        assertNotNull(pairing.getGT());
        assertNotNull(pairing.getZr());
    }

    public static void main(String[] args) {
        try {
            PBCPairingTest test = new PBCPairingTest();
            test.setUp();
            test.pairingBenchmark();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
