package it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.engines;

import it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.params.UTMAStrongParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.ElGamalEngine;

import java.io.ByteArrayOutputStream;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongRandomizer {

    private UTMAStrongParameters parameters;
    private AsymmetricBlockCipher ssEngine;
    private Pairing pairing;

    private int blockSize, firstPartSize;


    public UTMAStrongRandomizer(AsymmetricBlockCipher ssEngine) {
        this.ssEngine = ssEngine;
    }


    public UTMAStrongRandomizer() {
        this(new ElGamalEngine());
    }

    /**
     * initialise the UTMA engine.
     *
     * @param param         the necessary UTMA parameters parameters.
     */
    public void init(CipherParameters param) {
        if (!(param instanceof UTMAStrongParameters)) {
            throw new IllegalArgumentException("UTMAStrongParameters are required for encryption.");
        }

        this.parameters = (UTMAStrongParameters) param;
        this.pairing = PairingFactory.getPairing(parameters.getPublicParameters().getCurveParams());
        this.ssEngine.init(true, parameters.getPublicParameters().getRPublicKey());
        this.firstPartSize = (pairing.getGT().getLengthInBytes() + (4  * pairing.getG1().getLengthInBytes()));
        this.blockSize = firstPartSize + ssEngine.getOutputBlockSize();
    }

    /**
     * Return the maximum size for an input/ouput block to this engine.
     *
     * @return maximum size for an input/ouput block.
     */
    public int getBlockSize() {
        return blockSize;
    }

    /**
     * Process a single block using the basic UTMA algorithm.
     *
     * @param in    the input array.
     * @param inOff the offset into the input buffer where the data starts.
     * @param inLen the length of the data to be processed.
     * @return the result of the UTMA process.
     * @throws org.bouncycastle.crypto.DataLengthException
     *          the input block is too large.
     */
    public byte[] processBlock(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
        if (parameters == null) {
            throw new IllegalStateException("UTMA randomizer not initialised");
        }

        if (inLen != getBlockSize())
            throw new DataLengthException("input too large for UTMA randomizer.\n");

        // The ciphertext is composed by two parts:
        // the first part is the basic encryption of the message;
        // the second one is the encryption of the public parameters.

        // Get the first part
        Element[] ct = extractCipherText(in, inOff);

        // Get the second part
        ssEngine.init(false, parameters.getRPublicParameters().getRPrivateKey());
        byte[] pkBytes = ssEngine.processBlock(in, inOff + firstPartSize, inLen - firstPartSize);
        Point pk = (Point) pairing.getG1().newElement();
        pk.setFromBytesCompressed(pkBytes);

        // Randomize the first part

        Element ctOne[] = encryptOne(pk);
        ct = mulComponentWise(ct, ctOne);

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getBlockSize());
            for (Element e : ct) {
                bytes.write(e.toBytes());
            }

            // Re-encrypt the public parameters
            ssEngine.init(true, parameters.getPublicParameters().getRPublicKey());
            bytes.write(ssEngine.processBlock(pkBytes, 0, pkBytes.length));

            return bytes.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    private Element[] extractCipherText(byte[] in, int inOff) {
        int offset = inOff;

        // load omega...
        Element C = pairing.getGT().newElement();
        offset += C.setFromBytes(in, offset);

        // load C0...
        Element C0 = pairing.getG1().newElement();
        offset += C0.setFromBytes(in, offset);

        // load C1...
        Element C1 = pairing.getG1().newElement();
        offset += C1.setFromBytes(in, offset);

        // load C2...
        Element C2 = pairing.getG1().newElement();
        offset += C2.setFromBytes(in, offset);

        // load C3...
        Element C3 = pairing.getG1().newElement();
        offset += C3.setFromBytes(in, offset);

        return new Element[]{C, C0, C1, C2, C3};
    }

    private Element[] mulComponentWise(Element[] ct1, Element[] ct2) {
        ct1[0].mul(ct2[0]);
        ct1[1].mul(ct2[1]);
        ct1[2].mul(ct2[2]);
        ct1[3].mul(ct2[3]);
        ct1[4].mul(ct2[4]);

        return ct1;
    }

    private Element[] encryptOne(Element pk) {
        Element s = pairing.getZr().newRandomElement();
        Element s1 = pairing.getZr().newRandomElement();
        Element s2 = pairing.getZr().newRandomElement();

        Element ct[] = new Element[5];

        ct[0] = parameters.getPublicParameters().getOmega().powZn(s);
        ct[1] = pk.mulZn(s);
        ct[2] = parameters.getPublicParameters().getT2().powZn(s2);
        ct[3] = parameters.getPublicParameters().getT3().powZn(s.sub(s1).sub(s2));
        ct[4] = parameters.getPublicParameters().getT1().powZn(s1);

        return ct;
    }


}