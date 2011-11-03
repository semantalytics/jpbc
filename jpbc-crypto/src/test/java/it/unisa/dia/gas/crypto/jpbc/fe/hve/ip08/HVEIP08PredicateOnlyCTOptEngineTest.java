package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HVEIP08PredicateOnlyCTOptEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08PredicateOnlySecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro
 */
public class HVEIP08PredicateOnlyCTOptEngineTest extends AbstractJPBCCryptoTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"},
        };

        return Arrays.asList(data);
    }

    public HVEIP08PredicateOnlyCTOptEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testHVEIP08PredicateOnlyCTOptEngine() {
        int n = 5;
        AsymmetricCipherKeyPair keyPair = setup(genBinaryParam(n));

        int[][] vectors = createMatchingVectors(n);
        assertEquals(true, 
                test(
                        keyGen(keyPair.getPrivate(), vectors[0]),
                        preprocess(keyPair.getPublic(), enc(keyPair.getPublic(), vectors[1]))
                )
        );

        vectors = createNonMatchingVectors(n);
        assertEquals(false,
                test(
                        keyGen(keyPair.getPrivate(), vectors[0]),
                        preprocess(keyPair.getPublic(), enc(keyPair.getPublic(), vectors[1]))
                )
        );
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
            result[0][i] = random.nextInt(2);
            result[1][i] = 1 - result[0][i];
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
        generator.init(n, curveParameters);

        return generator.generateParameters();
    }

    protected HVEIP08Parameters genParam(int... attributeLengths) {
        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(curveParameters, attributeLengths);

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(HVEIP08Parameters hveParameters) {
        HVEIP08KeyPairGenerator generator = new HVEIP08KeyPairGenerator();
        generator.init(new HVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return generator.generateKeyPair();
    }

    protected byte[] enc(CipherParameters publicKey, int... attributes) {
        try {
            HVEIP08PredicateOnlyCTOptEngine engine = new HVEIP08PredicateOnlyCTOptEngine();
            engine.init(true, new HVEIP08EncryptionParameters((HVEIP08PublicKeyParameters) publicKey, attributes));

            return engine.processBlock(new byte[0], 0, 0);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }

    protected byte[] preprocess(CipherParameters publicKey, byte[] enc) {
        try {
            HVEIP08PredicateOnlyCTOptEngine engine = new HVEIP08PredicateOnlyCTOptEngine();
            engine.init(true, new HVEIP08CiphertextPreprocessingParameters((HVEIP08PublicKeyParameters) publicKey));

            return engine.processBlock(enc, 0, enc.length);
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }

    
    
    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HVEIP08PredicateOnlySecretKeyGenerator generator = new HVEIP08PredicateOnlySecretKeyGenerator();
        generator.init(new HVEIP08SecretKeyGenerationParameters(
                (HVEIP08MasterSecretKeyParameters) privateKey, pattern)
        );

        return generator.generateKey();
    }

    protected boolean test(CipherParameters searchKey, byte[] ct) {
        try {
            HVEIP08PredicateOnlyCTOptEngine engine = new HVEIP08PredicateOnlyCTOptEngine();
            engine.init(false, searchKey);

            return engine.processBlock(ct, 0, ct.length)[0] == 1; // Meaning that the predicate is satisfied
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }
}

