package it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.engines;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.crypto.rfid.utma.strong.params.UTMAStrongPublicKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.params.ParametersWithRandom;

import java.io.ByteArrayOutputStream;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongRandomizer {

    private UTMAStrongPublicKeyParameters key;
    private AsymmetricBlockCipher ssEngine;
    private Pairing pairing;


    public UTMAStrongRandomizer(AsymmetricBlockCipher ssEngine) {
        this.ssEngine = ssEngine;
    }

    
    /**
     * initialise the UTMA engine.
     *
     * @param param         the necessary UTMA key parameters.
     */
    public void init(CipherParameters param) {
        if (param instanceof ParametersWithRandom) {
            ParametersWithRandom p = (ParametersWithRandom) param;

            this.key = (UTMAStrongPublicKeyParameters) p.getParameters();
        } else {
            this.key = (UTMAStrongPublicKeyParameters) param;
        }

        if (!(key instanceof UTMAStrongPublicKeyParameters)) {
            throw new IllegalArgumentException("UTMAStrongPublicKeyParameters are required for encryption.");
        }

        this.pairing = PairingFactory.getPairing(key.getParameters().getCurveParams());
        this.ssEngine.init(true, key.getParameters().getRPublicKey());
    }

    /**
     * Return the maximum size for an input block to this engine.
     * For UTMA this is always one byte less than the size of P on
     * encryption, and twice the length as the size of P on decryption.
     *
     * @return maximum size for an input block.
     */
    public int getBlockSize() {
        return (pairing.getGT().getLengthInBytes() + (4  * pairing.getG1().getLengthInBytes())) + ssEngine.getOutputBlockSize();
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

        // Convert bytes to Elements...
        int offset = inOff;
        Element[] ct = extractCipherText(in, offset);
        offset += (pairing.getGT().getLengthInBytes() + (4  * pairing.getG1().getLengthInBytes()));

        // Randomize the first part

        Element ctOne[] = encryptOne();
        ct = mulComponentWise(ct, ctOne);

        try {
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getBlockSize());
            for (Element e : ct) {
                bytes.write(e.toBytes());
            }

            // Re-encrypt the public key
            byte[] pkMaterial = ((Point) key.getPk()).toBytesCompressed();
            bytes.write(ssEngine.processBlock(pkMaterial, 0, pkMaterial.length));

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

    private Element[] encryptOne() {
        Element s = pairing.getZr().newRandomElement();
        Element s1 = pairing.getZr().newRandomElement();
        Element s2 = pairing.getZr().newRandomElement();

        Element ct[] = new Element[5];

        ct[0] = key.getParameters().getOmega().powZn(s);
        ct[1] = key.getPk().duplicate().mulZn(s);
        ct[2] = key.getParameters().getT2().powZn(s2);
        ct[3] = key.getParameters().getT3().powZn(s.duplicate().sub(s1).sub(s2));
        ct[4] = key.getParameters().getT1().powZn(s1);

        return ct;
    }


}