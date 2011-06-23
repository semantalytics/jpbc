package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10;

import it.unisa.dia.gas.crypto.engines.kem.KeyEncapsulationMechanism;
import it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.engines.UTMAWeakEngine;
import it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.generators.UTMAWeakKeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.generators.UTMAWeakParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params.UTMAWeakKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params.UTMAWeakParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params.UTMAWeakPublicParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params.UTMAWeakRandomizeParameters;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAWeakEngineTest extends TestCase {

    public void testUTMAWeakEngine() {
        UTMAWeakParameters parameters = createParameters();
        AsymmetricCipherKeyPair keyPair = setup(parameters);

        byte[][] ct = encaps(keyPair.getPublic());

        assertEquals(true, Arrays.equals(ct[0], decaps(keyPair.getPrivate(), ct[1])));
        assertEquals(true, Arrays.equals(ct[0], decaps(keyPair.getPrivate(), randomize(parameters.getPublicParameters(),ct[1]))));
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

    protected byte[][] encaps(CipherParameters publicKey) {
        try {
            KeyEncapsulationMechanism kem = new UTMAWeakEngine();
            kem.init(true, publicKey);

            byte[] ciphertext = kem.processBlock(new byte[0], 0, 0);

            assertNotNull(ciphertext);
            assertNotSame(0, ciphertext.length);

            byte[] key = Arrays.copyOfRange(ciphertext, 0, kem.getKeyBlockSize());
            byte[] ct = Arrays.copyOfRange(ciphertext, kem.getKeyBlockSize(), ciphertext.length);

            return new byte[][]{key, ct};
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    protected byte[] decaps(CipherParameters privateKey, byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism engine = new UTMAWeakEngine();
            engine.init(false, privateKey);

            return engine.processBlock(ciphertext, 0, ciphertext.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected byte[] randomize(UTMAWeakPublicParameters publicParameters, byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism engine = new UTMAWeakEngine();
            engine.init(true, new UTMAWeakRandomizeParameters(publicParameters));

            return engine.processBlock(ciphertext, 0, ciphertext.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected CurveParameters getCurveParameters() {
        return PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties");
    }
}
