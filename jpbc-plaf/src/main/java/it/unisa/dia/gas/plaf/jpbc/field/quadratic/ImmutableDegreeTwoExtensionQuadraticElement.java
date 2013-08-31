package it.unisa.dia.gas.plaf.jpbc.field.quadratic;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ImmutableDegreeTwoExtensionQuadraticElement<E extends Element> extends DegreeTwoExtensionQuadraticElement<E> {

    public ImmutableDegreeTwoExtensionQuadraticElement(DegreeTwoExtensionQuadraticElement<E> element) {
        super((DegreeTwoExtensionQuadraticField)element.getField());

        this.x = (E) element.getX().getImmutable();
        this.y = (E) element.getY().getImmutable();

        this.immutable = true;
    }


    @Override
    public QuadraticElement set(Element e) {
        return duplicate().set(e);    
    }

    @Override
    public QuadraticElement set(int value) {
        return duplicate().set(value);    
    }

    @Override
    public QuadraticElement set(BigInteger value) {
        return duplicate().set(value);    
    }

    @Override
    public QuadraticElement setToZero() {
        return duplicate().setToZero();    
    }

    @Override
    public QuadraticElement setToOne() {
        return duplicate().setToOne();    
    }

    @Override
    public QuadraticElement setToRandom() {
        return duplicate().setToRandom();    
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public QuadraticElement twice() {
        return duplicate().twice();    
    }

    @Override
    public QuadraticElement mul(int z) {
        return duplicate().mul(z);    
    }

    @Override
    public DegreeTwoExtensionQuadraticElement square() {
        return duplicate().square();    
    }

    @Override
    public DegreeTwoExtensionQuadraticElement invert() {
        return duplicate().invert();    
    }

    @Override
    public QuadraticElement negate() {
        return duplicate().negate();    
    }

    @Override
    public QuadraticElement add(Element e) {
        return duplicate().add(e);    
    }

    @Override
    public QuadraticElement sub(Element e) {
        return duplicate().sub(e);    
    }

    @Override
    public DegreeTwoExtensionQuadraticElement mul(Element e) {
        return duplicate().mul(e);    
    }

    @Override
    public QuadraticElement mul(BigInteger n) {
        return duplicate().mul(n);    
    }

    @Override
    public QuadraticElement mulZn(Element e) {
        return duplicate().mulZn(e);    
    }

    @Override
    public DegreeTwoExtensionQuadraticElement sqrt() {
        return duplicate().sqrt();    
    }

    @Override
    public QuadraticElement powZn(Element n) {
        return duplicate().powZn(n);    
    }

    @Override
    public QuadraticElement setFromHash(byte[] source, int offset, int length) {
        return duplicate().setFromHash(source, offset, length);    
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
        return this;
    }

    @Override
    public int setFromBytes(byte[] source) {
        return duplicate().setFromBytes(source);    
    }

    @Override
    public Element pow(BigInteger n) {
        return duplicate().pow(n);    
    }

    @Override
    public Element halve() {
        return duplicate().halve();    
    }

    @Override
    public Element div(Element element) {
        return duplicate().div(element);    
    }

}