package it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.generators;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12PLP2KeyPairGenerationParameters;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12PLP2PublicKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.sampler.Sampler;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixElement;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorElement;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import it.unisa.dia.gas.plaf.jpbc.field.z.ZrField;
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
    protected Sampler<BigInteger> sampler;

    protected VectorElement g; // primitiv vector
    protected MatrixElement G; // parity-check matrix

    protected Field syndromeField;

    protected ZrField Zq;
    protected VectorField<ZrField> preimageField;

    protected AsymmetricCipherKeyPair keyPair;

    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (MP12PLP2KeyPairGenerationParameters) keyGenerationParameters;

        this.n = params.getParameters().getN();
        this.k = params.getK();
        this.sampler = params.getSampler();

        SecureRandom random = params.getRandom();
        int q = 1 << params.getK();

        this.Zq = new ZrField(q);
        this.syndromeField = new VectorField<ZrField>(random, Zq, n);
        this.preimageField = new VectorField<ZrField>(random, Zq, n * k);

        // Construct primitive G
        VectorField<ZrField> gField = new VectorField<ZrField>(random, Zq, k);
        this.g = gField.newElement();
        long value = 1;
        for (int i = 0; i < k; i++) {
            g.getAt(i).set(BigInteger.valueOf(value));
            value = value << 1;
        }

        MatrixField<ZrField> GField = new MatrixField<ZrField>(random, Zq, n, n * k);
        this.G = GField.newDiagonalElement(g);

        this.keyPair = new AsymmetricCipherKeyPair(
                new MP12PLP2PublicKeyParameters(
                        params.getParameters(),
                        k,
                        sampler,
                        g, G, syndromeField, Zq, preimageField
                ),
                null
        );
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        return keyPair;
    }

    private Element sample(int n, int m) {
        MatrixField<ZrField> RField = new MatrixField<ZrField>(random, Zq, n, m);
        MatrixElement R = RField.newElement();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                R.getAt(i,j).set(sampler.sample());
            }
        }

        return R;
    }


}
