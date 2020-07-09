package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHSW13EncryptionParameters extends GGHSW13KeyParameters {

    private final GGHSW13PublicKeyParameters publicKey;
    private final String assignment;


    public GGHSW13EncryptionParameters(final GGHSW13PublicKeyParameters publicKey,
                                       final String assignment) {
        super(false, publicKey.getParameters());

        this.publicKey = publicKey;
        this.assignment = assignment;
    }


    public GGHSW13PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public String getAssignment() {
        return assignment;
    }
}
