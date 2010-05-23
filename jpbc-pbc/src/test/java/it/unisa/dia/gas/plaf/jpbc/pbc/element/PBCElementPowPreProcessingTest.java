package it.unisa.dia.gas.plaf.jpbc.pbc.element;

import it.unisa.dia.gas.plaf.jpbc.element.ElementPowPreProcessingTest;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.pbc.pairing.PBCPairingTest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElementPowPreProcessingTest extends ElementPowPreProcessingTest {

   protected void setUp() throws Exception {
        if (PBCLibraryProvider.isAvailable())
            pairing = new PBCPairing(getCurveParams());
        else
            pairing = null;
    }

    protected CurveParams getCurveParams() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(PBCPairingTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_181_603.properties"));
        return curveParams;
    }

}