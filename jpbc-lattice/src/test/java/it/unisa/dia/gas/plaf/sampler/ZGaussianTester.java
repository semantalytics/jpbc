package it.unisa.dia.gas.plaf.sampler;

import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.utils.LatticeUtils;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jlbc.sampler.*;
import it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.field.z.SymmetricZrField;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import org.apfloat.Apfloat;

import java.math.BigInteger;
import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils.newApfloat;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ZGaussianTester {

    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        int k = 16;
        int nn = 10;
        int mm = 10;
        BigInteger q = BigInteger.ONE.shiftLeft(k);

        Apfloat gaussianParameter = LatticeUtils.RRP;
        GaussianSampler<BigInteger> sampler = new DiscreteGaussianLazyRSSampler(random, gaussianParameter);
        sampler.setCenter(newApfloat("0.000002456"));
        MatrixField<Field> RField = new MatrixField<Field>(random, new SymmetricZrField(q), nn, mm);

        Matrix R = RField.newElementFromSampler(sampler);

        System.out.println("R = " + R);
        System.out.println("LatticeUtils.getS1R() = " + ApfloatUtils.toString(LatticeUtils.getS1R(gaussianParameter, nn, mm)));

    }


}
