package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.engines;

import it.unisa.dia.gas.crypto.engines.kem.PairingKeyEncapsulationMechanism;
import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.combiner.PairingAccumulator;
import it.unisa.dia.gas.plaf.jpbc.pairing.combiner.PairingAccumulatorFactory;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamReader;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class RLW12KemEngine extends PairingKeyEncapsulationMechanism {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof RLW12EncryptionParameters))
                throw new IllegalArgumentException("RLW12EncryptionParameters are required for encryption.");
        } else {
            if (!(key instanceof RLW12SecretKeyParameters))
                throw new IllegalArgumentException("RLW12SecretKeyParameters are required for decryption.");
        }

        RLW12KeyParameters rlKey = (RLW12KeyParameters) key;
        this.pairing = PairingFactory.getPairing(rlKey.getParameters().getParameters());

        this.keyBytes = pairing.getGT().getLengthInBytes();
//        this.outBytes = 2 * pairing.getGT().getLengthInBytes() + N * pairing.getG1().getLengthInBytes();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof RLW12SecretKeyParameters) {
            // Decrypt
            RLW12SecretKeyParameters sk = (RLW12SecretKeyParameters) key;

            PairingStreamReader streamParser = new PairingStreamReader(pairing, in, inOff);
            String w = streamParser.loadString();
            Element[] wEnc = streamParser.loadG1(((inLen - pairing.getGT().getLengthInBytes()) / pairing.getG1().getLengthInBytes()));
            Element cm = streamParser.loadGT();

            // Run the decryption...
            // Init
            PairingAccumulator combiner = PairingAccumulatorFactory.getInstance().getPairingMultiplier(pairing);

            int index = 0;
            combiner.addPairing(wEnc[index++], sk.getkStart(0))
                    .addPairingInverse(wEnc[index++], sk.getkStart(1));

            // Run
            int currentState = sk.getDfa().getInitialState(); // Initial state

            for (int i = 0; i < w.length(); i++) {
                DFATransition DFATransition = sk.getDfa().getTransition(currentState, w.charAt(i));

                combiner.addPairing(wEnc[index - 2], sk.getkTransition(DFATransition, 0))
                        .addPairing(wEnc[index++], sk.getkTransition(DFATransition, 2))
                        .addPairingInverse(wEnc[index++], sk.getkTransition(DFATransition, 1));

                currentState = DFATransition.getTo();
            }

            // Finalize
            if (sk.getDfa().isFinalState(currentState)) {
                combiner.addPairingInverse(wEnc[index++], sk.getkEnd(currentState, 0))
                        .addPairing(wEnc[index], sk.getkEnd(currentState, 1));

                // Recover the message...
                Element M = cm.div(combiner.doFinal());

                return M.toBytes();
            } else {
                return cm.toBytes();
            }
        } else {
            Element M = pairing.getGT().newRandomElement();

            // Encrypt the massage under the specified attributes
            RLW12EncryptionParameters encKey = (RLW12EncryptionParameters) key;
            RLW12PublicKeyParameters publicKey = encKey.getPublicKey();
            String w = encKey.getW();

            ByteArrayOutputStream bytes = new ByteArrayOutputStream(getOutputBlockSize());
            try {
                // Store M
                bytes.write(M.toBytes());

                // Store the ciphertext

                // Store w
                bytes.write(w.length());
                bytes.write(w.getBytes());

                // Initialize
                Element s0 = pairing.getZr().newRandomElement();
                bytes.write(
                        publicKey.getParameters().getG().powZn(s0).toBytes()
                );
                bytes.write(
                        publicKey.gethStart().powZn(s0).toBytes()
                );

                // Sequence
                Element sPrev = s0;
                for (int i = 0, l = w.length(); i < l; i++) {
                    Element sNext = pairing.getZr().newRandomElement();

                    bytes.write(
                            publicKey.getParameters().getG().powZn(sNext).toBytes()
                    );
                    bytes.write(
                            publicKey.getHAt(w.charAt(i)).powZn(sNext)
                                    .mul(publicKey.getZ().powZn(sPrev)).toBytes()
                    );

                    sPrev = sNext;
                }

                // Finalize
                bytes.write(
                        publicKey.getParameters().getG().powZn(sPrev).toBytes()
                );
                bytes.write(
                        publicKey.gethEnd().powZn(sPrev).toBytes()
                );

                // Store the masked message
                bytes.write(publicKey.getOmega().powZn(sPrev).mul(M).toBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return bytes.toByteArray();
        }
    }


}