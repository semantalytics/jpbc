package it.unisa.dia.gas.jpbc.plaf.crypto.bls.generators;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.plaf.crypto.bls.params.BLSKeyGenerationParameters;
import it.unisa.dia.gas.jpbc.plaf.crypto.bls.params.BLSParameters;
import it.unisa.dia.gas.jpbc.plaf.crypto.bls.params.BLSPrivateKeyParameters;
import it.unisa.dia.gas.jpbc.plaf.crypto.bls.params.BLSPublicKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BLSKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private BLSKeyGenerationParameters param;


    public void init(KeyGenerationParameters param) {
        this.param = (BLSKeyGenerationParameters) param;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        BLSParameters parameters = param.getParameters();

        Pairing pairing = PairingFactory.getPairing(parameters.getCurveParams());
        Element g = parameters.getG();

        // Generate the secret key
        Element sk = pairing.getZr().newRandomElement();

        // Generate the corresponding public key
        Element pk = g.duplicate().powZn(sk);

        return new AsymmetricCipherKeyPair(
            new BLSPublicKeyParameters(parameters, pk),
            new BLSPrivateKeyParameters(parameters, sk)
        );
    }

}