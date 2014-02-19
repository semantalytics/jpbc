package it.unisa.dia.gas.crypto.cipher;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public interface ElementCipher {

    public void init(CipherParameters param);

    public Element processElements(Element... input) throws InvalidCipherTextException;

}
