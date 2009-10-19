package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak;

import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak.engines.UTMAWeakEngine;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak.engines.UTMAWeakRandomizer;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak.generators.UTMAWeakKeyPairGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak.generators.UTMAWeakParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak.params.UTMAWeakKeyGenerationParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.weak.params.UTMAWeakParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAWeakEngineTest extends TestCase {

    public void testEngineEncryptDecrypt() {
        // Generate public info

        UTMAWeakParametersGenerator utmaWeakParametersGenerator = new UTMAWeakParametersGenerator();
        utmaWeakParametersGenerator.init(getCurveParamas());
        UTMAWeakParameters utmaParameters =  utmaWeakParametersGenerator.generateParameters();

        UTMAWeakKeyPairGenerator utmaWeakKeyPairGenerator = new UTMAWeakKeyPairGenerator();
        utmaWeakKeyPairGenerator.init(new UTMAWeakKeyGenerationParameters(new SecureRandom(), utmaParameters));
        AsymmetricCipherKeyPair keyPair = utmaWeakKeyPairGenerator.generateKeyPair();

        String message = "Hello World!!!";
        byte[] messageAsBytes = message.getBytes();

        UTMAWeakEngine weakEngine = new UTMAWeakEngine();

        // Encrypt
        weakEngine.init(true, keyPair.getPublic());
        byte[] cipherText = weakEngine.processBlock(messageAsBytes, 0, messageAsBytes.length);

        // Decrypt
        weakEngine.init(false, keyPair.getPrivate());
        byte[] plainText = weakEngine.processBlock(cipherText, 0, cipherText.length);

        assertEquals(message, new String(plainText));
    }

    public void testEngineEncryptRandomizeDecrypt() {
        // Generate public info

        UTMAWeakParametersGenerator utmaWeakParametersGenerator = new UTMAWeakParametersGenerator();
        utmaWeakParametersGenerator.init(getCurveParamas());
        UTMAWeakParameters utmaParameters =  utmaWeakParametersGenerator.generateParameters();

        UTMAWeakKeyPairGenerator utmaWeakKeyPairGenerator = new UTMAWeakKeyPairGenerator();
        utmaWeakKeyPairGenerator.init(new UTMAWeakKeyGenerationParameters(new SecureRandom(), utmaParameters));
        AsymmetricCipherKeyPair keyPair = utmaWeakKeyPairGenerator.generateKeyPair();

        String message = "Hello World!!!";
        byte[] messageAsBytes = message.getBytes();

        UTMAWeakEngine weakEngine = new UTMAWeakEngine();

        // Encrypt
        weakEngine.init(true, keyPair.getPublic());
        byte[] cipherText = weakEngine.processBlock(messageAsBytes, 0, messageAsBytes.length);

        // Randomize
        UTMAWeakRandomizer randomizer = new UTMAWeakRandomizer();
        randomizer.init(keyPair.getPublic());
        for (int i = 0; i < 10; i++) {
            cipherText = randomizer.processBlock(cipherText, 0, cipherText.length);
        }

        // Decrypt
        weakEngine.init(false, keyPair.getPrivate());
        byte[] plainText = weakEngine.processBlock(cipherText, 0, cipherText.length);

        assertEquals(message, new String(plainText));
    }

    

    protected CurveParams getCurveParamas() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));
        return curveParams;
    }
}
