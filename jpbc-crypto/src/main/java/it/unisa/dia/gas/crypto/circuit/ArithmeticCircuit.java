package it.unisa.dia.gas.crypto.circuit;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.plaf.jpbc.util.ElementUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ArithmeticCircuit implements Circuit<ArithmeticGate> {

    private int n;
    private int q;
    private int depth;
    private ArithmeticGate[] gates;

    public ArithmeticCircuit(final int n,
                             final int q,
                             final int depth,
                             final ArithmeticCircuitGate[] gates) {
        this.n = n;
        this.q = q;
        this.depth = depth;
        this.gates = gates;

        for (final ArithmeticCircuitGate gate : gates)
            gate.setCircuit(this);
    }


    public int getN() {
        return n;
    }

    public int getQ() {
        return q;
    }

    public int getDepth() {
        return depth;
    }

    public Iterator<ArithmeticGate> iterator() {
        return Arrays.asList(gates).iterator();
    }

    public ArithmeticGate getGateAt(final int index) {
        return gates[index];
    }

    public ArithmeticGate getOutputGate() {
        return gates[n + q - 1];
    }

    public Element evaluate(final Element... inputs) {
        for (final ArithmeticGate gate : gates) {
            switch (gate.getType()) {
                case INPUT:
                    gate.set(inputs[gate.getIndex()]);
                    break;
                case OR:
                case AND:
                    gate.evaluate();
                    break;
            }
        }

        return getOutputGate().get();
    }


    public static class ArithmeticCircuitGate implements ArithmeticGate {

        private ArithmeticCircuit circuit;
        private final Type type;
        private final int index;
        private final int depth;
        private final Map<Integer, Element> values;

        private Element value;
        private Element[] alphas;
        private int[] inputs;

        public ArithmeticCircuitGate(final Type type,
                                     final int index,
                                     final int depth) {
            this.type = type;
            this.index = index;
            this.depth = depth;

            this.values = new HashMap<Integer, Element>();
        }

        public ArithmeticCircuitGate(final Type type,
                                     final int index,
                                     final int depth,
                                     final int[] inputs,
                                     final Element... alphas) {
            this.type = type;
            this.index = index;
            this.depth = depth;

            this.values = new HashMap<Integer, Element>();

            this.inputs = Arrays.copyOf(inputs, inputs.length);
            this.alphas = ElementUtils.cloneImmutable(alphas);
        }

        public int getInputNum() {
            return inputs.length;
        }

        public Element get() {
            return value;
        }

        public Element getAlphaAt(int index) {
            return alphas[index];
        }

        public Type getType() {
            return type;
        }

        public int getIndex() {
            return index;
        }

        public int getDepth() {
            return depth;
        }

        public int getInputIndexAt(int index) {
            return inputs[index];
        }

        public ArithmeticGate getInputAt(int index) {
            return circuit.getGateAt(getInputIndexAt(index));
        }


        public ArithmeticGate set(final Element value) {
            this.value = value;
            return this;
        }

        public ArithmeticGate evaluate() {
            switch (type) {
                case AND:
                    value = getInputAt(0).get().duplicate().mul(getAlphaAt(0));
                    for (int i = 1; i < inputs.length; i++) {
                        value.mul(getInputAt(i).get());
                    }
                    break;

                case OR:
                    value = getInputAt(0).get().duplicate().mul(getAlphaAt(0));
                    for (int i = 1; i < inputs.length; i++) {
                        value.add(getInputAt(i).get().duplicate().mul(getAlphaAt(i)));
                    }
                    break;

                default:
                    throw new IllegalStateException("Invalid type");
            }

            return this;
        }

        @Override
        public String toString() {
            return "ArithmeticGate{" +
                    "type=" + type +
                    ", index=" + index +
                    ", depth=" + depth +
                    ", inputs=" + Arrays.toString(inputs) +
                    ", value=" + value +
                    '}';
        }


        public Gate<Element> putAt(final int index, final Element value) {
            values.put(index, value);
            return this;
        }

        public Element getAt(final int index) {
            return values.get(index);
        }

        protected void setCircuit(final ArithmeticCircuit circuit) {
            this.circuit = circuit;
        }

    }

}
