package it.unisa.dia.gas.plaf.jpbc.pbc.curve;

import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCTypeA1CurveGenerator extends PBCCurveGenerator {


    public PBCTypeA1CurveGenerator() {
    }


    protected void pbcGenerate(String fileName) {
        PBCLibraryProvider.getPbcLibrary().pbc_curvegen_a1(fileName);
    }
}