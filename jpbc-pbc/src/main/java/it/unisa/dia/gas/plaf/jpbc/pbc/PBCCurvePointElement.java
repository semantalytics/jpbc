package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCElementType;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.util.Utils;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCCurvePointElement extends PBCElement implements Point {

    public PBCCurvePointElement(PBCElementType value, PBCField field) {
        super(value, field);
    }


    public Element getX() {
        PBCElementType dest = new PBCElementType();
        PBCLibraryProvider.getPbcLibrary().pbc_curve_x_coord(dest, this.value);

        return new PBCElement(dest, null);
    }

    public Element getY() {
        PBCElementType dest = new PBCElementType();
        PBCLibraryProvider.getPbcLibrary().pbc_curve_y_coord(dest, this.value);

        return new PBCElement(dest, null);
    }

    public int getLengthInBytesCompressed() {
        return PBCLibraryProvider.getPbcLibrary().element_length_in_bytes_compressed(this.value);
    }

    public byte[] toBytesCompressed() {
        byte[] bytes = new byte[PBCLibraryProvider.getPbcLibrary().element_length_in_bytes_compressed(value)];
        PBCLibraryProvider.getPbcLibrary().element_to_bytes_compressed(bytes, value);
        return bytes;
    }

    public int setFromBytesCompressed(byte[] source) {
        return setFromBytesCompressed(source, 0);
    }

    public int setFromBytesCompressed(byte[] source, int offset) {
        int lengthInBytes = PBCLibraryProvider.getPbcLibrary().element_length_in_bytes_compressed(value);
        PBCLibraryProvider.getPbcLibrary().element_from_bytes_compressed(value, Utils.copyOf(source, offset, lengthInBytes));
        return lengthInBytes;
    }

    public int getLengthInBytesX() {
        return PBCLibraryProvider.getPbcLibrary().element_length_in_bytes_x_only(this.value);
    }

    public byte[] toBytesX() {
        byte[] bytes = new byte[PBCLibraryProvider.getPbcLibrary().element_length_in_bytes_x_only(value)];
        PBCLibraryProvider.getPbcLibrary().element_to_bytes_x_only(bytes, value);
        return bytes;
    }

    public int setFromBytesX(byte[] source) {
        return setFromBytesX(source, 0);
    }

    public int setFromBytesX(byte[] source, int offset) {
        int lengthInBytes = PBCLibraryProvider.getPbcLibrary().element_length_in_bytes_x_only(value);
        PBCLibraryProvider.getPbcLibrary().element_from_bytes_x_only(value, Utils.copyOf(source, offset, lengthInBytes));
        return lengthInBytes;
    }

}