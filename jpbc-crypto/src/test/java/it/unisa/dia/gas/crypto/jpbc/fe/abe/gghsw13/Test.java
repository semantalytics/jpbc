package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13;

import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.parameters.Circuit;
import it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.parameters.Gate;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.generators.CTL13MMInstanceGenerator;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.pairing.CTL13MMPairing;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13InstanceParameters;
import it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters.CTL13MMInstance;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Collection;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
@RunWith(value = Parameterized.class)
public class Test {

    static SecureRandom random;

    static {
        random = new SecureRandom();

    }

    @Parameterized.Parameters
    public static Collection parameters() {
        Object[][] data = {
                {CTL13InstanceParameters.TOY.setKappa(4)}
        };

        return Arrays.asList(data);
    }


    protected CTL13InstanceParameters instanceParameters;


    public Test(CTL13InstanceParameters instanceParameters) {
        this.instanceParameters = instanceParameters;
    }


    @org.junit.Test
    public void testEngine() {
        CTL13MMInstance instance = new CTL13MMInstanceGenerator(random, instanceParameters).generateInstance();

        Pairing pairing = new CTL13MMPairing(instance);

        // =============== Setup ==================

        // Sample secret key
        Element alpha = pairing.getFieldAt(0).newRandomElement().getImmutable();

        // Sample public key
        int n = 2;
        Element[] hs = new Element[n];
        for (int i = 0; i < hs.length; i++)
            hs[i] = pairing.getFieldAt(1).newRandomElement().getImmutable();

        Element H = pairing.getFieldAt(pairing.getDegree()).newElement().powZn(alpha).getImmutable();

        // =============== Enc ==================

        // sample the randomness
        Element s = pairing.getFieldAt(0).newRandomElement();

        // generate the ciphertext for x = 1101

//        Element cM = pairing.getFieldAt(pairing.getDegree()).newRandomElement();    // Encode 0
        Element cM = H.powZn(s);    // Encode 1


        Element gs = pairing.getFieldAt(1).newElement().powZn(s);

        boolean[] xs = new boolean[]{true, true, false, true};

        Element[] cs = new Element[n];
        for (int i = 0; i < n; i++) {
            if (xs[i])
                cs[i] = hs[i].powZn(s).getImmutable();
        }

        // =============== KeyGen ==================

        int q = 1;
        Circuit circuit = new Circuit(n, q, 2, new Gate[]{
                new Gate(Gate.Type.INPUT, 0, 0, null),
                new Gate(Gate.Type.INPUT, 1, 0, null),
//                new Gate(Gate.Type.INPUT, 2, 0, null),
//                new Gate(Gate.Type.INPUT, 3, 0, null),

                new Gate(Gate.Type.AND, 2, 1, new int[]{0, 1}),
//                new Gate(Gate.Type.OR, 5, 1, new int[]{2, 3}),

//                new Gate(Gate.Type.AND, 6, 2, new int[]{4, 5}),
        });

        // sample the randomness
        Element[] rs = new Element[n + q];
        for (int i = 0; i < rs.length; i++)
            rs[i] = pairing.getFieldAt(0).newRandomElement().getImmutable();

        circuit.setKey(pairing.getFieldAt(circuit.getDepth()).newElement().powZn(alpha.sub(rs[rs.length - 1])));

        // encode the circuit
        for (Gate gate : circuit) {
            System.out.println("encode gate = " + gate);

            int index = gate.getIndex();
            int depth = gate.getDepth();

            switch (gate.getType()) {
                case INPUT:

                    Element zw = pairing.getFieldAt(0).newRandomElement();
                    Element e1 = pairing.getFieldAt(1).newElement().powZn(rs[index]).add(hs[index].powZn(zw));
                    Element e2 = pairing.getFieldAt(1).newElement().powZn(zw.negate());

                    gate.setKeys(e1, e2);
                    break;

                case OR:
                    Element a = pairing.getFieldAt(0).newRandomElement();
                    Element b = pairing.getFieldAt(0).newRandomElement();

                    e1 = pairing.getFieldAt(1).newElement().powZn(a);
                    e2 = pairing.getFieldAt(1).newElement().powZn(b);

                    Element e3 = pairing.getFieldAt(depth).newElement().powZn(
                            rs[index].sub(a.mul(rs[gate.getInputIndexAt(0)]))
                    );
                    Element e4 = pairing.getFieldAt(depth).newElement().powZn(
                            rs[index].sub(b.mul(rs[gate.getInputIndexAt(1)]))
                    );

                    gate.setKeys(e1, e2, e3, e4);
                    break;

                case AND:
                    a = pairing.getFieldAt(0).newRandomElement();
                    b = pairing.getFieldAt(0).newRandomElement();

                    e1 = pairing.getFieldAt(1).newElement().powZn(a);
                    e2 = pairing.getFieldAt(1).newElement().powZn(b);

                    e3 = pairing.getFieldAt(depth).newElement().powZn(
                            rs[index].sub(a.mul(rs[gate.getInputIndexAt(0)]))
                                    .sub(b.mul(rs[gate.getInputIndexAt(1)]))
                    );

                    gate.setKeys(e1, e2, e3);
                    break;
            }
        }


        // =============== Dec ==================

        circuit.setEval(pairing.pairing(circuit.getKey(), gs));

        // evaluate the circuit
        for (Gate gate : circuit) {
            System.out.println("evaluate gate = " + gate);

            int index = gate.getIndex();

            switch (gate.getType()) {
                case INPUT:
                    gate.setValue(xs[index]);

                    if (gate.isValue()) {
                        Element t1 = pairing.pairing(gate.getKeyAt(0), gs);
                        Element t2 = pairing.pairing(gate.getKeyAt(1), cs[index]);

                        gate.setEval(t1.add(t2));
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

                        gate.setEval(t1.add(t2));

                    } else if (gate.getInputAt(1).isValue()) {
                        Element t1 = pairing.pairing(
                                gate.getInputAt(1).getEval(),
                                gate.getKeyAt(1)
                        );

                        Element t2 = pairing.pairing(
                                gate.getKeyAt(3),
                                gs
                        );

                        gate.setEval(t1.add(t2));
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
                    gate.setEval(t1.add(t2).add(t3));

                    break;
            }
        }

        System.out.println("circuit.getOutputGate().isValue() = " + circuit.getOutputGate().isValue());

        if (circuit.getOutputGate().isValue()) {
            Element result = circuit.getEval().add(circuit.getOutputGate().getEval()).sub(cM);
            System.out.println("result = " + result);
            if (result.isZero())
                System.out.println("1");
            else
                System.out.println("0");
        }
    }

}
