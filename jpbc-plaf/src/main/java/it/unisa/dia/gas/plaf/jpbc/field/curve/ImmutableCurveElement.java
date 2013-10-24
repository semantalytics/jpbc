package it.unisa.dia.gas.plaf.jpbc.field.curve;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ImmutableCurveElement<E extends Element> extends CurveElement<E> {

    public ImmutableCurveElement(CurveElement curveElement) {
        super(curveElement);
        this.x = (E) curveElement.getX().getImmutable();
        this.y = (E) curveElement.getY().getImmutable();

        this.immutable = true;
    }


    @Override
    public CurveElement set(Element e) {
        return duplicate().set(e);    
    }

    @Override
    public CurveElement set(int value) {
        return duplicate().set(value);    
    }

    @Override
    public CurveElement set(BigInteger value) {
        return duplicate().set(value);    
    }

    @Override
    public CurveElement twice() {
        return duplicate().twice();    
    }

    @Override
    public CurveElement setToZero() {
        return duplicate().setToZero();    
    }

    @Override
    public CurveElement setToOne() {
        return duplicate().setToOne();    
    }

    @Override
    public CurveElement setToRandom() {
        return duplicate().setToRandom();    
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public CurveElement square() {
        return duplicate().square();    
    }

    @Override
    public CurveElement invert() {
        return duplicate().invert();    
    }

    @Override
    public CurveElement negate() {
        return duplicate().negate();    
    }

    @Override
    public CurveElement add(Element e) {
        return duplicate().add(e);    
    }

    @Override
    public CurveElement mul(Element e) {
        return duplicate().mul(e);    
    }

    @Override
    public CurveElement mul(BigInteger n) {
        return duplicate().mul(n);    
    }

    @Override
    public CurveElement mulZn(Element e) {
        return duplicate().mulZn(e);    
    }

    @Override
    public CurveElement powZn(Element e) {
        return duplicate().powZn(e);    
    }

    @Override
    public CurveElement setFromHash(byte[] source, int offset, int length) {
        throw new IllegalStateException("Invalid call on an immutable element");
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
    public int setFromBytes(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
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
    public Element sub(Element element) {
        return duplicate().sub(element);    
    }

    @Override
    public Element div(Element element) {
        return duplicate().div(element);    
    }

    @Override
    public Element mul(int z) {
        return duplicate().mul(z);    
    }

    @Override
    public Element sqrt() {
        return duplicate().sqrt();    
    }

}
