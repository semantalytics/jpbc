package it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08;

import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.engines.HHVEIP08AttributesEngine;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.generators.HHVEIP08KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.generators.HHVEIP08ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.generators.HHVEIP08SearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hhve.ip08.params.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro
 */
public class TestHHVE extends TestCase {

    public void testHVE() {
        AsymmetricCipherKeyPair keyPair = setup(genParam(1, 7, 1, 4, 2, 1));

        assertEquals(true,
                test(
                        keyGen(keyPair.getPrivate(), 0, 8, -1, 3, -1, 1),
                        enc(keyPair.getPublic(),     0, 8,  0, 3,  2, 1)
                )
        );
    }


    protected HHVEIP08Parameters genParam(int... attributeLengths) {
        CurveParams curveParams = new CurveParams();
        curveParams.load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties"));

        HHVEIP08ParametersGenerator generator = new HHVEIP08ParametersGenerator();
        generator.init(curveParams, attributeLengths);

        return generator.generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(HHVEIP08Parameters hveParameters) {
        HHVEIP08KeyPairGenerator generator = new HHVEIP08KeyPairGenerator();
        generator.init(new HHVEIP08KeyGenerationParameters(new SecureRandom(), hveParameters));

        return generator.generateKeyPair();
    }

    protected byte[] enc(CipherParameters publicKey, int... attributesPattern) {
        byte[] attributes = HVEAttributes.attributesPatternToByte(attributesPattern) ;

        HHVEIP08AttributesEngine engine = new HHVEIP08AttributesEngine();
        engine.init(true, publicKey);

        return engine.processBlock(attributes, 0, attributes.length);
    }

    protected CipherParameters keyGen(CipherParameters privateKey, int... attributesPattern) {
        HHVEIP08SearchKeyGenerator generator = new HHVEIP08SearchKeyGenerator();
        generator.init(new HHVEIP08SearchKeyGenerationParameters((HHVEIP08PrivateKeyParameters) privateKey, attributesPattern));

        return generator.generateKey();
    }

    protected boolean test(CipherParameters searchKey, byte[] ct) {
        HHVEIP08AttributesEngine engine = new HHVEIP08AttributesEngine();
        engine.init(false, searchKey);

        return engine.processBlock(ct, 0, ct.length)[0] == 0;
    }
}

