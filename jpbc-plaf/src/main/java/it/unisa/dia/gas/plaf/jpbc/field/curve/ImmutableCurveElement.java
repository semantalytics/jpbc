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
    public CurveElement duplicate() {
        return super.duplicate();
    }

    @Override
    public CurveElement set(Element e) {
        return (CurveElement) super.duplicate().set(e).getImmutable();
    }

    @Override
    public CurveElement set(int value) {
        return (CurveElement) super.duplicate().set(value).getImmutable();
    }

    @Override
    public CurveElement set(BigInteger value) {
        return (CurveElement) super.duplicate().set(value).getImmutable();
    }

    @Override
    public CurveElement twice() {
        return (CurveElement) super.duplicate().twice().getImmutable();
    }

    @Override
    public CurveElement setToZero() {
        return (CurveElement) super.duplicate().setToZero().getImmutable();
    }

    @Override
    public CurveElement setToOne() {
        return (CurveElement) super.duplicate().setToOne().getImmutable();
    }

    @Override
    public CurveElement setToRandom() {
        return (CurveElement) super.duplicate().setToRandom().getImmutable();
    }

    @Override
    public int setFromBytes(byte[] source, int offset) {
        throw new IllegalStateException("Invalid call on an immutable element");
    }

    @Override
    public CurveElement square() {
        return (CurveElement) super.duplicate().square().getImmutable();
    }

    @Override
    public CurveElement invert() {
        return (CurveElement) super.duplicate().invert().getImmutable();
    }

    @Override
    public CurveElement negate() {
        return (CurveElement) super.duplicate().negate().getImmutable();
    }

    @Override
    public CurveElement add(Element e) {
        return (CurveElement) super.duplicate().add(e).getImmutable();
    }

    @Override
    public CurveElement mul(Element e) {
        return (CurveElement) super.duplicate().mul(e).getImmutable();
    }

    @Override
    public CurveElement mul(BigInteger n) {
        return (CurveElement) super.duplicate().mul(n).getImmutable();
    }

    @Override
    public CurveElement mulZn(Element e) {
        return (CurveElement) super.duplicate().mulZn(e).getImmutable();
    }

    @Override
    public CurveElement powZn(Element e) {
        return (CurveElement) super.duplicate().powZn(e).getImmutable();
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
        return (CurveElement) super.duplicate().pow(n).getImmutable();
    }

    @Override
    public Element halve() {
        return (CurveElement) super.duplicate().halve().getImmutable();
    }

    @Override
    public Element sub(Element element) {
        return (CurveElement) super.duplicate().sub(element).getImmutable();
    }

    @Override
    public Element div(Element element) {
        return (CurveElement) super.duplicate().div(element).getImmutable();
    }

    @Override
    public Element mul(int z) {
        return (CurveElement) super.duplicate().mul(z).getImmutable();
    }

    @Override
    public Element sqrt() {
        return (CurveElement) super.duplicate().sqrt().getImmutable();
    }

}
