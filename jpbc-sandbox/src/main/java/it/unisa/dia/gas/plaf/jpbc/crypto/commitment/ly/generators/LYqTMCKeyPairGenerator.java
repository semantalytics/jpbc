package it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.generators;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.params.LYqTMCKeyGenerationParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.params.LYqTMCParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.params.LYqTMCPrivateKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.params.LYqTMCPublicKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class LYqTMCKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private LYqTMCKeyGenerationParameters param;


    public void init(KeyGenerationParameters param) {
        this.param = (LYqTMCKeyGenerationParameters) param;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        LYqTMCParameters parameters = param.getParameters();

        Pairing pairing = PairingFactory.getPairing(parameters.getCurveParams());
        int q = param.getQ();
        Element g = pairing.getG1().newRandomElement();
        Element alpha = pairing.getZr().newRandomElement();

        ElementPowPreProcessing gp = g.pow();
        ElementPowPreProcessing alphap = alpha.pow();

        List<Element> pks = new ArrayList<Element>();
        Element sk = null;

        pks.add(0, g.getImmutable());
        for (int i = 1; i <= (2 * q); i++) {
            Element ai = alphap.powZn(pairing.getZr().newElement(i));
            Element gi = gp.powZn(ai);
            if (i == (q + 1)) {
                sk = gi.duplicate();
                pks.add(i, pairing.getG1().newZeroElement().setToZero().getImmutable());
            } else {
                pks.add(i, gi.getImmutable());
            }
        }

        return new AsymmetricCipherKeyPair(
            new LYqTMCPublicKeyParameters(parameters, pks),
            new LYqTMCPrivateKeyParameters(parameters, sk)
        );
    }

}