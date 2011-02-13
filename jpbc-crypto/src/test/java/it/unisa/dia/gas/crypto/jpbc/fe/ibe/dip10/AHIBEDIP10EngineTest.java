package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10;

import it.unisa.dia.gas.crypto.engines.MultiBlockAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.engines.AHIBEDIP10Engine;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators.AHIBEDIP10SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators.AHIBEDIP10SetupGenerator;
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
        Pairing pairing = PairingFactory.getPairing(((AHIBEDIP10PublicKeyParameters) keyPair.getPublic()).getCurveParams());
        Element[] ids = new Element[3];
        ids[0] = pairing.getZr().newRandomElement().getImmutable();
        ids[1] = pairing.getZr().newRandomElement().getImmutable();
        ids[2] = pairing.getZr().newRandomElement().getImmutable();

        CipherParameters sk0 = keyGen(keyPair, ids[0]);
        CipherParameters sk01 = keyGen(keyPair, ids[0], ids[1]);
        CipherParameters sk012 = keyGen(keyPair, ids[0], ids[1], ids[2]);

        // Encryption/Decryption
        String message = "H";
        byte[] ciphertext0 = encrypt(keyPair.getPublic(), message, ids[0]);
        assertEquals(message, decrypt(sk0, ciphertext0));

        byte[] ciphertext01 = encrypt(keyPair.getPublic(), message, ids[0], ids[1]);
        assertEquals(message, decrypt(sk01, ciphertext01));

        byte[] ciphertext012 = encrypt(keyPair.getPublic(), message, ids[0], ids[1], ids[2]);
        assertEquals(message, decrypt(sk012, ciphertext012));

        // Delegation/Decryption
        assertEquals(message, decrypt(delegate(keyPair, sk0, ids[1]), ciphertext01));
        assertEquals(message, decrypt(delegate(keyPair, sk01, ids[2]), ciphertext012));
        assertEquals(message, decrypt(delegate(keyPair, delegate(keyPair, sk0, ids[1]), ids[2]), ciphertext012));
    }


    protected AsymmetricCipherKeyPair setup(int bitLength, int length) {
        AHIBEDIP10SetupGenerator setup = new AHIBEDIP10SetupGenerator();
        setup.init(new AHIBEDIP10SetupGenerationParameters(bitLength, length));

        return setup.generateKeyPair();
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
        generator.init(new AHIBEDIP10DelegateSecretKeyGenerationParameters(
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