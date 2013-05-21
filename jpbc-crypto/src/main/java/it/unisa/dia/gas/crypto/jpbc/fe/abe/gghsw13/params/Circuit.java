package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params;

import it.unisa.dia.gas.jpbc.Element;

import java.util.Arrays;
import java.util.Iterator;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class Circuit implements Iterable<Gate> {

    private int n, q;
    private int depth;
    private Element key;
    private Element eval;

    private Gate[] gates;

    public Circuit(int n, int q, int depth, Gate[] gates) {
        this.n = n;
        this.q = q;
        this.depth = depth;

        this.gates = gates;
        for (Gate gate : gates)
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


    public Iterator<Gate> iterator() {
        return Arrays.asList(gates).iterator();
    }

    public Gate getGateAt(int index) {
        return gates[index];
    }

    public Gate getOutputGate() {
        return gates[n+q-1];
    }


    public Element getKey() {
        return key;
    }

    public void setKey(Element key) {
        this.key = key;
    }


    public void setEval(Element eval) {
        this.eval = eval.getImmutable();
    }

    public Element getEval() {
        return eval;
    }

    public boolean accept(String assignment) {
        // TODO:
        return false;  //To change body of created methods use File | Settings | File Templates.
    }

    public Circuit duplicate() {
        // TODO:
        return this;
    }
}
