package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13;

import it.unisa.dia.gas.crypto.circuit.BooleanCircuit;
import it.unisa.dia.gas.crypto.jpbc.AbstractJPBCCryptoTest;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.engines.GGHSW13KEMEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.generators.GGHSW13KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.generators.GGHSW13ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.generators.GGHSW13SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.*;
import it.unisa.dia.gas.crypto.kem.KeyEncapsulationMechanism;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.AssumptionViolatedException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Stopwatch;
import org.junit.runner.Description;
import org.junit.runners.Parameterized;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.TimeUnit;

import static it.unisa.dia.gas.crypto.circuit.BooleanCircuit.BooleanCircuitGate;
import static it.unisa.dia.gas.crypto.circuit.Gate.Type.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Fail.fail;

/**
 * @author Angelo De Caro
 */
public class GGHSW13KEMEngineTest extends AbstractJPBCCryptoTest {

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {false, "it/unisa/dia/gas/plaf/jpbc/crypto/ctl13_toy.properties"}
        };

        return Arrays.asList(data);
    }

    @Rule
    public final Stopwatch stopwatch = new Stopwatch() {
        private void logInfo(Description description, String status, long nanos) {
            String testName = description.getMethodName();
            System.out.println(String.format("Test %s %s, spent %f seconds",
                                             testName, status, TimeUnit.NANOSECONDS.toMillis(nanos)/1000.0));
        }

        @Override
        protected void succeeded(long nanos, Description description) {
            logInfo(description, "succeeded", nanos);
        }

        @Override
        protected void failed(long nanos, Throwable e, Description description) {
            logInfo(description, "failed", nanos);
        }

        @Override
        protected void skipped(long nanos, AssumptionViolatedException e, Description description) {
            logInfo(description, "skipped", nanos);
        }

        @Override
        protected void finished(long nanos, Description description) {
            logInfo(description, "finished", nanos);
        }
    };

    protected Pairing pairing;

    public GGHSW13KEMEngineTest(boolean usePBC, String curvePath) {
        super(usePBC, curvePath);
    }

    @Override
    public void before() throws Exception {
        super.before();

        this.pairing = PairingFactory.getPairing(parameters);
    }

    @Test
    public void testGGHSW13KEMEngine() {
        int n = 4;
        int q = 3;
        BooleanCircuit circuit = new BooleanCircuit(n, q, 3, new BooleanCircuitGate[]{
                new BooleanCircuitGate(INPUT, 0, 1),
                new BooleanCircuitGate(INPUT, 1, 1),
                new BooleanCircuitGate(INPUT, 2, 1),
                new BooleanCircuitGate(INPUT, 3, 1),

                new BooleanCircuitGate(AND, 4, 2, new int[]{0, 1}),
                new BooleanCircuitGate(OR, 5, 2, new int[]{2, 3}),

                new BooleanCircuitGate(AND, 6, 3, new int[]{4, 5}),
        });

        AsymmetricCipherKeyPair keyPair = setup(createParameters(n));
        CipherParameters secretKey = keyGen(keyPair.getPublic(), keyPair.getPrivate(), circuit);

        String assignment = "1101";
        byte[][] ct = encaps(keyPair.getPublic(), assignment);
        assertThat(Arrays.equals(ct[0], decaps(secretKey, ct[1]))).isTrue();

        assignment = "1001";
        ct = encaps(keyPair.getPublic(), assignment);
        assertThat(Arrays.equals(ct[0], decaps(secretKey, ct[1]))).isFalse();
    }


    protected GGHSW13Parameters createParameters(int n) {
        return GGHSW13ParametersGenerator.generate(pairing, n);
    }

    protected AsymmetricCipherKeyPair setup(GGHSW13Parameters parameters) {
        GGHSW13KeyPairGenerator setup = new GGHSW13KeyPairGenerator();
        setup.init(new GGHSW13KeyPairGenerationParameters(
                new SecureRandom(),
                parameters
        ));

        return setup.generateKeyPair();
    }

    protected byte[][] encaps(CipherParameters publicKey, String w) {
        try {
            KeyEncapsulationMechanism kem = new GGHSW13KEMEngine();
            kem.init(true, new GGHSW13EncryptionParameters((GGHSW13PublicKeyParameters) publicKey, w));

            byte[] ciphertext = kem.processBlock(new byte[0], 0, 0);

            assertThat(ciphertext).isNotNull();
            assertThat(ciphertext.length).isNotSameAs(0);

            byte[] key = Arrays.copyOfRange(ciphertext, 0, kem.getKeyBlockSize());
            byte[] ct = Arrays.copyOfRange(ciphertext, kem.getKeyBlockSize(), ciphertext.length);

            return new byte[][]{key, ct};
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }
        return null;
    }

    protected CipherParameters keyGen(CipherParameters publicKey, CipherParameters masterSecretKey, BooleanCircuit circuit) {
        // Init the Generator
        GGHSW13SecretKeyGenerator keyGen = new GGHSW13SecretKeyGenerator(new GGHSW13SecretKeyGenerationParameters(
                (GGHSW13PublicKeyParameters) publicKey,
                (GGHSW13MasterSecretKeyParameters) masterSecretKey,
                circuit
        ));

        // Generate the key
        return keyGen.generateKey();
    }

    protected byte[] decaps(CipherParameters secretKey, byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism kem = new GGHSW13KEMEngine();

            kem.init(false, secretKey);
            byte[] key = kem.processBlock(ciphertext, 0, ciphertext.length);

            assertThat(key).isNotNull();
            assertThat(key.length).isNotSameAs(0);

            return key;
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return null;
    }

}