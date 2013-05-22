package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.engines;

import it.unisa.dia.gas.crypto.engines.kem.PairingKeyEncapsulationMechanism;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing.CTL13MMElement;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing.CTL13MMPairing;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamReader;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingStreamWriter;

import java.io.IOException;
import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class GGHSW13KemEngine extends PairingKeyEncapsulationMechanism {

    public void initialize() {
        if (forEncryption) {
            if (!(key instanceof GGHSW13EncryptionParameters))
                throw new IllegalArgumentException("GGHSW13EncryptionParameters are required for encryption.");
        } else {
            if (!(key instanceof GGHSW13SecretKeyParameters))
                throw new IllegalArgumentException("GGHSW13SecretKeyParameters are required for decryption.");
        }

        GGHSW13KeyParameters gghswKey = (GGHSW13KeyParameters) key;

        this.pairing = gghswKey.getParameters().getPairing();
        this.keyBytes =
                ((CTL13MMPairing)gghswKey.getParameters().getPairing()).getCTL13MMInstance().getParameters().getBound()/8;
    }

    public byte[] process(byte[] in, int inOff, int inLen) {
        if (key instanceof GGHSW13SecretKeyParameters) {
            // Decrypt
            GGHSW13SecretKeyParameters sk = (GGHSW13SecretKeyParameters) key;

            // Load the ciphertext
            PairingStreamReader streamParser = new PairingStreamReader(pairing, in, inOff);

            String assignment = streamParser.readString();

            Element gs = streamParser.readG1Element();

            // compute hamming with
            Element[] cs = new Element[sk.getParameters().getN()];
            for (int i = 0; i < assignment.length(); i++)
                if (assignment.charAt(i) == '1')
                    cs[i] = streamParser.readG1Element();

            // Evaluate the circuit against the ciphertext
            Circuit circuit = sk.getCircuit();
            circuit.setEval(pairing.pairing(circuit.getKey(), gs));

            // evaluate the circuit
            for (Gate gate : sk.getCircuit()) {
                int index = gate.getIndex();

                switch (gate.getType()) {
                    case INPUT:
                        gate.setValue(assignment.charAt(index) == '1');

                        if (gate.isValue()) {
                            Element t1 = pairing.pairing(gate.getKeyAt(0), gs);
                            Element t2 = pairing.pairing(gate.getKeyAt(1), cs[index]);

                            gate.setEval(t1.mul(t2));
                        }
                        break;

                    case OR:

                        if (gate.getInputAt(0).isValue()) {
                            Element t1 = pairing.pairing(
                                    gate.getInputAt(0).getEval(),
                                    gate.getKeyAt(0)
                            );

                            Element t2 = pairing.pairing(
                                    gate.getKeyAt(2),
                                    gs
                            );

                            gate.setEval(t1.mul(t2));

                        } else if (gate.getInputAt(1).isValue()) {
                            Element t1 = pairing.pairing(
                                    gate.getInputAt(1).getEval(),
                                    gate.getKeyAt(1)
                            );

                            Element t2 = pairing.pairing(
                                    gate.getKeyAt(3),
                                    gs
                            );

                            gate.setEval(t1.mul(t2));
                        }
                        gate.eval();
                        break;

                    case AND:
                        Element t1 = pairing.pairing(
                                gate.getInputAt(0).getEval(),
                                gate.getKeyAt(0)
                        );

                        Element t2 = pairing.pairing(
                                gate.getInputAt(1).getEval(),
                                gate.getKeyAt(1)
                        );

                        Element t3 = pairing.pairing(
                                gate.getKeyAt(2),
                                gs
                        );

                        gate.eval();
                        gate.setEval(t1.mul(t2).mul(t3));

                        break;
                }
            }

            Element result = circuit.getEval().mul(circuit.getOutputGate().getEval());

            // extract key from result
            BigInteger value =  ((CTL13MMPairing) pairing).getCTL13MMInstance().extract(
                    result.toBigInteger(),
                    ((CTL13MMElement) result).getIndex());

            return value.toByteArray();
        } else {
            // Encrypt the massage under the specified attributes
            GGHSW13EncryptionParameters encKey = (GGHSW13EncryptionParameters) key;
            GGHSW13PublicKeyParameters publicKey = encKey.getPublicKey();
            String assignment = encKey.getAssignment();

            PairingStreamWriter writer = new PairingStreamWriter(getOutputBlockSize());
            try {

                // Sample the randomness
                Element s = pairing.getZr().newRandomElement().getImmutable();

                Element mask = publicKey.getH().powZn(s);

                BigInteger value = ((CTL13MMPairing) pairing).getCTL13MMInstance().extract(mask.toBigInteger(), pairing.getDegree());
                System.out.println("value = " + value);
                writer.write(value.toByteArray());

                // Store assignment
                writer.write(assignment);
                writer.write(pairing.getFieldAt(1).newElement().powZn(s));
                int n = publicKey.getParameters().getN();
                for (int i = 0; i < n; i++) {
                    if (assignment.charAt(i) == '1')
                        writer.write(publicKey.getHAt(i).powZn(s));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return writer.toBytes();
        }
    }


}