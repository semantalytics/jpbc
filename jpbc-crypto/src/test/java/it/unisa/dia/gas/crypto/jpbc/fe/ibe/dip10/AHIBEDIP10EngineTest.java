package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10;

import it.unisa.dia.gas.crypto.engines.MultiBlockAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.engines.AHIBEDIP10Engine;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators.AHIBEDIP10KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators.AHIBEDIP10SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.paddings.ZeroBytePadding;

/**
 * @author Angelo De Caro
 */
public class AHIBEDIP10EngineTest extends TestCase {


    public void testAHIBE() {
        // Setup
        AsymmetricCipherKeyPair keyPair = setup(32, 10);

        // KeyGen
        Pairing pairing = PairingFactory.getPairing(((AHIBEDIP10PublicKeyParameters) keyPair.getPublic()).getCurveParameters());
        Element[] ids = map(pairing, "angelo", "de caro", "unisa");

        CipherParameters sk0 = keyGen(keyPair, ids[0]);
        CipherParameters sk01 = keyGen(keyPair, ids[0], ids[1]);
        CipherParameters sk012 = keyGen(keyPair, ids[0], ids[1], ids[2]);

        CipherParameters sk1 = keyGen(keyPair, ids[1]);
        CipherParameters sk10 = keyGen(keyPair, ids[1], ids[0]);
        CipherParameters sk021 = keyGen(keyPair, ids[0], ids[2], ids[1]);

        // Encryption/Decryption
        String message = "HW!";
        byte[] ciphertext0 = encrypt(keyPair.getPublic(), message, ids[0]);
        byte[] ciphertext01 = encrypt(keyPair.getPublic(), message, ids[0], ids[1]);
        byte[] ciphertext012 = encrypt(keyPair.getPublic(), message, ids[0], ids[1], ids[2]);

        // Decrypt
        assertEquals(message, decrypt(sk0, ciphertext0));
        assertEquals(message, decrypt(sk01, ciphertext01));
        assertEquals(message, decrypt(sk012, ciphertext012));

        assertNotSame(message, decrypt(sk1, ciphertext0));
        assertNotSame(message, decrypt(sk10, ciphertext01));
        assertNotSame(message, decrypt(sk021, ciphertext012));

        // Delegate/Decrypt
        assertEquals(message, decrypt(delegate(keyPair, sk0, ids[1]), ciphertext01));
        assertEquals(message, decrypt(delegate(keyPair, sk01, ids[2]), ciphertext012));
        assertEquals(message, decrypt(delegate(keyPair, delegate(keyPair, sk0, ids[1]), ids[2]), ciphertext012));

        assertNotSame(message, decrypt(delegate(keyPair, sk0, ids[0]), ciphertext01));
        assertNotSame(message, decrypt(delegate(keyPair, sk01, ids[1]), ciphertext012));
        assertNotSame(message, decrypt(delegate(keyPair, delegate(keyPair, sk0, ids[2]), ids[1]), ciphertext012));
    }


    protected AsymmetricCipherKeyPair setup(int bitLength, int length) {
        AHIBEDIP10KeyPairGenerator setup = new AHIBEDIP10KeyPairGenerator();
        setup.init(new AHIBEDIP10KeyPairGenerationParameters(bitLength, length));

        return setup.generateKeyPair();
    }

    protected Element[] map(Pairing pairing, String... ids) {
        Element[] elements = new Element[ids.length];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = pairing.getZr().newRandomElement();
        }
        return elements;
    }


    protected CipherParameters keyGen(AsymmetricCipherKeyPair masterKey, Element... ids) {
        AHIBEDIP10SecretKeyGenerator generator = new AHIBEDIP10SecretKeyGenerator();
        generator.init(new AHIBEDIP10SecretKeyGenerationParameters(
                (AHIBEDIP10MasterSecretKeyParameters) masterKey.getPrivate(),
                (AHIBEDIP10PublicKeyParameters) masterKey.getPublic(),
                ids
        ));

        return generator.generateKey();
    }

    protected CipherParameters delegate(AsymmetricCipherKeyPair masterKey, CipherParameters secretKey, Element id) {
        AHIBEDIP10SecretKeyGenerator generator = new AHIBEDIP10SecretKeyGenerator();
        generator.init(new AHIBEDIP10DelegateGenerationParameters(
                (AHIBEDIP10PublicKeyParameters) masterKey.getPublic(),
                (AHIBEDIP10SecretKeyParameters) secretKey,
                id
        ));

        return generator.generateKey();
    }

    protected byte[] encrypt(CipherParameters publicKey, String message, Element... ids) {
        byte[] bytes = message.getBytes();
        byte[] ciphertext = new byte[0];

        try {
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new AHIBEDIP10Engine(),
                    new ZeroBytePadding()
            );
            engine.init(true, new AHIBEDIP10EncryptionParameters((AHIBEDIP10PublicKeyParameters) publicKey, ids));
            ciphertext = engine.processBlock(bytes, 0, bytes.length);

            assertNotNull(ciphertext);
            assertNotSame(0, ciphertext.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return ciphertext;
    }

    protected String decrypt(CipherParameters secretKey, byte[] cipherText) {
        byte[] plainText = new byte[0];
        try {
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new AHIBEDIP10Engine(),
                    new ZeroBytePadding()
            );
            // Decrypt
            engine.init(false, secretKey);
            plainText = engine.processBlock(cipherText, 0, cipherText.length);

            assertNotNull(plainText);
            assertNotSame(0, plainText.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return new String(plainText).trim();
    }
}