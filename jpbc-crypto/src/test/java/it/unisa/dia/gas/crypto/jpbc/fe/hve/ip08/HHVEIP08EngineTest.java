package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.engines.MultiBlockAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HHVEIP08Engine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HHVEIP08SearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro
 */
public class HHVEIP08EngineTest extends TestCase {

    public void testHHVE() {
        AsymmetricCipherKeyPair keyPair = setup(genParam(1, 3, 1, 3, 2, 1));

        String message = "Hello World!!!";

        assertEquals(message,
                decrypt(
                        keyGen(keyPair.getPrivate(), 0, 7, -1, 3, -1, 1),
                        enc(keyPair.getPublic(), message, 0, 7, 0, 3, 2, 1)
                )
        );

        assertNotSame(message,
                decrypt(
                        keyGen(keyPair.getPrivate(), 0, 5, -1, 3, -1, 1),
                        enc(keyPair.getPublic(), message, 0, 7, 0, 3, 2, 1)
                )
        );

        assertEquals(message,
                decrypt(
                        keyGen(keyPair.getPrivate(), -1, -1, -1, -1, -1, -1),
                        enc(keyPair.getPublic(), message, 0, 7, 0, 3, 2, 1)
                )
        );

        assertEquals(message,
                decrypt(
                        delegate(
                                keyPair.getPublic(),
                                keyGen(keyPair.getPrivate(), -1, -1, -1, -1, -1, -1),
                                0, 7, 0, 3, -1, 1
                        ),
                        enc(keyPair.getPublic(), message, 0, 7, 0, 3, 2, 1)
                )
        );

        assertEquals(message,
                decrypt(
                        delegate(
                                keyPair.getPublic(),
                                keyGen(keyPair.getPrivate(), 0, 7, -1, 3, -1, 1),
                                0, 7, 0, 3, -1, 1
                        ),
                        enc(keyPair.getPublic(), message, 0, 7, 0, 3, 2, 1)
                )
        );
    }

    private CipherParameters delegate(CipherParameters publicKey, CipherParameters searchKey, int... attributesPattern) {
        HHVEIP08SearchKeyGenerator generator = new HHVEIP08SearchKeyGenerator();
        generator.init(new HHVEIP08DelegateSecretKeyGenerationParameters(
                (HVEIP08PublicKeyParameters) publicKey,
                (HHVEIP08SearchKeyParameters) searchKey,
                attributesPattern)
        );

        return generator.generateKey();
    }


    protected HVEIP08Parameters genParam(int... attributeLengths) {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));

        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(curveParams, attributeLengths);

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(HVEIP08Parameters hveParameters) {
        HVEIP08KeyPairGenerator generator = new HVEIP08KeyPairGenerator();
        generator.init(new HVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return generator.generateKeyPair();
    }

    protected byte[] enc(CipherParameters publicKey, String message, int... attributes) {
        byte[] bytes = message.getBytes();

        byte[] ciphertext = new byte[0];
        try {
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new HHVEIP08Engine(),
                    new ZeroBytePadding()
            );
            engine.init(true, new HVEIP08EncryptionParameters((HVEIP08PublicKeyParameters) publicKey, attributes));
            ciphertext = engine.processBlock(bytes, 0, bytes.length);

            assertNotNull(ciphertext);
            assertNotSame(0, ciphertext.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return ciphertext;
    }

    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HHVEIP08SearchKeyGenerator generator = new HHVEIP08SearchKeyGenerator();
        generator.init(new HVEIP08SearchKeyGenerationParameters((HVEIP08PrivateKeyParameters) privateKey, pattern));

        return generator.generateKey();
    }

    protected String decrypt(CipherParameters searchKey, byte[] ct) {
        byte[] plainText = new byte[0];
        try {
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new HHVEIP08Engine(),
                    new ZeroBytePadding()
            );
            // Decrypt
            engine.init(false, searchKey);
            plainText = engine.processBlock(ct, 0, ct.length);

            assertNotNull(plainText);
            assertNotSame(0, plainText.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return new String(plainText).trim();
    }

}

