package it.unisa.dia.gas.plaf.jpbc.pairing.d;

import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingTest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeDPairingTest extends PairingTest {

    @Override
    protected void setUp() throws Exception {
        pairing = new TypeDPairing(getCurveParams());

        assertNotNull(pairing.getG1());
        assertNotNull(pairing.getG2());
        assertNotNull(pairing.getGT());
        assertNotNull(pairing.getZr());
    }

    protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties"));
        return curveParams;
    }


    public static void main(String[] args) {
        try {
            TypeDPairingTest test = new TypeDPairingTest();
            test.setUp();
            test.pairingBenchmark();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}