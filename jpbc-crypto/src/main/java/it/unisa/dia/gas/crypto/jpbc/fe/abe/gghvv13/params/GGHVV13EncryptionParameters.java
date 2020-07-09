package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghvv13.params;


/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GGHVV13EncryptionParameters extends GGHVV13KeyParameters {

    private final GGHVV13PublicKeyParameters publicKey;
    private final String assignment;


    public GGHVV13EncryptionParameters(final GGHVV13PublicKeyParameters publicKey,
                                       final String assignment) {
        super(false, publicKey.getParameters());

        this.publicKey = publicKey;
        this.assignment = assignment;
    }


    public GGHVV13PublicKeyParameters getPublicKey() {
        return publicKey;
    }

    public String getAssignment() {
        return assignment;
    }
}
