package it.unisa.dia.gas.plaf.jpbc.pbc;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.MPZElementType;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCElementPPType;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.util.Utils;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCElement implements Element {
    protected Pointer value;
    protected PBCField field;
    protected PBCElementPPType elementPPType;
    protected boolean immutable;


    public PBCElement(Pointer value, PBCField field) {
        this.value = value;
        this.field = field;
        this.immutable = false;
    }

    public PBCElement(PBCElement pbcElement) {
        PBCElement duplicate = pbcElement.duplicate();

        this.value = duplicate.value;
        this.field = duplicate.field;
    }


    public Field getField() {
        return field;
    }

    public int getLengthInBytes() {
        return PBCLibraryProvider.getPbcLibrary().pbc_element_length_in_bytes(this.value);
    }

    public boolean isImmutable() {
        return immutable;
    }

    public PBCElement getImmutable() {
        return new ImmutablePBCElement(this);
    }

    public PBCElement duplicate() {
        return (PBCElement) field.newElement().set(this);
    }

    public PBCElement set(Element value) {
        PBCElement pbcElement = (PBCElement) value;

        PBCLibraryProvider.getPbcLibrary().pbc_element_set(this.value, pbcElement.value);
        this.elementPPType = pbcElement.elementPPType; 

        return this;
    }

    public PBCElement set(int value) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_set_si(this.value, value);

        return this;
    }

    public PBCElement set(BigInteger value) {
        MPZElementType z = MPZElementType.fromBigInteger(value);
        PBCLibraryProvider.getPbcLibrary().pbc_element_set_mpz(this.value, z);

        return this;
    }

    public PBCElement setToRandom() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_random(value);

        return this;
    }

    public PBCElement setFromHash(byte[] source, int offset, int length) {
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

    public PBCElement setToZero() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_set0(value);

        return this;
    }

    public boolean isZero() {
        return PBCLibraryProvider.getPbcLibrary().pbc_element_is0(this.value) == 1;
    }

    public PBCElement setToOne() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_set1(value);
        
        return this;
    }

    public boolean isOne() {
        return PBCLibraryProvider.getPbcLibrary().pbc_element_is1(this.value) == 1;
    }

    public PBCElement twice() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_double(this.value, this.value);

        return this;
    }

    public PBCElement square() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_square(this.value, this.value);

        return this;
    }

    public PBCElement invert() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_invert(value, value);

        return this;
    }

    public PBCElement halve() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_halve(this.value, this.value);

        return this;
    }

    public PBCElement negate() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_neg(this.value, this.value);

        return this;
    }

    public PBCElement add(Element element) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_add(value, value, ((PBCElement) element).value);

        return this;
    }

    public PBCElement sub(Element element) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_sub(this.value, this.value, ((PBCElement) element).value);

        return this;
    }

    public PBCElement div(Element element) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_div(this.value, this.value, ((PBCElement) element).value);

        return this;
    }

    public PBCElement mul(Element element) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_mul(this.value, this.value, ((PBCElement) element).value);

        return this;
    }

    public PBCElement mul(int z) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_mul_si(this.value, this.value, z);

        return this;
    }

    public PBCElement mul(BigInteger n) {
        MPZElementType z = MPZElementType.fromBigInteger(n);
        PBCLibraryProvider.getPbcLibrary().pbc_element_mul_mpz(this.value, this.value, z);

        return this;
    }

    public PBCElement mulZn(Element z) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_mul_zn(value, value, ((PBCElement) z).value);
                
        return this;
    }

    public PBCElement pow(BigInteger n) {
        MPZElementType z = MPZElementType.fromBigInteger(n);
        PBCLibraryProvider.getPbcLibrary().pbc_element_pow_mpz(this.value, this.value, z);

        return this;
    }

    public PBCElement powZn(Element n) {
        PBCLibraryProvider.getPbcLibrary().pbc_element_pow_zn(value, value, ((PBCElement) n).value);

        return this;
    }

    public PBCElement sqrt() {
        PBCLibraryProvider.getPbcLibrary().pbc_element_sqrt(value, value);

        return this;
    }

    public boolean isSqr() {
        return PBCLibraryProvider.getPbcLibrary().pbc_element_is_sqr(value) == 1;
    }

    public int sign() {
        return PBCLibraryProvider.getPbcLibrary().pbc_element_sign(value);
    }

    public BigInteger toBigInteger() {
        MPZElementType mpzElement = new MPZElementType();
        mpzElement.init();

        PBCLibraryProvider.getPbcLibrary().pbc_element_to_mpz(mpzElement, value);

        return new BigInteger(mpzElement.toString(10));
    }

    public byte[] toBytes() {
        int numBytes = PBCLibraryProvider.getPbcLibrary().pbc_element_length_in_bytes(value);
        byte[] bytes = new byte[numBytes];

        PBCLibraryProvider.getPbcLibrary().pbc_element_to_bytes(bytes, value);

        return bytes;
    }

    public boolean isEqual(Element e) {
        return this == e || PBCLibraryProvider.getPbcLibrary().pbc_element_cmp(value, ((PBCElement) e).value) == 0;

    }

    public ElementPowPreProcessing pow() {
        return new PBCElementPowPreProcessing(field, value);
    }

    @Override
    public String toString() {
        Memory memory = new Memory(getField().getLengthInBytes()*3);
        PBCLibraryProvider.getPbcLibrary().pbc_element_snprint(memory, getField().getLengthInBytes()*3, value);
        return memory.getString(0);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof Element && isEqual((Element) obj);
    }

    public Pointer getValue() {
        return value;
    }
}
