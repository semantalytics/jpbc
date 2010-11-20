package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators;

import it.unisa.dia.gas.crypto.engines.CipherParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class AHIBESecretKeyGenerator implements CipherParametersGenerator {
    private KeyGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = keyGenerationParameters;
    }

    public CipherParameters generateKey() {
        if (params instanceof AHIBESecretKeyGenerationParameters) {
            AHIBESecretKeyGenerationParameters parameters = (AHIBESecretKeyGenerationParameters) params;

            AHIBEPublicKeyParameters pk = parameters.getPublicKey();
            AHIBEMasterSecretKeyParameters msk = parameters.getMasterSecretKey();
            Pairing pairing = PairingFactory.getPairing(pk.getCurveParams());

            // Generate the key
            Element r1 = pairing.getZr().newRandomElement();
            Element r2 = pairing.getZr().newRandomElement();

            // K11
            Element K11 = pk.getY1().powZn(r1).mul(AHIBEUtils.randomIn(pairing, pk.getY3()));

            // K12
            Element K12 = pairing.getG1().newOneElement();
            Element[] ids = parameters.getIds();
            Element[] us = pk.getuElements();
            for (int i = 0; i < ids.length; i++) {
                K12.mul(us[i].powZn(ids[i]));
            }
            K12.mul(msk.getX1())
              .powZn(r1)
              .mul(pk.getY1().powZn(msk.getAlpha()))
              .mul(AHIBEUtils.randomIn(pairing, pk.getY3()));

            // E1s
            Element[] E1s = new Element[us.length-ids.length];
            for (int i = 0; i < E1s.length; i++) {
                E1s[i] = us[ids.length + i].powZn(r1).mul(AHIBEUtils.randomIn(pairing, pk.getY3())).getImmutable();
            }

            // K21
            Element K21 = pk.getY1().powZn(r2).mul(AHIBEUtils.randomIn(pairing, pk.getY3()));

            // K22
            Element K22 = pairing.getG1().newOneElement();
            for (int i = 0; i < ids.length; i++) {
                K22.mul(us[i].powZn(ids[i]));
            }
            K22.mul(msk.getX1())
              .powZn(r2)
              .mul(AHIBEUtils.randomIn(pairing, pk.getY3()));

            // E2s
            Element[] E2s = new Element[us.length-ids.length];
            for (int i = 0; i < E2s.length; i++) {
                E2s[i] = us[ids.length + i].powZn(r2).mul(AHIBEUtils.randomIn(pairing, pk.getY3())).getImmutable();
            }

            return new AHIBESecretKeyParameters(pk.getCurveParams(),
                                                K11.getImmutable(), K12.getImmutable(), E1s,
                                                K21.getImmutable(), K22.getImmutable(), E2s,
                                                ids);
        } else if (params instanceof AHIBEDelegateSecretKeyGenerationParameters) {
            AHIBEDelegateSecretKeyGenerationParameters parameters = (AHIBEDelegateSecretKeyGenerationParameters) params;

            AHIBEPublicKeyParameters pk = parameters.getPublicKey();
            AHIBESecretKeyParameters sk = parameters.getSecretKey();
            Pairing pairing = PairingFactory.getPairing(pk.getCurveParams());

            // Generate the key
            Element r1 = pairing.getZr().newRandomElement();
            Element r2 = pairing.getZr().newRandomElement();

            // K11
            Element K11 = sk.getK11()
                    .mul(sk.getK21().powZn(r1))
                    .mul(AHIBEUtils.randomIn(pairing, pk.getY3()));

            // K12
            Element K12 = sk.getK12()
                    .mul(sk.getK22().powZn(r1))
                    .mul(sk.getE1s()[0].powZn(parameters.getId()))
                    .mul(sk.getE2s()[0].powZn(r1.duplicate().mul(parameters.getId())))
                    .mul(AHIBEUtils.randomIn(pairing, pk.getY3()));

            // E1s
            Element[] E1s = new Element[sk.getE1s().length - 1];
            for (int i = 0; i < E1s.length - 1; i++) {
                E1s[i] = sk.getE1s()[i+1]
                        .mul(sk.getE2s()[i+1].powZn(r1))
                        .mul(AHIBEUtils.randomIn(pairing, pk.getY3())).getImmutable();
            }

            // K21
            Element K21 = sk.getK21().powZn(r2)
                    .mul(AHIBEUtils.randomIn(pairing, pk.getY3()));

            // K12
            Element K22 = sk.getK22().powZn(r2)
                    .mul(sk.getE2s()[0].powZn(r2.duplicate().mul(parameters.getId())))
                    .mul(AHIBEUtils.randomIn(pairing, pk.getY3()));

            // E1s
            Element[] E2s = new Element[sk.getE1s().length - 1];
            for (int i = 0; i < E1s.length - 1; i++) {
                E2s[i] = sk.getE2s()[i+1]
                        .powZn(r2)
                        .mul(AHIBEUtils.randomIn(pairing, pk.getY3())).getImmutable();
            }

            Element[] ids = new Element[sk.getIds().length + 1];
            System.arraycopy(sk.getIds(), 0, ids, 0, sk.getIds().length);
            ids[sk.getIds().length] = parameters.getId();

            return new AHIBESecretKeyParameters(sk.getCurveParams(),
                                                K11.getImmutable(), K12.getImmutable(), E1s,
                                                K21.getImmutable(), K22.getImmutable(), E2s,
                                                ids);
        }

        throw new IllegalStateException("AHIBESecretKeyGenerator not initialized correctly.");
    }
}
