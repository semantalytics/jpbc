package it.unisa.dia.gas.plaf.crypto.engines;

import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface CipherParametersGenerator {

    public void init(org.bouncycastle.crypto.KeyGenerationParameters keyGenerationParameters);

    public CipherParameters generateKey();

}
