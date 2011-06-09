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

import java.math.BigInteger;

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

        CurveParams curveParams = generateCurveParams();

        // Get the generators of the subgroups

        BigInteger n0 = curveParams.getBigInteger("n0");
        BigInteger n1 = curveParams.getBigInteger("n1");
        BigInteger n2 = curveParams.getBigInteger("n2");

        Pairing pairing = PairingFactory.getPairing(curveParams);

        Element gen = pairing.getG1().newRandomElement().getImmutable();

        // Construct Public Key and Master Secret Key

        Element g = gen.pow(n0.multiply(n2)).getImmutable();
        Element u = ElementUtil.randomIn(pairing, g).getImmutable();
        Element h = ElementUtil.randomIn(pairing, g).getImmutable();
        Element v = ElementUtil.randomIn(pairing, g).getImmutable();
        Element w = ElementUtil.randomIn(pairing, g).getImmutable();

        Element alpha = pairing.getZr().newRandomElement().getImmutable();

        Element omega = pairing.pairing(g, g).powZn(alpha).getImmutable();

        // Remove factorization from curveParams
        for (int i = 0; i < 3; i++) {
            curveParams.remove("n"+i);
        }

        // Return the keypair

        return new AsymmetricCipherKeyPair(
                new UHIBELW11PublicKeyParameters(curveParams, g, u, h, v, w, omega),
                new UHIBELW11MasterSecretKeyParameters(curveParams, alpha)
        );
    }


    private CurveParams generateCurveParams() {
        CurveGenerator curveGenerator = new TypeA1CurveGenerator(3, parameters.getBitLength());

        return (CurveParams) curveGenerator.generate();
    }
}
