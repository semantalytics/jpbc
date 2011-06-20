package it.unisa.dia.gas.crypto.engines;

import it.unisa.dia.gas.crypto.engines.kem.KEMCipher;
import it.unisa.dia.gas.crypto.engines.kem.KEMCipherDecryptionParameters;
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

    public void test() {
        Security.addProvider(new BouncyCastleProvider());
        AsymmetricCipherKeyPair keyPair = setup(32, 10);
        Pairing pairing = PairingFactory.getPairing(((AHIBEDIP10PublicKeyParameters) keyPair.getPublic()).getCurveParameters());
        Element[] ids = map(pairing, "angelo", "de caro", "unisa");

        try {
            // Encrypt
            KEMCipher kemCipher = new KEMCipher(Cipher.getInstance("AES/ECB/PKCS7Padding", "BC"), new AHIBEDIP10KEMEngine());
            byte[] encapsulation = kemCipher.init(true, new AHIBEDIP10EncryptionParameters((AHIBEDIP10PublicKeyParameters) keyPair.getPublic(), ids));

            // Message
            byte[] message = new byte[4096];
            Random random = new Random();
            random.nextBytes(message);

            // Encrypt
            byte[] ciphertext = kemCipher.doFinal(message);

            // Decrypt
            kemCipher.init(false, new KEMCipherDecryptionParameters(keyGen(keyPair, ids), encapsulation));
            byte[] messagePrime = kemCipher.doFinal(ciphertext);

            assertEquals(true, Arrays.equals(message, messagePrime));
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

    protected Element[] map(Pairing pairing, String... ids) {
        Element[] elements = new Element[ids.length];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = pairing.getZr().newRandomElement();
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
