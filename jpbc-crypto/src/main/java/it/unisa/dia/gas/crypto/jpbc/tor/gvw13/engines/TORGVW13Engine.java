package it.unisa.dia.gas.crypto.jpbc.tor.gvw13.engines;

import it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params.TORGVW13KeyParameters;
import it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params.TORGVW13PublicKeyParameters;
import it.unisa.dia.gas.crypto.jpbc.tor.gvw13.params.TORGVW13RecodeParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamReader;
import org.bouncycastle.crypto.AsymmetricBlockCipher;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.InvalidCipherTextException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class TORGVW13Engine implements AsymmetricBlockCipher {

    private CipherParameters param;
    private Pairing pairing;
    private int inputBlockSize, outputBlockSize;

    public void init(boolean forEncryption, CipherParameters param) {
        this.param = param;

        TORGVW13KeyParameters keyParameters = (TORGVW13KeyParameters) param;
        pairing = PairingFactory.getPairing(keyParameters.getParameters().getParameters());

        if (param instanceof TORGVW13PublicKeyParameters) {
            inputBlockSize = pairing.getZr().getLengthInBytes();
            outputBlockSize = ((TORGVW13PublicKeyParameters) param).getLevel() == 0 ?
                    pairing.getG1().getLengthInBytes() :
                    pairing.getGT().getLengthInBytes();
        } else if (param instanceof TORGVW13RecodeParameters) {
            inputBlockSize = pairing.getG1().getLengthInBytes() + pairing.getGT().getLengthInBytes();
            outputBlockSize = pairing.getGT().getLengthInBytes();
        } else
            throw new IllegalArgumentException("Invalid key parameters!");
    }


    public int getInputBlockSize() {
        return inputBlockSize;
    }

    public int getOutputBlockSize() {
        return outputBlockSize;
    }

    public byte[] processBlock(byte[] in, int inOff, int len) throws InvalidCipherTextException {
        if (param instanceof TORGVW13PublicKeyParameters) {
            TORGVW13PublicKeyParameters keyParameters = (TORGVW13PublicKeyParameters) param;

            // Read Input
            Element s = new PairingStreamReader(pairing, in, inOff).readG1Element();

            // Encode
            Element result;
            if (keyParameters.getLevel() == 0) {
                result = keyParameters.getLeft().powZn(s);
            } else {
                result = pairing.pairing(
                        keyParameters.getParameters().getG1a(),
                        keyParameters.getRight()
                ).powZn(s);
            }

            return result.toBytes();
        } else {
            TORGVW13RecodeParameters keyParameters = (TORGVW13RecodeParameters) param;

            // Read Input
            PairingStreamReader reader = new PairingStreamReader(pairing, in, inOff);
            Element c0 = reader.readG1Element();
            Element c1 = reader.readGTElement();

            // Recode
            Element result =pairing.pairing(c0, keyParameters.getRk()).mul(c1);

            return result.toBytes();
       }

    }
}
