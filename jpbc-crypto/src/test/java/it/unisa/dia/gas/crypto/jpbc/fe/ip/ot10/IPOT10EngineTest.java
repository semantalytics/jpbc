package it.unisa.dia.gas.crypto.jpbc.fe.ip.ot10;

import it.unisa.dia.gas.crypto.engines.MultiBlockAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.ot10.engines.IPOT10Engine;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.ot10.generators.IPOT10KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.ot10.generators.IPOT10ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.ot10.generators.IPOT10SearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.ot10.params.*;
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
public class IPOT10EngineTest extends TestCase {

    public void testIPOT10AttributesEngine() {
        // Setup
        AsymmetricCipherKeyPair keyPair = setup(createParameters(2));
        Pairing pairing = PairingFactory.getPairing(((IPOT10PublicKeyParameters) keyPair.getPublic()).getParameters().getCurveParams());

        // Encrypt
        String message = "Hello World";

        Element[] x = new Element[2];
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
        y[0] = pairing.getZr().newElement().set(5);
        y[1] = pairing.getZr().newOneElement();

        searchKey = keyGen(keyPair.getPrivate(), y);

        // Search
        assertNotSame(message, new String(decrypt(searchKey, ciphertext)).trim());
    }


    protected IPOT10Parameters createParameters(int n) {
        return new IPOT10ParametersGenerator().init(
                new CurveParams().load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties")),
                n
        ).generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(IPOT10Parameters parameters) {
        IPOT10KeyPairGenerator setup = new IPOT10KeyPairGenerator();
        setup.init(new IPOT10KeyGenerationParameters(
                new SecureRandom(),
                parameters
        ));

        return setup.generateKeyPair();
    }

    protected byte[] encrypt(CipherParameters publicKey, String message, Element[] x) {
        byte[] messageAsBytes = message.getBytes();
        byte[] cipherText = new byte[0];

        try {
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new IPOT10Engine(),
                    new ZeroBytePadding()
            );
            engine.init(true, publicKey);
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
        IPOT10SearchKeyGenerator keyGen = new IPOT10SearchKeyGenerator();
        keyGen.init(new IPOT10SearchKeyGenerationParameters(
                (IPOT10PrivateKeyParameters) privateKey, y
        ));
        
        return keyGen.generateKey();
    }
    
    
    protected byte[] decrypt(CipherParameters searchKey, byte[] ciphertext) {
        byte[] plainText = new byte[0];
        try {
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new IPOT10Engine(),
                    new ZeroBytePadding()
            );
            // Decrypt
            engine.init(false, searchKey);
            plainText = engine.processBlock(ciphertext, 0, ciphertext.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return plainText;
    }
    
    
}

