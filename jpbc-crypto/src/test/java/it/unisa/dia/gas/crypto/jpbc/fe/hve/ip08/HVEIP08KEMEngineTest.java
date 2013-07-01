package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HVEIP08KEMEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SecretKeyGenerationParameters;
import it.unisa.dia.gas.crypto.kem.KeyEncapsulationMechanism;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * @author Angelo De Caro
 */
public class HVEIP08KEMEngineTest extends HVEIP08AbstractTest {


    public HVEIP08KEMEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Test
    public void testHVEIP08KEMEngine() {
        int n = 100;
        AsymmetricCipherKeyPair keyPair = setup(genBinaryParam(n));

        int[][] vectors = createMatchingVectors(n);
        byte[][] ct = encaps(keyPair.getPublic(), vectors[1]);
        assertEquals(true, Arrays.equals(ct[0], decaps(keyGen(keyPair.getPrivate(), vectors[0]), ct[1])));

        vectors = createAllStarMatchingVectors(n);
        ct = encaps(keyPair.getPublic(), vectors[1]);
        assertEquals(true, Arrays.equals(ct[0], decaps(keyGen(keyPair.getPrivate(), vectors[0]), ct[1])));

        vectors = createNonMatchingVectors(n);
        ct = encaps(keyPair.getPublic(), vectors[1]);
        assertEquals(false, Arrays.equals(ct[0], decaps(keyGen(keyPair.getPrivate(), vectors[0]), ct[1])));
    }


    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HVEIP08SecretKeyGenerator generator = new HVEIP08SecretKeyGenerator();
        generator.init(new HVEIP08SecretKeyGenerationParameters(
                (HVEIP08MasterSecretKeyParameters) privateKey, pattern)
        );

        return generator.generateKey();
    }

    protected byte[][] encaps(CipherParameters publicKey, int... attributes) {
        try {
            KeyEncapsulationMechanism kem = new HVEIP08KEMEngine();
            kem.init(true, new HVEIP08EncryptionParameters((HVEIP08PublicKeyParameters) publicKey, attributes));

            byte[] ciphertext = kem.processBlock(new byte[0], 0, 0);

            assertNotNull(ciphertext);
            assertNotSame(0, ciphertext.length);

            byte[] key = Arrays.copyOfRange(ciphertext, 0, kem.getKeyBlockSize());
            byte[] ct = Arrays.copyOfRange(ciphertext, kem.getKeyBlockSize(), ciphertext.length);

            return new byte[][]{key, ct};
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    protected byte[] decaps(CipherParameters secretKey, byte[] cipherText) {
        try {
            KeyEncapsulationMechanism kem = new HVEIP08KEMEngine();

            kem.init(false, secretKey);
            long start = System.currentTimeMillis();
            byte[] key = kem.processBlock(cipherText, 0, cipherText.length);
            long end = System.currentTimeMillis();

            System.out.println("(elapsed) = " + (end - start));

            assertNotNull(key);
            assertNotSame(0, key.length);

            return key;
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return null;
    }
}

