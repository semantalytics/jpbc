package it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.generators;

import it.unisa.dia.gas.crypto.jpbc.encryption.utma.bdp10.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMABDP10WeakKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private UTMABDP10WeakKeyGenerationParameters param;


    public void init(KeyGenerationParameters param) {
        this.param = (UTMABDP10WeakKeyGenerationParameters) param;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        UTMABDP10WeakParameters parameters = param.getParameters();

        UTMABDP10WeakPublicParameters publicParameters = parameters.getPublicParameters();
        UTMABDP10WeakMasterSecretKeyParameters masterSecretKeyParameters = parameters.getMasterSecretKeyParameters();

        Pairing pairing = PairingFactory.getPairing(publicParameters.getCurveParams());

        Element r = pairing.getZr().newElement().setToRandom();
        Element pk = publicParameters.getG1().powZn(r).mul(publicParameters.getG0());

        Element D0 = publicParameters.getG().powZn(
                r.duplicate().mul(masterSecretKeyParameters.getT1())
                        .mul(masterSecretKeyParameters.getT2())
                        .mul(masterSecretKeyParameters.getT3())
        );

        Element D1 = publicParameters.getG().powZn(
                masterSecretKeyParameters.getW().duplicate().mul(masterSecretKeyParameters.getT1()).mul(masterSecretKeyParameters.getT3()).negate()
        ).mul(
                pk.duplicate().powZn(
                        r.duplicate().mul(masterSecretKeyParameters.getT1()).mul(masterSecretKeyParameters.getT3()).negate()
                )
        );

        Element D2 = publicParameters.getG().powZn(
                masterSecretKeyParameters.getW().duplicate().mul(masterSecretKeyParameters.getT1()).mul(masterSecretKeyParameters.getT2()).negate()
        ).mul(
                pk.duplicate().powZn(
                        r.duplicate().mul(masterSecretKeyParameters.getT1()).mul(masterSecretKeyParameters.getT2()).negate()
                )
        );

        Element D3 = publicParameters.getG().powZn(
                masterSecretKeyParameters.getW().duplicate().mul(masterSecretKeyParameters.getT2()).mul(masterSecretKeyParameters.getT3()).negate()
        ).mul(
                pk.duplicate().powZn(
                        r.duplicate().mul(masterSecretKeyParameters.getT2()).mul(masterSecretKeyParameters.getT3()).negate()
                )
        );
        
        return new AsymmetricCipherKeyPair(
                new UTMABDP10WeakPublicKeyParameters(publicParameters, pk),
                new UTMABDP10WeakPrivateKeyParameters(publicParameters, D0, D1, D2, D3)
        );
    }

}