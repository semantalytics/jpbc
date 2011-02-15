package it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong;

import it.unisa.dia.gas.crypto.engines.MultiBlockAsymmetricBlockCipher;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.engines.UTMAStrongEngine;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.engines.UTMAStrongRandomizer;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.generators.UTMAStrongKeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.generators.UTMAStrongParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.params.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle.crypto.paddings.PKCS7Padding;
import org.bouncycastle.crypto.params.ElGamalParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongEngineTest extends TestCase {

    public void testEngine() {
        UTMAStrongParameters parameters = createParameters(1024);

        AsymmetricCipherKeyPair keyPair = setup(parameters);

        String message = "Hello World!!!";

        assertEquals(message, decrypt(keyPair.getPrivate(), encrypt(keyPair.getPublic(), message)));

        assertEquals(
                message,
                decrypt(keyPair.getPrivate(),
                        randomize(
                                parameters.getPublicParameters(),
                                parameters.getRPublicParameters(),
                                encrypt(keyPair.getPublic(), message)
                        )
                )
        );
    }


    protected UTMAStrongParameters createParameters(int elgamalLength) {
        ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
        elGamalParametersGenerator.init(elgamalLength, 12, new SecureRandom());
        ElGamalParameters elGamalParameters = elGamalParametersGenerator.generateParameters();

        UTMAStrongParametersGenerator generator = new UTMAStrongParametersGenerator();
        generator.init(getCurveParamas(), elGamalParameters);
        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(UTMAStrongParameters parameters) {
        UTMAStrongKeyPairGenerator setup = new UTMAStrongKeyPairGenerator();
        setup.init(new UTMAStrongKeyGenerationParameters(new SecureRandom(), parameters));

        return setup.generateKeyPair();
    }

    protected byte[] encrypt(CipherParameters publicKey, String message) {
        byte[] bytes = message.getBytes();
        try {
            AsymmetricBlockCipher engine = new MultiBlockAsymmetricBlockCipher(
                    new UTMAStrongEngine(),
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
                    new UTMAStrongEngine(),
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

    protected byte[] randomize(UTMAStrongPublicParameters publicParameters,
                               UTMAStrongRPublicParameters rPublicParameters,
                               byte[] ciphertext) {
        try {
            UTMAStrongRandomizer randomizer = new UTMAStrongRandomizer();
            randomizer.init(new UTMAStrongRandomizeParameters(publicParameters, rPublicParameters));

            return randomizer.processBlock(ciphertext, 0, ciphertext.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected CurveParams getCurveParamas() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));
        return curveParams;
    }

}