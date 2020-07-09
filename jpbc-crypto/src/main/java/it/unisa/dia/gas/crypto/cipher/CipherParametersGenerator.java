package it.unisa.dia.gas.crypto.cipher;

import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public interface CipherParametersGenerator {

    CipherParametersGenerator init(KeyGenerationParameters keyGenerationParameters);

    CipherParameters generateKey();

}
