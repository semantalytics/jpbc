package it.unisa.dia.gas.crypto.jpbc.signature.bbs.engines;

import it.unisa.dia.gas.crypto.jpbc.signature.bbs.params.BBSKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bbs.params.BBSPrivateKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bbs.params.BBSPublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.*;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class BBSSigner implements Signer {
    private BBSKeyParameters keyParameters;
    private Digest digest;

    private Pairing pairing;


    public BBSSigner(Digest digest) {
        this.digest = digest;
    }

    
    public void init(boolean forSigning, CipherParameters param) {
        if (!(param instanceof BBSKeyParameters))
            throw new IllegalArgumentException("Invalid parameters. Expected an instance of BBSKeyParameters.");

        keyParameters = (BBSKeyParameters) param;

        if (forSigning && !keyParameters.isPrivate())
            throw new IllegalArgumentException("signing requires private key");
        if (!forSigning && keyParameters.isPrivate())
            throw new IllegalArgumentException("verification requires public key");

        this.pairing = PairingFactory.getPairing(keyParameters.getParameters().getCurveParams());

        // Reset the digest
        digest.reset();
    }

    public boolean verifySignature(byte[] signature) {
        if (keyParameters == null)
            throw new IllegalStateException("BBS engine not initialised");

        BBSPublicKeyParameters publicKey = (BBSPublicKeyParameters) keyParameters;

        Element sig = pairing.getG1().newElement();
        sig.setFromBytes(signature);

        // Generate the digest
        int digestSize = digest.getDigestSize();
        byte[] hash = new byte[digestSize];
        digest.doFinal(hash, 0);

        // Map the hash of the message m to some element of G1
        Element h = pairing.getG1().newElement().setFromHash(hash, 0, hash.length);

        Element temp1 = pairing.pairing(sig, publicKey.getParameters().getG());
        Element temp2 = pairing.pairing(h, publicKey.getPk());

        return temp1.isEqual(temp2);
    }

    public byte[] generateSignature() throws CryptoException, DataLengthException {
        if (keyParameters == null)
            throw new IllegalStateException("BBS engine not initialised");

        BBSPrivateKeyParameters privateKey = (BBSPrivateKeyParameters) keyParameters;

        // Generate the digest
        int digestSize = digest.getDigestSize();
        byte[] hash = new byte[digestSize];
        digest.doFinal(hash, 0);

        // Map the hash of the message m to some element of G1
        Element h = pairing.getG1().newElement().setFromHash(hash, 0, hash.length);

        // Generate the signature
        Element sig = h.powZn(privateKey.getSk());

        return sig.toBytes();
    }

    public void reset() {
        digest.reset();
    }

    public void update(byte b) {
        digest.update(b);
    }

    public void update(byte[] in, int off, int len) {
        digest.update(in, off, len);
    }

}
