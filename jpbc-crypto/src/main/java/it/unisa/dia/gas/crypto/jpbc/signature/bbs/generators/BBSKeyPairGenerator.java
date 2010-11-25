package it.unisa.dia.gas.crypto.jpbc.signature.bbs.generators;

import it.unisa.dia.gas.crypto.jpbc.signature.bbs.params.BBSKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bbs.params.BBSParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;


/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BBSKeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private BBSKeyGenerationParameters param;


    public void init(KeyGenerationParameters param) {
        this.param = (BBSKeyGenerationParameters) param;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        BBSParameters parameters = param.getParameters();

//        Pairing pairing = PairingFactory.getPairing(parameters.getCurveParams());
//
//        int i;
//
//        Element g1 = pairing.getG1().newRandomElement();
//        Element g2 = pairing.getG2().newRandomElement();
//        Element h = pairing.getG1().newRandomElement();
//        Element u = pairing.getG1().newRandomElement();
//        Element v = pairing.getG1().newRandomElement();
//        Element w = pairing.getG1().newRandomElement();
//
//        Element xi1 = pairing.getZr().newRandomElement();
//        Element xi2 = pairing.getZr().newRandomElement();
//        Element gamma = pairing.getZr().newRandomElement();
//
//
//        Element z0 = xi1.duplicate().invert();
//        u = h.duplicate().powZn(z0);
//        z0.set(xi2).invert();
//        v = h.duplicate().powZn(z0);
//        w = g2.duplicate().powZn(gamma);
//
//        for (i = 0; i < param.getN(); i++) {
//            gsk[i] - > param = param;
//            element_init_G1(gsk[i] - > A, pairing);
//            element_init_Zr(gsk[i] - > x, pairing);
//
//            element_random(gsk[i] - > x);
//            element_add(z0, gamma, gsk[i] - > x);
//            element_invert(z0, z0);
//            element_pow_zn(gsk[i] - > A, gpk - > g1, z0);
//
//            /* do some precomputation */
//            /* TODO: could instead compute from e(g1,g2) ... */
//            element_init_GT(gsk[i] - > pr_A_g2, pairing);
//            pairing_apply(gsk[i] - > pr_A_g2, gsk[i] - > A, gpk - > g2, pairing);
//        }
//
//
//        /* do some precomputation */
//        element_init_GT(gpk - > pr_g1_g2, pairing);
//        element_init_GT(gpk - > pr_g1_g2_inv, pairing);
//        element_init_GT(gpk - > pr_h_g2, pairing);
//        element_init_GT(gpk - > pr_h_w, pairing);
//        pairing_apply(gpk - > pr_g1_g2, gpk - > g1, gpk - > g2, pairing);
//        element_invert(gpk - > pr_g1_g2_inv, gpk - > pr_g1_g2);
//        pairing_apply(gpk - > pr_h_g2, gpk - > h, gpk - > g2, pairing);
//        pairing_apply(gpk - > pr_h_w, gpk - > h, gpk - > w, pairing);
//
//
//        return new AsymmetricCipherKeyPair(
//                new BBSPublicKeyParameters(parameters, pk.getImmutable()),
//                new BBSPrivateKeyParameters(parameters, sk.getImmutable())
//        );
        return null;
    }

}