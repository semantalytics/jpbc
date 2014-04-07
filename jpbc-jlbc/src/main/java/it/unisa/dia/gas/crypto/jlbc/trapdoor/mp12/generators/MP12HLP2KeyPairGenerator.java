package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.generators;

import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.utils.MP12P2Utils;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
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
    private Sampler<BigInteger> hlZSampler;

    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (MP12HLP2KeyPairGenerationParameters) keyGenerationParameters;

        super.init(params);

        this.barM = 2 * n;
        this.w = n * k;
        this.m = barM + w;
        this.mInBytes = (m + 7) / 8;

        this.inputField = new VectorField<Field>(params.getRandom(), Zq, n);
        this.outputField = new VectorField<Field>(params.getRandom(), Zq, m);

        this.hlZSampler = MP12P2Utils.getLWENoiseSampler(random, n);
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        super.generateKeyPair();

        // Construct Parity-check matrix

        // 1. Choose barA random in Z_q[n x barM]
        MatrixField<Field> hatAField = new MatrixField<Field>(random, Zq, n);
        Matrix hatA = hatAField.newRandomElement();
        Matrix barA = hatAField.union(hatAField.newIdentity(), hatA);

        // 2. Sample R from Z[barM x w] using distribution D
        Matrix R = MatrixField.newElementFromSampler(hatAField, barM, w, hlZSampler);
//        System.out.println("R = " + R);

        // 3. Compute G - barA R
//        Element A1 = G.duplicate().sub(barA.mul(R));
        Element A1 = ((Matrix) barA.mul(R)).transform(new Matrix.Transformer() {
            public void transform(int row, int col, Element e) {
                e.negate();
                if (!G.isZeroAt(row, col))
                    e.add(G.getAt(row, col));
            }
        });

        Element A = hatAField.union(barA, A1);

        return new AsymmetricCipherKeyPair(
                new MP12HLP2PublicKeyParameters(
                        params.getParameters(), k, m,
                        discreteGaussianSampler,
                        G,
                        syndromeField, Zq, preimageField,
                        A
                ),
                new MP12HLP2PrivateKeyParameters(params.getParameters(), R)
        );
    }

    public AsymmetricCipherKeyPair loadKeyPair() {
        super.generateKeyPair();

        // Load PK
        Element A = new MatrixField<Field>(random, Zq, n, w + barM).newElementFromBytes(null);

        // Load SK
        Matrix R = new MatrixField<Field>(random, Zq, barM, w).newElementFromBytes(null);

        return new AsymmetricCipherKeyPair(
                new MP12HLP2PublicKeyParameters(
                        params.getParameters(), k, m,
                        discreteGaussianSampler,
                        G,
                        syndromeField, Zq, preimageField,
                        A
                ),
                new MP12HLP2PrivateKeyParameters(params.getParameters(), R)
        );
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


    public void store(AsymmetricCipherKeyPair keyPair) {
        MP12HLP2PublicKeyParameters pk = (MP12HLP2PublicKeyParameters) keyPair.getPublic();
        MP12HLP2PrivateKeyParameters sk = (MP12HLP2PrivateKeyParameters) keyPair.getPrivate();

        // Store A and R

//        pk.getA().toBytes();
//        sk.getR().toBytes();
    }


}
