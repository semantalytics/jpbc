package it.unisa.dia.gas.plaf.jpbc.pbc.pairing;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.curve.PBCTypeA1CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pbc.curve.PBCTypeACurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pbc.curve.PBCTypeDCurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pbc.curve.PBCTypeECurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCCurveGeneratorJPBCPairingTest extends PairingTest {


    public void testTypeA() {
        // Check for link library
        if (!PBCLibraryProvider.isAvailable())
            return;

        CurveGenerator curveGenerator = new PBCTypeACurveGenerator(181, 603);
        CurveParams curveParams = (CurveParams) curveGenerator.generate();
        curveParams.put("type", "a");

        initPairing(curveParams);
        doTest();
    }

    public void testTypeA1() {
        // Check for link library
        if (!PBCLibraryProvider.isAvailable())
            return;

        CurveGenerator curveGenerator = new PBCTypeA1CurveGenerator();
        CurveParams curveParams = (CurveParams) curveGenerator.generate();
        curveParams.put("type", "a1");

        initPairing(curveParams);
        doTest();
    }

    public void testTypeD() {
        // Check for link library
        if (!PBCLibraryProvider.isAvailable())
            return;

        CurveGenerator curveGenerator = new PBCTypeDCurveGenerator(9563);
        CurveParams curveParams = (CurveParams) curveGenerator.generate();
        curveParams.put("type", "d");

        initPairing(curveParams);
        doTest();
    }

    public void testTypeE() {
        // Check for link library
        if (!PBCLibraryProvider.isAvailable())
            return;

        CurveGenerator curveGenerator = new PBCTypeECurveGenerator(160, 1024);
        CurveParams curveParams = (CurveParams) curveGenerator.generate();
        curveParams.put("type", "e");

        initPairing(curveParams);
        doTest();
    }


    protected void initPairing(CurveParams curve) {
        pairing = it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory.getPairing(curve);

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