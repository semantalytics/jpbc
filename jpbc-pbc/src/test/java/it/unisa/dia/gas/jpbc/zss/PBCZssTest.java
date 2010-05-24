package it.unisa.dia.gas.jpbc.zss;

import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.PBCPairing;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCZssTest extends ZssTest {

    @Override
    protected void setUp() throws Exception {
        if (PBCLibraryProvider.isAvailable()) {

            CurveParams curveParams = new CurveParams();
            curveParams.load(PBCZssTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/pbc/pairing/a_181_603.properties"));

            pairing = new PBCPairing(curveParams);
        } else
            pairing = null;
    }

}