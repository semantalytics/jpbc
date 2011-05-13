package it.unisa.dia.gas.plaf.jpbc.pairing.f;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingTest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeFPairingTest extends PairingTest {

    protected CurveParameters getCurveParameters() {
        return PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties");
    }

}