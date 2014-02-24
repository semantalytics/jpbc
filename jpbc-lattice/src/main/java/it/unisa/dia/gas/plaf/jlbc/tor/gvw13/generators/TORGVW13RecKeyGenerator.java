package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.generators;

import it.unisa.dia.gas.crypto.cipher.CipherParametersGenerator;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13ReKeyGenerationParameters;
import it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params.TORGVW13RecodeParameters;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13RecKeyGenerator implements CipherParametersGenerator {
    private TORGVW13ReKeyGenerationParameters params;


    public CipherParametersGenerator init(KeyGenerationParameters keyGenerationParameters) {
        this.params = (TORGVW13ReKeyGenerationParameters) keyGenerationParameters;

        return this;
    }

    public CipherParameters generateKey() {
        return new TORGVW13RecodeParameters(params.getParameters(), null);
    }


}
