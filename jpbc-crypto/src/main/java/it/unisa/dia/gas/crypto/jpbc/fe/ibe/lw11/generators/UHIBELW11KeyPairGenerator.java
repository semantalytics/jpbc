package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBELW11PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
import it.unisa.dia.gas.jpbc.CurveGenerator;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.a1.TypeA1CurveGenerator;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBELW11KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private UHIBELW11KeyPairGenerationParameters parameters;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.parameters = (UHIBELW11KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        // Generate curve parameters
        CurveParams curveParameters = generateCurveParams();

        // Get the generators of the subgroups
        Pairing pairing = PairingFactory.getPairing(curveParameters);

        Element g = pairing.getG1().newElement();
        g.setFromBytes(curveParameters.getBytes("gen1"));

        // Generate required elements
        Element u = ElementUtil.randomIn(pairing, g).getImmutable();
        Element h = ElementUtil.randomIn(pairing, g).getImmutable();
        Element v = ElementUtil.randomIn(pairing, g).getImmutable();
        Element w = ElementUtil.randomIn(pairing, g).getImmutable();

        Element alpha = pairing.getZr().newRandomElement().getImmutable();

        Element omega = pairing.pairing(g, g).powZn(alpha).getImmutable();

        // Remove factorization from curveParams
        curveParameters.remove("gen2");
        curveParameters.remove("gen3");

        // Return the keypair

        return new AsymmetricCipherKeyPair(
                new UHIBELW11PublicKeyParameters(curveParameters, g, u, h, v, w, omega),
                new UHIBELW11MasterSecretKeyParameters(curveParameters, alpha)
        );
    }


    private CurveParams generateCurveParams() {
        CurveGenerator curveGenerator = new TypeA1CurveGenerator(3, parameters.getBitLength());

        return (CurveParams) curveGenerator.generate();
    }
}
