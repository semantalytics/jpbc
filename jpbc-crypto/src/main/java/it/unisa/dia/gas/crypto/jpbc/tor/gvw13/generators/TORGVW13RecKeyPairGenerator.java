package it.unisa.dia.gas.crypto.jpbc.tor.gvw13.generators;

import it.unisa.dia.gas.crypto.cipher.CipherParametersGenerator;
import it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params.TORGVW13ReKeyPairGenerationParameters;
import it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params.TORGVW13RecodeParameters;
import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class TORGVW13RecKeyPairGenerator implements CipherParametersGenerator {
    private TORGVW13ReKeyPairGenerationParameters params;


    public void init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (TORGVW13ReKeyPairGenerationParameters) keyGenerationParameters;
    }

    public CipherParameters generateKey() {
        Element rk = params.getTargetPk().getRight().mul(
                params.getRightPk().getRight()
        ).powZn(params.getLeftSk().getT()).getImmutable();

        return new TORGVW13RecodeParameters(params.getParameters(), rk);
    }


}
