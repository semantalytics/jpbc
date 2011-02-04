package it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.engines;

import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBEEncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBEPublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.lw11.params.UHIBESecretKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithRandom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UHIBEEngine implements AsymmetricBlockCipher {

    private CipherParameters key;
    private boolean forEncryption;

    private int idsNumber;
    private int inBytes, outBytes;
    private Pairing pairing;

    /**
     * initialise the UHIBE engine.
     *
     * @param forEncryption true if we are encrypting, false otherwise.
     * @param param         the necessary UHIBE key parameters.
     */
    public void init(boolean forEncryption, CipherParameters param) {
        if (param instanceof ParametersWithRandom) {
            ParametersWithRandom p = (ParametersWithRandom) param;

            this.key = p.getParameters();
        } else {
            this.key = param;
        }

        this.forEncryption = forEncryption;
        if (forEncryption) {
            if (!(key instanceof UHIBEEncryptionParameters)) {
                throw new IllegalArgumentException("UHIBEEncryptionParameters are required for encryption.");
            }

            this.pairing = PairingFactory.getPairing(((UHIBEEncryptionParameters) key).getPublicKey().getCurveParams());
            this.idsNumber = ((UHIBEEncryptionParameters) key).getLength();
        } else {
            if (!(key instanceof UHIBESecretKeyParameters)) {
                throw new IllegalArgumentException("UHIBESecretKeyParameters are required for decryption.");
            }

            this.pairing = PairingFactory.getPairing(((UHIBESecretKeyParameters) key).getCurveParams());
            this.idsNumber = ((UHIBESecretKeyParameters) key).getLength();
        }

        this.inBytes = pairing.getGT().getLengthInBytes();
        this.outBytes = pairing.getGT().getLengthInBytes() +
                pairing.getG1().getLengthInBytes() +
                (idsNumber * 3 * pairing.getG1().getLengthInBytes());
    }

    /**
     * Return the maximum size for an input block to this engine.
     *
     * @return maximum size for an input block.
     */
    public int getInputBlockSize() {
        if (forEncryption)
            return inBytes;

        return outBytes;
    }

    /**
     * Return the maximum size for an output block to this engine.
     *
     * @return maximum size for an output block.
     */
    public int getOutputBlockSize() {
        if (forEncryption)
            return outBytes;

        return inBytes;
    }

    /**
     * Process a single block using the basic UHIBE algorithm.
     *
     * @param in    the input array.
     * @param inOff the offset into the input buffer where the data starts.
     * @param inLen the length of the data to be processed.
     * @return the result of the UHIBE process.
     * @throws org.bouncycastle.crypto.DataLengthException
     *          the input block is too large.
     */
    public byte[] processBlock(byte[] in, int inOff, int inLen) {
        if (key == null) {
            throw new IllegalStateException("UHIBE engine not initialised");
        }

        int maxLength = forEncryption ? inBytes : getInputBlockSize();

        if (inLen > maxLength) {
            throw new DataLengthException("input too large for UHIBE cipher.\n");
        }

        if (key instanceof UHIBESecretKeyParameters) {
            // decrypts
            UHIBESecretKeyParameters sk = (UHIBESecretKeyParameters) key;

            // Convert bytes to Elements...
            int offset = inOff;

            // load C
            Element C = pairing.getGT().newElement();
            offset += C.setFromBytes(in, offset);

//            System.out.println("C = " + C);

            // load C0
            Element C0 = pairing.getG1().newElement();
            offset += C0.setFromBytes(in, offset);

//            System.out.println("C0 = " + C0);

            Element numerator = pairing.getGT().newOneElement();
            Element denominator = pairing.getGT().newOneElement();

            for (int i = 0; i < idsNumber; i++) {
                Element C1 = pairing.getG1().newElement();
                offset += C1.setFromBytes(in, offset);

                Element C2 = pairing.getG1().newElement();
                offset += C2.setFromBytes(in, offset);

                Element C3 = pairing.getG1().newElement();
                offset += C3.setFromBytes(in, offset);

//                System.out.println("C1 = " + C1);
//                System.out.println("C2 = " + C2);
//                System.out.println("C3 = " + C3);

                numerator.mul(pairing.pairing(C0, sk.getK0At(i)))
                        .mul(pairing.pairing(C2, sk.getK2At(i)));

                denominator.mul(pairing.pairing(C1, sk.getK1At(i)))
                        .mul(pairing.pairing(C3, sk.getK3At(i)));
            }

            Element M = C.div(numerator.div(denominator));

//            System.out.printf("Dec %s\n", new String(M.toBytes()).trim());

            return M.toBytes();
        } else {
            // encrypt the message
            if (inLen > inBytes)
                throw new DataLengthException("input must be of size " + inBytes);

            byte[] block;
            if (inOff != 0 || inLen != in.length) {
                block = new byte[inLen];
                System.arraycopy(in, inOff, block, 0, inLen);
            } else {
                block = in;
            }

            Element M = pairing.getGT().newOneElement();
            M.setFromBytes(block);

//            System.out.printf("End. %s - %s\n", new String(M.toBytes()).trim(), new String(block).trim());

            // Compute ciphertext
            UHIBEEncryptionParameters encParams = (UHIBEEncryptionParameters) key;
            UHIBEPublicKeyParameters pk = encParams.getPublicKey();
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getOutputBlockSize());

            try {
                Element s = pairing.getZr().newRandomElement();

                Element C = M.mul(pk.getOmega().powZn(s));
                Element C0 = pk.getG().powZn(s);

                bytes.write(C.toBytes());
                bytes.write(C0.toBytes());

                System.out.println("C = " + C);
                System.out.println("C0 = " + C0);

                for (int i = 0; i < idsNumber; i++) {
                    Element t = pairing.getZr().newRandomElement();

                    // Computes C_{i,1}, C_{i,2}, C_{i,3} and writes them to the byte stream
                    Element C1 = pk.getW().powZn(s).mul(pk.getV().powZn(t));
                    Element C2 = pk.getG().powZn(t);
                    Element C3 = pk.getU().powZn(encParams.getIdAt(i)).mul(pk.getH()).powZn(t);

//                    System.out.println("C1 = " + C1);
//                    System.out.println("C2 = " + C2);
//                    System.out.println("C3 = " + C3);

                    bytes.write(C1.toBytes());
                    bytes.write(C2.toBytes());
                    bytes.write(C3.toBytes());
                }

            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return bytes.toByteArray();
        }
    }


}
