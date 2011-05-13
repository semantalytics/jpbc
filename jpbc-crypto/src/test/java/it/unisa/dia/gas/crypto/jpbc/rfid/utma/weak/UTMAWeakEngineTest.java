package it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak;

import it.unisa.dia.gas.crypto.engines.MultiBlockAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak.engines.UTMAWeakEngine;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak.engines.UTMAWeakRandomizer;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak.generators.UTMAWeakKeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak.generators.UTMAWeakParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak.params.UTMAWeakKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak.params.UTMAWeakParameters;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak.params.UTMAWeakPublicParameters;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.paddings.PKCS7Padding;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAWeakEngineTest extends TestCase {

    public void testEngine() {
        UTMAWeakParameters parameters = createParameters();

        AsymmetricCipherKeyPair keyPair = setup(parameters);

        String message = "Hello World!!!";

        assertEquals(message, decrypt(keyPair.getPrivate(), encrypt(keyPair.getPublic(), message)));

        assertEquals(
                message,
                decrypt(keyPair.getPrivate(),
                        randomize(
                                parameters.getPublicParameters(),
                                encrypt(keyPair.getPublic(), message)
                        )
                )
        );
    }


    protected UTMAWeakParameters createParameters() {
        UTMAWeakParametersGenerator generator = new UTMAWeakParametersGenerator();
        generator.init(getCurveParameters());
        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(UTMAWeakParameters parameters) {
        UTMAWeakKeyPairGenerator setup = new UTMAWeakKeyPairGenerator();
        setup.init(new UTMAWeakKeyGenerationParameters(new SecureRandom(), parameters));

        return setup.generateKeyPair();
    }

    protected byte[] encrypt(CipherParameters publicKey, String message) {
        byte[] bytes = message.getBytes();
        try {
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new UTMAWeakEngine(),
                    new PKCS7Padding()
            );

            engine.init(true, publicKey);
            return engine.processBlock(bytes, 0, bytes.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected String decrypt(CipherParameters privateKey, byte[] ciphertext) {
        try {
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new UTMAWeakEngine(),
                    new PKCS7Padding()
            );

            engine.init(false, privateKey);
            return new String(engine.processBlock(ciphertext, 0, ciphertext.length)).trim();
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected byte[] randomize(UTMAWeakPublicParameters publicParameters,
                               byte[] ciphertext) {
        UTMAWeakRandomizer randomizer = new UTMAWeakRandomizer();
        randomizer.init(publicParameters);

        return randomizer.processBlock(ciphertext, 0, ciphertext.length);
    }

    protected CurveParameters getCurveParameters() {
        return PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties");
    }
}
