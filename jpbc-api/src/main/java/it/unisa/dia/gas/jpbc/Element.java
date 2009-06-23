package it.unisa.dia.gas.jpbc;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface Element extends Comparable<Element> {

    Field getField();


    Element duplicate();


    Element set(Element element);

    Element set(int value);

    Element set(BigInteger value);

    Element setToRandom();

    Element setFromHash(byte[] hash);

    int setFromBytes(byte[] bytes);

    int setEncoding(byte[] bytes);

    byte[] getDecoding();

    Element setToZero();

    boolean isZero();

    Element setToOne();

    boolean isOne();

    Element map(Element Element);

    
    Element twice();

    Element square();

    Element invert();

    Element halve();

    Element negate();

    Element add(Element element);

    Element sub(Element element);

    Element div(Element element);

    Element mul(Element element);

    Element mul(int value);

    Element mul(BigInteger value);

    Element mulZn(Element element);

    Element pow(BigInteger value);

    Element powZn(Element element);

    Element sqrt();

    boolean isSqr();

    int sign();


    BigInteger toBigInteger();

    byte[] toBytes();

}