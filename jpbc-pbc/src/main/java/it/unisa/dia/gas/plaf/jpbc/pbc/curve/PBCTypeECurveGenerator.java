package it.unisa.dia.gas.plaf.jpbc.pbc.curve;

import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCTypeECurveGenerator extends PBCCurveGenerator {
    protected int rbits, qbits;


    public PBCTypeECurveGenerator(int rbits, int qbits) {
        this.rbits = rbits;
        this.qbits = qbits;
    }


    protected void pbcGenerate(String fileName) {
        PBCLibraryProvider.getPbcLibrary().pbc_curvegen_e(fileName, rbits, qbits);
    }
}