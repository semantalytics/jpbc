package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines.HHVEIP08AttributesEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HHVEIP08AttributesOnlySearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro
 */
public class HHVEIP08AttributesEngineTest extends TestCase {

    public void testHHVE() {
        AsymmetricCipherKeyPair keyPair = setup(genParam(1, 3, 1, 3, 2, 1));

        assertEquals(true,
                test(
                        keyGen(keyPair.getPrivate(), 0, 7, -1, 3, -1, 1),
                        enc(keyPair.getPublic(),     0, 7,  0, 3,  2, 1)
                )
        );

        assertEquals(false,
                test(
                        keyGen(keyPair.getPrivate(), 0, 5, -1, 3, -1, 1),
                        enc(keyPair.getPublic(),     0, 7,  0, 3,  2, 1)
                )
        );

        assertEquals(true,
                test(
                        keyGen(keyPair.getPrivate(), -1, -1, -1, -1, -1, -1),
                        enc(keyPair.getPublic(),     0, 7,  0, 3,  2, 1)
                )
        );

        assertEquals(true,
                test(
                        delegate(
                                keyPair.getPublic(),
                                keyGen(keyPair.getPrivate(), -1, -1, -1, -1, -1, -1),
                                0, 7, 0, 3, -1, 1
                        ),
                        enc(keyPair.getPublic(),     0, 7,  0, 3,  2, 1)
                )
        );

        assertEquals(true,
                test(
                        delegate(
                                keyPair.getPublic(),
                                keyGen(keyPair.getPrivate(), 0, 7, -1, 3, -1, 1),
                                0, 7, 0, 3, -1, 1
                        ),
                        enc(keyPair.getPublic(), 0, 7,  0, 3,  2, 1)
                )
        );
    }

    private CipherParameters delegate(CipherParameters publicKey, CipherParameters searchKey, int... attributesPattern) {
        HHVEIP08AttributesOnlySearchKeyGenerator generator = new HHVEIP08AttributesOnlySearchKeyGenerator();
        generator.init(new HHVEIP08DelegateSecretKeyGenerationParameters(
                (HVEIP08PublicKeyParameters) publicKey,
                (HHVEIP08SearchKeyParameters) searchKey,
                attributesPattern)
        );

        return generator.generateKey();
    }


    protected HVEIP08Parameters genParam(int... attributeLengths) {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));

        HVEIP08ParametersGenerator generator = new HVEIP08ParametersGenerator();
        generator.init(curveParams, attributeLengths);

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(HVEIP08Parameters hveParameters) {
        HVEIP08KeyPairGenerator generator = new HVEIP08KeyPairGenerator();
        generator.init(new HVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return generator.generateKeyPair();
    }

    protected byte[] enc(CipherParameters publicKey, int... attributes) {
        byte[] attrs = HVEAttributes.attributesToByteArray(
                ((HVEIP08PublicKeyParameters)publicKey).getParameters(),
                attributes
        );

        HHVEIP08AttributesEngine engine = new HHVEIP08AttributesEngine();
        engine.init(true, publicKey);

        return engine.processBlock(attrs, 0, attrs.length);
    }

    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HHVEIP08AttributesOnlySearchKeyGenerator generator = new HHVEIP08AttributesOnlySearchKeyGenerator();
        generator.init(new HVEIP08SearchKeyGenerationParameters((HVEIP08PrivateKeyParameters) privateKey, pattern));

        return generator.generateKey();
    }

    protected boolean test(CipherParameters searchKey, byte[] ct) {
        HHVEIP08AttributesEngine engine = new HHVEIP08AttributesEngine();
        engine.init(false, searchKey);

        return engine.processBlock(ct, 0, ct.length)[0] == 0;
    }

}

