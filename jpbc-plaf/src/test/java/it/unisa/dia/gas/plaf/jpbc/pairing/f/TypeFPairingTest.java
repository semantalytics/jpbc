package it.unisa.dia.gas.plaf.jpbc.pairing.f;

import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingTest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeFPairingTest extends PairingTest {

    protected CurveParams getCurveParameters() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties"));
        return curveParams;
    }

}