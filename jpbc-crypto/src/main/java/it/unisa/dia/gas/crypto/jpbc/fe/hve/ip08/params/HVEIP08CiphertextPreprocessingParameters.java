package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08CiphertextPreprocessingParameters extends HVEIP08KeyParameters {

    private HVEIP08PublicKeyParameters publicKey;


    public HVEIP08CiphertextPreprocessingParameters(HVEIP08PublicKeyParameters publicKey) {
        super(false, publicKey.getParameters());
        this.publicKey = publicKey;
    }


    public HVEIP08PublicKeyParameters getPublicKey() {
        return publicKey;
    }

}
