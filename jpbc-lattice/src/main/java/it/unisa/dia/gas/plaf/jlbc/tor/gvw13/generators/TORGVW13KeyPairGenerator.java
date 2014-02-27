package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.generators;


import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.engines.MP12HLP2OneWayFunction;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.generators.MP12HLP2KeyPairGenerator;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12HLP2KeyPairGenerationParameters;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12HLP2OneWayFunctionParameters;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.lattice.trapdoor.mp12.params.MP12Parameters;
import it.unisa.dia.gas.plaf.jlbc.sampler.ZGaussianSampler;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13KeyPairGenerationParameters;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13PublicKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13SecretKeyParameters;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {
    private TORGVW13KeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (TORGVW13KeyPairGenerationParameters) keyGenerationParameters;
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        MP12HLP2KeyPairGenerator gen = new MP12HLP2KeyPairGenerator();
        gen.init(new MP12HLP2KeyPairGenerationParameters(
                params.getRandom(),
                new MP12Parameters(params.getRandom(), params.getParameters().getN()),
                12,
                new ZGaussianSampler(100, params.getRandom(), 4)
        ));
        AsymmetricCipherKeyPair keyPair = gen.generateKeyPair();

        ElementCipher owf = new MP12HLP2OneWayFunction();
        MP12HLP2OneWayFunctionParameters owfParams = new MP12HLP2OneWayFunctionParameters(
                (MP12HLP2PublicKeyParameters) keyPair.getPublic(),
                new ZGaussianSampler(100, params.getRandom(), 4)
        );
        owf.init(owfParams);

        return new AsymmetricCipherKeyPair(
                new TORGVW13PublicKeyParameters(
                        params.getParameters(),
                        keyPair.getPublic(),
                        owf,
                        owfParams.getInputField(),
                        owfParams.getOutputField()
                ),
                new TORGVW13SecretKeyParameters(
                        params.getParameters(),
                        keyPair.getPrivate(),
                        owfParams.getOutputField())
        );
    }

}
