package it.unisa.dia.gas.crypto.jlbc.tor.gvw13.generators;


import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.crypto.jlbc.tor.gvw13.params.TORGVW13KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jlbc.tor.gvw13.params.TORGVW13PublicKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.tor.gvw13.params.TORGVW13SecretKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines.MP12HLP2ErrorTolerantOneTimePad;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines.MP12HLP2OneWayFunction;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.generators.MP12HLP2KeyPairGenerator;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2KeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2OneWayFunctionParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Field;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13KeyPairGenerator implements AsymmetricCipherKeyPairGenerator {

    private TORGVW13KeyPairGenerationParameters params;
    private MP12HLP2KeyPairGenerator gen;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (TORGVW13KeyPairGenerationParameters) keyGenerationParameters;

        // Init Lattice generator
        gen = new MP12HLP2KeyPairGenerator();
        gen.init(new MP12HLP2KeyPairGenerationParameters(
                params.getRandom(),
                params.getParameters().getN(),
                32
        ));
    }

    public AsymmetricCipherKeyPair generateKeyPair() {
        AsymmetricCipherKeyPair keyPair = gen.generateKeyPair();

        // One-way function
        ElementCipher owf = new MP12HLP2OneWayFunction();
        MP12HLP2OneWayFunctionParameters owfParams = new MP12HLP2OneWayFunctionParameters(
                (MP12HLP2PublicKeyParameters) keyPair.getPublic()
        );
        owf.init(owfParams);

        // error-tolerant version of the one-time pad
        ElementCipher otp = new MP12HLP2ErrorTolerantOneTimePad();

        return new AsymmetricCipherKeyPair(
                new TORGVW13PublicKeyParameters(
                        params.getParameters(),
                        keyPair.getPublic(),
                        owf,
                        gen.getInputField(),
                        gen.getOutputField(),
                        otp
                ),
                new TORGVW13SecretKeyParameters(
                        params.getParameters(),
                        keyPair.getPrivate(),
                        gen.getOutputField())
        );
    }


    public int getKeyLengthInBytes() {
        return gen.getMInBytes();
    }

    public Field getOwfInputField() {
        return gen.getInputField();
    }
}
