package it.unisa.dia.gas.plaf.jpbc.pbc;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.GMPLibrary;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.MPZElementType;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.util.Utils;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElement implements Element {
    protected Pointer value;
    protected PBCField field;


    public PBCElement(Pointer value, PBCField field) {
        this.value = value;
        this.field = field;
    }


    public Field getField() {
        return field;
    }

    public int getLengthInBytes() {
        return PBCLibraryProvider.getPbcLibrary().pbc_element_length_in_bytes(this.value);
    }

    public Element duplicate() {
        return field.newElement().set(this);
    }

    public Element set(Element value) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_set(this.value, ((PBCElement) value).value);

        return this;
    }

    public Element set(int value) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_set_si(this.value, value);

        return this;
    }

    public Element set(BigInteger value) {
        MPZElementType z = MPZElementType.fromBigInteger(value);
        PBCLibraryProvider.getPbcLibrary().pbc_element_set_mpz(this.value, z);

        return this;
    }

    public Element setToRandom() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_random(value);

        return this;
    }

    public Element setFromHash(byte[] source, int offset, int length) {
        Memory memory = new Memory(length);
        memory.write(0, source, offset, length);

        PBCLibraryProvider.getPbcLibrary().pbc_element_from_hash(value, memory, source.length);

        return this;
    }

    public int setFromBytes(byte[] source) {
        return setFromBytes(source, 0);
    }

    public int setFromBytes(byte[] source, int offset) {
        int lengthInBytes = PBCLibraryProvider.getPbcLibrary().pbc_element_length_in_bytes(value);

        PBCLibraryProvider.getPbcLibrary().pbc_element_from_bytes(value, Utils.copyOf(source, offset, lengthInBytes));

        return lengthInBytes;
    }

    public int setEncoding(byte[] bytes) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public byte[] getDecoding() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element setToZero() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_set0(value);

        return this;
    }

    public boolean isZero() {
        return PBCLibraryProvider.getPbcLibrary().pbc_element_is0(this.value) == 0;
    }

    public Element setToOne() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_set1(value);
        
        return this;
    }

    public boolean isOne() {
        return PBCLibraryProvider.getPbcLibrary().pbc_element_is1(this.value) == 0;
    }

    public Element map(Element Element) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Element twice() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_double(this.value, this.value);

        return this;
    }

    public Element square() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_square(this.value, this.value);

        return this;
    }

    public Element invert() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_invert(value, value);

        return this;
    }

    public Element halve() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_halve(this.value, this.value);

        return this;
    }

    public Element negate() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_neg(this.value, this.value);

        return this;
    }

    public Element add(Element element) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_add(value, value, ((PBCElement) element).value);

        return this;
    }

    public Element sub(Element element) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_sub(this.value, this.value, ((PBCElement) element).value);

        return this;
    }

    public Element div(Element element) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_div(this.value, this.value, ((PBCElement) element).value);

        return this;
    }

    public Element mul(Element element) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_mul(this.value, this.value, ((PBCElement) element).value);

        return this;
    }

    public Element mul(int z) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_mul_si(this.value, this.value, z);

        return this;
    }

    public Element mul(BigInteger n) {
        MPZElementType z = MPZElementType.fromBigInteger(n);
        PBCLibraryProvider.getPbcLibrary().pbc_element_mul_mpz(this.value, this.value, z);

        return this;
    }

    public Element mulZn(Element z) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_mul_zn(value, value, ((PBCElement) z).value);
                
        return this;
    }

    public Element pow(BigInteger n) {
        MPZElementType z = MPZElementType.fromBigInteger(n);
        PBCLibraryProvider.getPbcLibrary().pbc_element_pow_mpz(this.value, this.value, z);

        return this;
    }

    public Element powZn(Element n) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_pow_zn(value, value, ((PBCElement) n).value);

        return this;
    }

    public Element sqrt() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_sqrt(value, value);

        return this;
    }

    public boolean isSqr() {
        return PBCLibraryProvider.getPbcLibrary().pbc_element_is_sqr(value) == 0;
    }

    public int sign() {
        return PBCLibraryProvider.getPbcLibrary().pbc_element_sign(value);
    }

    public BigInteger toBigInteger() {
        MPZElementType mpzElement = new MPZElementType();
        GMPLibrary.INSTANCE.__gmpz_init(mpzElement);

        PBCLibraryProvider.getPbcLibrary().pbc_element_to_mpz(mpzElement, value);
        return new BigInteger(GMPLibrary.INSTANCE.__gmpz_get_str(null, 10, mpzElement));
    }

    public byte[] toBytes() {
        int numBytes = PBCLibraryProvider.getPbcLibrary().pbc_element_length_in_bytes(value);
        byte[] bytes = new byte[numBytes];

        PBCLibraryProvider.getPbcLibrary().pbc_element_to_bytes(bytes, value);

        return bytes;
    }

    public int compareTo(Element o) {
        return PBCLibraryProvider.getPbcLibrary().pbc_element_cmp(value, ((PBCElement) o).value);
    }

    @Override
    public String toString() {
        Memory memory = new Memory(getField().getFixedLengthInBytes()*3);
        PBCLibraryProvider.getPbcLibrary().pbc_element_snprint(memory, getField().getFixedLengthInBytes()*3, value);
        return memory.getString(0);
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Element))
            return false;

        return compareTo((Element) obj) == 0;
    }

    public Pointer getValue() {
        return value;
    }
}
