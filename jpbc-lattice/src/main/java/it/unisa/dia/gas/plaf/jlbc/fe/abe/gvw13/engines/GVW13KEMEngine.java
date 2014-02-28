package it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.engines;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.crypto.circuit.Circuit;
import it.unisa.dia.gas.crypto.jpbc.kem.AbstractKeyEncapsulationMechanism;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params.GVW13EncryptionParameters;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params.GVW13PublicKeyParameters;
import it.unisa.dia.gas.plaf.jlbc.fe.abe.gvw13.params.GVW13SecretKeyParameters;
import it.unisa.dia.gas.plaf.jpbc.util.io.ElementStreamReader;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class GVW13KEMEngine extends AbstractKeyEncapsulationMechanism {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof GVW13EncryptionParameters))
                throw new IllegalArgumentException("GVW13EncryptionParameters are required for encryption.");

            GVW13EncryptionParameters encKey = (GVW13EncryptionParameters) key;
            GVW13PublicKeyParameters publicKey = encKey.getPublicKey();

            this.keyBytes = 128; //((TORGVW13PublicKeyParameters)publicKey.getCipherParametersOut()).getOwfOutputField().getLengthInBytes();
            this.outBytes = (encKey.getAssignment().length() + 1) * keyBytes;
        } else {
            if (!(key instanceof GVW13SecretKeyParameters))
                throw new IllegalArgumentException("GVW13SecretKeyParameters are required for decryption.");
        }
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof GVW13SecretKeyParameters) {
            // Decrypt
            GVW13SecretKeyParameters sk = (GVW13SecretKeyParameters) key;

            // Load the ciphertext
            ElementStreamReader reader = new ElementStreamReader(in, inOff);

            String assignment = reader.readString();

            // Evaluate the circuit against the ciphertext
            Circuit circuit = sk.getCircuit();

            Element e = reader.readElement(sk.getCiphertextElementField());
            System.out.println("element = " + e);

            // evaluate the circuit
            Map<Integer, Element> evaluations = new HashMap<Integer, Element>();
            for (Circuit.Gate gate : sk.getCircuit()) {
                int index = gate.getIndex();

                switch (gate.getType()) {
                    case INPUT:
                        gate.set(assignment.charAt(index) == '1');

                        // Read input
                        Element element = reader.readElement(sk.getCiphertextElementField());
                        System.out.println("element = " + element);
                        evaluations.put(index, element);

                        break;
                    case OR:
                    case AND:
                        gate.evaluate();

                        // Init TOR for recoding
                        ElementCipher tor = sk.getParameters().getTor();
                        tor.init(sk.getCipherParametersAt(index, gate.getInputAt(0).isSet() ? 1 : 0, gate.getInputAt(1).isSet() ? 1 : 0));

                        evaluations.put(
                                index,
                                // recode
                                tor.processElements(
                                        evaluations.get(gate.getInputIndexAt(0)),
                                        evaluations.get(gate.getInputIndexAt(1))
                                )
                        );
                        break;
                }
            }

            Element key = evaluations.get(circuit.getOutputGate().getIndex());

            System.out.println("decrypted key = " + key);

            ElementCipher tor = sk.getParameters().getTor();
            tor.init(sk.getCipherParametersOut());
            tor.init(key);
            return tor.processElementsToBytes(e);
            // Decrypt key

//            if (circuit.getOutputGate().isSet()) {
                // use key to decrypt e


//            } else
//                return evaluations.get(circuit.getOutputGate().getIndex()).toBytes();
        } else {
            GVW13EncryptionParameters encKey = (GVW13EncryptionParameters) key;
            GVW13PublicKeyParameters publicKey = encKey.getPublicKey();

            ElementCipher tor = publicKey.getParameters().getTor();
            String assignment = encKey.getAssignment();

            PairingStreamWriter writer = new PairingStreamWriter(getOutputBlockSize());
            try {
                Element s = publicKey.getParameters().getRandomnessField().newRandomElement();

                // choose random bit string
                byte[] bytes = new byte[128];
                publicKey.getParameters().getRandom().nextBytes(bytes);
                writer.write(bytes);

                // encrypt bytes
                tor.init(publicKey.getCipherParametersOut());
                Element key = tor.processElements(s);
                System.out.println("key = " + key);

                tor.init(key);
                Element e = tor.processBytes(bytes);

                writer.write(assignment);
                writer.write(e);
                for (int i = 0, n = assignment.length(); i < n; i++) {
                    // init for encoding
                    tor.init(publicKey.getCipherParametersAt(i, assignment.charAt(i) == '1'));
                    // encode
                    e = tor.processElements(s);

                    System.out.println("e = " + e);
                    writer.write(e);
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            return writer.toBytes();
        }
    }


}