package it.unisa.dia.gas.crypto.circuit;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class BooleanCircuit implements Circuit<BooleanGate> {

    private final int n;
    private final int q;
    private final int depth;
    private final BooleanGate[] gates;

    public BooleanCircuit(final int n,
                          final int q,
                          final int depth,
                          final BooleanCircuitGate[] gates) {
        this.n = n;
        this.q = q;
        this.depth = depth;
        this.gates = gates;

        for (final BooleanCircuitGate gate : gates)
            gate.setCircuit(this);
    }

    @Override
    public int getN() {
        return n;
    }

    @Override
    public int getQ() {
        return q;
    }

    @Override
    public int getDepth() {
        return depth;
    }

    @Override
    public Iterator<BooleanGate> iterator() {
        return Arrays.asList(gates).iterator();
    }

    @Override
    public BooleanGate getGateAt(final int index) {
        return gates[index];
    }

    @Override
    public BooleanGate getOutputGate() {
        return gates[n + q - 1];
    }

    public static class BooleanCircuitGate implements BooleanGate {

        private BooleanCircuit circuit;

        private final Type type;
        private final int index;
        private final int depth;

        private int[] inputs;
        private boolean value;

        public BooleanCircuitGate(final Type type, final int index, final int depth) {
            this.type = type;
            this.index = index;
            this.depth = depth;
        }

        public BooleanCircuitGate(Type type, int index, int depth, int[] inputs) {
            this.type = type;
            this.index = index;
            this.depth = depth;
            this.inputs = Arrays.copyOf(inputs, inputs.length);
        }

        public int getInputNum() {
            return inputs.length;
        }

        @Override
        public Type getType() {
            return type;
        }

        @Override
        public int getIndex() {
            return index;
        }

        @Override
        public int getDepth() {
            return depth;
        }

        @Override
        public int getInputIndexAt(final int index) {
            return inputs[index];
        }

        @Override
        public BooleanGate getInputAt(final int index) {
            return circuit.getGateAt(getInputIndexAt(index));
        }

        @Override
        public BooleanGate set(final Boolean value) {
            this.value = value;
            return this;
        }

        @Override
        public Boolean get() {
            return value;
        }

        @Override
        public BooleanGate evaluate() {
            switch (type) {
                case AND: {
                    this.value = getInputAt(0).get() && getInputAt(1).get();
                    break;
                }
                case OR: {
                    this.value = getInputAt(0).get() || getInputAt(1).get();
                    break;
                }
                case NAND: {
                    this.value = !(getInputAt(0).get() && getInputAt(1).get());
                    break;
                }
                default: {
                    throw new IllegalStateException("Invalid type");
                }
            }

            return this;
        }

        @Override
        public String toString() {
            return "BooleanGate{" +
                    "type=" + type +
                    ", index=" + index +
                    ", depth=" + depth +
                    ", inputs=" + Arrays.toString(inputs) +
                    ", value=" + value +
                    '}';
        }

        @Override
        public Gate<Boolean> putAt(final int index, final Boolean value) {
            throw new IllegalStateException("Not implemented yet!!!");
        }

        @Override
        public Boolean getAt(int index) {
            throw new IllegalStateException("Not implemented yet!!!");
        }

        protected void setCircuit(final BooleanCircuit circuit) {
            this.circuit = circuit;
        }

    }

}
