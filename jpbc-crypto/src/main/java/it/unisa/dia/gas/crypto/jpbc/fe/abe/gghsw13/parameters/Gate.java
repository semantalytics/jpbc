package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.parameters;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Arrays;

/**
* @author Angelo De Caro (angelo.decaro@gmail.com)
* @since 1.3.0
*/
public class Gate {

    public static enum Type {INPUT, AND, OR}

    private Circuit circuit;

    private Type type;
    private int index;
    private int depth;
    private int[] inputs;

    private Element[] keys;
    private Element eval;

    private boolean value;


    public Gate(Type type, int index, int depth, int[] inputs) {
        this.type = type;
        this.index = index;
        this.depth = depth;
        this.inputs = inputs;
    }


    public void setCircuit(Circuit circuit) {
        this.circuit = circuit;
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

    public Gate getInputAt(int index) {
        return circuit.getGateAt(getInputIndexAt(index));
    }


    public void setKeys(Element... keys) {
        this.keys = keys;
    }

    public Element getKeyAt(int index) {
        return keys[index];
    }


    public void setEval(Element eval) {
        this.eval = eval.getImmutable();
    }

    public Element getEval() {
        return eval;
    }


    public void setValue(boolean value) {
        this.value = value;
    }

    public boolean isValue() {
        return value;
    }

    public Gate eval() {
        switch (type) {
            case AND :
                this.value =
                        circuit.getGateAt(getInputIndexAt(0)).isValue() &&
                                circuit.getGateAt(getInputIndexAt(1)).isValue();
                break;

            case OR :
                this.value =
                        circuit.getGateAt(getInputIndexAt(0)).isValue() ||
                                circuit.getGateAt(getInputIndexAt(1)).isValue();
                break;

            default:
                throw new IllegalStateException("Invalid type");
        }

        return this;
    }

    @Override
    public String toString() {
        return "Gate{" +
                "type=" + type +
                ", index=" + index +
                ", depth=" + depth +
                ", inputs=" + Arrays.toString(inputs) +
                ", keys=" + ( (keys == null) ? 0 : keys.length ) +
                ", value=" + value +
                '}';
    }
}
