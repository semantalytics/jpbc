package it.unisa.dia.gas.plaf.jpbc.crypto.bls;

import it.unisa.dia.gas.jpbc.plaf.crypto.bls.engines.BLSSigner;
import it.unisa.dia.gas.jpbc.plaf.crypto.bls.generators.BLSKeyPairGenerator;
import it.unisa.dia.gas.jpbc.plaf.crypto.bls.generators.BLSParametersGenerator;
import it.unisa.dia.gas.jpbc.plaf.crypto.bls.params.BLSKeyGenerationParameters;
import it.unisa.dia.gas.jpbc.plaf.crypto.bls.params.BLSParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLSTest extends TestCase {

    public void testSignerEngine() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(BLSTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/jpbc/examples/bls/a_181_603.properties"));

        byte[] message = "Hello World!!!".getBytes();

        // Generate public parameters
        BLSParametersGenerator parametersGenerator = new BLSParametersGenerator();
        parametersGenerator.init(curveParams);
        BLSParameters parameters = parametersGenerator.generateParameters();

        // Generate a key-pair
        BLSKeyPairGenerator keyPairGenerator = new BLSKeyPairGenerator();
        keyPairGenerator.init(new BLSKeyGenerationParameters(null, parameters));
        AsymmetricCipherKeyPair keyPair = keyPairGenerator.generateKeyPair();

        BLSSigner blsSigner = new BLSSigner(new SHA256Digest());

        // Sign
        blsSigner.init(true, keyPair.getPrivate());
        blsSigner.update(message, 0, message.length);
        byte[] sig = null;
        try {
            sig = blsSigner.generateSignature();
        } catch (CryptoException e) {
            fail(e.getMessage());
        }

        // Verify
        blsSigner.init(false, keyPair.getPublic());
        assertTrue(blsSigner.verifySignature(sig));
    }
}
