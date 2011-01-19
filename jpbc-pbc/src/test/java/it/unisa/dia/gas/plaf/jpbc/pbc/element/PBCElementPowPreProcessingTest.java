package it.unisa.dia.gas.plaf.jpbc.pbc.element;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.element.ElementPowPreProcessingTest;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;
import it.unisa.dia.gas.plaf.jpbc.pbc.pairing.PBCPairingTest;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElementPowPreProcessingTest extends ElementPowPreProcessingTest {

   protected void setUp() throws Exception {
        if (WrapperLibraryProvider.isAvailable())
            pairing = new PBCPairing((CurveParams) getCurveParameters());
        else
            pairing = null;
    }

    protected CurveParameters getCurveParameters() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(PBCPairingTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_181_603.properties"));
        return curveParams;
    }

}