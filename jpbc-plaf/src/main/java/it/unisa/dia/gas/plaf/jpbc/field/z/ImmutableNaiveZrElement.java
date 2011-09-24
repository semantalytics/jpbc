package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class ImmutableNaiveZrElement extends NaiveZrElement {

    public ImmutableNaiveZrElement(NaiveZrElement naiveZrElement) {
        super(naiveZrElement);
        this.immutable = true;
    }

    @Override
    public NaiveZrElement set(Element value) {
        return duplicate().set(value);    
    }

    @Override
    public NaiveZrElement set(int value) {
        return duplicate().set(value);    
    }

    @Override
    public NaiveZrElement set(BigInteger value) {
        return duplicate().set(value);    
    }

    @Override
    public NaiveZrElement twice() {
        return duplicate().twice();    
    }

    @Override
    public NaiveZrElement mul(int z) {
        return duplicate().mul(z);    
    }

    @Override
    public NaiveZrElement setToZero() {
        return duplicate().setToZero();    
    }

    @Override
    public NaiveZrElement setToOne() {
        return duplicate().setToOne();    
    }

    @Override
    public NaiveZrElement setToRandom() {
        return duplicate().setToRandom();    
    }

    @Override
    public NaiveZrElement setFromHash(byte[] source, int offset, int length) {
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
    public NaiveZrElement square() {
        return duplicate().square();    
    }

    @Override
    public NaiveZrElement invert() {
        return duplicate().invert();    
    }

    @Override
    public NaiveZrElement halve() {
        return duplicate().halve();    
    }

    @Override
    public NaiveZrElement negate() {
        return duplicate().negate();    
    }

    @Override
    public NaiveZrElement add(Element element) {
        return duplicate().add(element);    
    }

    @Override
    public NaiveZrElement sub(Element element) {
        return duplicate().sub(element);    
    }

    @Override
    public NaiveZrElement div(Element element) {
        return duplicate().div(element);    
    }

    @Override
    public NaiveZrElement mul(Element element) {
        return duplicate().mul(element);    
    }

    @Override
    public NaiveZrElement mul(BigInteger n) {
        return duplicate().mul(n);    
    }

    @Override
    public NaiveZrElement mulZn(Element z) {
        return duplicate().mulZn(z);    
    }

    @Override
    public NaiveZrElement sqrt() {
        return duplicate().sqrt();    
    }

    @Override
    public NaiveZrElement pow(BigInteger n) {
        return duplicate().pow(n);    
    }

    @Override
    public NaiveZrElement powZn(Element n) {
        return duplicate().powZn(n);    
    }

}
