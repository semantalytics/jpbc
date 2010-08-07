package it.unisa.dia.gas.plaf.jpbc.crypto.ahibe;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.crypto.engines.MultiBlockAsymmetricBlockCipher;
import it.unisa.dia.gas.plaf.jpbc.crypto.ahibe.engines.AHIBEEngine;
import it.unisa.dia.gas.plaf.jpbc.crypto.ahibe.generators.AHIBESecretKeyGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.ahibe.generators.AHIBESetupGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.ahibe.params.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.paddings.PKCS7Padding;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro
 */
public class TestAHIBE extends TestCase {

    public void testHVE() {

        // ------------- Setup
        System.out.println("Setup");

        AHIBESetupGenerationParameters setupGenerationParameters = new AHIBESetupGenerationParameters(
                new SecureRandom(), 12, 512, 10
        );
        AHIBESetupGenerator ahibeSetupGenerator = new AHIBESetupGenerator();
        ahibeSetupGenerator.init(setupGenerationParameters);
        AsymmetricCipherKeyPair keyPair = ahibeSetupGenerator.generateKeyPair();

        // Asserts
        assertNotNull(keyPair);

        System.out.println(((AHIBEPublicKeyParameters) keyPair.getPublic()).getCurveParams());
        
        System.out.println("End");

        // ------------- KeyGen

        System.out.println("KeyGen");

        Pairing pairing = PairingFactory.getPairing(((AHIBEPublicKeyParameters) keyPair.getPublic()).getCurveParams());

        Element[] ids = new Element[2];
        ids[0] = pairing.getZr().newRandomElement();
        ids[1] = pairing.getZr().newRandomElement();

        AHIBESecretKeyGenerationParameters AHIBESecretKeyGenerationParameters =
                new AHIBESecretKeyGenerationParameters(
                        new SecureRandom(), 12,
                        (AHIBEMasterSecretKeyParameters) keyPair.getPrivate(),
                        (AHIBEPublicKeyParameters) keyPair.getPublic(),
                        ids
                );
        AHIBESecretKeyGenerator ahibeSecretKeyGenerator = new AHIBESecretKeyGenerator();
        ahibeSecretKeyGenerator.init(AHIBESecretKeyGenerationParameters);
        CipherParameters secretKey = ahibeSecretKeyGenerator.generateKey();

        // Asserts
        assertNotNull(secretKey);

        System.out.println("End");

        // ------------- Enc

        System.out.println("Enc");

        String message = "Hello World!!!";
        byte[] messageAsBytes = message.getBytes();

        AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                new AHIBEEngine(),
                new PKCS7Padding()
        );

        byte[] cipherText = new byte[0];
        try {
            engine.init(true, new AHIBESEncryptionParameters((AHIBEPublicKeyParameters) keyPair.getPublic(), ids));
            cipherText = engine.processBlock(messageAsBytes, 0, messageAsBytes.length);

            assertNotNull(cipherText);
            assertNotSame(0, cipherText.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        System.out.println("End");

        // ------------- Dec

        System.out.println("Dec");

        try {
            // Decrypt
            engine.init(false, secretKey);
            byte[] plainText = engine.processBlock(cipherText, 0, cipherText.length);

            assertEquals(message, new String(plainText));
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        System.out.println("End");

    }

}

