package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ImmutableQuadraticElement<E extends Element> extends QuadraticElement<E> {

    public ImmutableQuadraticElement(QuadraticElement<E> element) {
        super(element.getField());

        this.x = (E) element.getX().getImmutable();
        this.y = (E) element.getY().getImmutable();

        this.immutable = true;
    }

    @Override
    public QuadraticElement duplicate() {
        return super.duplicate();
    }

    @Override
    public QuadraticElement set(Element e) {
        return (QuadraticElement) super.duplicate().set(e).getImmutable();    
    }

    @Override
    public QuadraticElement set(int value) {
        return (QuadraticElement) super.duplicate().set(value).getImmutable();    
    }

    @Override
    public QuadraticElement set(BigInteger value) {
        return (QuadraticElement) super.duplicate().set(value).getImmutable();    
    }

    @Override
    public QuadraticElement setToZero() {
        return (QuadraticElement) super.duplicate().setToZero().getImmutable();    
    }

    @Override
    public QuadraticElement setToOne() {
        return (QuadraticElement) super.duplicate().setToOne().getImmutable();    
    }

    @Override
    public QuadraticElement setToRandom() {
        return (QuadraticElement) super.duplicate().setToRandom().getImmutable();    
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public QuadraticElement twice() {
        return (QuadraticElement) super.duplicate().twice().getImmutable();    
    }

    @Override
    public QuadraticElement mul(int z) {
        return (QuadraticElement) super.duplicate().mul(z).getImmutable();    
    }

    @Override
    public QuadraticElement square() {
        return (QuadraticElement) super.duplicate().square().getImmutable();    
    }

    @Override
    public QuadraticElement invert() {
        return (QuadraticElement) super.duplicate().invert().getImmutable();    
    }

    @Override
    public QuadraticElement negate() {
        return (QuadraticElement) super.duplicate().negate().getImmutable();    
    }

    @Override
    public QuadraticElement add(Element e) {
        return (QuadraticElement) super.duplicate().add(e).getImmutable();    
    }

    @Override
    public QuadraticElement sub(Element e) {
        return (QuadraticElement) super.duplicate().sub(e).getImmutable();    
    }

    @Override
    public QuadraticElement mul(Element e) {
        return (QuadraticElement) super.duplicate().mul(e).getImmutable();    
    }

    @Override
    public QuadraticElement mul(BigInteger n) {
        return (QuadraticElement) super.duplicate().mul(n).getImmutable();    
    }

    @Override
    public QuadraticElement mulZn(Element e) {
        return (QuadraticElement) super.duplicate().mulZn(e).getImmutable();    
    }

    @Override
    public QuadraticElement sqrt() {
        return (QuadraticElement) super.duplicate().sqrt().getImmutable();    
    }

    @Override
    public QuadraticElement powZn(Element n) {
        return (QuadraticElement) super.duplicate().powZn(n).getImmutable();    
    }

    @Override
    public QuadraticElement setFromHash(byte[] source, int offset, int length) {
        return (QuadraticElement) super.duplicate().setFromHash(source, offset, length).getImmutable();    
    }

    @Override
    public int setFromBytesCompressed(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytesCompressed(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytesX(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytesX(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public Element getImmutable() {
        return (QuadraticElement) this;
    }

    @Override
    public int setFromBytes(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public Element pow(BigInteger n) {
        return (QuadraticElement) super.duplicate().pow(n).getImmutable();    
    }

    @Override
    public Element halve() {
        return (QuadraticElement) super.duplicate().halve().getImmutable();    
    }

    @Override
    public Element div(Element element) {
        return (QuadraticElement) super.duplicate().div(element).getImmutable();    
    }

}
