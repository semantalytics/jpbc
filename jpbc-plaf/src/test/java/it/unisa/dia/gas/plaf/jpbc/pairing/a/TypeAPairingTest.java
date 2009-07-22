package it.unisa.dia.gas.plaf.jpbc.pairing.a;

import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingTest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeAPairingTest extends PairingTest {

    @Override
    protected void setUp() throws Exception {
        pairing = new TypeAPairing(getCurveParams());

        assertNotNull(pairing.getG1());
        assertNotNull(pairing.getG2());
        assertNotNull(pairing.getGT());
        assertNotNull(pairing.getZr());  
    }

    protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a.properties"));
        return curveParams;
    }


    public static void main(String[] args) {
        try {
            TypeAPairingTest test = new TypeAPairingTest();
            test.setUp();
            test.pairingBenchmark();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
