package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.generators;

import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jlbc.field.floating.FloatingField;
import it.unisa.dia.gas.plaf.jlbc.sampler.Sampler;
import it.unisa.dia.gas.plaf.jlbc.sampler.ZGaussianCDTSampler;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixElement;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2KeyPairGenerator extends MP12PLP2KeyPairGenerator {
    private MP12HLP2KeyPairGenerationParameters params;

    private int barM, w, m, mInBytes;
    private Field inputField, outputField;
    private Sampler<BigInteger> lweErrorSampler;

    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (MP12HLP2KeyPairGenerationParameters) keyGenerationParameters;

        super.init(params);

        this.barM = 2 * n;
        this.w = n * k;
        this.m = barM + w;
        this.mInBytes = (m + 7) / 8;

        this.inputField = new VectorField<Field>(params.getRandom(), Zq, n);
        this.outputField = new VectorField<Field>(params.getRandom(), Zq, m);

        this.lweErrorSampler = new ZGaussianCDTSampler(random, (int) (2 * Math.sqrt(n)));
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        super.generateKeyPair();

        // Construct Parity-check matrix

        // 1. Choose barA random in Z_q[n x barM]

        MatrixField<Field> hatAField = new MatrixField<Field>(random, Zq, n, n);
        Element In = hatAField.newIdentity();
//        System.out.println("In = " + In);
        Element hatA = hatAField.newRandomElement();
//        System.out.println("hatA = " + hatA);

        // TODO: optimize this...
        Element barA = hatAField.union(In, hatA);
//        System.out.println("barA = " + barA);

        // 2. Sample R from Z[barM x w] using distribution D
        Matrix R = sample(barM, w);
        System.out.println("R = " + R);

        // compute covariance matrix
        MatrixField<FloatingField> ff = new MatrixField<FloatingField>(
                random, new FloatingField(random),
                barM + w
        );
        Matrix cov = ff.newElement()
                .setSubMatrixFromMatrixAt(0, 0, R.mulByTranspose())
                .setSubMatrixFromMatrixAt(0, barM, R)
                .setSubMatrixFromMatrixTransposeAt(barM, 0, R)
                .setSubMatrixToIdentityAt(barM, barM, w)
                .transform(new Matrix.Transformer() {
                    public void transform(int row, int col, Element e) {
                        e.negate();
                        if (row == col)
                            e.add(e.getField().newOneElement());
                    }
                }).transform(new Matrix.Transformer() {
                    public void transform(int row, int col, Element e) {
                        if (row == col)
                            e.sub(e.getField().newOneElement());
                    }
                });


        System.out.println("cov = " + cov);
        System.out.println("cov.isSymmetric() = " + cov.isSymmetric());


        // 3. Compute G - barA R
        Element A1 = G.duplicate().sub(barA.duplicate().mul(R));
//        System.out.println("A1 = " + A1);

        Element A = hatAField.union(barA, A1);

//        System.out.println("A = " + A);

        return new AsymmetricCipherKeyPair(
                new MP12HLP2PublicKeyParameters(
                        params.getParameters(), k, m, sampler,
                        g, G,
                        syndromeField, Zq, preimageField,
                        A, barA),
                new MP12HLP2PrivateKeyParameters(params.getParameters(), R)
        );
    }

    public int getM() {
        return m;
    }

    public int getMInBytes() {
        return mInBytes;
    }

    public Field getInputField() {
        return inputField;
    }

    public Field getOutputField() {
        return outputField;
    }


    protected Matrix sample(int n, int m) {
        MatrixField<Field> RField = new MatrixField<Field>(random, Zq, n, m);
        MatrixElement R = RField.newElement();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                R.getAt(i, j).set(lweErrorSampler.sample());
            }
        }

        return R;
    }


}