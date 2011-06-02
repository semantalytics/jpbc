package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HVEIP08AttributesEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08AttributesOnlySearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Angelo De Caro
 */
public class HVEIP08AttributesEngineTest extends TestCase {
    HVEIP08AttributesEngine engine;

    public void testHVE() {
        engine = new HVEIP08AttributesEngine();

        int n = 20;
        AsymmetricCipherKeyPair keyPair = setup(genBinaryParam(n));

        System.out.println("After keygen");
        for (int i = 0; i < 2; i++) {
            int[][] matching = createMatchingVectors(n);
            assertEquals(true,
                    test(
                            keyGen(keyPair.getPrivate(), matching[0]),
                            enc(keyPair.getPublic(),     matching[1])
                    )
            );

            int[][] nonMatching = createNonMatchingVectors(n);
            assertEquals(false,
                    test(
                            keyGen(keyPair.getPrivate(), nonMatching[0]),
                            enc(keyPair.getPublic(),     nonMatching[1])
                    )
            );
        }
    }

    protected int[][] createMatchingVectors(int n) {
        int[][] result = new int[2][n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            if (random.nextBoolean()) {
                // it's a star
                result[0][i] = -1;
                result[1][i] = random.nextInt(2);
            } else {
                result[0][i] = random.nextInt(2);
                result[1][i] = result[0][i];
            }
        }
        return result;
    }

    protected int[][] createNonMatchingVectors(int n) {
        int[][] result = new int[2][n];
        Random random = new Random();
        for (int i = 0; i < n; i++) {
            if (random.nextBoolean()) {
                // it's a star
                result[0][i] = -1;
                result[1][i] = random.nextInt(2);
            } else {
                result[0][i] = random.nextInt(2);
                result[1][i] = 1 - result[0][i];
            }
        }
        return result;
    }

    protected Element[] createRandom(Pairing pairing, int n) {
        Element[] result = new Element[n];
        for (int i = 0; i < n; i++)
            result[i] = pairing.getZr().newRandomElement();
        return result;
    }



    protected HVEIP08Parameters genBinaryParam(int n) {
        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(
                n, PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties")
        );

        return generator.generateParameters();
    }

    protected HVEIP08Parameters genParam(int... attributeLengths) {
        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(
                PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"),
                attributeLengths);

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(HVEIP08Parameters hveParameters) {
        HVEIP08KeyPairGenerator generator = new HVEIP08KeyPairGenerator();
        generator.init(new HVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return generator.generateKeyPair();
    }

    protected byte[] enc(CipherParameters publicKey, int... attributes) {
        byte[] attrs = HVEAttributes.attributesToByteArray(
                ((HVEIP08PublicKeyParameters)publicKey).getParameters(),
                attributes
        );

        engine.init(true, publicKey);

        return engine.processBlock(attrs, 0, attrs.length);
    }

    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HVEIP08AttributesOnlySearchKeyGenerator generator = new HVEIP08AttributesOnlySearchKeyGenerator();
        generator.init(new HVEIP08SearchKeyGenerationParameters(
                (HVEIP08PrivateKeyParameters) privateKey, pattern)
        );

        return generator.generateKey();
    }

    protected boolean test(CipherParameters searchKey, byte[] ct) {
        engine.init(false, searchKey);

        return engine.processBlock(ct, 0, ct.length)[0] == 0;
    }
}

