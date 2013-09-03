import it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines.BLS01Signer;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01Parameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01PublicKeyParameters;
import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.f.TypeFCurveGenerator;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;

import java.security.SecureRandom;

public class BLS {

    protected String curvePath;
    protected boolean usePBC;

    protected PairingParameters parameters;

    public BLS() {
//        Load the curve parameters from a file
//        parameters = PairingFactory.getInstance().loadCurveParameters("it/unisa/dia/gas/plaf/jpbc/pairing/f/f.properties");

//        Or you can generate the curve parameters in this way.
//
        TypeFCurveGenerator curveGenerator = new TypeFCurveGenerator(new SecureRandom(),160);
        parameters = curveGenerator.generate();
    }


    public void testSignerEngine() {
        // Setup
        AsymmetricCipherKeyPair keyPair = keyGen(setup());

        // Test
        String message = "Hello World!";
        assert (verify(sign(message, keyPair.getPrivate()), message, keyPair.getPublic()));
    }


    protected BLS01Parameters setup() {
        BLS01ParametersGenerator setup = new BLS01ParametersGenerator();
        setup.init(parameters);

        return setup.generateParameters();
    }

    protected AsymmetricCipherKeyPair keyGen(BLS01Parameters parameters) {
        BLS01KeyPairGenerator keyGen = new BLS01KeyPairGenerator();
        keyGen.init(new BLS01KeyGenerationParameters(null, parameters));

        AsymmetricCipherKeyPair keyPair = keyGen.generateKeyPair();
        BLS01PublicKeyParameters pk = (BLS01PublicKeyParameters) keyPair.getPublic();
        BLS01PrivateKeyParameters sk = (BLS01PrivateKeyParameters) keyPair.getPrivate();

        // Store those values using the toBytes method
        // which returns a stream of bytes representing
        // an element.
        System.out.println("pk.getPk() = " + pk.getPk());

//        pk.getPk().toBytes();
//        sk.getSk().toBytes();

        return keyPair;
    }

    protected byte[] sign(String message, CipherParameters privateKey) {
        byte[] bytes = message.getBytes();

        BLS01Signer signer = new BLS01Signer(new SHA256Digest());
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

    protected boolean verify(byte[] signature, String message, CipherParameters publicKey) {
        byte[] bytes = message.getBytes();

        BLS01Signer signer = new BLS01Signer(new SHA256Digest());
        signer.init(false, publicKey);
        signer.update(bytes, 0, bytes.length);

        return signer.verifySignature(signature);
    }


    public static void main(String[] args) {
        BLS bls = new BLS();
        bls.testSignerEngine();
    }

}
