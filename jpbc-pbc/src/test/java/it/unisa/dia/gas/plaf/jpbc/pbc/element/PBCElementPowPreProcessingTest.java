package it.unisa.dia.gas.plaf.jpbc.pbc.element;

import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.element.ElementPowPreProcessingTest;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;
import it.unisa.dia.gas.plaf.jpbc.wrapper.jna.WrapperLibraryProvider;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElementPowPreProcessingTest extends ElementPowPreProcessingTest {

   protected void setUp() throws Exception {
        if (WrapperLibraryProvider.isAvailable())
            pairing = new PBCPairing(getCurveParameters());
        else
            pairing = null;
    }

    protected CurveParameters getCurveParameters() {
        return PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_181_603.properties");
    }

}