package it.unisa.dia.gas.plaf.jpbc.pairing.d;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingTest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeDPairingTest extends PairingTest {

    protected CurveParameters getCurveParameters() {
        return PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/pairing/d/d_9563.properties");
    }

}