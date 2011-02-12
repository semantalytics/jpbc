package it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.engines;

import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ip.lostw10.params.IPLOSTW10SearchKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.product.ProductPairing;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithRandom;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPLOSTW10AttributesEngine implements AsymmetricBlockCipher {

    private IPLOSTW10KeyParameters key;
    private boolean forEncryption;

    private int n;
    private int N;
    private int inLen, outLen;
    private Pairing pairing;
    private Pairing productPairing;

    /**
     * initialise the HVE engine.
     *
     * @param forEncryption true if we are encrypting, false otherwise.
     * @param param         the necessary HVE key parameters.
     */
    public void init(boolean forEncryption, CipherParameters param) {
        if (param instanceof ParametersWithRandom) {
            ParametersWithRandom p = (ParametersWithRandom) param;

            this.key = (IPLOSTW10KeyParameters) p.getParameters();
        } else {
            this.key = (IPLOSTW10KeyParameters) param;
        }

        this.forEncryption = forEncryption;
        if (forEncryption) {
            if (!(key instanceof IPLOSTW10PublicKeyParameters)) {
                throw new IllegalArgumentException("IPLOSTW10PublicKeyParameters are required for encryption.");
            }
        } else {
            if (!(key instanceof IPLOSTW10SearchKeyParameters)) {
                throw new IllegalArgumentException("IPLOSTW10SearchKeyParameters are required for decryption.");
            }
        }

        this.n = key.getParameters().getN();
        this.N = (2 * n + 3);
        this.pairing = PairingFactory.getPairing(key.getParameters().getCurveParams());
        this.productPairing = new ProductPairing(null, pairing, N);

        this.inLen = n * pairing.getZr().getLengthInBytes();
        this.outLen = N * pairing.getG1().getLengthInBytes();
    }

    /**
     * Return the maximum size for an input block to this engine.
     * For HVE this is always one byte less than the size of P on
     * encryption, and twice the length as the size of P on decryption.
     *
     * @return maximum size for an input block.
     */
    public int getInputBlockSize() {
        if (forEncryption) {
            return inLen;
        }

        return outLen;
    }

    /**
     * Return the maximum size for an output block to this engine.
     * For HVE this is always one byte less than the size of P on
     * decryption, and twice the length as the size of P on encryption.
     *
     * @return maximum size for an output block.
     */
    public int getOutputBlockSize() {
        if (forEncryption) {
            return outLen;
        }

        return 1;
    }

    /**
     * Process a single block using the basic HVE algorithm.
     *
     * @param in    the input array.
     * @param inOff the offset into the input buffer where the data starts.
     * @param inLen the length of the data to be processed.
     * @return the result of the HVE process.
     * @throws org.bouncycastle.crypto.DataLengthException
     *          the input block is too large.
     */
    public byte[] processBlock(byte[] in, int inOff, int inLen) {
        if (key == null) {
            throw new IllegalStateException("HVE engine not initialised");
        }

        int maxLength = getInputBlockSize();

        if (inLen > maxLength) {
            throw new DataLengthException("input too large for HVE cipher.\n");
        }

        if (key instanceof IPLOSTW10SearchKeyParameters) {
            // match
            // Convert bytes to Elements...

            Element c1 = productPairing.getG1().newElement();
            c1.setFromBytes(in, inOff);

            IPLOSTW10SearchKeyParameters searchKey = (IPLOSTW10SearchKeyParameters) key;

            Element result = productPairing.pairing(c1, searchKey.getK());

            System.out.println("result = " + result);

            return new byte[]{(byte) (result.isOne() ? 0 : 1)};
        } else {
            // encryption
            if (inLen > this.inLen || inLen < this.inLen)
                throw new DataLengthException("input must be of size " + this.inLen);

            byte[] block;
            if (inOff != 0 || inLen != in.length) {
                block = new byte[inLen];
                System.arraycopy(in, inOff, block, 0, inLen);
            } else {
                block = in;
            }

            Element[] x = new Element[n];
            int offset = 0;
            for (int i = 0; i < n; i++) {
                x[i] = pairing.getZr().newElement();
                offset += x[i].setFromBytes(block, offset);
            }

            IPLOSTW10PublicKeyParameters pub = (IPLOSTW10PublicKeyParameters) key;

            Element delta1 = pairing.getZr().newRandomElement();
            Element delta2 = pairing.getZr().newRandomElement();
            Element eta = pairing.getZr().newRandomElement();

            Element c1 = pub.getBAt(0).mulZn(x[0]);
            for (int i = 1; i < n; i++) {
                c1.add(pub.getBAt(i).mulZn(x[i]));
            }
            c1.mulZn(delta1);

//            c1.add(pub.getBAt(n));
            c1.add(pub.getBAt(n+1).mulZn(delta2));

            // Move to bytes
            return c1.toBytes();
        }
    }


}