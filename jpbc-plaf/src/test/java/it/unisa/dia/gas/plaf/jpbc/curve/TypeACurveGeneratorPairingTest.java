package it.unisa.dia.gas.plaf.jpbc.curve;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeACurveGeneratorPairingTest extends CurveGeneratorPairingTest {

    @Override
    protected CurveGenerator getCurveGenerator() {
        return new TypeACurveGenerator(181, 603);
    }

}
