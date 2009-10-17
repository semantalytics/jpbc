package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.generators;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongMasterSecretKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongPublicParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongRPublicParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongParametersGenerator {

    private CurveParams curveParams;
    private AsymmetricCipherKeyPairGenerator rKeyPairGenerator;

    private Pairing pairing;


    public UTMAStrongParametersGenerator(AsymmetricCipherKeyPairGenerator rKeyPairGenerator) {
        this.rKeyPairGenerator = rKeyPairGenerator;
    }


    public void init(CurveParams curveParams, KeyGenerationParameters keyGenerationParameters) {
        this.curveParams = curveParams;
        this.pairing = PairingFactory.getPairing(curveParams);

        rKeyPairGenerator.init(keyGenerationParameters);
    }

    public UTMAStrongParameters generateParameters() {
        Element g = pairing.getG1().newRandomElement();
        Element g0 = pairing.getG1().newRandomElement();
        Element g1 = pairing.getG1().newRandomElement();

        Element t1 = pairing.getZr().newRandomElement();
        Element t2 = pairing.getZr().newRandomElement();
        Element t3 = pairing.getZr().newRandomElement();
        Element omega = pairing.getZr().newRandomElement();

        Element Omega = pairing.pairing(g, g).powZn(omega.duplicate().mul(t1).mul(t2).mul(t3));
        Element T1 = g.duplicate().powZn(t1);
        Element T2 = g.duplicate().powZn(t2);
        Element T3 = g.duplicate().powZn(t3);


        AsymmetricCipherKeyPair rKeyPair = rKeyPairGenerator.generateKeyPair();

        UTMAStrongPublicParameters utmaPublicParameters = new UTMAStrongPublicParameters(curveParams, g,
                                                                                         g0, g1, Omega, T1, T2, T3,
                                                                                         rKeyPair.getPublic());
        return new UTMAStrongParameters(
                utmaPublicParameters,
                new UTMAStrongRPublicParameters(rKeyPair.getPrivate()),
                new UTMAStrongMasterSecretKeyParameters(utmaPublicParameters,
                                                        t1, t2, t3, omega)
        );
    }

}