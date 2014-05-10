package it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.generators;

import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params.BNS14KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params.BNS14MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params.BNS14Parameters;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params.BNS14PublicKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.generators.MP12HLP2KeyPairGenerator;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.generators.MP12PLP2KeyPairGenerator;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.*;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BNS14KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private BNS14KeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (BNS14KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        BNS14Parameters parameters = params.getParameters();

        // Generate trapdoor
        MP12HLP2KeyPairGenerator gen = new MP12HLP2KeyPairGenerator();
        gen.init(new MP12HLP2KeyPairGenerationParameters(
                parameters.getRandom(),
                parameters.getN(),
                parameters.getK()
        ));
        AsymmetricCipherKeyPair keyPair = gen.generateKeyPair();
        MP12HLP2PublicKeyParameters latticePk = (MP12HLP2PublicKeyParameters) keyPair.getPublic();

        // Generate primitive trapdoor
        MP12PLP2KeyPairGenerator primitiveGen = new MP12PLP2KeyPairGenerator();
        primitiveGen.init(new MP12PLP2KeyPairGenerationParameters(
                parameters.getRandom(),
                parameters.getN(),
                parameters.getK(),
                latticePk.getM() - (parameters.getN() * parameters.getK())
        ));
        AsymmetricCipherKeyPair primitiveKeyPair = primitiveGen.generateKeyPair();
        MP12PLP2PublicKeyParameters primitiveLatticePk = (MP12PLP2PublicKeyParameters) primitiveKeyPair.getPublic();

        // generate public matrices
        Element D = latticePk.getA().getField().newRandomElement();

        Element[] Bs = new Element[parameters.getEll()];
        for (int i = 0, ell = parameters.getEll(); i < ell; i++) {
            Bs[i] = latticePk.getA().getField().newRandomElement();
        }

        // Return the keypair
        return new AsymmetricCipherKeyPair(
                new BNS14PublicKeyParameters(parameters, latticePk, primitiveLatticePk, D, Bs),
                new BNS14MasterSecretKeyParameters(
                        parameters,
                        (MP12HLP2PrivateKeyParameters) keyPair.getPrivate()
                )
        );
    }


}
