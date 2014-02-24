package it.unisa.dia.gas.plaf.jlbc.tor.gvw13.params;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class TORGVW13RecodeParameters extends TORGVW13KeyParameters {

    private ElementCipher recoder;

    public TORGVW13RecodeParameters(TORGVW13Parameters parameters, ElementCipher recoder) {
        super(true, parameters);

        this.recoder = recoder;
    }

    public ElementCipher getRecoder() {
        return recoder;
    }
}
