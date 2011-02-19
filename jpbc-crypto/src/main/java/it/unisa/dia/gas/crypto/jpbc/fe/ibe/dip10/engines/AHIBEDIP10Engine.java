package it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.engines;

import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10EncryptionParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.fe.ibe.dip10.params.AHIBEDIP10SecretKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.utils.ElementUtil;
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
public class AHIBEDIP10Engine implements AsymmetricBlockCipher {

    private CipherParameters key;
    private boolean forEncryption;

    private int byteLength;
    private Pairing pairing;

    /**
     * initialise the AHIBE engine.
     *
     * @param forEncryption true if we are encrypting, false otherwise.
     * @param param         the necessary AHIBE key parameters.
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
            if (!(key instanceof AHIBEDIP10EncryptionParameters)) {
                throw new IllegalArgumentException("AHIBEDIP10EncryptionParameters are required for encryption.");
            }

            this.pairing = PairingFactory.getPairing(((AHIBEDIP10EncryptionParameters) key).getPublicKey().getCurveParams());
        } else {
            if (!(key instanceof AHIBEDIP10SecretKeyParameters)) {
                throw new IllegalArgumentException("AHIBEDIP10SecretKeyParameters are required for decryption.");
            }

            this.pairing = PairingFactory.getPairing(((AHIBEDIP10SecretKeyParameters) key).getCurveParams());
        }

        this.byteLength = pairing.getGT().getLengthInBytes();
    }

    /**
     * Return the maximum size for an input block to this engine.
     *
     * @return maximum size for an input block.
     */
    public int getInputBlockSize() {
        if (forEncryption) {
            return byteLength;
        }

        return (pairing.getGT().getLengthInBytes() + (2 * pairing.getG1().getLengthInBytes()));
    }

    /**
     * Return the maximum size for an output block to this engine.
     *
     * @return maximum size for an output block.
     */
    public int getOutputBlockSize() {
        if (forEncryption) {
            return (pairing.getGT().getLengthInBytes() + (2 * pairing.getG1().getLengthInBytes()));
        }

        return byteLength;
    }

    /**
     * Process a single block using the basic AHIBE algorithm.
     *
     * @param in    the input array.
     * @param inOff the offset into the input buffer where the data starts.
     * @param inLen the length of the data to be processed.
     * @return the result of the AHIBE process.
     * @throws org.bouncycastle.crypto.DataLengthException
     *          the input block is too large.
     */
    public byte[] processBlock(byte[] in, int inOff, int inLen) {
        if (key == null) {
            throw new IllegalStateException("AHIBE engine not initialised");
        }

        int maxLength = forEncryption ? byteLength : getInputBlockSize();

        if (inLen > maxLength) {
            throw new DataLengthException("input too large for AHIBE cipher.\n");
        }

        if (key instanceof AHIBEDIP10SecretKeyParameters) {
            // decrypts

            // Convert bytes to Elements...

            int offset = inOff;

            // load C0...
            Element C0 = pairing.getGT().newElement();
            offset += C0.setFromBytes(in, offset);

            // load C1...
            Element C1 = pairing.getG1().newElement();
            offset += C1.setFromBytes(in, offset);

            // load C2...
            Element C2 = pairing.getG1().newElement();
            offset += C2.setFromBytes(in, offset);

//            System.out.println("C0 = " + C0);
//            System.out.println("C1 = " + C1);
//            System.out.println("C2 = " + C2);

            AHIBEDIP10SecretKeyParameters sk = (AHIBEDIP10SecretKeyParameters) key;

            Element T = pairing.pairing(sk.getK22(), C2).mul(pairing.pairing(sk.getK21(), C1).invert());

            Element M = C0.mul(pairing.pairing(sk.getK12(), C2).mul(pairing.pairing(sk.getK11(), C1).invert()).invert());

//            System.out.println("T = " + T);
//            System.out.println("M = " + M);

            return M.toBytes();
        } else {
            // encrypt the message
            if (inLen > byteLength)
                throw new DataLengthException("input must be of size " + byteLength);

            byte[] block;
            if (inOff != 0 || inLen != in.length) {
                block = new byte[inLen];
                System.arraycopy(in, inOff, block, 0, inLen);
            } else {
                block = in;
            }

            Element M = pairing.getGT().newElement();
            M.setFromBytes(block);

//            System.out.println(new String(M.toBytes()).trim());
//            System.out.println(new String(block).trim());

            // Compute ciphertext
            AHIBEDIP10EncryptionParameters encParams = (AHIBEDIP10EncryptionParameters) key;
            AHIBEDIP10PublicKeyParameters pk = encParams.getPublicKey();

            Element s = pairing.getZr().newRandomElement();

            Element C0 = M.mul(pk.getOmega().powZn(s));

            Element C1 = pairing.getG1().newOneElement();
            for (int i = 0; i < encParams.getLength(); i++) {
                C1.mul(pk.getUAt(i).powZn(encParams.getIdAt(i)));
            }
            C1.mul(pk.getT()).powZn(s).mul(ElementUtil.randomIn(pairing, pk.getY4()));

            Element C2 = pk.getY1().powZn(s).mul(ElementUtil.randomIn(pairing, pk.getY4()));

            // Convert the Elements to byte arrays
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getOutputBlockSize());

//            System.out.println("C0 = " + C0);
//            System.out.println("C1 = " + C1);
//            System.out.println("C2 = " + C2);

            try {
                bytes.write(C0.toBytes());
                bytes.write(C1.toBytes());
                bytes.write(C2.toBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return bytes.toByteArray();
        }
    }


}