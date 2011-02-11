package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10SetupGenerationParameters;
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
public class AHIBEDIP10SetupGenerator implements AsymmetricCipherKeyPairGenerator {
    private AHIBEDIP10SetupGenerationParameters ahibeSetupGenerationParameters;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.ahibeSetupGenerationParameters = (AHIBEDIP10SetupGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        // Generate curve parameters

        CurveParams curveParams = generateCurveParams();

        // Get the generators of the subgroups

        BigInteger n0 = curveParams.getBigInteger("n0");
        BigInteger n1 = curveParams.getBigInteger("n1");
        BigInteger n2 = curveParams.getBigInteger("n2");
        BigInteger n3 = curveParams.getBigInteger("n3");

        Pairing pairing = PairingFactory.getPairing(curveParams);

        Element gen = pairing.getG1().newRandomElement().getImmutable();
        Element gen1 = gen.pow(n1.multiply(n2).multiply(n3)).getImmutable();
        Element gen3 = gen.pow(n0.multiply(n1).multiply(n3)).getImmutable();
        Element gen4 = gen.pow(n0.multiply(n1).multiply(n2)).getImmutable();

        // Construct Publick Key and Master Secret Key

        Element Y1 = gen1.powZn(pairing.getZr().newRandomElement()).getImmutable();
        Element X1 = gen1.powZn(pairing.getZr().newRandomElement()).getImmutable();

        Element[] uElements = new Element[ahibeSetupGenerationParameters.getLength()];
        for (int i = 0; i < uElements.length; i++) {
            uElements[i] = gen1.powZn(pairing.getZr().newRandomElement()).getImmutable();
        }

        Element Y3 = gen3.powZn(pairing.getZr().newRandomElement()).getImmutable();

        Element X4 = gen4.powZn(pairing.getZr().newRandomElement()).getImmutable();
        Element Y4 = gen4.powZn(pairing.getZr().newRandomElement()).getImmutable();

        Element alpha = pairing.getZr().newRandomElement().getImmutable();

        // Remove factorization from curveParams
        for (int i = 0; i < 4; i++) {
            curveParams.remove("n"+i);
        }

        // Return the keypair

        return new AsymmetricCipherKeyPair(
                new AHIBEDIP10PublicKeyParameters(
                        curveParams, Y1, Y3, Y4,
                        X1.mul(X4),
                        uElements,
                        pairing.pairing(Y1,Y1).powZn(alpha).getImmutable()
                ),
                new AHIBEDIP10MasterSecretKeyParameters(
                        X1, alpha
                )
        );
    }


    private CurveParams generateCurveParams() {
        CurveGenerator curveGenerator = new TypeA1CurveGenerator(4, ahibeSetupGenerationParameters.getBitLength());

        return (CurveParams) curveGenerator.generate();
    }
}
