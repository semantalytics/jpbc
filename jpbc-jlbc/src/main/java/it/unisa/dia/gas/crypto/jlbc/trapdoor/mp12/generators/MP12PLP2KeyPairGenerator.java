package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.generators;

import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12PLP2KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jlbc.field.z.ZFieldSelector;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.math.BigInteger;
import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12PLP2KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private MP12PLP2KeyPairGenerationParameters params;

    protected SecureRandom random;
    protected int n, k;
    protected BigInteger q;
    protected Sampler<BigInteger> discreteGaussianSampler;

    protected Vector g; // primitiv vector
    protected Matrix G; // parity-check matrix

    protected Field syndromeField;

    protected Field Zq;
    protected VectorField<Field> preimageField;

    protected AsymmetricCipherKeyPair keyPair;

    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (MP12PLP2KeyPairGenerationParameters) keyGenerationParameters;

        this.n = params.getParameters().getN();
        this.k = params.getK();
        this.discreteGaussianSampler = params.getDiscreteGaussianSampler();

        SecureRandom random = params.getRandom();

        this.Zq = ZFieldSelector.getInstance().getSymmetricZrFieldPowerOfTwo(random, k);
        this.syndromeField = new VectorField<Field>(random, Zq, n);
        this.preimageField = new VectorField<Field>(random, Zq, n * k);

        // Construct primitive G
        VectorField<Field> gField = new VectorField<Field>(random, Zq, k);
        this.g = gField.newElement();
        BigInteger value = BigInteger.ONE;
        for (int i = 0; i < k; i++) {
            g.getAt(i).set(value);
            value = value.shiftLeft(1);
        }

        this.G = new MatrixField<Field>(random, Zq, n, n * k).newDiagonalElement(g);

        this.keyPair = new AsymmetricCipherKeyPair(
                new MP12PLP2PublicKeyParameters(
                        params.getParameters(),
                        k, discreteGaussianSampler,
                        G,
                        syndromeField, Zq, preimageField
                ),
                null
        );
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        return keyPair;
    }

}
