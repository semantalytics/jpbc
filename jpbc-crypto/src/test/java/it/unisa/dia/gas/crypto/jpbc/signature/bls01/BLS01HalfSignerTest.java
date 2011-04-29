package it.unisa.dia.gas.crypto.jpbc.signature.bls01;

import it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines.BLS01HalfSigner;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01Parameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLS01HalfSignerTest extends TestCase {

    public void testSignerEngine() {
        // Setup
        AsymmetricCipherKeyPair keyPair = keyGen(setup(
                new CurveParams().load(BLS01HalfSignerTest.class.getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"))
        ));

        // Test
        String message = "Hello World!";
        assertTrue(verify(sign(message, keyPair.getPrivate()), message, keyPair.getPublic()));
    }


    protected BLS01Parameters setup(CurveParams curveParams) {
        BLS01ParametersGenerator setup = new BLS01ParametersGenerator();
        setup.init(curveParams);

        return setup.generateParameters();
    }

    protected AsymmetricCipherKeyPair keyGen(BLS01Parameters parameters) {
        BLS01KeyPairGenerator keyGen = new BLS01KeyPairGenerator();
        keyGen.init(new BLS01KeyGenerationParameters(null, parameters));

        return keyGen.generateKeyPair();
    }

    protected byte[] sign(String message, CipherParameters privateKey) {
        byte[] bytes = message.getBytes();

        BLS01HalfSigner signer = new BLS01HalfSigner(new SHA256Digest());
        signer.init(true, privateKey);
        signer.update(bytes, 0, bytes.length);

        byte[] signature = null;
        try {
            signature = signer.generateSignature();
        } catch (CryptoException e) {
            fail(e.getMessage());
        }
        return signature;
    }

    protected boolean verify(byte[] signature, String message, CipherParameters publicKey) {
        byte[] bytes = message.getBytes();

        BLS01HalfSigner signer = new BLS01HalfSigner(new SHA256Digest());
        signer.init(false, publicKey);
        signer.update(bytes, 0, bytes.length);

        return signer.verifySignature(signature);
    }

}
