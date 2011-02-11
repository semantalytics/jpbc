package it.unisa.dia.gas.crypto.jpbc.signature.bls01;

import it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines.BLS01Signer;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01Parameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLS01SignerTest extends TestCase {

    public void testSignerEngine() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(BLS01SignerTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));

        byte[] message = "Hello World!!!".getBytes();

        // Generate public parameters
        BLS01ParametersGenerator setup = new BLS01ParametersGenerator();
        setup.init(curveParams);
        BLS01Parameters parameters = setup.generateParameters();

        // Generate a key-pair
        BLS01KeyPairGenerator keyPairGenerator = new BLS01KeyPairGenerator();
        keyPairGenerator.init(new BLS01KeyGenerationParameters(null, parameters));
        AsymmetricCipherKeyPair keyPair = keyPairGenerator.generateKeyPair();


        BLS01Signer blsSigner = new BLS01Signer(new SHA256Digest());
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
