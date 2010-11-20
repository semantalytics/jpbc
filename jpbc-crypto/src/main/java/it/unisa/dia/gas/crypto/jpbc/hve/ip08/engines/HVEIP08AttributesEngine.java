package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.engines;

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
    private int length;
    private int[] attributeLengths;
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

        this.n = key.getParameters().getN();
        this.length = key.getParameters().getLength();
        this.attributeLengths = key.getParameters().getAttributeLengths();
        this.pairing = PairingFactory.getPairing(key.getParameters().getCurveParams());
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
            return length;
        }

        return pairing.getGT().getLengthInBytes() + (2 * length + 1) * pairing.getG1().getLengthInBytes();
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
            return pairing.getGT().getLengthInBytes() + (2 * length + 1) * pairing.getG1().getLengthInBytes();
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

        int maxLength = forEncryption ? length : getInputBlockSize();

        if (inLen > maxLength) {
            throw new DataLengthException("input too large for HVE cipher.\n");
        }

        if (key instanceof HVEIP08SearchKeyParameters) {
            // match
            // Convert bytes to Elements...

            int offset = inOff;

            // load omega...
            Element omega = pairing.getGT().newElement();
            offset += omega.setFromBytes(in, offset);

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

            HVEIP08SearchKeyParameters searchKey = (HVEIP08SearchKeyParameters) key;

            Element result = omega.duplicate();

            if (searchKey.getK() != null) {
                result.mul(
                        pairing.pairing(C0, searchKey.getK())
                );    
            } else {
                for (int i = 0; i < searchKey.getL().size(); i++) {
                    if (searchKey.getL().get(i) != null) {
                        result.mul(
                                pairing.pairing(X.get(i), searchKey.getY().get(i))
                        ).mul(
                                pairing.pairing(W.get(i), searchKey.getL().get(i))
                        );
                    }
                }
            }

            return new byte[]{(byte) (pairing.getGT().newOneElement().isEqual(result) ? 0 : 1)};
        } else {
            // encryption
            if (inLen > length || inLen < length)
                throw new DataLengthException("input must be of size " + length);

            byte[] block;
            if (inOff != 0 || inLen != in.length) {
                block = new byte[inLen];
                System.arraycopy(in, inOff, block, 0, inLen);
            } else {
                block = in;
            }

            HVEIP08PublicKeyParameters pub = (HVEIP08PublicKeyParameters) key;

            Element s = pairing.getZr().newRandomElement().getImmutable();
//        Element M = pairing.getGT().newOneElement();

            Element omega = pub.getY().powZn(s.negate())/*.mul(M)*/;
            Element C0 = pub.getParameters().getG().powZn(s);

            List<Element> X = new ArrayList<Element>();
            List<Element> W = new ArrayList<Element>();

            int blockOffset = 0;
            for (int i = 0; i < n; i++) {
                Element si = pairing.getZr().newElement().setToRandom();
                Element sMinusSi = s.sub(si);

                // Compute j
                int pow = 1;
                int j = 0;
                for (int k = attributeLengths[i] - 1; k >= 0; k--) {
                    j += block[blockOffset + k] * pow;
                    pow <<= 1;
                }
                blockOffset += attributeLengths[i];

                // Populate X and W
                X.add(pub.getT().get(i).get(j).powZn(sMinusSi));
                W.add(pub.getV().get(i).get(j).powZn(si));
            }

            // Convert the Elements to byte arrays
            ByteArrayOutputStream outputStream;
            try {
                outputStream = new ByteArrayOutputStream(getOutputBlockSize());
                outputStream.write(omega.toBytes());
                outputStream.write(C0.toBytes());

                for (int i = 0; i < n; i++) {
                    outputStream.write(X.get(i).toBytes());
                    outputStream.write(W.get(i).toBytes());
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return outputStream.toByteArray();
        }
    }


}