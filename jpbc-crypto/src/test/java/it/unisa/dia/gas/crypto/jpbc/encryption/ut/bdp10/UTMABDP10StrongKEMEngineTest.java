package it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10;

import it.unisa.dia.gas.crypto.engines.kem.KeyEncapsulationMechanism;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.engines.UTMABDP10StrongKEMEngine;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.generators.UTMABDP10StrongKeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.generators.UTMABDP10StrongParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.encryption.ut.bdp10.params.*;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.generators.ElGamalParametersGenerator;
import org.bouncycastle.crypto.params.ElGamalParameters;

import java.security.SecureRandom;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMABDP10StrongKEMEngineTest extends TestCase {

    public void testUTMABDP10StrongKEMEngine() {
        UTMABDP10StrongParameters parameters = createParameters(1024);
        AsymmetricCipherKeyPair keyPair = setup(parameters);

        byte[][] ct = encaps(keyPair.getPublic());

        assertEquals(true, Arrays.equals(ct[0], decaps(keyPair.getPrivate(), ct[1])));
        assertEquals(true, Arrays.equals(ct[0], decaps(keyPair.getPrivate(),
                randomize(parameters.getPublicParameters(), parameters.getRPublicParameters(), ct[1]))));
    }


    protected UTMABDP10StrongParameters createParameters(int elgamalLength) {
        ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
        elGamalParametersGenerator.init(elgamalLength, 12, new SecureRandom());
        ElGamalParameters elGamalParameters = elGamalParametersGenerator.generateParameters();

        UTMABDP10StrongParametersGenerator generator = new UTMABDP10StrongParametersGenerator();
        generator.init(getCurveParameters(), elGamalParameters);
        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(UTMABDP10StrongParameters parameters) {
        UTMABDP10StrongKeyPairGenerator setup = new UTMABDP10StrongKeyPairGenerator();
        setup.init(new UTMABDP10StrongKeyGenerationParameters(new SecureRandom(), parameters));

        return setup.generateKeyPair();
    }

    protected byte[][] encaps(CipherParameters publicKey) {
        try {
            KeyEncapsulationMechanism kem = new UTMABDP10StrongKEMEngine();
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
            KeyEncapsulationMechanism engine = new UTMABDP10StrongKEMEngine();
            engine.init(false, privateKey);

            return engine.processBlock(ciphertext, 0, ciphertext.length);
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    protected byte[] randomize(UTMABDP10StrongPublicParameters publicParameters,
                               UTMABDP10StrongRPublicParameters rPublicParameters,
                               byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism engine = new UTMABDP10StrongKEMEngine();
            engine.init(true, new UTMABDP10StrongRandomizeParameters(publicParameters, rPublicParameters));

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