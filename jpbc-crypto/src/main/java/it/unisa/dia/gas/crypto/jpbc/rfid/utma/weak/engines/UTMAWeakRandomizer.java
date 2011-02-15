package it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak.engines;

import it.unisa.dia.gas.crypto.jpbc.rfid.utma.weak.params.UTMAWeakPublicParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithRandom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAWeakRandomizer {

    private UTMAWeakPublicParameters key;
    private Pairing pairing;

    /**
     * initialise the UTMA engine.
     *
     * @param param         the necessary UTMA key parameters.
     */
    public void init(CipherParameters param) {
        if (param instanceof ParametersWithRandom) {
            ParametersWithRandom p = (ParametersWithRandom) param;

            this.key = (UTMAWeakPublicParameters) p.getParameters();
        } else {
            this.key = (UTMAWeakPublicParameters) param;
        }

        this.pairing = PairingFactory.getPairing(key.getCurveParams());
    }

    /**
     * Return the maximum size for an input block to this engine.
     * For UTMA this is always one byte less than the size of P on
     * encryption, and twice the length as the size of P on decryption.
     *
     * @return maximum size for an input block.
     */
    public int getBlockSize() {
        return (pairing.getGT().getLengthInBytes() + (4  * pairing.getG1().getLengthInBytes())) * 2;
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
    public byte[] processBlock(byte[] in, int inOff, int inLen) {
        if (key == null) {
            throw new IllegalStateException("UTMA randomizer not initialised");
        }

        if (inLen != getBlockSize())
            throw new DataLengthException("input too large for UTMA randomizer.\n");

        // encrypts

        // Convert bytes to Elements...
        int offset = inOff;
        Element[] ct = extractCipherText(in, offset);
        offset += (pairing.getGT().getLengthInBytes() + (4  * pairing.getG1().getLengthInBytes()));
        Element[] ct1 = extractCipherText(in, offset);

        Element r  = pairing.getZr().newElement().setToRandom();
        Element r2 = pairing.getZr().newElement().setToRandom();
        Element r3 = pairing.getZr().newElement().setToRandom();

        // Convert the Elements to byte arrays
        ct1 = star(ct1, r, r2, r3);
        ct = mulComponentWise(ct, ct1);
        ct1 = star(ct1, r, r2, r3);

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getBlockSize());
            for (Element e : ct) {
                bytes.write(e.toBytes());
            }
            for (Element e : ct1) {
                bytes.write(e.toBytes());
            }

            return bytes.toByteArray();
        } catch (IOException e) {
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

    private Element[] star(Element[] ct, Element r, Element r2, Element r3) {
        ct[0].powZn(r);
        ct[1].powZn(r);
        ct[2] = ct[2].powZn(r).mul(key.getT2().powZn(r2));
        ct[3] = ct[3].powZn(r).mul(key.getT3().powZn(r3));
        ct[4] = ct[4].powZn(r).mul(key.getT1().powZn(r2.duplicate().add(r3).negate()));

        return ct;
    }

    private Element[] mulComponentWise(Element[] ct1, Element[] ct2) {
        ct1[0].mul(ct2[0]);
        ct1[1].mul(ct2[1]);
        ct1[2].mul(ct2[2]);
        ct1[3].mul(ct2[3]);
        ct1[4].mul(ct2[4]);

        return ct1;
    }

}
