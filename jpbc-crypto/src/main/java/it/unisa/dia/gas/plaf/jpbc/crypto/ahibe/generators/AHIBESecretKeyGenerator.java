package it.unisa.dia.gas.plaf.jpbc.crypto.ahibe.generators;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.crypto.engines.CipherParametersGenerator;
import it.unisa.dia.gas.plaf.jpbc.crypto.ahibe.params.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBESecretKeyGenerator implements CipherParametersGenerator {
    private AHIBESecretKeyGenerationParameters parameters;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.parameters = (AHIBESecretKeyGenerationParameters) keyGenerationParameters;
    }

    public CipherParameters generateKey() {
        AHIBEPublicKeyParameters pk = parameters.getPublicKey();
        AHIBEMasterSecretKeyParameters msk = parameters.getMasterSecretKey();
        Pairing pairing = PairingFactory.getPairing(pk.getCurveParams());

        // Generate the key
        Element r = pairing.getZr().newRandomElement();
        Element K1 = pk.getY1().powZn(r).mul(AHIBEUtils.randomIn(pairing, pk.getY3()));

        Element K2 = pairing.getG1().newOneElement();

        Element[] ids = parameters.getIds();
        Element[] us = pk.getuElements();
        for (int i = 0; i < ids.length; i++) {
            K2.mul(us[i].powZn(ids[i]));
        }

        K2.mul(msk.getX1())
          .powZn(r)
          .mul(pk.getY1().powZn(msk.getAlpha()))
          .mul(AHIBEUtils.randomIn(pairing, pk.getY3()));


        Element[] Es = new Element[us.length-ids.length];
        for (int i = 0; i < Es.length; i++) {
            Es[0] = us[ids.length + i].powZn(r).mul(AHIBEUtils.randomIn(pairing, pk.getY3()));
        }


        return new AHIBESecretKeyParameters(K1.getImmutable(),
                                            K2.getImmutable(),
                                            Es);   // TODO: Es should contains immutable elements
    }
}
