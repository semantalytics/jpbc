package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10PublicKeyParameters;
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
public class AHIBEDIP10KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private AHIBEDIP10KeyPairGenerationParameters parameters;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.parameters = (AHIBEDIP10KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        // Generate curve parameters
        CurveParams curveParameters;
        Pairing pairing;
        Element gen1, gen3, gen4;

        while (true) {
            curveParameters = generateCurveParameters();
            pairing = PairingFactory.getPairing(curveParameters);

            Element generator = pairing.getG1().newRandomElement().getImmutable();
            gen1 = ElementUtil.getGenerator(pairing, generator, curveParameters, 0, 4).getImmutable();

            if (!pairing.pairing(gen1, gen1).isOne()) {
                gen3 = ElementUtil.getGenerator(pairing, generator, curveParameters, 2, 4).getImmutable();
                gen4 = ElementUtil.getGenerator(pairing, generator, curveParameters, 3, 4).getImmutable();
                break;
            }
        }

        // Construct Public Key and Master Secret Key
        Element Y1 = ElementUtil.randomIn(pairing, gen1).getImmutable();
        Element X1 = ElementUtil.randomIn(pairing, gen1).getImmutable();

        Element[] uElements = new Element[parameters.getLength()];
        for (int i = 0; i < uElements.length; i++) {
            uElements[i] = ElementUtil.randomIn(pairing, gen1).getImmutable();
        }

        Element Y3 = ElementUtil.randomIn(pairing, gen3).getImmutable();

        Element X4 = ElementUtil.randomIn(pairing, gen4).getImmutable();
        Element Y4 = ElementUtil.randomIn(pairing, gen4).getImmutable();

        Element alpha = pairing.getZr().newRandomElement().getImmutable();

        // Remove factorization from curveParameters
        curveParameters.remove("n0");
        curveParameters.remove("n1");
        curveParameters.remove("n2");
        curveParameters.remove("n3");

        // Return the keypair

        return new AsymmetricCipherKeyPair(
                new AHIBEDIP10PublicKeyParameters(
                        curveParameters,
                        Y1, Y3, Y4,
                        X1.mul(X4),
                        uElements,
                        pairing.pairing(Y1, Y1).powZn(alpha).getImmutable()
                ),
                new AHIBEDIP10MasterSecretKeyParameters(
                        curveParameters,
                        X1, alpha
                )
        );
    }


    private CurveParams generateCurveParameters() {
        CurveGenerator curveGenerator = new TypeA1CurveGenerator(4, parameters.getBitLength());

        return (CurveParams) curveGenerator.generate();
    }
}
