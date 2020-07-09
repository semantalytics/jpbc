package it.unisa.dia.gas.crypto.cipher;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public interface ElementCipher {

    ElementCipher init(CipherParameters param);

    ElementCipher init(Element key);

    Element processBytes(byte[] buffer);

    Element processElements(Element... input);

    byte[] processElementsToBytes(Element... input);

}
