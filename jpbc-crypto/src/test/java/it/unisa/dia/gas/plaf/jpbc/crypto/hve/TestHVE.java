package it.unisa.dia.gas.plaf.jpbc.crypto.hve;

import it.unisa.dia.gas.plaf.jpbc.crypto.hve.engines.HVEAttributesEngine;
import it.unisa.dia.gas.plaf.jpbc.crypto.hve.generators.HVEKeyPairGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.hve.generators.HVEParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.hve.generators.HVESearchKeyGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.hve.params.HVEKeyGenerationParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.hve.params.HVEParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.hve.params.HVEPrivateKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.hve.params.HVESearchKeyGenerationParameters;
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
        HVEParameters hveParameters = createHveParameters(new int[]{1, 7, 1, 4, 2, 1});
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


    protected HVEParameters createHveParameters(int[] attributesSpec) {
        // Carica i parametri della curva
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));

        // Inizializza il generatore.
        HVEParametersGenerator generator = new HVEParametersGenerator();

/*
        // Se hai tutti attributi di tipo binario...puoi usare il seguente init:
        generator.init(getCurveParamas(), 6);
*/

        // Se hai attributi con differenti numeri di bit puoi usare il seguente init:
        generator.init(curveParams, attributesSpec);

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair generateKeyPair(HVEParameters hveParameters) {
        HVEKeyPairGenerator hveKeyPairGenerator = new HVEKeyPairGenerator();
        hveKeyPairGenerator.init(new HVEKeyGenerationParameters(new SecureRandom(), hveParameters));

        return hveKeyPairGenerator.generateKeyPair();
    }

    protected byte[] encryptAttributes(CipherParameters publicKey, byte[] attributes) {
        HVEAttributesEngine hveAttributesEngine = new HVEAttributesEngine();
        hveAttributesEngine.init(true, publicKey);
        return hveAttributesEngine.processBlock(attributes, 0, attributes.length);
    }

    protected CipherParameters generateSearchKey(CipherParameters privateKey, byte[] pattern) {
        HVESearchKeyGenerator hveSearchKeyGenerator = new HVESearchKeyGenerator();
        hveSearchKeyGenerator.init(new HVESearchKeyGenerationParameters((HVEPrivateKeyParameters) privateKey, pattern));
        return hveSearchKeyGenerator.generateKey();
    }

    protected boolean matchAttributes(CipherParameters searchKey, byte[] attributesCT) {
        HVEAttributesEngine hveAttributesEngine = new HVEAttributesEngine();
        hveAttributesEngine.init(false, searchKey);
        return hveAttributesEngine.processBlock(attributesCT, 0, attributesCT.length)[0] == 0;
    }
}

