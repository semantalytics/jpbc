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

}
