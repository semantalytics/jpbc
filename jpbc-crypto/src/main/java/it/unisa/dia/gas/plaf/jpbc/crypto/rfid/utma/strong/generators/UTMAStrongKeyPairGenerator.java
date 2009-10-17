package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.generators;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private UTMAStrongKeyGenerationParameters param;


    public void init(KeyGenerationParameters param) {
        this.param = (UTMAStrongKeyGenerationParameters) param;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        UTMAStrongParameters parameters = param.getParameters();

        UTMAStrongPublicParameters publicParameters = parameters.getPublicParameters();
        UTMAStrongMasterSecretKeyParameters mskParameters = parameters.getMasterSecretKeyParameters();

        Pairing pairing = PairingFactory.getPairing(publicParameters.getCurveParams());

        Element r = pairing.getZr().newElement().setToRandom();
        Element pk = publicParameters.getG1().powZn(r).mul(publicParameters.getG0());

        Element D0 = publicParameters.getG().powZn(
                r.duplicate().mul(mskParameters.getT1())
                        .mul(mskParameters.getT2())
                        .mul(mskParameters.getT3())
        );

        Element D1 = publicParameters.getG().powZn(
                mskParameters.getW().duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT3()).negate()
        ).mul(
                pk.duplicate().powZn(
                        r.duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT3()).negate()
                )
        );

        Element D2 = publicParameters.getG().powZn(
                mskParameters.getW().duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT2()).negate()
        ).mul(
                pk.duplicate().powZn(
                        r.duplicate().mul(mskParameters.getT1()).mul(mskParameters.getT2()).negate()
                )
        );

        Element D3 = publicParameters.getG().powZn(
                mskParameters.getW().duplicate().mul(mskParameters.getT2()).mul(mskParameters.getT3()).negate()
        ).mul(
                pk.duplicate().powZn(
                        r.duplicate().mul(mskParameters.getT2()).mul(mskParameters.getT3()).negate()
                )
        );
        
        return new AsymmetricCipherKeyPair(
                new UTMAStrongPublicKeyParameters(publicParameters, pk),
                new UTMAStrongPrivateKeyParameters(publicParameters,
                                                   D0, D1, D2, D3,
                                                   parameters.getRPublicParameters().getRPrivateKey())
        );
    }

}