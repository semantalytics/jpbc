package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ImmutableNaiveElement extends NaiveElement {

    public ImmutableNaiveElement(NaiveElement naiveElement) {
        super(naiveElement);
        this.immutable = true;
    }

    @Override
    public NaiveElement set(Element value) {
        return duplicate().set(value);    
    }

    @Override
    public NaiveElement set(int value) {
        return duplicate().set(value);    
    }

    @Override
    public NaiveElement set(BigInteger value) {
        return duplicate().set(value);    
    }

    @Override
    public NaiveElement twice() {
        return duplicate().twice();    
    }

    @Override
    public NaiveElement mul(int z) {
        return duplicate().mul(z);    
    }

    @Override
    public NaiveElement setToZero() {
        return duplicate().setToZero();    
    }

    @Override
    public NaiveElement setToOne() {
        return duplicate().setToOne();    
    }

    @Override
    public NaiveElement setToRandom() {
        return duplicate().setToRandom();    
    }

    @Override
    public NaiveElement setFromHash(byte[] source, int offset, int length) {
        return duplicate().setFromHash(source, offset, length);    
    }

    @Override
    public int setFromBytes(byte[] source) {
        return duplicate().setFromBytes(source);    
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        return duplicate().setFromBytes(source, offset);    
    }

    @Override
    public NaiveElement square() {
        return duplicate().square();    
    }

    @Override
    public NaiveElement invert() {
        return duplicate().invert();    
    }

    @Override
    public NaiveElement halve() {
        return duplicate().halve();    
    }

    @Override
    public NaiveElement negate() {
        return duplicate().negate();    
    }

    @Override
    public NaiveElement add(Element element) {
        return duplicate().add(element);    
    }

    @Override
    public NaiveElement sub(Element element) {
        return duplicate().sub(element);    
    }

    @Override
    public NaiveElement div(Element element) {
        return duplicate().div(element);    
    }

    @Override
    public NaiveElement mul(Element element) {
        return duplicate().mul(element);    
    }

    @Override
    public NaiveElement mul(BigInteger n) {
        return duplicate().mul(n);    
    }

    @Override
    public NaiveElement mulZn(Element z) {
        return duplicate().mulZn(z);    
    }

    @Override
    public NaiveElement sqrt() {
        return duplicate().sqrt();    
    }

    @Override
    public NaiveElement pow(BigInteger n) {
        return duplicate().pow(n);    
    }

    @Override
    public NaiveElement powZn(Element n) {
        return duplicate().powZn(n);    
    }

}
