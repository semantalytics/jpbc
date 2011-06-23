package it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.engines;

import it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.params.UTMAStrongKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.params.UTMAStrongPrivateKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.params.UTMAStrongPublicKeyParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.DataLengthException;
import org.bouncycastle.crypto.engines.ElGamalEngine;
import org.bouncycastle.crypto.params.ParametersWithRandom;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongEngine implements AsymmetricBlockCipher {

    private UTMAStrongKeyParameters key;
    private AsymmetricBlockCipher ssEngine;
    private boolean forEncryption;

    private int byteLength;
    private Pairing pairing;


    public UTMAStrongEngine(AsymmetricBlockCipher ssEngine) {
        this.ssEngine = ssEngine;
    }

    public UTMAStrongEngine() {
        this(new ElGamalEngine());
    }
    
    /**
     * initialise the UTMA engine.
     *                            
     * @param forEncryption true if we are encrypting, false otherwise.
     * @param param         the necessary UTMA key parameters.
     */
    public void init(boolean forEncryption, CipherParameters param) {
        if (param instanceof ParametersWithRandom) {
            ParametersWithRandom p = (ParametersWithRandom) param;

            this.key = (UTMAStrongKeyParameters) p.getParameters();
        } else {
            this.key = (UTMAStrongKeyParameters) param;
        }

        this.forEncryption = forEncryption;
        if (forEncryption) {
            if (!(key instanceof UTMAStrongPublicKeyParameters)) {
                throw new IllegalArgumentException("UTMAStrongPublicKeyParameters are required for encryption.");
            }

            // Init the engine also
            ssEngine.init(forEncryption, key.getParameters().getRPublicKey());
        } else {
            if (!(key instanceof UTMAStrongPrivateKeyParameters)) {
                throw new IllegalArgumentException("UTMAStrongPrivateKeyParameters are required for decryption.");
            }

            // Init the engine also
            ssEngine.init(forEncryption, ((UTMAStrongPrivateKeyParameters) key).getRPrivateKey());
        }

        this.pairing = PairingFactory.getPairing(key.getParameters().getCurveParams());
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

        return (pairing.getGT().getLengthInBytes() + (4  * pairing.getG1().getLengthInBytes())) + ssEngine.getInputBlockSize();
    }

    /**
     * Return the maximum size for an output block to this engine.
     *
     * @return maximum size for an output block.
     */
    public int getOutputBlockSize() {
        if (forEncryption) {
            return (pairing.getGT().getLengthInBytes() + (4  * pairing.getG1().getLengthInBytes())) + ssEngine.getOutputBlockSize();
        }

        return 1;
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
            throw new IllegalStateException("UTMA engine not initialised");
        }

        int maxLength = forEncryption ? byteLength : getInputBlockSize();

        if (inLen > maxLength) {
            throw new DataLengthException("input too large for UTMA cipher.\n");
        }

        if (key instanceof UTMAStrongPrivateKeyParameters) {
            // encrypts

            // Convert bytes to Elements...

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

            UTMAStrongPrivateKeyParameters privateKeyParameters = (UTMAStrongPrivateKeyParameters) key;

            C.mul(pairing.pairing(C0, privateKeyParameters.getD0()))
                    .mul(pairing.pairing(C1, privateKeyParameters.getD1()))
                    .mul(pairing.pairing(C2, privateKeyParameters.getD2()))
                    .mul(pairing.pairing(C3, privateKeyParameters.getD3()));
            return C.toBytes();
        } else {
            // encrypt the message
            if (inLen > byteLength)
                throw new DataLengthException("input must be of size " + byteLength);

            Element M = pairing.getGT().newElement();
            M.setFromBytes(in, inOff);

            // Convert the Elements to byte arrays
            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getOutputBlockSize());
            encrypt(bytes, M);

            // Put the encryption of the public key
            UTMAStrongPublicKeyParameters publicKeyParameters = (UTMAStrongPublicKeyParameters) key;
            byte[] pkMaterial = ((Point) publicKeyParameters.getPk()).toBytesCompressed();
            try {
                bytes.write(ssEngine.processBlock(pkMaterial, 0, pkMaterial.length));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return bytes.toByteArray();
        }
    }


    private void encrypt(ByteArrayOutputStream outputStream, Element M) {
        UTMAStrongPublicKeyParameters publicKeyParameters = (UTMAStrongPublicKeyParameters) key;

        Element s = pairing.getZr().newElement().setToRandom();
        Element s1 = pairing.getZr().newElement().setToRandom();
        Element s2 = pairing.getZr().newElement().setToRandom();

        Element C = publicKeyParameters.getParameters().getOmega().powZn(s).mul(M);
        Element C0 = publicKeyParameters.getPk().mulZn(s);
        Element C1 = publicKeyParameters.getParameters().getT2().powZn(s2);
        Element C2 = publicKeyParameters.getParameters().getT3().powZn(s.sub(s1).sub(s2));
        Element C3 = publicKeyParameters.getParameters().getT1().powZn(s1);

        // Convert the Elements to byte arrays
        try {
            outputStream.write(C.toBytes());
            outputStream.write(C0.toBytes());
            outputStream.write(C1.toBytes());
            outputStream.write(C2.toBytes());
            outputStream.write(C3.toBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}