package it.unisa.dia.gas.plaf.jpbc.field.z;

import it.unisa.dia.gas.jpbc.Element;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class ImmutableZrElement extends ZrElement {

    public ImmutableZrElement(ZrElement zrElement) {
        super(zrElement);
        this.immutable = true;
    }

    @Override
    public ZrElement set(Element value) {
        return (ZrElement) duplicate().set(value).getImmutable();    
    }

    @Override
    public ZrElement set(int value) {
        return (ZrElement) duplicate().set(value).getImmutable();    
    }

    @Override
    public ZrElement set(BigInteger value) {
        return (ZrElement) duplicate().set(value).getImmutable();    
    }

    @Override
    public ZrElement twice() {
        return (ZrElement) duplicate().twice().getImmutable();    
    }

    @Override
    public ZrElement mul(int z) {
        return (ZrElement) duplicate().mul(z).getImmutable();    
    }

    @Override
    public ZrElement setToZero() {
        return (ZrElement) duplicate().setToZero().getImmutable();    
    }

    @Override
    public ZrElement setToOne() {
        return (ZrElement) duplicate().setToOne().getImmutable();    
    }

    @Override
    public ZrElement setToRandom() {
        return (ZrElement) duplicate().setToRandom().getImmutable();    
    }

    @Override
    public ZrElement setFromHash(byte[] source, int offset, int length) {
        return (ZrElement) duplicate().setFromHash(source, offset, length).getImmutable();
    }

    @Override
    public int setFromBytes(byte[] source) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public ZrElement square() {
        return (ZrElement) duplicate().square().getImmutable();    
    }

    @Override
    public ZrElement invert() {
        return (ZrElement) duplicate().invert().getImmutable();    
    }

    @Override
    public ZrElement halve() {
        return (ZrElement) duplicate().halve().getImmutable();    
    }

    @Override
    public ZrElement negate() {
        return (ZrElement) duplicate().negate().getImmutable();    
    }

    @Override
    public ZrElement add(Element element) {
        return (ZrElement) duplicate().add(element).getImmutable();    
    }

    @Override
    public ZrElement sub(Element element) {
        return (ZrElement) duplicate().sub(element).getImmutable();    
    }

    @Override
    public ZrElement div(Element element) {
        return (ZrElement) duplicate().div(element).getImmutable();    
    }

    @Override
    public ZrElement mul(Element element) {
        return (ZrElement) duplicate().mul(element).getImmutable();    
    }

    @Override
    public ZrElement mul(BigInteger n) {
        return (ZrElement) duplicate().mul(n).getImmutable();    
    }

    @Override
    public ZrElement mulZn(Element z) {
        return (ZrElement) duplicate().mulZn(z).getImmutable();    
    }

    @Override
    public ZrElement sqrt() {
        return (ZrElement) duplicate().sqrt().getImmutable();    
    }

    @Override
    public ZrElement pow(BigInteger n) {
        return (ZrElement) duplicate().pow(n).getImmutable();    
    }

    @Override
    public ZrElement powZn(Element n) {
        return (ZrElement) duplicate().powZn(n).getImmutable();    
    }

}
