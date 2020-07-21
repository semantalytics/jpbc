package it.unisa.dia.gas.crypto.jpbc.signature.bls01;

import it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines.BLS01Signer;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.generators.BLS01ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01Parameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.CryptoException;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BLS01Test {

    static class BLS01 {

        private final BLS01Parameters parameters;

        public BLS01() {
            final BLS01ParametersGenerator setup = new BLS01ParametersGenerator();
            setup.init(PairingFactory.getPairingParameters("params/curves/a.properties"));

            this.parameters = setup.generateParameters();
        }

        public BLS01(final BLS01Parameters parameters) {
            this.parameters = parameters;
        }

        public AsymmetricCipherKeyPair keyGen() {
            final BLS01KeyPairGenerator keyGen = new BLS01KeyPairGenerator();
            keyGen.init(new BLS01KeyGenerationParameters(null, parameters));

            return keyGen.generateKeyPair();
        }

        public static byte[] sign(final String message, final AsymmetricCipherKeyPair keyPair) {
            return sign(message, keyPair.getPrivate());
        }

        public static byte[] sign(final String message, final CipherParameters privateKey) {
            final byte[] bytes = message.getBytes();

            final BLS01Signer signer = new BLS01Signer(new SHA256Digest());
            signer.init(true, privateKey);
            signer.update(bytes, 0, bytes.length);

            final byte[] signature;

            try {
                signature = signer.generateSignature();
            }
            catch (CryptoException e) {
                throw new RuntimeException(e);
            }
            return signature;
        }

        public static boolean verify(final byte[] signature, final String message, final AsymmetricCipherKeyPair keyPair) {
            return verify(signature, message, keyPair.getPublic());
        }

        public static boolean verify(final byte[] signature, final String message, final CipherParameters publicKey) {
            final byte[] bytes = message.getBytes();

            final BLS01Signer signer = new BLS01Signer(new SHA256Digest());
            signer.init(false, publicKey);
            signer.update(bytes, 0, bytes.length);

            return signer.verifySignature(signature);
        }
    }

    @Test
    public void test() {

        // Setup
        AsymmetricCipherKeyPair keyPair = new BLS01().keyGen();

        // Test same message
        String message = "Hello World!";
        final byte[] signature = BLS01.sign(message, keyPair);
        assertThat(BLS01.verify(signature, message, keyPair)).isTrue();

        // Test different messages
        assertThat(BLS01.verify(signature, "Hello Italy!", keyPair)).isFalse();
    }

}
