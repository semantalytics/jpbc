package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10;

import it.unisa.dia.gas.crypto.engines.MultiBlockAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.engines.AHIBEEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators.AHIBESecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators.AHIBESetupGenerator;
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
public class TestAHIBE extends TestCase {

    protected Pairing pairing;


    public void testAHIBE() {
        AsymmetricCipherKeyPair keyPair = setup(32, 10);

        Pairing pairing = PairingFactory.getPairing(((AHIBEPublicKeyParameters) keyPair.getPublic()).getCurveParams());

        Element[] ids = new Element[3];
        ids[0] = pairing.getZr().newRandomElement().getImmutable();
        ids[1] = pairing.getZr().newRandomElement().getImmutable();
        ids[2] = pairing.getZr().newRandomElement().getImmutable();

        // KeyGen
        CipherParameters sk0 = keygen(keyPair, ids[0]);
        CipherParameters sk01 = keygen(keyPair, ids[0], ids[1]);
        CipherParameters sk012 = keygen(keyPair, ids[0], ids[1], ids[2]);

        // Enc-Dec
        String message = "H";
        byte[] ciphertext012 = encrypt(keyPair.getPublic(), message, ids[0], ids[1], ids[2]);
        byte[] plaintext012 = decrypt(sk012, ciphertext012);

        byte[] ciphertext01 = encrypt(keyPair.getPublic(), message, ids[0], ids[1]);
        byte[] plaintext01 = decrypt(sk01, ciphertext01);

        byte[] ciphertext0 = encrypt(keyPair.getPublic(), message, ids[0]);
        byte[] plaintext0 = decrypt(sk0, ciphertext0);

        assertEquals(message, new String(plaintext012).trim());
        assertEquals(message, new String(plaintext01).trim());
        assertEquals(message, new String(plaintext0).trim());

        // Delegate from sk01 to sk012
        CipherParameters sk01_012 = delegate(keyPair, sk01, ids[2]);
        plaintext012 = decrypt(sk01_012, ciphertext012);
        assertEquals(message, new String(plaintext012).trim());

        // Delegate from sk0 to sk01
        CipherParameters sk0_01 = delegate(keyPair, sk0, ids[1]);
        plaintext01 = decrypt(sk0_01, ciphertext01);
        assertEquals(message, new String(plaintext01).trim());

        // Delegate from sk0_01 to sk012
        CipherParameters sk0_012 = delegate(keyPair, sk0_01, ids[2]);
        plaintext012 = decrypt(sk0_012, ciphertext012);
        assertEquals(message, new String(plaintext012).trim());
    }


    protected AsymmetricCipherKeyPair setup(int bitLength, int length) {
        AHIBESetupGenerator generator = new AHIBESetupGenerator();
        generator.init(new AHIBESetupGenerationParameters(bitLength, length));

        return generator.generateKeyPair();
    }

    protected CipherParameters keygen(AsymmetricCipherKeyPair masterKey, Element... ids) {
        AHIBESecretKeyGenerator generator = new AHIBESecretKeyGenerator();
        generator.init(new AHIBESecretKeyGenerationParameters(
                (AHIBEMasterSecretKeyParameters) masterKey.getPrivate(),
                (AHIBEPublicKeyParameters) masterKey.getPublic(),
                ids
        ));

        return generator.generateKey();
    }

    protected CipherParameters delegate(AsymmetricCipherKeyPair masterKey, CipherParameters secretKey, Element id) {
        AHIBESecretKeyGenerator generator = new AHIBESecretKeyGenerator();
        generator.init(new AHIBEDelegateSecretKeyGenerationParameters(
                (AHIBEPublicKeyParameters) masterKey.getPublic(),
                (AHIBESecretKeyParameters) secretKey,
                id
        ));

        return generator.generateKey();
    }

    protected byte[] encrypt(CipherParameters publicKey, String message, Element... ids) {
        byte[] messageAsBytes = message.getBytes();
        byte[] cipherText = new byte[0];

        try {
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new AHIBEEngine(),
                    new ZeroBytePadding()
            );
            engine.init(true, new AHIBEEncryptionParameters((AHIBEPublicKeyParameters) publicKey, ids));
            cipherText = engine.processBlock(messageAsBytes, 0, messageAsBytes.length);

            assertNotNull(cipherText);
            assertNotSame(0, cipherText.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return cipherText;
    }

    protected byte[] decrypt(CipherParameters secretKey, byte[] cipherText) {
        byte[] plainText = new byte[0];
        try {
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new AHIBEEngine(),
                    new ZeroBytePadding()
            );
            // Decrypt
            engine.init(false, secretKey);
            plainText = engine.processBlock(cipherText, 0, cipherText.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return plainText;
    }
}

