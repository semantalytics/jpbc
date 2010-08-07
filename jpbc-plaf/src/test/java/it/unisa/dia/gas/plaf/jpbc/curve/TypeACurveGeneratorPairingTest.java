package it.unisa.dia.gas.plaf.jpbc.curve;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TypeACurveGeneratorPairingTest extends CurveGeneratorPairingTest {

    public void testTypeA() {
        CurveGenerator curveGenerator = new TypeACurveGenerator(181, 603);
        
        initPairing(curveGenerator.generate());
        doTest();
    }

}
