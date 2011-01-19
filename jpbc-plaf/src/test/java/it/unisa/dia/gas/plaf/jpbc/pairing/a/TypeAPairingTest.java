package it.unisa.dia.gas.plaf.jpbc.pairing.a;

import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingTest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeAPairingTest extends PairingTest {

    protected CurveParams getCurveParameters() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/a/a_181_603.properties"));
        return curveParams;
    }

}
