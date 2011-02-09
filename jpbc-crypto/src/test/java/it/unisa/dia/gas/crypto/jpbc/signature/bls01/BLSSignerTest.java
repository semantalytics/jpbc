package it.unisa.dia.gas.crypto.jpbc.signature.bls01;

import it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines.BLSSigner;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLSKeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLSParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLSKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLSParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLSSignerTest extends TestCase {

    public void testSignerEngine() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(BLSSignerTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));

        byte[] message = "Hello World!!!".getBytes();

        // Generate public parameters
        BLSParametersGenerator setup = new BLSParametersGenerator();
        setup.init(curveParams);
        BLSParameters parameters = setup.generateParameters();

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
        blsSigner.update(message, 0, message.length);
        assertTrue(blsSigner.verifySignature(sig));
    }
}
