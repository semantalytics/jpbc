package it.unisa.dia.gas.crypto.jpbc.signature.bls01.engines;

import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.signature.bls01.params.BLS01PublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.*;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BLS01HalfSigner implements Signer {

    private BLS01KeyParameters keyParameters;
    private final Digest digest;
    private Pairing pairing;

    public BLS01HalfSigner(final Digest digest) {
        this.digest = digest;
    }

    @Override
    public void init(final boolean forSigning, final CipherParameters param) {
        if (!(param instanceof BLS01KeyParameters)) {
            throw new IllegalArgumentException("Invalid parameters. Expected an instance of BLS01KeyParameters.");
        }

        keyParameters = (BLS01KeyParameters) param;

        if (forSigning && !keyParameters.isPrivate()) {
            throw new IllegalArgumentException("signing requires private key");
        }
        if (!forSigning && keyParameters.isPrivate()) {
            throw new IllegalArgumentException("verification requires public key");
        }

        this.pairing = PairingFactory.getPairing(keyParameters.getParameters().getParameters());

        digest.reset();
    }

    @Override
    public boolean verifySignature(byte[] signature) {
        if (keyParameters == null) {
            throw new IllegalStateException("BLS engine not initialised");
        }

        final BLS01PublicKeyParameters publicKey = (BLS01PublicKeyParameters) keyParameters;

        final Point sig = (Point) pairing.getG1().newElement();
        sig.setFromBytesX(signature);

        // Generate the digest
        final int digestSize = digest.getDigestSize();
        final byte[] hash = new byte[digestSize];
        final int OUT_OFFSET = 0;
        digest.doFinal(hash, OUT_OFFSET);

        // Map the hash of the message m to some element of G1
        final Element h = pairing.getG1().newElementFromHash(hash, 0, hash.length);

        final Element temp1 = pairing.pairing(sig, publicKey.getParameters().getG());
        final Element temp2 = pairing.pairing(h, publicKey.getPk());

        return temp1.isEqual(temp2) || temp1.invert().isEqual(temp2);
    }

    @Override
    public byte[] generateSignature() throws CryptoException, DataLengthException {
        if (keyParameters == null) {
            throw new IllegalStateException("BLS engine not initialised");
        }

        final BLS01PrivateKeyParameters privateKey = (BLS01PrivateKeyParameters) keyParameters;

        // Generate the digest
        final int digestSize = digest.getDigestSize();
        final byte[] hash = new byte[digestSize];
        digest.doFinal(hash, 0);

        // Map the hash of the message m to some element of G1
        final Element h = pairing.getG1().newElementFromHash(hash, 0, hash.length);

        // Generate the signature
        final Point sig = (Point) h.powZn(privateKey.getSk());

        return sig.toBytesX();
    }

    @Override
    public void reset() {
        digest.reset();
    }

    @Override
    public void update(byte b) {
        digest.update(b);
    }

    @Override
    public void update(byte[] in, int off, int len) {
        digest.update(in, off, len);
    }

}
