package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HVEIP08PredicateOnlyEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08PredicateOnlySecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SecretKeyGenerationParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Angelo De Caro
 */
public class HVEIP08PredicateOnlyEngineTest extends HVEIP08AbstractTest {

    public HVEIP08PredicateOnlyEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testHVEIP08PredicateOnlyEngine() {
        int n = 5;
        AsymmetricCipherKeyPair keyPair = setup(genBinaryParam(n));

        int[][] vectors = createMatchingVectors(n);
        assertEquals(true, test(keyGen(keyPair.getPrivate(), vectors[0]), enc(keyPair.getPublic(), vectors[1])));

        vectors = createNonMatchingVectors(n);
        assertEquals(false, test(keyGen(keyPair.getPrivate(), vectors[0]), enc(keyPair.getPublic(), vectors[1])));
    }


    protected byte[] enc(CipherParameters publicKey, int... attributes) {
        try {
            HVEIP08PredicateOnlyEngine engine = new HVEIP08PredicateOnlyEngine();
            engine.init(true, new HVEIP08EncryptionParameters((HVEIP08PublicKeyParameters) publicKey, attributes));

            return engine.processBlock(new byte[0], 0, 0);
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
            HVEIP08PredicateOnlyEngine engine = new HVEIP08PredicateOnlyEngine();
            engine.init(false, searchKey);

            return engine.processBlock(ct, 0, ct.length)[0] == 1; // Meaning that the predicate is satisfied
        } catch (InvalidCipherTextException e) {
            throw new RuntimeException(e);
        }
    }
}

