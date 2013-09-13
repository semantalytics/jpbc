package it.unisa.dia.gas.crypto.jpbc.signature.bls01;

import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01Parameters;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * @author Angelo De Caro
 */
public class BLS01Signer {

    private PairingParameters parameters;

    public BLS01Signer() {
        parameters = PairingFactory.getPairingParameters("params/curves/a.properties");
    }


    public BLS01Parameters setup() {
        BLS01ParametersGenerator setup = new BLS01ParametersGenerator();
        setup.init(parameters);

        return setup.generateParameters();
    }

    public AsymmetricCipherKeyPair keyGen(BLS01Parameters parameters) {
        BLS01KeyPairGenerator keyGen = new BLS01KeyPairGenerator();
        keyGen.init(new BLS01KeyGenerationParameters(null, parameters));

        return keyGen.generateKeyPair();
    }

    public byte[] sign(String message, CipherParameters privateKey) {
        byte[] bytes = message.getBytes();

        it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines.BLS01Signer signer = new it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines.BLS01Signer(new SHA256Digest());
        signer.init(true, privateKey);
        signer.update(bytes, 0, bytes.length);

        byte[] signature = null;
        try {
            signature = signer.generateSignature();
        } catch (CryptoException e) {
            throw new RuntimeException(e);
        }
        return signature;
    }

    public boolean verify(byte[] signature, String message, CipherParameters publicKey) {
        byte[] bytes = message.getBytes();

        it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines.BLS01Signer signer = new it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines.BLS01Signer(new SHA256Digest());
        signer.init(false, publicKey);
        signer.update(bytes, 0, bytes.length);

        return signer.verifySignature(signature);
    }

    public static void main(String[] args) {
        BLS01Signer signer = new BLS01Signer();

        // Setup
        AsymmetricCipherKeyPair keyPair = signer.keyGen(signer.setup());

        // Test same message
        String message = "Hello World!";
        assertTrue(signer.verify(signer.sign(message, keyPair.getPrivate()), message, keyPair.getPublic()));

        // Test different message
        assertFalse(signer.verify(signer.sign(message, keyPair.getPrivate()), "Hello Italy!", keyPair.getPublic()));
    }

}
