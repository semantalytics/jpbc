package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines;

import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEAttributes;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params.HVEIP08SearchKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithRandom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEIP08AttributesEngine implements AsymmetricBlockCipher {

    private HVEIP08KeyParameters key;
    private boolean forEncryption;

    private int n;
    private int inBytes;
    private int outBytes;
    private Pairing pairing;

    /**
     * initialise the HVE engine.
     *
     * @param forEncryption true if we are encrypting, false otherwise.
     * @param param         the necessary HVE key parameters.
     */
    public void init(boolean forEncryption, CipherParameters param) {
        if (param instanceof ParametersWithRandom) {
            ParametersWithRandom p = (ParametersWithRandom) param;

            this.key = (HVEIP08KeyParameters) p.getParameters();
        } else {
            this.key = (HVEIP08KeyParameters) param;
        }

        this.forEncryption = forEncryption;
        if (forEncryption) {
            if (!(key instanceof HVEIP08PublicKeyParameters)) {
                throw new IllegalArgumentException("HVEIP08PublicKeyParameters are required for encryption.");
            }
        } else {
            if (!(key instanceof HVEIP08SearchKeyParameters)) {
                throw new IllegalArgumentException("HVEIP08SearchKeyParameters are required for decryption.");
            }
        }

        this.pairing = PairingFactory.getPairing(key.getParameters().getCurveParams());

        this.n = key.getParameters().getN();
        this.inBytes = key.getParameters().getAttributesLengthInBytes();
        this.outBytes = (2 * n + 1) * pairing.getG1().getLengthInBytes();
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
            return inBytes;
        }

        return outBytes;
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
            return outBytes;
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

        int maxLength = forEncryption ? inBytes : getInputBlockSize();

        if (inLen > maxLength) {
            throw new DataLengthException("input too large for HVE cipher.\n");
        }

        if (key instanceof HVEIP08SearchKeyParameters) {
            // match
            HVEIP08SearchKeyParameters searchKey = (HVEIP08SearchKeyParameters) key;
            if (searchKey.isAllStar())
                return new byte[]{0};

            // Convert bytes to Elements...
            int offset = inOff;

            // load C0...
            Element C0 = pairing.getG1().newElement();
            offset += C0.setFromBytes(in, offset);

            // load Xs, Ws..
            List<Element> X = new ArrayList<Element>();
            List<Element> W = new ArrayList<Element>();
            for (int i = 0; i < n; i++) {
                Element x = pairing.getG1().newElement();
                offset += x.setFromBytes(in, offset);
                X.add(x);

                Element w = pairing.getG1().newElement();
                offset += w.setFromBytes(in, offset);
                W.add(w);
            }

            Element result = pairing.getGT().newOneElement();
            if (searchKey.isPreProcessed()) {
                for (int i = 0; i < searchKey.getParameters().getN(); i++) {
                    if (!searchKey.isStar(i)) {
                        result.mul(
                                searchKey.getPreYAt(i).pairing(X.get(i))
                        ).mul(
                                searchKey.getPreLAt(i).pairing(W.get(i))
                        );
                    }
                }
            } else {
                for (int i = 0; i < searchKey.getParameters().getN(); i++) {
                    if (!searchKey.isStar(i)) {
                        result.mul(
                                pairing.pairing(searchKey.getYAt(i), X.get(i))
                        ).mul(
                                pairing.pairing(searchKey.getLAt(i), W.get(i))
                        );
                    }
                }
            }
            return new byte[]{(byte) (result.isOne() ? 0 : 1)};
        } else {
            // encryption
            if (inLen > inBytes || inLen < inBytes)
                throw new DataLengthException("input must be of size " + inBytes);

            byte[] block;
            if (inOff != 0 || inLen != in.length) {
                block = new byte[inLen];
                System.arraycopy(in, inOff, block, 0, inLen);
            } else {
                block = in;
            }
            int[] attributes = HVEAttributes.byteArrayToAttributes(key.getParameters(), block);

            HVEIP08PublicKeyParameters pub = (HVEIP08PublicKeyParameters) key;

            Element s = pairing.getZr().newRandomElement().getImmutable();
            Element C0 = pub.getParameters().getPowG().powZn(s);

            List<Element> elements = new ArrayList<Element>();

            if (pub.isPreProcessed()) {
                for (int i = 0; i < n; i++) {
                    Element si = pairing.getZr().newElement().setToRandom();
                    Element sMinusSi = s.sub(si);

                    int j = attributes[i];

                    // Populate elements
                    elements.add(pub.getPreTAt(i, j).powZn(sMinusSi));  // X_i
                    elements.add(pub.getPreVAt(i, j).powZn(si));        // W_i
                }
            } else {
                for (int i = 0; i < n; i++) {
                    Element si = pairing.getZr().newElement().setToRandom();
                    Element sMinusSi = s.sub(si);

                    int j = attributes[i];

                    // Populate elements
                    elements.add(pub.getTAt(i, j).powZn(sMinusSi));  // X_i
                    elements.add(pub.getVAt(i, j).powZn(si));        // W_i
                }
            }

            // Convert the Elements to byte arrays
            ByteArrayOutputStream outputStream;
            try {
                outputStream = new ByteArrayOutputStream(getOutputBlockSize());
                outputStream.write(C0.toBytes());

                for (Element element : elements)
                    outputStream.write(element.toBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return outputStream.toByteArray();
        }
    }


}