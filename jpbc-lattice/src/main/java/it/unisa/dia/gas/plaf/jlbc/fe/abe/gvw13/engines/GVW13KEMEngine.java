package it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.engines;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.crypto.circuit.Circuit;
import it.unisa.dia.gas.crypto.jpbc.kem.PairingKeyEncapsulationMechanism;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params.GVW13EncryptionParameters;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params.GVW13KeyParameters;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params.GVW13PublicKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params.GVW13SecretKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamReader;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13KEMEngine extends PairingKeyEncapsulationMechanism {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof GVW13EncryptionParameters))
                throw new IllegalArgumentException("GVW13EncryptionParameters are required for encryption.");
        } else {
            if (!(key instanceof GVW13SecretKeyParameters))
                throw new IllegalArgumentException("GVW13SecretKeyParameters are required for decryption.");
        }

        GVW13KeyParameters gghswKey = (GVW13KeyParameters) key;

        this.keyBytes = pairing.getFieldAt(pairing.getDegree()).getCanonicalRepresentationLengthInBytes();
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof GVW13SecretKeyParameters) {
            // Decrypt
            GVW13SecretKeyParameters sk = (GVW13SecretKeyParameters) key;

            // Load the ciphertext
            PairingStreamReader reader = new PairingStreamReader(pairing, in, inOff);

            String assignment = reader.readString();

            // Evaluate the circuit against the ciphertext
            Circuit circuit = sk.getCircuit();

            // evaluate the circuit
            Map<Integer, Element> evaluations = new HashMap<Integer, Element>();
            for (Circuit.Gate gate : sk.getCircuit()) {
                int index = gate.getIndex();

                switch (gate.getType()) {
                    case INPUT:
                        gate.set(assignment.charAt(index) == '1');
                        break;

                    case OR:
                    case AND:
                        gate.evaluate();

                        try {
                            ElementCipher cipher = sk.getParameters().getTor();
                            cipher.init(sk.getCipherParametersAt(index, gate.getInputAt(0).isSet()? 1 : 0, gate.getInputAt(1).isSet()? 1 : 0));
                            evaluations.put(
                                    index,
                                    cipher.processElements(
                                            evaluations.get(gate.getInputIndexAt(0)),
                                            evaluations.get(gate.getInputIndexAt(1))
                                    )
                            );
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                        break;
                }
            }

            if (circuit.getOutputGate().isSet()) {
                return evaluations.get(circuit.getOutputGate().getIndex()).toBytes();
            } else
                return new byte[]{-1};
        } else {
            // Encrypt the massage under the specified attributes
            GVW13EncryptionParameters encKey = (GVW13EncryptionParameters) key;
            GVW13PublicKeyParameters publicKey = encKey.getPublicKey();
            ElementCipher cipher = publicKey.getParameters().getTor();
            String assignment = encKey.getAssignment();

            PairingStreamWriter writer = new PairingStreamWriter(getOutputBlockSize());
            try {
                Element s = publicKey.getParameters().getRandomnessField().newRandomElement();

                writer.write(assignment);
                for (int i = 0, n = assignment.length(); i < n; i++) {
                    cipher.init(publicKey.getCipherParametersAt(i, assignment.charAt(i) == '1'));
                    writer.write(cipher.processElements(s));
                }

                cipher.init(publicKey.getCipherParametersOut());
                Element key = cipher.processElements(s);

                // TODO: encrypt message using key

                writer.write(key);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return writer.toBytes();
        }
    }


}