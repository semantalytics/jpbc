package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HVEIP08AttributesEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08SearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08Parameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SearchKeyGenerationParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro
 */
public class TestHVE extends TestCase {

    public void testHVE() {
        // Generate HVE Paramaters
        HVEIP08Parameters hveParameters = createHveParameters(new int[]{1, 7, 1, 4, 2, 1});
        // Generate an HVE KeyPair
        AsymmetricCipherKeyPair keyPair = generateKeyPair(hveParameters);

        // Encrypts attributes

        // Nell'altro caso i byte necessari a memorizzare tutti
        //gli attributi sono di più. Ce ne vogliono, sempre seguendo lo schema
        //precedente,
        // 1+7+1+4+2+1 = 16 e quindi un esempio può essere:
        byte[] attributes = new byte[]{
                0,                        // attributo 0
                1, 0, 1, 0, 0, 1, 1,      // attributo 1
                1,                        // attributo 2
                0, 1, 1, 1,               // attributo 3
                1, 0,                     // attributo 4
                1                         // attributo 5
        };

        byte[] attributesCT = encryptAttributes(keyPair.getPublic(), attributes);

        // Create a Search Key

        // Nell'altro caso avremo:
        byte[] pattern = new byte[]{
                0,                     // attributo 0
                1, 0, 1, 0, 0, 1, 1,   // attributo 1
                -1,                    // attributo 2
                0, 1, 1, 1,            // attributo 3
                -1, -1,                // attributo 4
                1                      // attributo 5
        };

        CipherParameters searchKey = generateSearchKey(keyPair.getPrivate(), pattern);

        assertEquals(true, matchAttributes(searchKey, attributesCT));
    }


    protected HVEIP08Parameters createHveParameters(int[] attributesSpec) {
        // Carica i parametri della curva
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));

        // Inizializza il generatore.
        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();

/*
        // Se hai tutti attributi di tipo binario...puoi usare il seguente init:
        generator.init(getCurveParamas(), 6);
*/

        // Se hai attributi con differenti numeri di bit puoi usare il seguente init:
        generator.init(curveParams, attributesSpec);

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair generateKeyPair(HVEIP08Parameters hveParameters) {
        HVEIP08KeyPairGenerator hveKeyPairGenerator = new HVEIP08KeyPairGenerator();
        hveKeyPairGenerator.init(new HVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return hveKeyPairGenerator.generateKeyPair();
    }

    protected byte[] encryptAttributes(CipherParameters publicKey, byte[] attributes) {
        HVEIP08AttributesEngine hveAttributesEngine = new HVEIP08AttributesEngine();
        hveAttributesEngine.init(true, publicKey);
        return hveAttributesEngine.processBlock(attributes, 0, attributes.length);
    }

    protected CipherParameters generateSearchKey(CipherParameters privateKey, byte[] pattern) {
        HVEIP08SearchKeyGenerator hveSearchKeyGenerator = new HVEIP08SearchKeyGenerator();
        hveSearchKeyGenerator.init(new HVEIP08SearchKeyGenerationParameters((HVEIP08PrivateKeyParameters) privateKey, pattern));
        return hveSearchKeyGenerator.generateKey();
    }

    protected boolean matchAttributes(CipherParameters searchKey, byte[] attributesCT) {
        HVEIP08AttributesEngine hveAttributesEngine = new HVEIP08AttributesEngine();
        hveAttributesEngine.init(false, searchKey);
        return hveAttributesEngine.processBlock(attributesCT, 0, attributesCT.length)[0] == 0;
    }
}

