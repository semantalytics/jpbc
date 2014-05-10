package it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params;


import it.unisa.dia.gas.jpbc.Element;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BNS14EncryptionParameters extends BNS14KeyParameters {

    private BNS14PublicKeyParameters publicKey;
    private Element[] assignment;


    public BNS14EncryptionParameters(BNS14PublicKeyParameters publicKey,
                                     Element[] assignment) {
        super(false, publicKey.getParameters());

        this.publicKey = publicKey;
        this.assignment = assignment;
    }


    public BNS14PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public Element getXAt(int index) {
        return assignment[index];
    }

}
