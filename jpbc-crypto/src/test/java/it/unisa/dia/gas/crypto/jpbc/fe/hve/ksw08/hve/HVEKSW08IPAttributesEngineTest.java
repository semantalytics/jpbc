package it.unisa.dia.gas.crypto.jpbc.fe.ip.ksw08.hve;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.generators.HVEIP08SearchKeyGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SearchKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators.IPLOSTW10ParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10KeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10Parameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import junit.framework.TestCase;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro
 */
public class HVEKSW08IPAttributesEngineTest extends TestCase {

    public void testHVE() {
//        AsymmetricCipherKeyPair keyPair = setup(createParameters(6));
//
//        assertEquals(true,
//                test(
//                        keyGen(keyPair.getPrivate(), 0, 7, -1, 3, -1, 1),
//                        enc(keyPair.getPublic(),     0, 7,  0, 3,  2, 1)
//                )
//        );
//
//        assertEquals(false,
//                test(
//                        keyGen(keyPair.getPrivate(), 0, 6, 0, 3, 2, 1),
//                        enc(keyPair.getPublic(),     0, 7, 0, 3, 2, 1)
//                )
//        );
//
//        assertEquals(false,
//                test(
//                        keyGen(keyPair.getPrivate(), 0, 5, -1, 3, -1, 1),
//                        enc(keyPair.getPublic(),     0, 7,  0, 3,  2, 1)
//                )
//        );
//
//        assertEquals(true,
//                test(
//                        keyGen(keyPair.getPrivate(), 0, 7, -1, 3, -1, 1),
//                        enc(keyPair.getPublic(),     0, 7,  0, 3,  2, 1)
//                )
//        );
//
//        assertEquals(true,
//                test(
//                        keyGen(keyPair.getPrivate(), -1, -1, -1, -1, -1, -1),
//                        enc(keyPair.getPublic(),     0, 7,  0, 3,  2, 1)
//                )
//        );
    }


    protected CipherParameters createParameters(int n) {
        return new IPLOSTW10ParametersGenerator().init(
                new CurveParams().load(this.getClass().getClassLoader().getResourceAsStream("it/unisa/dia/gas/plaf/jpbc/crypto/a_181_603.properties")),
                2 * n
        ).generateParameters();
    }

    protected AsymmetricCipherKeyPair setup(CipherParameters parameters) {
        IPLOSTW10KeyPairGenerator setup = new IPLOSTW10KeyPairGenerator();
        setup.init(new IPLOSTW10KeyGenerationParameters(
                new SecureRandom(),
                (IPLOSTW10Parameters) parameters
        ));

        return setup.generateKeyPair();
    }

    protected byte[] enc(CipherParameters publicKey, int... attributes) {
        return null;
    }

    protected CipherParameters keyGen(CipherParameters privateKey, int... pattern) {
        HVEIP08SearchKeyGenerator generator = new HVEIP08SearchKeyGenerator();
        generator.init(new HVEIP08SearchKeyGenerationParameters(
                (HVEIP08PrivateKeyParameters) privateKey, pattern)
        );

        return generator.generateKey();
    }

    protected boolean test(CipherParameters searchKey, byte[] ct) {
        return false;
    }
}

