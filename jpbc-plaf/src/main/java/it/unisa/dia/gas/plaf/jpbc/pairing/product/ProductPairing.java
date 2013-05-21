package it.unisa.dia.gas.plaf.jpbc.pairing.product;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import it.unisa.dia.gas.plaf.jpbc.pairing.combiner.PairingAccumulator;
import it.unisa.dia.gas.plaf.jpbc.pairing.combiner.PairingAccumulatorFactory;
import it.unisa.dia.gas.plaf.jpbc.pairing.combiner.ProductPairingAccumulator;
import it.unisa.dia.gas.plaf.jpbc.pairing.map.AbstractMillerPairingPreProcessing;

import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ProductPairing implements Pairing {
    protected Pairing basePairing;
    protected int n;

    protected Field G1, G2;

    public ProductPairing(Random random, Pairing basePairing, int n) {
        this.basePairing = basePairing;

        this.G1 = new VectorField(random, basePairing.getG1(), n);
        this.G2 = new VectorField(random, basePairing.getG2(), n);
    }


    public boolean isSymmetric() {
        return basePairing.isSymmetric();
    }

    public Field getG1() {
        return G1;
    }

    public Field getG2() {
        return G2;
    }

    public Field getGT() {
        return basePairing.getGT();
    }

    public Field getZr() {
        return basePairing.getZr();
    }

    public int getDegree() {
        return 2;
    }

    public Field getFieldAt(int index) {
        switch (index) {
            case 0:
                return basePairing.getZr();
            case 1:
                return G1;
            case 2:
                return G2;
            case 3:
                return basePairing.getGT();
            default:
                throw new IllegalArgumentException("invalid index");
        }
    }

    public Element pairing(Element in1, Element in2) {
        Vector v1 = (Vector) in1;
        Vector v2 = (Vector) in2;

        PairingAccumulator combiner = (basePairing.isProductPairingSupported()) ? new ProductPairingAccumulator(basePairing, v1.getSize())
                : PairingAccumulatorFactory.getInstance().getPairingMultiplier(basePairing);
        for (int i = 0; i < v1.getSize(); i++) {
            combiner.addPairing(v1.getAt(i), v2.getAt(i));
        }
        return combiner.doFinal();
    }

    public boolean isProductPairingSupported() {
        return false;
    }

    public Element pairing(Element[] in1, Element[] in2) {
        PairingAccumulator combiner = PairingAccumulatorFactory.getInstance().getPairingMultiplier(basePairing);
        for (int i = 0; i < in1.length; i++) {
            combiner.addPairing(in1[i], in2[i]);
        }
        return combiner.doFinal();
    }

    public PairingPreProcessing pairing(final Element in1) {
        return new AbstractMillerPairingPreProcessing() {
            public Element pairing(Element in2) {
                return ProductPairing.this.pairing(in1, in2);
            }
        };
    }

    public int getPairingPreProcessingLengthInBytes() {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public PairingPreProcessing pairing(byte[] source) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public PairingPreProcessing pairing(byte[] source, int offset) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public boolean isAlmostCoddh(Element a, Element b, Element c, Element d) {
        throw new IllegalStateException("Not implemented yet!!!");
    }

    public PairingFieldIdentifier getPairingFieldIdentifier(Field field) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Field getField(PairingFieldIdentifier id) {
        switch (id) {
            case G1:
                return G1;
            case G2:
                return G2;
            case GT:
                return basePairing.getGT();
            default:
                throw new IllegalArgumentException("Invalid Identifier.");
        }
    }
}