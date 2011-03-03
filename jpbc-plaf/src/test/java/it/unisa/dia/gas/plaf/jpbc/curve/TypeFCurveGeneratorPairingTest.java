package it.unisa.dia.gas.plaf.jpbc.curve;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.f.TypeFCurveGenerator;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeFCurveGeneratorPairingTest extends CurveGeneratorPairingTest {

    @Override
    protected CurveGenerator getCurveGenerator() {
        return new TypeFCurveGenerator(160);
    }

}
