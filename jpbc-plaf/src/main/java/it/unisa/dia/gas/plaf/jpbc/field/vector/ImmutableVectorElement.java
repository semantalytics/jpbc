package it.unisa.dia.gas.plaf.jpbc.field.vector;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ImmutableVectorElement<E extends Element> extends VectorElement<E> {
    
    public ImmutableVectorElement(VectorElement element) {
        super(element.field);
        this.field = element.field;

        this.coeff.clear();
        for (int i = 0; i < field.n; i++)
            coeff.add((E) element.getAt(i).getImmutable());

        this.immutable = true;
    }

    @Override
    public VectorElement<E> duplicate() {
        return super.duplicate();
    }

    @Override
    public Element getImmutable() {
        return this;
    }

    @Override
    public VectorElement set(Element e) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public VectorElement set(int value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public VectorElement set(BigInteger value) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public VectorElement twice() {
        return (VectorElement) super.duplicate().twice().getImmutable();    
    }

    @Override
    public VectorElement setToZero() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public VectorElement setToOne() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public VectorElement setToRandom() {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public VectorElement square() {
        return (VectorElement) super.duplicate().square().getImmutable();
    }

    @Override
    public VectorElement invert() {
        return (VectorElement) super.duplicate().invert().getImmutable();
    }

    @Override
    public VectorElement negate() {
        return (VectorElement) super.duplicate().negate().getImmutable();
    }

    @Override
    public VectorElement add(Element e) {
        return (VectorElement) super.duplicate().add(e).getImmutable();
    }

    @Override
    public VectorElement mul(Element e) {
        return (VectorElement) super.duplicate().mul(e).getImmutable();
    }

    @Override
    public VectorElement mul(BigInteger n) {
        return (VectorElement) super.duplicate().mul(n).getImmutable();
    }

    @Override
    public VectorElement mulZn(Element e) {
        return (VectorElement) super.duplicate().mulZn(e).getImmutable();
    }

    @Override
    public VectorElement powZn(Element e) {
        return (VectorElement) super.duplicate().powZn(e).getImmutable();
    }

    @Override
    public VectorElement setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public Element pow(BigInteger n) {
        return (VectorElement) super.duplicate().pow(n).getImmutable();
    }

    @Override
    public Element halve() {
        return (VectorElement) super.duplicate().halve().getImmutable();
    }

    @Override
    public VectorElement sub(Element element) {
        return (VectorElement) super.duplicate().sub(element).getImmutable();
    }

    @Override
    public Element div(Element element) {
        return (VectorElement) super.duplicate().div(element).getImmutable();
    }

    @Override
    public VectorElement mul(int z) {
        return (VectorElement) super.duplicate().mul(z).getImmutable();
    }

    @Override
    public VectorElement sqrt() {
        return (VectorElement) super.duplicate().sqrt().getImmutable();
    }
    
}
