package it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.generators;

import it.unisa.dia.gas.crypto.circuit.ArithmeticCircuit;
import it.unisa.dia.gas.crypto.circuit.ArithmeticGate;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params.BNS14MasterSecretKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params.BNS14PublicKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params.BNS14SecretKeyGenerationParameters;
import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params.BNS14SecretKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines.MP12HLP2MatrixLeftSampler;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines.MP12PLP2MatrixSolver;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2SampleLeftParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BNS14SecretKeyGenerator {
    private BNS14SecretKeyGenerationParameters param;

    private ArithmeticCircuit circuit;

    public void init(KeyGenerationParameters param) {
        this.param = (BNS14SecretKeyGenerationParameters) param;

        this.circuit = this.param.getCircuit();
    }

    public CipherParameters generateKey() {
        BNS14MasterSecretKeyParameters msk = param.getMasterSecretKeyParameters();
        BNS14PublicKeyParameters pk = param.getPublicKeyParameters();

        ArithmeticCircuit circuit = this.circuit;

        // encode the circuit
        Map<Integer, Element> keys = new HashMap<Integer, Element>();

        MP12PLP2MatrixSolver sampleD = new MP12PLP2MatrixSolver();
        sampleD.init(pk.getPrimitiveLatticePk());

        for (ArithmeticGate gate : circuit) {
            int index = gate.getIndex();

            switch (gate.getType()) {
                case INPUT:
                    keys.put(index, pk.getBAt(index));
                    break;

                case OR:
                    // addition
                    Element B = pk.getBAt(0).getField().newZeroElement();
                    for (int j = 0, k = gate.getInputNum(); j < k; j++) {
                        Element R = sampleD.processElements(
                                // \alpha_i G
                                pk.getPrimitiveLatticePk().getG().duplicate().mulZn(gate.getAlphaAt(j))
                        );
                        gate.putAt(j, R);
                        B.add(keys.get(gate.getInputIndexAt(j)).mul(R));
                    }

                    keys.put(index, B);
                    break;

                case AND:
                    // multiplication

                    // Compute R_0 = SolveR(G, T_G, \alpha G)
                    Element R = sampleD.processElements(
                            pk.getPrimitiveLatticePk().getG().duplicate().mulZn(gate.getAlphaAt(0))
                    );
                    for (int j = 1, k = gate.getInputNum(); j < k; j++) {
                        // R_j = SolveR(G, T_G, - B_{j-1} R_{j-1})
                        R = sampleD.processElements(pk.getBAt(j - 1).mul(R).negate());
                    }

                    // Compute B_g = B_{k-1} R_{k-1}
                    keys.put(index, pk.getBAt(gate.getInputNum() - 1).mul(R));
                    break;
            }
        }

        circuit.getOutputGate().putAt(-1, keys.get(circuit.getOutputGate().getIndex()));

        // SampleLeft

        MP12HLP2MatrixLeftSampler sampler = new MP12HLP2MatrixLeftSampler();
        sampler.init(new MP12HLP2SampleLeftParameters(pk.getLatticePk(), msk.getLatticeSk()));

        Element skC = sampler.processElements(
                keys.get(circuit.getOutputGate().getIndex()),
                pk.getD()
        );

        Element F = MatrixField.unionByCol(pk.getLatticePk().getA(), keys.get(circuit.getOutputGate().getIndex()));
        Element DPrime = F.mul(skC);
        System.out.println(DPrime.equals(pk.getD()));

        return new BNS14SecretKeyParameters(pk, circuit, skC);
    }

}