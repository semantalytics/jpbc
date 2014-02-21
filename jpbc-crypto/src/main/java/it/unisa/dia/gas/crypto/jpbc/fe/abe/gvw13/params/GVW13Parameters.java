package it.unisa.dia.gas.crypto.jpbc.fe.abe.gvw13.params;

import it.unisa.dia.gas.crypto.cipher.CipherParametersGenerator;
import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.crypto.jpbc.tor.gvw13.generators.TORGVW13KeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.tor.gvw13.generators.TORGVW13RecKeyPairGenerator;
import it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params.TORGVW13PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params.TORGVW13ReKeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params.TORGVW13SecretKeyParameters;
import it.unisa.dia.gas.jpbc.Field;
import org.bouncycastle.crypto.AsymmetricCipherKeyPairGenerator;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13Parameters implements CipherParameters {

    private SecureRandom random;
    private int strength;
    private int ell;
    private AsymmetricCipherKeyPairGenerator torKeyPairGenerater;
    private CipherParametersGenerator torReKeyPairGenerater;
    private ElementCipher tor;

    public GVW13Parameters(SecureRandom random, int ell) {
        this.random = random;
        this.strength = 12;
        this.ell = ell;

        this.torKeyPairGenerater = new TORGVW13KeyPairGenerator();
        this.torReKeyPairGenerater = new TORGVW13RecKeyPairGenerator();
    }

    public int getEll() {
        return ell;
    }

    public KeyGenerationParameters getReKeyPairGenerationParameters(CipherParameters leftTorPK, CipherParameters leftTorSK, CipherParameters rightTorPK, CipherParameters targetTorPK) {
        return new TORGVW13ReKeyPairGenerationParameters(
                random, strength,
                null,
                (TORGVW13PublicKeyParameters) leftTorPK,
                (TORGVW13SecretKeyParameters) leftTorSK,
                (TORGVW13PublicKeyParameters) rightTorPK,
                (TORGVW13PublicKeyParameters) targetTorPK
        );
    }

    public AsymmetricCipherKeyPairGenerator getTorKeyPairGenerater() {
        return torKeyPairGenerater;
    }

    public CipherParametersGenerator getTorReKeyPairGenerater() {
        return torReKeyPairGenerater;
    }

    public Field getRandomnessField() {
        return null;
    }

    public ElementCipher getTor() {
        return tor;
    }
}