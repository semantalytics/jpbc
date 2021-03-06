package it.unisa.dia.gas.crypto.jpbc.tor.weak.gvw13.engines;

import it.unisa.dia.gas.crypto.cipher.AbstractElementCipher;
import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.crypto.jpbc.tor.weak.gvw13.params.WTORGVW13KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.tor.weak.gvw13.params.WTORGVW13PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.tor.weak.gvw13.params.WTORGVW13RecodeParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class WTORGVW13Engine extends AbstractElementCipher {

    private CipherParameters param;
    private Pairing pairing;

    public ElementCipher init(final CipherParameters param) {
        this.param = param;

        WTORGVW13KeyParameters keyParameters = (WTORGVW13KeyParameters) param;
        pairing = PairingFactory.getPairing(keyParameters.getParameters().getParameters());

        return this;
    }


    public Element processElements(final Element... input) {
        if (param instanceof WTORGVW13PublicKeyParameters) {
            final WTORGVW13PublicKeyParameters keyParameters = (WTORGVW13PublicKeyParameters) param;

            // Read Input
            //TODO better hope there's one here
            final Element s = input[0];

            // Encode
            final Element result;
            if (keyParameters.getLevel() == 0) {
                result = keyParameters.getLeft().powZn(s);
            } else {
                result = pairing.pairing(
                        keyParameters.getParameters().getG1a(),
                        keyParameters.getRight()
                ).powZn(s);
            }

            return result;
        } else {
            WTORGVW13RecodeParameters keyParameters = (WTORGVW13RecodeParameters) param;

            //TODO better hope there are actually two here
            // Read Input
            final Element c0 = input[0];
            final Element c1 = input[1];

            // Recode
            return pairing.pairing(c0, keyParameters.getRk()).mul(c1);
       }

    }
}
