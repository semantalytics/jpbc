package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10;

import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.engines.IPLOSTW10AttributesEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10SearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.*;
import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;
import java.util.Random;

/**
 * @author Angelo De Caro
 */
public class IPLOSTW10AttributesEngineTest extends TestCase {

    public void testIPOT10AttributesEngine() {
        PairingFactory.getInstance().setUsePBCWhenPossible(true);
        int n = 20;

        // Setup
        AsymmetricCipherKeyPair keyPair = setup(createParameters(n));

        // Encrypt
        Pairing pairing = PairingFactory.getPairing(
                ((IPLOSTW10PublicKeyParameters) keyPair.getPublic()).getParameters().getCurveParams()
        );

        Element[][] orthogonal = createOrthogonalVectors(pairing, n);
        byte[] ciphertext = encrypt(keyPair.getPublic(), orthogonal[0]);

        assertTrue(test(keyGen(keyPair.getPrivate(), orthogonal[1]), ciphertext));

        // Gen non-matching SearchKey
        assertFalse(test(keyGen(keyPair.getPrivate(), createRandom(pairing, n)), ciphertext));
    }


    protected IPLOSTW10Parameters createParameters(int n) {
        return new IPLOSTW10ParametersGenerator().init(
                PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"),
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

    protected Element[][] createOrthogonalVectors(Pairing pairing, int n) {
        int nHalf = n / 2;
        Element[][] result = new Element[2][n];
        Random random = new Random();
        for (int i = 0; i < n; i+=2) {
            Element randomElement = pairing.getZr().newRandomElement();
            if (random.nextBoolean()) {
                // it's a star
                result[0][i] = pairing.getZr().newZeroElement();
                result[0][i+1] = pairing.getZr().newZeroElement();
            } else {
                result[0][i] = pairing.getZr().newOneElement().negate();
                result[0][i+1] = randomElement;
            }

            if (random.nextBoolean()) {
                // it's a star
                result[1][i] = pairing.getZr().newZeroElement();
                result[1][i+1] = pairing.getZr().newZeroElement();
            } else {
                result[1][i] = randomElement;
                result[1][i+1] = pairing.getZr().newOneElement();
            }
        }
        return result;
    }

    protected Element[] createRandom(Pairing pairing, int n) {
        Element[] result = new Element[n];
        for (int i = 0; i < n; i++)
            result[i] = pairing.getZr().newRandomElement();
        return result;
    }

    protected byte[] encrypt(CipherParameters publicKey, Element[] x) {
        IPLOSTW10AttributesEngine engine = new IPLOSTW10AttributesEngine();
        engine.init(true, publicKey);

        byte[] buffer = ElementUtil.toBytes(x);
        return engine.processBlock(buffer, 0, buffer.length);
    }
    
    protected CipherParameters keyGen(CipherParameters privateKey, Element[] y) {
        IPLOSTW10SearchKeyGenerator keyGen = new IPLOSTW10SearchKeyGenerator();
        keyGen.init(new IPLOSTW10SearchKeyGenerationParameters(
                (IPLOSTW10PrivateKeyParameters) privateKey, y
        ));
        
        return keyGen.generateKey();
    }
    
    
    protected boolean test(CipherParameters searchKey, byte[] ciphertext) {
        IPLOSTW10AttributesEngine engine = new IPLOSTW10AttributesEngine();
        engine.init(false, searchKey);

        return engine.processBlock(ciphertext, 0, ciphertext.length)[0] == 0;
    }

}