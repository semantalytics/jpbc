package it.unisa.dia.gas.plaf.jpbc.pbc;

import com.sun.jna.Memory;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.MPZElementType;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCElementType;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibrary;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElement implements Element {
    protected PBCElementType value;
    protected PBCField field;


    public PBCElement(PBCElementType value, PBCField field) {
        this.value = value;
        this.field = field;
    }


    public Field getField() {
        return field;
    }

    public Element duplicate() {
        return field.newElement().set(this);
    }

    public Element set(Element element) {
        PBCLibrary.INSTANCE.pbc_element_set(value, ((PBCElement) element).value);

        return this;
    }

    public Element set(int value) {
        PBCLibrary.INSTANCE.pbc_element_set_si(this.value, value);

        return this;
    }

    public Element set(BigInteger value) {
        MPZElementType z = MPZElementType.fromBigInteger(value);
        PBCLibrary.INSTANCE.pbc_element_set_mpz(this.value, z);

        return this;
    }

    public Element setToRandom() {
        PBCLibrary.INSTANCE.pbc_element_random(value);

        return this;
    }

    public Element setFromHash(byte[] hash) {
        Memory memory = new Memory(hash.length);
        memory.write(0, hash, 0, hash.length);

        PBCLibrary.INSTANCE.pbc_element_from_hash(value, memory, hash.length);

        return this;
    }

    public int setFromBytes(byte[] bytes) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int setFromBytes(byte[] bytes, int offset) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int setEncoding(byte[] bytes) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public byte[] getDecoding() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element setToZero() {
        PBCLibrary.INSTANCE.pbc_element_set0(value);

        return this;
    }

    public boolean isZero() {
        return PBCLibrary.INSTANCE.pbc_element_is0(this.value) == 0;
    }

    public Element setToOne() {
        PBCLibrary.INSTANCE.pbc_element_set1(value);
        
        return this;
    }

    public boolean isOne() {
        return PBCLibrary.INSTANCE.pbc_element_is1(this.value) == 0;
    }

    public Element map(Element Element) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element twice() {
        PBCLibrary.INSTANCE.pbc_element_double(this.value, this.value);

        return this;
    }

    public Element square() {
        PBCLibrary.INSTANCE.pbc_element_square(this.value, this.value);

        return this;
    }

    public Element invert() {
        PBCLibrary.INSTANCE.pbc_element_invert(value, value);

        return this;
    }

    public Element halve() {
        PBCLibrary.INSTANCE.pbc_element_halve(this.value, this.value);

        return this;
    }

    public Element negate() {
        PBCLibrary.INSTANCE.pbc_element_neg(this.value, this.value);

        return this;
    }

    public Element add(Element element) {
        PBCLibrary.INSTANCE.pbc_element_add(value, value, ((PBCElement) element).value);

        return this;
    }

    public Element sub(Element element) {
        PBCLibrary.INSTANCE.pbc_element_sub(this.value, this.value, ((PBCElement) element).value);

        return this;
    }

    public Element div(Element element) {
        PBCLibrary.INSTANCE.pbc_element_div(this.value, this.value, ((PBCElement) element).value);

        return this;
    }

    public Element mul(Element element) {
        PBCLibrary.INSTANCE.pbc_element_mul(this.value, this.value, ((PBCElement) element).value);

        return this;
    }

    public Element mul(int value) {
        PBCLibrary.INSTANCE.pbc_element_mul_si(this.value, this.value, value);

        return this;
    }

    public Element mul(BigInteger value) {
        MPZElementType z = MPZElementType.fromBigInteger(value);
        PBCLibrary.INSTANCE.pbc_element_mul_mpz(this.value, this.value, z);

        return this;
    }

    public Element mulZn(Element element) {
        PBCLibrary.INSTANCE.pbc_element_mul_zn(value, value, ((PBCElement) element).value);
                
        return this;
    }

    public Element pow(BigInteger value) {
        MPZElementType z = MPZElementType.fromBigInteger(value);
        PBCLibrary.INSTANCE.pbc_element_pow_mpz(this.value, this.value, z);

        return this;
    }

    public Element powZn(Element element) {
        PBCLibrary.INSTANCE.pbc_element_pow_zn(value, value, ((PBCElement) element).value);

        return this;
    }

    public Element sqrt() {
        PBCLibrary.INSTANCE.pbc_element_sqrt(value, value);

        return this;
    }

    public boolean isSqr() {
        return PBCLibrary.INSTANCE.pbc_element_is_sqr(value) == 0;
    }

    public int sign() {
        return PBCLibrary.INSTANCE.pbc_element_sign(value);
    }

    public BigInteger toBigInteger() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public byte[] toBytes() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public int compareTo(Element o) {
        return PBCLibrary.INSTANCE.pbc_element_cmp(value, ((PBCElement) o).value);
    }

    @Override
    public String toString() {
        Memory memory = new Memory(2048);
        PBCLibrary.INSTANCE.pbc_element_snprint(memory, 2048, value);

        return new String(memory.getByteArray(0, 256));
    }


    public PBCElementType getValue() {
        return value;
    }
}
