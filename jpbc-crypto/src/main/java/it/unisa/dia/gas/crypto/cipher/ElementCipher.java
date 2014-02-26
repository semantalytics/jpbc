package it.unisa.dia.gas.crypto.cipher;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public interface ElementCipher {

    public ElementCipher init(CipherParameters param);

    public Element processElements(Element... input);

}
