package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPOT10PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPOT10SearchKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPOT10SearchKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPOT10SearchKeyGenerator {
    private IPOT10SearchKeyGenerationParameters param;
    private Pairing pairing;
    private int n;

    public void init(KeyGenerationParameters param) {
        this.param = (IPOT10SearchKeyGenerationParameters) param;
        this.n = this.param.getParameters().getParameters().getN();
        this.pairing = PairingFactory.getPairing(this.param.getParameters().getParameters().getCurveParams());
    }

    public CipherParameters generateKey() {
        IPOT10PrivateKeyParameters secretKey = param.getParameters();

        Element sigma = pairing.getZr().newRandomElement();
        Element eta = pairing.getZr().newRandomElement();

        Element k = secretKey.getBStarAt(0).mulZn(param.getYAt(0));
        for (int i = 1; i < n; i++) {
            k.add(secretKey.getBStarAt(i).mulZn(param.getYAt(i)));
        }
        k.mulZn(sigma)
                .add(secretKey.getBStarAt(n))
                .add(secretKey.getBStarAt(n + 1).mulZn(eta));

        return new IPOT10SearchKeyParameters(param.getParameters().getParameters(), k);
    }

}