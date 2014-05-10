package it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.engines;

import it.unisa.dia.gas.crypto.circuit.ArithmeticCircuit;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.generators.BNS14KeyPairGenerator;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.generators.BNS14ParametersGenerator;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.generators.BNS14SecretKeyGenerator;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params.*;
import it.unisa.dia.gas.crypto.kem.KeyEncapsulationMechanism;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.junit.Before;
import org.junit.Test;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.StringTokenizer;

import static it.unisa.dia.gas.crypto.circuit.Gate.Type.INPUT;
import static it.unisa.dia.gas.crypto.circuit.Gate.Type.OR;
import static org.junit.Assert.*;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BNS14KEMEngineTest {

    private SecureRandom random;

    @Before
    public void setUp() throws Exception {
        random = SecureRandom.getInstance("SHA1PRNG");
    }

    @Test
    public void testBNS14KEMEngine() {
        // Setup
        int ell = 2;
        int depth = 2;

        AsymmetricCipherKeyPair keyPair = setup(ell, depth);
        Field Zq = ((BNS14PublicKeyParameters)keyPair.getPublic()).getLatticePk().getZq();

        // Key Gen
        int q = 1;
        ArithmeticCircuit circuit = new ArithmeticCircuit(ell, q, depth, new ArithmeticCircuit.ArithmeticCircuitGate[]{
                new ArithmeticCircuit.ArithmeticCircuitGate(INPUT, 0, 1),
                new ArithmeticCircuit.ArithmeticCircuitGate(INPUT, 1, 1),
                new ArithmeticCircuit.ArithmeticCircuitGate(OR, 3, 2, new int[]{0, 1}, Zq.newOneElement(), Zq.newOneElement()),
        });

        BNS14SecretKeyParameters secretKey = (BNS14SecretKeyParameters) keyGen(keyPair.getPublic(), keyPair.getPrivate(), circuit);

        // Encaps/Decaps for a satisfying assignment
        byte[][] ct = encaps(keyPair.getPublic(), toElement(Zq, "1 1", ell));
        byte[] key = ct[0];
        byte[] keyPrime = decaps(secretKey, ct[1]);

        System.out.println("key      = " + Arrays.toString(key));
        System.out.println("keyPrime = " + Arrays.toString(keyPrime));
        assertEquals(true, Arrays.equals(key, keyPrime));

        // Encaps/Decaps for a non-satisfying assignmen-
        ct = encaps(keyPair.getPublic(), toElement(Zq, "1 0", ell));
        key = ct[0];
        keyPrime = decaps(secretKey, ct[1]);

        System.out.println("key      = " + Arrays.toString(key));
        System.out.println("keyPrime = " + Arrays.toString(keyPrime));
        assertEquals(false, Arrays.equals(key, keyPrime));
    }


    protected AsymmetricCipherKeyPair setup(int ell, int depth) {
        BNS14KeyPairGenerator setup = new BNS14KeyPairGenerator();
        setup.init(new BNS14KeyPairGenerationParameters(
            random,
            new BNS14ParametersGenerator(random, ell, depth).generateParameters())
        );

        return setup.generateKeyPair();
    }

    protected Element[] toElement(Field Zq, String assignment, int ell) {
        Element[] elements = new Element[ell];
        StringTokenizer st = new StringTokenizer(assignment, " ");
        int i = 0;
        while (st.hasMoreTokens()) {
            elements[i++] = Zq.newElement(new BigInteger(st.nextToken()));
        }

        return elements;
    }

    protected byte[][] encaps(CipherParameters publicKey, Element[] w) {
        try {
            KeyEncapsulationMechanism kem = new BNS14KEMEngine();
            kem.init(true, new BNS14EncryptionParameters((BNS14PublicKeyParameters) publicKey, w));

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

    protected CipherParameters keyGen(CipherParameters publicKey, CipherParameters masterSecretKey, ArithmeticCircuit circuit) {
        // Init the Generator
        BNS14SecretKeyGenerator keyGen = new BNS14SecretKeyGenerator();
        keyGen.init(new BNS14SecretKeyGenerationParameters(
                (BNS14PublicKeyParameters) publicKey,
                (BNS14MasterSecretKeyParameters) masterSecretKey,
                circuit
        ));

        // Generate the key
        return keyGen.generateKey();
    }

    protected byte[] decaps(CipherParameters secretKey, byte[] ciphertext) {
        try {
            KeyEncapsulationMechanism kem = new BNS14KEMEngine();

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