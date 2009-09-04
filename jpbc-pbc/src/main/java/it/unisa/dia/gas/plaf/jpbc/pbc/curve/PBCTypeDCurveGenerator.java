package it.unisa.dia.gas.plaf.jpbc.pbc.curve;

import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCTypeDCurveGenerator extends PBCCurveGenerator {
    protected int discriminant;


    public PBCTypeDCurveGenerator(int discriminant) {
        this.discriminant = discriminant;
    }

    protected void pbcGenerate(String fileName) {
        PBCLibraryProvider.getPbcLibrary().pbc_curvegen_d(fileName, discriminant);
    }
}