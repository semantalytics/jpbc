package it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.generators;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12HLP2KeyPairGenerationParameters;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12HLP2PrivateKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixElement;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2KeyPairGenerator extends MP12PLP2KeyPairGenerator {
    private MP12HLP2KeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (MP12HLP2KeyPairGenerationParameters) keyGenerationParameters;

        super.init(params);
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        super.generateKeyPair();

        // Construct Parity-check matrix

        // 1. Choose barA random in Z_q[n x barM]
        int barM = 2 * n;
        int w = n * k;
        int m = barM + w;

        MatrixField<Field> hatAField = new MatrixField<Field>(random, Zq, n, n);
        Element In = hatAField.newIdentity();
//        System.out.println("In = " + In);
        Element hatA = hatAField.newRandomElement();
//        System.out.println("hatA = " + hatA);

        Element barA = hatAField.union(In, hatA);
//        System.out.println("barA = " + barA);

        // 2. Sample R from Z[barM x w] using distribution D
        Element R = sample(barM, w);
//        System.out.println("R = " + R);

        // 3. Compute G - barA R
        Element A1 = G.duplicate().sub(barA.duplicate().mul(R));
//        System.out.println("A1 = " + A1);

        Element A = hatAField.union(barA, A1);

//        System.out.println("A = " + A);

        return new AsymmetricCipherKeyPair(
                new MP12HLP2PublicKeyParameters(params.getParameters(), k, m, sampler,
                        g, G,
                        syndromeField, Zq, preimageField,
                        A, barA),
                new MP12HLP2PrivateKeyParameters(params.getParameters(), R)
        );
    }

    private Element sample(int n, int m) {
        MatrixField<Field> RField = new MatrixField<Field>(random, Zq, n, m);
        MatrixElement R = RField.newElement();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                R.getAt(i, j).set(sampler.sample());
            }
        }

        return R;
    }


}
