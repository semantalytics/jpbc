package it.unisa.dia.gas.crypto.jlbc.fe.abe.gvw13.engines;

import it.unisa.dia.gas.crypto.circuit.BooleanCircuit;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.gvw13.generators.GVW13KeyPairGenerator;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.gvw13.generators.GVW13ParametersGenerator;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.gvw13.generators.GVW13SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.gvw13.params.*;
import it.unisa.dia.gas.crypto.kem.KeyEncapsulationMechanism;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;

import static it.unisa.dia.gas.crypto.circuit.BooleanCircuit.BooleanCircuitGate;
import static it.unisa.dia.gas.crypto.circuit.Gate.Type.*;
import static org.junit.Assert.*;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13KEMEngineTest {

    private SecureRandom random;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");
    }

    @Test
    public void testGVW13KEMEngine() {
        // Setup
        int ell = 4;
        int depth = 3;

        AsymmetricCipherKeyPair keyPair = setup(ell, depth);

        // Key Gen
        int q = 3;
        BooleanCircuit circuit = new BooleanCircuit(ell, q, depth, new BooleanCircuitGate[]{
                new BooleanCircuitGate(INPUT, 0, 1),
                new BooleanCircuitGate(INPUT, 1, 1),
                new BooleanCircuitGate(INPUT, 2, 1),
                new BooleanCircuitGate(INPUT, 3, 1),

                new BooleanCircuitGate(AND, 4, 2, new int[]{0, 1}),
                new BooleanCircuitGate(OR, 5, 2, new int[]{2, 3}),

                new BooleanCircuitGate(AND, 6, 3, new int[]{4, 5}),
        });
        GVW13SecretKeyParameters secretKey = (GVW13SecretKeyParameters) keyGen(keyPair.getPublic(), keyPair.getPrivate(), circuit);

        // Encaps/Decaps for a satisfying assignment
        String assignment = "1111";
        byte[][] ct = encaps(keyPair.getPublic(), assignment);
        byte[] key = ct[0];
        byte[] keyPrime = decaps(secretKey, ct[1]);

        System.out.println("key      = " + Arrays.toString(key));
        System.out.println("keyPrime = " + Arrays.toString(keyPrime));
        assertEquals(true, Arrays.equals(key, keyPrime));

        // Encaps/Decaps for a non-satisfying assignment
        assignment = "0001";
        ct = encaps(keyPair.getPublic(), assignment);
        key = ct[0];
        keyPrime = decaps(secretKey, ct[1]);

        System.out.println("key      = " + Arrays.toString(key));
        System.out.println("keyPrime = " + Arrays.toString(keyPrime));
        assertEquals(false, Arrays.equals(key, keyPrime));
    }


    protected AsymmetricCipherKeyPair setup(int ell, int depth) {
        GVW13KeyPairGenerator setup = new GVW13KeyPairGenerator();
        setup.init(new GVW13KeyPairGenerationParameters(
            random,
            new GVW13ParametersGenerator(random, ell, depth).generateParameters())
        );

        return setup.generateKeyPair();
    }

    protected byte[][] encaps(CipherParameters publicKey, String w) {
        try {
            KeyEncapsulationMechanism kem = new GVW13KEMEngine();
            kem.init(true, new GVW13EncryptionParameters((GVW13PublicKeyParameters) publicKey, w));

            byte[] ciphertext = kem.process();

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

    protected CipherParameters keyGen(CipherParameters publicKey, CipherParameters masterSecretKey, BooleanCircuit circuit) {
        // Init the Generator
        GVW13SecretKeyGenerator keyGen = new GVW13SecretKeyGenerator();
        keyGen.init(new GVW13SecretKeyGenerationParameters(
                (GVW13PublicKeyParameters) publicKey,
                (GVW13MasterSecretKeyParameters) masterSecretKey,
                circuit
        ));

        // Generate the key
        return keyGen.generateKey();
    }

    protected byte[] decaps(CipherParameters secretKey, byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism kem = new GVW13KEMEngine();

            kem.init(false, secretKey);
            byte[] key = kem.processBlock(ciphertext);

            assertNotNull(key);
            assertNotSame(0, key.length);

            return key;
        } catch (InvalidCipherTextException e) {
            e.printStackTrace();
            fail(e.getMessage());
        }

        return null;
    }

}