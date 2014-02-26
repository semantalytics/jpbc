package it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.engines;

import it.unisa.dia.gas.crypto.circuit.Circuit;
import it.unisa.dia.gas.crypto.circuit.DefaultCircuit;
import it.unisa.dia.gas.crypto.kem.KeyEncapsulationMechanism;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.generators.GVW13KeyPairGenerator;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.generators.GVW13ParametersGenerator;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.generators.GVW13SecretKeyGenerator;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params.*;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;
import java.util.Arrays;

import static it.unisa.dia.gas.crypto.circuit.Circuit.Gate.Type.AND;
import static it.unisa.dia.gas.crypto.circuit.Circuit.Gate.Type.INPUT;
import static org.junit.Assert.*;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13KEMEngineTest {

    private SecureRandom random;

    @Before
    public void setUp() throws Exception {
        random = new SecureRandom();
    }

    @Test
    public void testGVW13KEMEngine() {
//        int n = 4;
//        int q = 3;
//        Circuit circuit = new DefaultCircuit(n, q, 3, new DefaultCircuit.DefaultGate[]{
//                new DefaultCircuit.DefaultGate(INPUT, 0, 1),
//                new DefaultCircuit.DefaultGate(INPUT, 1, 1),
//                new DefaultCircuit.DefaultGate(INPUT, 2, 1),
//                new DefaultCircuit.DefaultGate(INPUT, 3, 1),
//
//                new DefaultCircuit.DefaultGate(AND, 4, 2, new int[]{0, 1}),
//                new DefaultCircuit.DefaultGate(OR, 5, 2, new int[]{2, 3}),
//
//                new DefaultCircuit.DefaultGate(AND, 6, 3, new int[]{4, 5}),
//        });
        int n = 2;
        int q = 1;
        Circuit circuit = new DefaultCircuit(n, q, 2, new DefaultCircuit.DefaultGate[]{
                new DefaultCircuit.DefaultGate(INPUT, 0, 1),
                new DefaultCircuit.DefaultGate(INPUT, 1, 1),

                new DefaultCircuit.DefaultGate(AND, 2, 2, new int[]{0, 1}),
        });

        AsymmetricCipherKeyPair keyPair = setup(createParameters(n));
        CipherParameters secretKey = keyGen(keyPair.getPublic(), keyPair.getPrivate(), circuit);

        String assignment = "11";
        byte[][] ct = encaps(keyPair.getPublic(), assignment);
        System.out.println("ct[0] = " + Arrays.toString(ct[0]));
        System.out.println("ct[1] = " + Arrays.toString(ct[1]));
        System.out.println("decaps = " + Arrays.toString(decaps(secretKey, ct[1])));

        assignment = "10";
        ct = encaps(keyPair.getPublic(), assignment);
        System.out.println("ct[0] = " + Arrays.toString(ct[0]));
        System.out.println("ct[1] = " + Arrays.toString(ct[1]));
        System.out.println("decaps = " + Arrays.toString(decaps(secretKey, ct[1])));
    }


    protected GVW13Parameters createParameters(int n) {
        return new GVW13ParametersGenerator(new SecureRandom(), n).generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(GVW13Parameters parameters) {
        GVW13KeyPairGenerator setup = new GVW13KeyPairGenerator();
        setup.init(new GVW13KeyPairGenerationParameters(
                new SecureRandom(),
                parameters
        ));

        return setup.generateKeyPair();
    }

    protected byte[][] encaps(CipherParameters publicKey, String w) {
        try {
            KeyEncapsulationMechanism kem = new GVW13KEMEngine();
            kem.init(true, new GVW13EncryptionParameters((GVW13PublicKeyParameters) publicKey, w));

            byte[] ciphertext = kem.processBlock(new byte[0], 0, 0);

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

    protected CipherParameters keyGen(CipherParameters publicKey, CipherParameters masterSecretKey, Circuit circuit) {
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
            byte[] key = kem.processBlock(ciphertext, 0, ciphertext.length);

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
