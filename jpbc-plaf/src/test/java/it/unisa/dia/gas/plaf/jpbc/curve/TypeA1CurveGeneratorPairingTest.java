package it.unisa.dia.gas.plaf.jpbc.curve;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeA1CurveGeneratorPairingTest extends CurveGeneratorPairingTest {

    public void testTypeA1() {
        // Generate TypeA1 curve
        CurveGenerator curveGenerator = new TypeA1CurveGenerator(2, 512);
        initPairing(curveGenerator.generate());
        doTest();
    }

}
