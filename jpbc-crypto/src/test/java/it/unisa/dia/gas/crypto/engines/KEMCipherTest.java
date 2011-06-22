package it.unisa.dia.gas.crypto.engines;

import it.unisa.dia.gas.crypto.engines.kem.KEMCipher;
import it.unisa.dia.gas.crypto.engines.kem.KEMCipherDecryptionParameters;
import it.unisa.dia.gas.crypto.engines.kem.KEMCipherEncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.engines.AHIBEDIP10KEMEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators.AHIBEDIP10KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators.AHIBEDIP10SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.security.Security;
import java.util.Arrays;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class KEMCipherTest extends TestCase {

    public void testKEMCipherWithAESAHIBE() {
        Security.addProvider(new BouncyCastleProvider());

        AsymmetricCipherKeyPair keyPair = setup(64, 3);
        Element[] id1s = map(keyPair.getPublic(), "angelo", "de caro", "unisa");
        Element[] id2s = map(keyPair.getPublic(), "angelo", "de caro", "unina");

        try {
            // Encrypt
            KEMCipher kemCipher = new KEMCipher(Cipher.getInstance("AES/ECB/PKCS7Padding", "BC"), new AHIBEDIP10KEMEngine());
            byte[] encapsulation = kemCipher.init(
                    true,
                    new KEMCipherEncryptionParameters(128, new AHIBEDIP10EncryptionParameters((AHIBEDIP10PublicKeyParameters) keyPair.getPublic(), id1s))
            );

            Random random = new Random();
            byte[] message = new byte[4096];
            random.nextBytes(message);

            byte[] ct = kemCipher.doFinal(message);

            // Decrypt and Test for the same identity
            kemCipher.init(
                    false,
                    new KEMCipherDecryptionParameters(keyGen(keyPair, id1s), encapsulation, 128)
            );
            byte[] messagePrime = kemCipher.doFinal(ct);

            assertEquals(true, Arrays.equals(message, messagePrime));

            // Decrypt and Test for different identity
            try {
                kemCipher.init(
                        false,
                        new KEMCipherDecryptionParameters(keyGen(keyPair, id2s), encapsulation, 128)
                );
                kemCipher.doFinal(ct);
                fail("The decryption must fail in this case!");
            } catch (Exception e) {
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    protected AsymmetricCipherKeyPair setup(int bitLength, int length) {
        AHIBEDIP10KeyPairGenerator setup = new AHIBEDIP10KeyPairGenerator();
        setup.init(new AHIBEDIP10KeyPairGenerationParameters(bitLength, length));

        return setup.generateKeyPair();
    }

    protected Element[] map(CipherParameters publicKey, String... ids) {
        Pairing pairing = PairingFactory.getPairing(((AHIBEDIP10PublicKeyParameters) publicKey).getCurveParameters());

        Element[] elements = new Element[ids.length];
        for (int i = 0; i < elements.length; i++) {
            byte[] id = ids[i].getBytes();
            elements[i] = pairing.getZr().newElement().setFromHash(id, 0, id.length);
        }
        return elements;
    }

    protected CipherParameters keyGen(AsymmetricCipherKeyPair keyPair, Element... ids) {
        AHIBEDIP10SecretKeyGenerator generator = new AHIBEDIP10SecretKeyGenerator();
        generator.init(new AHIBEDIP10SecretKeyGenerationParameters(
                (AHIBEDIP10MasterSecretKeyParameters) keyPair.getPrivate(),
                (AHIBEDIP10PublicKeyParameters) keyPair.getPublic(),
                ids
        ));

        return generator.generateKey();
    }


    public static void main(String[] args) throws Exception {
        Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
        byte[] input = "www.java2s.comwww.java2s.comwww.java2s.comwww.java2s.comwww.java2s.comwww.java2s.comwww.java2s.com".getBytes();
        byte[] keyBytes = new byte[]{0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x06, 0x07, 0x08, 0x09,
                0x0a, 0x0b, 0x0c, 0x0d, 0x0e, 0x0f, 0x10, 0x11, 0x12, 0x13, 0x14, 0x15, 0x16, 0x17};

        SecretKeySpec key = new SecretKeySpec(keyBytes, "AES");

        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS7Padding", "BC");

        System.out.println(new String(input));

        // encryption pass
        cipher.init(Cipher.ENCRYPT_MODE, key);

//        byte[] cipherText = new byte[cipher.getOutputSize(input.length)];
//        int ctLength = cipher.update(input, 0, input.length, cipherText, 0);
//        ctLength += cipher.doFinal(cipherText, ctLength);
//        cipher.up
        byte[] cipherText = cipher.doFinal(input);
        int ctLength = cipherText.length;

        System.out.println(new String(cipherText));
        System.out.println(ctLength);

        // decryption pass
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] plainText = new byte[cipher.getOutputSize(ctLength)];
        int ptLength = cipher.update(cipherText, 0, ctLength, plainText, 0);
        ptLength += cipher.doFinal(plainText, ptLength);
        System.out.println(new String(plainText));
        System.out.println(ptLength);
    }
}
