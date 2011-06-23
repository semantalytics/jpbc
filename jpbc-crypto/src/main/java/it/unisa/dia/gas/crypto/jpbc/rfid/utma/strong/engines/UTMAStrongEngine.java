package it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.engines;

import it.unisa.dia.gas.crypto.engines.kem.PairingKeyEncapsulationMechanism;
import it.unisa.dia.gas.crypto.jpbc.rfid.utma.strong.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.InvalidCipherTextException;
import org.bouncycastle.crypto.engines.ElGamalEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class UTMAStrongEngine extends PairingKeyEncapsulationMechanism {

    protected AsymmetricBlockCipher ssEngine;
    protected boolean forRandomization;
    protected int firstPartBytes;


    public UTMAStrongEngine(AsymmetricBlockCipher ssEngine) {
        this.ssEngine = ssEngine;
    }

    public UTMAStrongEngine() {
        this(new ElGamalEngine());
    }


    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof UTMAStrongPublicKeyParameters) && !(key instanceof UTMAStrongRandomizeParameters))
                throw new IllegalArgumentException("UTMAStrongPublicKeyParameters are required for encryption/randomization.");
            forRandomization = key instanceof UTMAStrongRandomizeParameters;

            // Init the engine also
            ssEngine.init(forEncryption, ((UTMAStrongKeyParameters) key).getParameters().getRPublicKey());
        } else {
            if (!(key instanceof UTMAStrongPrivateKeyParameters)) {
                throw new IllegalArgumentException("UTMAStrongPrivateKeyParameters are required for decryption.");
            }

            // Init the engine also
            ssEngine.init(forEncryption, ((UTMAStrongPrivateKeyParameters) key).getRPrivateKey());
        }

        UTMAStrongKeyParameters keyParameters = (UTMAStrongKeyParameters) key;

        this.pairing = PairingFactory.getPairing(keyParameters.getParameters().getCurveParams());
        if (key instanceof UTMAStrongRandomizeParameters) {
            this.inBytes = (4 * pairing.getG1().getLengthInBytes()) + ssEngine.getOutputBlockSize();
            this.outBytes = inBytes;
            this.firstPartBytes = 4 * pairing.getG1().getLengthInBytes();
        } else {
            this.inBytes = 0;
            this.keyBytes = pairing.getGT().getLengthInBytes();
            this.outBytes = keyBytes + (4 * pairing.getG1().getLengthInBytes()) + ssEngine.getOutputBlockSize();
        }
    }

    public int getInputBlockSize() {
        if (forRandomization)
            return inBytes;

        return super.getInputBlockSize();
    }

    public int getOutputBlockSize() {
        if (forRandomization)
            return outBytes;

        return super.getOutputBlockSize();
    }


    public byte[] process(byte[] in, int inOff, int inLen) throws InvalidCipherTextException {
        if (key instanceof UTMAStrongPrivateKeyParameters) {
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
        } else if (key instanceof UTMAStrongPublicKeyParameters) {
            Element M = pairing.getGT().newRandomElement();

            UTMAStrongPublicKeyParameters publicKeyParameters = (UTMAStrongPublicKeyParameters) key;
            byte[] pkMaterial = ((Point) publicKeyParameters.getPk()).toBytesCompressed();
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream(getOutputBlockSize());
                out.write(M.toBytes());
                encrypt(out, M);
                out.write(ssEngine.processBlock(pkMaterial, 0, pkMaterial.length));

                return out.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } else {
            // The ciphertext is composed by two parts:
            // the first part is the basic encryption of the message;
            // the second one is the encryption of the public parameters.

            UTMAStrongRandomizeParameters keyParameters = (UTMAStrongRandomizeParameters) key;

            // Get the first part
            Element[] ct = extractCipherText(in, inOff);

            // Get the second part
            ssEngine.init(false, keyParameters.getRPublicParameters().getRPrivateKey());
            byte[] pkBytes = ssEngine.processBlock(in, inOff + firstPartBytes, inLen - firstPartBytes);
            Point pk = (Point) pairing.getG1().newElement();
            pk.setFromBytesCompressed(pkBytes);

            // Randomize the first part

            Element ctOne[] = encryptOne(keyParameters.getParameters(), pk);
            ct = mulComponentWise(ct, ctOne);

            try {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream(getOutputBlockSize());
                for (Element e : ct) {
                    bytes.write(e.toBytes());
                }

                // Re-encrypt the public parameters
                ssEngine.init(true, keyParameters.getParameters().getRPublicKey());
                bytes.write(ssEngine.processBlock(pkBytes, 0, pkBytes.length));

                return bytes.toByteArray();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

        }
    }


    protected void encrypt(ByteArrayOutputStream outputStream, Element M) {
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

    protected Element[] extractCipherText(byte[] in, int inOff) {
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

    protected Element[] mulComponentWise(Element[] ct1, Element[] ct2) {
        ct1[0].mul(ct2[0]);
        ct1[1].mul(ct2[1]);
        ct1[2].mul(ct2[2]);
        ct1[3].mul(ct2[3]);
        ct1[4].mul(ct2[4]);

        return ct1;
    }

    protected Element[] encryptOne(UTMAStrongPublicParameters strongPk, Element pk) {
        Element s = pairing.getZr().newRandomElement();
        Element s1 = pairing.getZr().newRandomElement();
        Element s2 = pairing.getZr().newRandomElement();

        Element ct[] = new Element[5];

        ct[0] = strongPk.getOmega().powZn(s);
        ct[1] = pk.mulZn(s);
        ct[2] = strongPk.getT2().powZn(s2);
        ct[3] = strongPk.getT3().powZn(s.sub(s1).sub(s2));
        ct[4] = strongPk.getT1().powZn(s1);

        return ct;
    }


}