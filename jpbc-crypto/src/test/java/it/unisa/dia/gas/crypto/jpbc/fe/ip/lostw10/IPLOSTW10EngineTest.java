package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10;

import it.unisa.dia.gas.crypto.engines.MultiBlockAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.engines.IPLOSTW10Engine;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10SearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
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
        // Setup
        AsymmetricCipherKeyPair keyPair = setup(createParameters(2));

        // Encrypt
        Pairing pairing = PairingFactory.getPairing(
                ((IPLOSTW10PublicKeyParameters) keyPair.getPublic()).getParameters().getCurveParams()
        );
        String message = "Hello World";   // Message

        Element[] x = new Element[2];     // Attributes
        x[0] = pairing.getZr().newOneElement();
        x[1] = pairing.getZr().newZeroElement();

        byte[] ciphertext = encrypt(keyPair.getPublic(), message, x);

        // Gen matching SearchKey
        Element[] y = new Element[2];
        y[0] = pairing.getZr().newZeroElement();
        y[1] = pairing.getZr().newOneElement();

        CipherParameters searchKey = keyGen(keyPair.getPrivate(), y);

        // Search
        assertEquals(message, new String(decrypt(searchKey, ciphertext)).trim());

        // Gen non-matching SearchKey
        y[0] = pairing.getZr().newElement(5);
        y[1] = pairing.getZr().newOneElement();

        searchKey = keyGen(keyPair.getPrivate(), y);

        // Search
        assertNotSame(message, new String(decrypt(searchKey, ciphertext)).trim());
    }


    protected IPLOSTW10Parameters createParameters(int n) {
        return new IPLOSTW10ParametersGenerator().init(
                new CurveParams().load(
                        this.getClass().getClassLoader().getResourceAsStream(
                                "it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties")
                ),
                n
        ).generateParameters();
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
            engine.init(true, publicKey);

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
        IPLOSTW10SearchKeyGenerator keyGen = new IPLOSTW10SearchKeyGenerator();
        keyGen.init(new IPOT10SearchKeyGenerationParameters(
                (IPLOSTW10PrivateKeyParameters) privateKey,
                y
        ));

        // Generate the key
        return keyGen.generateKey();
    }
    
    
    protected byte[] decrypt(CipherParameters searchKey, byte[] ciphertext) {
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

        return plainText;
    }
    
    
}

