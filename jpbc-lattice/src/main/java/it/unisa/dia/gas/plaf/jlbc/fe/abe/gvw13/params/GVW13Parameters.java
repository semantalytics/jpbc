package it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params;

import it.unisa.dia.gas.crypto.cipher.CipherParametersGenerator;
import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13PublicKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13ReKeyGenerationParameters;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13SecretKeyParameters;
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
    private Field randomnessField;

    public GVW13Parameters(SecureRandom random, int strength, int ell, AsymmetricCipherKeyPairGenerator torKeyPairGenerater, CipherParametersGenerator torReKeyPairGenerater, ElementCipher tor, Field randomnessField) {
        this.random = random;
        this.strength = strength;
        this.ell = ell;

        this.torKeyPairGenerater = torKeyPairGenerater;
        this.torReKeyPairGenerater = torReKeyPairGenerater;
        this.tor = tor;
        this.randomnessField = randomnessField;
    }


    public int getEll() {
        return ell;
    }

    public KeyGenerationParameters getReKeyPairGenerationParameters(CipherParameters leftTorPK, CipherParameters leftTorSK, CipherParameters rightTorPK, CipherParameters targetTorPK) {
        return new TORGVW13ReKeyGenerationParameters(
                random, strength,
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
        return randomnessField;
    }

    public ElementCipher getTor() {
        return tor;
    }
}