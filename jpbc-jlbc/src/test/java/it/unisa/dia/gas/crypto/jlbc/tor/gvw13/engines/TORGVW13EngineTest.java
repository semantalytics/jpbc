package it.unisa.dia.gas.crypto.jlbc.tor.gvw13.engines;

import it.unisa.dia.gas.crypto.jlbc.tor.gvw13.generators.TORGVW13KeyPairGenerator;
import it.unisa.dia.gas.crypto.jlbc.tor.gvw13.generators.TORGVW13RecKeyGenerator;
import it.unisa.dia.gas.crypto.jlbc.tor.gvw13.params.*;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;
import org.junit.Before;
import org.junit.Test;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13EngineTest {

    private AsymmetricCipherKeyPair keyPair0, keyPair1, keyPair2, keyPair3;
    private TORGVW13PublicKeyParameters pk0, pk1, pk2, pk3;
    private SecureRandom random;

    @Before
    public void setUp() throws Exception {
        random = new SecureRandom();

        TORGVW13Parameters parameters = new TORGVW13Parameters(random, 4, 5);

        TORGVW13KeyPairGenerator keyPairGenerator = new TORGVW13KeyPairGenerator();
        keyPairGenerator.init(new TORGVW13KeyPairGenerationParameters(random, 100, parameters));
        keyPair0 = keyPairGenerator.generateKeyPair();
        keyPair1 = keyPairGenerator.generateKeyPair();
        keyPair2 = keyPairGenerator.generateKeyPair();
        keyPair3 = keyPairGenerator.generateKeyPair();

        pk0 =  (TORGVW13PublicKeyParameters) keyPair0.getPublic();
        pk1 =  (TORGVW13PublicKeyParameters) keyPair1.getPublic();
        pk2 =  (TORGVW13PublicKeyParameters) keyPair2.getPublic();
        pk3 =  (TORGVW13PublicKeyParameters) keyPair3.getPublic();
    }

    @Test
    public void testEncode() throws Exception {
        // Encode
        TORGVW13Engine engine = new TORGVW13Engine();
        engine.init(pk0);
        engine.processElements(pk0.getOwfInputField().newRandomElement());
    }

    @Test
    public void testRecode() throws Exception {
        Element s = pk0.getOwfInputField().newRandomElement();

        // Encode
        TORGVW13Engine engine = new TORGVW13Engine();
        engine.init(pk0);
        Element e0 = engine.processElements(s);
        System.out.println("e0 = " + e0);

        engine.init(pk1);
        Element e1 = engine.processElements(s);
        System.out.println("e1 = " + e1);

        engine.init(pk2);
        Element e2 = engine.processElements(s);
        System.out.println("e2 = " + e2);

        engine.init(pk3);
        Element e3 = engine.processElements(s);
        System.out.println("e3 = " + e3);

        // Recode
        TORGVW13RecKeyGenerator recKeyGenerator = new TORGVW13RecKeyGenerator();
        recKeyGenerator.init(new TORGVW13ReKeyGenerationParameters(
                random, 100,
                (TORGVW13PublicKeyParameters) keyPair0.getPublic(),
                (TORGVW13SecretKeyParameters) keyPair0.getPrivate(),
                (TORGVW13PublicKeyParameters) keyPair1.getPublic(),
                (TORGVW13PublicKeyParameters) keyPair2.getPublic()
        ));
        CipherParameters recKey = recKeyGenerator.generateKey();
        engine.init(recKey);
        Element e2Prime = engine.processElements(e0, e1);
        System.out.println("e2Prime = " + e2Prime);

        Element diff = e2.duplicate().sub(e2Prime);
        System.out.println("diff = " + diff);

        e2Prime = engine.processElements(e0, e3);
        System.out.println("e2Prime = " + e2Prime);

        diff = e2.duplicate().sub(e2Prime);
        System.out.println("diff = " + diff);
    }
}
