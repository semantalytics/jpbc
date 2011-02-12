package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10;

import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.engines.IPLOSTW10AttributesEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10SearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.*;
import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro
 */
public class IPLOSTW10AttributesEngineTest extends TestCase {

    public void testIPOT10AttributesEngine() {
        // Setup
        AsymmetricCipherKeyPair keyPair = setup(createParameters(2));
        Pairing pairing = PairingFactory.getPairing(((IPLOSTW10PublicKeyParameters) keyPair.getPublic()).getParameters().getCurveParams());

        // Encrypt
        Element[] x = new Element[2];
        x[0] = pairing.getZr().newOneElement();
        x[1] = pairing.getZr().newZeroElement();

        byte[] ciphertext = encrypt(keyPair.getPublic(), x);

        // Gen matching SearchKey
        Element[] y = new Element[2];
        y[0] = pairing.getZr().newZeroElement();
        y[1] = pairing.getZr().newOneElement();

        CipherParameters searchKey = keyGen(keyPair.getPrivate(), y);

        // Search
        assertTrue(test(searchKey, ciphertext));

        // Gen non-matching SearchKey
        y[0] = pairing.getZr().newElement().set(5);
        y[1] = pairing.getZr().newOneElement();

        searchKey = keyGen(keyPair.getPrivate(), y);

        // Search
        assertFalse(test(searchKey, ciphertext));
    }


    protected IPLOSTW10Parameters createParameters(int n) {
        return new IPLOSTW10ParametersGenerator().init(
                new CurveParams().load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties")),
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

    protected byte[] encrypt(CipherParameters publicKey, Element[] x) {
        IPLOSTW10AttributesEngine engine = new IPLOSTW10AttributesEngine();
        engine.init(true, publicKey);

        byte[] buffer = ElementUtil.toBytes(x);
        return engine.processBlock(buffer, 0, buffer.length);
    }
    
    protected CipherParameters keyGen(CipherParameters privateKey, Element[] y) {
        IPLOSTW10SearchKeyGenerator keyGen = new IPLOSTW10SearchKeyGenerator();
        keyGen.init(new IPOT10SearchKeyGenerationParameters(
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

