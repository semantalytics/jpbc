package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params;

import org.bouncycastle.crypto.KeyGenerationParameters;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13ReKeyPairGenerationParameters extends KeyGenerationParameters {

    private TORGVW13Parameters params;

    private TORGVW13PublicKeyParameters leftPk;
    private TORGVW13SecretKeyParameters leftSk;

    private TORGVW13PublicKeyParameters rightPk;

    private TORGVW13PublicKeyParameters targetPk;


    public TORGVW13ReKeyPairGenerationParameters(SecureRandom random, int strength, TORGVW13Parameters params,
                                                 TORGVW13PublicKeyParameters leftPk,
                                                 TORGVW13SecretKeyParameters leftSk,
                                                 TORGVW13PublicKeyParameters rightPk,
                                                 TORGVW13PublicKeyParameters targetPk) {
        super(random, strength);

        this.params = params;

        this.leftPk = leftPk;
        this.leftSk = leftSk;

        this.rightPk = rightPk;

        this.targetPk = targetPk;
    }

    public TORGVW13Parameters getParameters() {
        return params;
    }


    public TORGVW13Parameters getParams() {
        return params;
    }

    public TORGVW13PublicKeyParameters getLeftPk() {
        return leftPk;
    }

    public TORGVW13SecretKeyParameters getLeftSk() {
        return leftSk;
    }

    public TORGVW13PublicKeyParameters getRightPk() {
        return rightPk;
    }

    public TORGVW13PublicKeyParameters getTargetPk() {
        return targetPk;
    }
}
