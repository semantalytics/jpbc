package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.generators;

import it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params.UTMAStrongMasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params.UTMAStrongParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params.UTMAStrongPublicParameters;
import it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params.UTMAStrongRPublicParameters;
import it.unisa.dia.gas.jpbc.CurveParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;
import org.bouncycastle.crypto.generators.ElGamalKeyPairGenerator;
import org.bouncycastle.crypto.params.ElGamalKeyGenerationParameters;
import org.bouncycastle.crypto.params.ElGamalParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongParametersGenerator {

    private CurveParameters curveParams;
    private AsymmetricCipherKeyPairGenerator rKeyPairGenerator;

    private Pairing pairing;


    public UTMAStrongParametersGenerator(AsymmetricCipherKeyPairGenerator rKeyPairGenerator) {
        this.rKeyPairGenerator = rKeyPairGenerator;
    }

    public UTMAStrongParametersGenerator() {
        this(new ElGamalKeyPairGenerator());
    }
    

    public void init(CurveParameters curveParams, KeyGenerationParameters keyGenerationParameters) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);

        rKeyPairGenerator.init(keyGenerationParameters);
    }

    public void init(CurveParameters curveParams, ElGamalParameters elGamalParameters) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);

        rKeyPairGenerator.init(new ElGamalKeyGenerationParameters(new SecureRandom(), elGamalParameters));
    }


    public UTMAStrongParameters generateParameters() {
        Element g = pairing.getG1().newRandomElement();
        Element g0 = pairing.getG1().newRandomElement();
        Element g1 = pairing.getG1().newRandomElement();

        // Values for the MSK
        Element t1 = pairing.getZr().newRandomElement();
        Element t2 = pairing.getZr().newRandomElement();
        Element t3 = pairing.getZr().newRandomElement();
        Element omega = pairing.getZr().newRandomElement();

        // Values for the PI
        Element T1 = g.duplicate().powZn(t1);
        Element T2 = g.duplicate().powZn(t2);
        Element T3 = g.duplicate().powZn(t3);
        Element Omega = pairing.pairing(g, g).powZn(omega.duplicate().mul(t1).mul(t2).mul(t3));

        AsymmetricCipherKeyPair rKeyPair = rKeyPairGenerator.generateKeyPair();

        UTMAStrongPublicParameters utmaPublicParameters = new UTMAStrongPublicParameters(
                curveParams,
                g.getImmutable(), g0.getImmutable(), g1.getImmutable(),
                Omega.getImmutable(),
                T1.getImmutable(), T2.getImmutable(), T3.getImmutable(),
                rKeyPair.getPublic());

        return new UTMAStrongParameters(
                utmaPublicParameters,
                new UTMAStrongRPublicParameters(rKeyPair.getPrivate()),
                new UTMAStrongMasterSecretKeyParameters(utmaPublicParameters,
                                                        t1.getImmutable(), t2.getImmutable(), t3.getImmutable(),
                                                        omega.getImmutable())
        );
    }

}