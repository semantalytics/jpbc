package it.unisa.dia.gas.plaf.jpbc.pbc.curve;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.a.TypeACurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.e.TypeECurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.f.TypeFCurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;
import it.unisa.dia.gas.plaf.jpbc.pbc.pairing.PairingTest;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class JPBCCurveGeneratorPBCPairingTest extends PairingTest {

    public void testTypeA() {
        CurveGenerator curveGenerator = new TypeACurveGenerator(181, 603);
        initPairing(curveGenerator.generate());
        doTest();
    }

    public void testTypeA1() {
        CurveGenerator curveGenerator = new TypeA1CurveGenerator(2, 512);
        initPairing(curveGenerator.generate());
        doTest();
    }

    public void testTypeD() {
//        CurveGenerator curveGenerator = new TypeDCurveGenerator(9563);
//        initPairing(curveGenerator.generate());
//        doTest();
    }

    public void testTypeE() {
        CurveGenerator curveGenerator = new TypeECurveGenerator(160, 1024);
        initPairing(curveGenerator.generate());
        doTest();
    }

    public void testTypeF() {
        CurveGenerator curveGenerator = new TypeFCurveGenerator(160);
        initPairing(curveGenerator.generate());
        doTest();
    }


    protected void initPairing(Map curve) {
        pairing = null;

        // Check for link library
        if (!WrapperLibraryProvider.isAvailable())
            return;

        pairing = new PBCPairing((CurveParams) curve);

        assertNotNull(pairing.getG1());
        assertNotNull(pairing.getG2());
        assertNotNull(pairing.getGT());
        assertNotNull(pairing.getZr());
    }

    protected void doTest() {
        doPairing();
        doPairingPreProcessing();
        doPairingSymmetric();
    }

}