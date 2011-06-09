package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10;

import it.unisa.dia.gas.crypto.engines.MultiBlockAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.engines.IPLOSTW10Engine;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
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
public class IPLOSTW10EngineTest extends TestCase {

    public void testIPOT10AttributesEngine() {
        int n = 2;

        // Setup
        AsymmetricCipherKeyPair keyPair = setup(createParameters(n));

        // Encrypt
        Pairing pairing = PairingFactory.getPairing(
                ((IPLOSTW10PublicKeyParameters) keyPair.getPublic()).getParameters().getCurveParameters()
        );
        String message = "Hello World";   // Message

        Element[] x = getCanonicalVector(pairing, n, 0);

        byte[] ciphertext = encrypt(keyPair.getPublic(), message, x);

        // Gen matching SearchKey
        Element[] y = getCanonicalVector(pairing, n, 1);

        assertEquals(message, decrypt(keyGen(keyPair.getPrivate(), y), ciphertext));

        // Gen non-matching SearchKey
        y[0] = pairing.getZr().newElement(5);

        assertNotSame(message, decrypt(keyGen(keyPair.getPrivate(), y), ciphertext));
    }


    protected IPLOSTW10Parameters createParameters(int n) {
        return new IPLOSTW10ParametersGenerator().init(
                PairingFactory.getInstance().loadCurveParameters(
                                "it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"
                ),
                n
        ).generateParameters();
    }

    protected Element[] getCanonicalVector(Pairing pairing, int length, int index) {
        Element[] elements = new Element[length];
        for (int i = 0; i < elements.length; i++) {
            elements[i] = (i == index) ? pairing.getZr().newOneElement() : pairing.getZr().newZeroElement();
        }
        return elements;
    }

    protected AsymmetricCipherKeyPair setup(IPLOSTW10Parameters parameters) {
        IPLOSTW10KeyPairGenerator setup = new IPLOSTW10KeyPairGenerator();
        setup.init(new IPLOSTW10KeyGenerationParameters(
                new SecureRandom(),
                parameters
        ));

        return setup.generateKeyPair();
    }

    protected byte[] encrypt(CipherParameters publicKey, String message, Element[] x) {
        byte[] messageAsBytes = message.getBytes();
        byte[] cipherText = new byte[0];

        try {
            // Init the engine
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new IPLOSTW10Engine(),
                    new ZeroBytePadding()
            );
            engine.init(true, new IPLOSTW10EncryptionParameters((IPLOSTW10PublicKeyParameters) publicKey, x));

            // Encrypt
            cipherText = engine.processBlock(messageAsBytes, 0, messageAsBytes.length);

            assertNotNull(cipherText);
            assertNotSame(0, cipherText.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return cipherText;
    }
    
    protected CipherParameters keyGen(CipherParameters privateKey, Element[] y) {
        // Init the Generator
        IPLOSTW10SecretKeyGenerator keyGen = new IPLOSTW10SecretKeyGenerator();
        keyGen.init(new IPLOSTW10SecretKeyGenerationParameters(
                (IPLOSTW10MasterSecretKeyParameters) privateKey,
                y
        ));

        // Generate the key
        return keyGen.generateKey();
    }
    
    
    protected String decrypt(CipherParameters searchKey, byte[] ciphertext) {
        byte[] plainText = new byte[0];
        try {
            // Init the engine
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new IPLOSTW10Engine(),
                    new ZeroBytePadding()
            );
            engine.init(false, searchKey);

            // Decrypt
            plainText = engine.processBlock(ciphertext, 0, ciphertext.length);

            assertNotNull(plainText);
            assertNotSame(0, plainText.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return new String(plainText).trim();
    }
    
    
}

