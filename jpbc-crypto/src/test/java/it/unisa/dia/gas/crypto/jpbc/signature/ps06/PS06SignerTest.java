package it.unisa.dia.gas.crypto.jpbc.signature.ps06;

import it.unisa.dia.gas.crypto.jpbc.signature.ps06.engines.PS06Signer;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.generators.PS06ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.generators.PS06SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.generators.PS06SetupGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.ps06.params.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PS06SignerTest extends TestCase {

    public void testSignerEngine() {
        CurveParams curveParams = new CurveParams();
        curveParams.load(PS06SignerTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));

        byte[] message = "Hello World!!!".getBytes();

        // generate public parameters
        PS06ParametersGenerator parametersGenerator = new PS06ParametersGenerator();
        parametersGenerator.init(curveParams, 14, 256);
        PS06Parameters parameters = parametersGenerator.generateParameters();

        // setup -> (public key, master secret key)
        PS06SetupGenerator setup = new PS06SetupGenerator();
        setup.init(new PS06PublicKeyGenerationParameters(null, parameters));
        AsymmetricCipherKeyPair keyPair = setup.generateKeyPair();

        // extract -> secret key for identity "01001101"
        PS06SecretKeyGenerator extract = new PS06SecretKeyGenerator();
        extract.init(new PS06SecretKeyGenerationParameters(keyPair, "01001101"));
        CipherParameters sk01001101 = extract.generateKey();


        // sign and verify
        PS06Signer PS06Signer = new PS06Signer(new SHA256Digest());

        // sign
        PS06Signer.init(true, new PS06SignParameters((PS06SecretKeyParameters) sk01001101));
        PS06Signer.update(message, 0, message.length);
        byte[] sig = null;
        try {
            sig = PS06Signer.generateSignature();
        } catch (CryptoException e) {
            fail(e.getMessage());
        }

        // verify
        PS06Signer.init(false, new PS06VerifyParameters((PS06PublicKeyParameters) keyPair.getPublic(), "01001101"));
        PS06Signer.update(message, 0, message.length);
        assertTrue(PS06Signer.verifySignature(sig));

        // verify
        PS06Signer.init(false, new PS06VerifyParameters((PS06PublicKeyParameters) keyPair.getPublic(), "01001100"));
        PS06Signer.update(message, 0, message.length);
        assertFalse(PS06Signer.verifySignature(sig));
    }
}
