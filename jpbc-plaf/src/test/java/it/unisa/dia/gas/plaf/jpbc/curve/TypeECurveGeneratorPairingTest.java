package it.unisa.dia.gas.plaf.jpbc.curve;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.e.TypeECurveGenerator;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeECurveGeneratorPairingTest extends CurveGeneratorPairingTest {


    @Override
    protected CurveGenerator getCurveGenerator() {
        return new TypeECurveGenerator(160, 1024);
    }


}
