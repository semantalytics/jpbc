package it.unisa.dia.gas.plaf.jpbc.pbc.curve;

import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
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


    public static void main(String[] args) {
        if (args.length < 1)
            throw new IllegalArgumentException("Too few arguments. Usage <rbits> <qbits>");

        if (args.length > 1)
            throw new IllegalArgumentException("Too many arguments. Usage <rbits> <qbits>");

        Integer discriminant = Integer.parseInt(args[0]);

        CurveGenerator generator = new PBCTypeDCurveGenerator(discriminant);
        CurveParams curveParams = (CurveParams) generator.generate();

        System.out.println(curveParams.toString(" "));
    }

}