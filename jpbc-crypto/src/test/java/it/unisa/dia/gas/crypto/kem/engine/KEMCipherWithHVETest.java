package it.unisa.dia.gas.crypto.kem.engine;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.HVEIP08AbstractTest;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HVEIP08KEMEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.*;
import it.unisa.dia.gas.crypto.kem.engines.KEMCipher;
import it.unisa.dia.gas.crypto.kem.params.KEMCipherDecryptionParameters;
import it.unisa.dia.gas.crypto.kem.params.KEMCipherEncryptionParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import java.security.SecureRandom;
import java.security.Security;
import java.security.spec.AlgorithmParameterSpec;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class KEMCipherWithHVETest extends HVEIP08AbstractTest {


    public KEMCipherWithHVETest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }


    @Test
    public void testKEMCipherWithAESHVE1() {
        Security.addProvider(new BouncyCastleProvider());

        int n = 5;
        AsymmetricCipherKeyPair keyPair = setup(genBinaryParam(n));

        try {
            // Encrypt
            KEMCipher kemCipher = new KEMCipher(
                    Cipher.getInstance("AES/CBC/PKCS7Padding", "BC"),
                    new HVEIP08KEMEngine()
            );

            // build the initialization vector.  This example is all zeros, but it
            // could be any value or generated using a random number generator.
            AlgorithmParameterSpec iv = new IvParameterSpec(new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });

            int[][] vectors = createMatchingVectors(n);
            System.out.println(Arrays.toString(vectors[1]));
            byte[] encapsulation = kemCipher.init(
                    true,
                    new KEMCipherEncryptionParameters(
                            128,
                            new HVEIP08EncryptionParameters((HVEIP08PublicKeyParameters) keyPair.getPublic(), vectors[1])
                    ),
                    iv
            );

            byte[] message = "Hello World!!!".getBytes();

            byte[] ct = kemCipher.doFinal(message);

            // Decrypt and Test for matching vectors
            kemCipher.init(
                    false,
                    new KEMCipherDecryptionParameters(keyGen(keyPair.getPrivate(), vectors[0]), encapsulation, 128),
                    iv
            );
            byte[] messagePrime = kemCipher.doFinal(ct);

            assertEquals(true, Arrays.equals(message, messagePrime));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }

    @Test
    public void testKEMCipherWithAESHVE2() {
        Security.addProvider(new BouncyCastleProvider());

        int n = 5;
        AsymmetricCipherKeyPair keyPair = setup(genBinaryParam(n));

        try {
            // Encrypt
            KEMCipher kemCipher = new KEMCipher(
                    Cipher.getInstance("AES/CBC/PKCS7Padding", "BC"),
                    new HVEIP08KEMEngine()
            );

            // build the initialization vector.  This example is all zeros, but it
            // could be any value or generated using a random number generator.
            AlgorithmParameterSpec iv = new IvParameterSpec(new byte[]{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 });

            int[][] vectors = createNonMatchingVectors(n);
            System.out.println(Arrays.toString(vectors[0]));
            System.out.println(Arrays.toString(vectors[1]));
            byte[] encapsulation = kemCipher.init(
                    true,
                    new KEMCipherEncryptionParameters(
                            128,
                            new HVEIP08EncryptionParameters((HVEIP08PublicKeyParameters) keyPair.getPublic(), vectors[1])
                    ),
                    iv
            );

            byte[] message = "Hello World!!!".getBytes();

            byte[] ct = kemCipher.doFinal(message);

            try {
                kemCipher.init(
                        false,
                        new KEMCipherDecryptionParameters(keyGen(keyPair.getPrivate(), vectors[0]), encapsulation, 128),
                        iv
                );
                kemCipher.doFinal(ct);
                fail("The decryption must fail in this case!");
            } catch (Exception e) {
//                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
            fail();
        }
    }


    protected AsymmetricCipherKeyPair setup(HVEIP08Parameters hveParameters) {
        HVEIP08KeyPairGenerator generator = new HVEIP08KeyPairGenerator();
        generator.init(new HVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return generator.generateKeyPair();
    }

    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HVEIP08SecretKeyGenerator generator = new HVEIP08SecretKeyGenerator();
        generator.init(new HVEIP08SecretKeyGenerationParameters(
                (HVEIP08MasterSecretKeyParameters) privateKey, pattern)
        );

        return generator.generateKey();
    }


}
