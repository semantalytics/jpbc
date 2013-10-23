package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.io.DataInputStream;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class PairingStreamReader {

    private Pairing pairing;
    private byte[] buffer;
    private int offset;

    private int cursor;

    private DataInputStream dis;
    private ExByteArrayInputStream bais;


    public PairingStreamReader(Pairing pairing, byte[] buffer, int offset) {
        this.pairing = pairing;
        this.buffer = buffer;
        this.offset = offset;

        this.cursor = offset;

        this.bais = new ExByteArrayInputStream(buffer, offset, buffer.length - offset);
        this.dis = new DataInputStream(bais);
    }


    public Element[] readElements(int... ids) {
        Element[] elements = new Element[ids.length];

        for (int i = 0; i < ids.length; i++) {
            elements[i] = pairing.getFieldAt(ids[i]).newElement();
            int length = elements[i].setFromBytes(buffer, cursor);
            cursor += length;
            bais.skip(length);
        }

        return elements;
    }

    public Element[] readElements(int id, int count) {
        Element[] elements = new Element[count];

        Field field = pairing.getFieldAt(id);
        int length = field.getLengthInBytes();
        for (int i = 0; i < count; i++) {
            elements[i] = field.newElementFromBytes(buffer, cursor);
            cursor += length;
            bais.skip(length);
        }

        return elements;
    }

    public Element[] readG1Elements(int count) {
        return readElements(1, count);
    }


    public Element readG1Element() {
        Element element = pairing.getG1().newElement();
        int length = element.setFromBytes(buffer, cursor);
        cursor += length;
        bais.skip(length);

        return element;
    }

    public Element readGTElement() {
        Element element = pairing.getGT().newElement();
        int length = element.setFromBytes(buffer, cursor);
        cursor += length;
        bais.skip(length);

        return element;
    }


    public String readString() {
        try {
            return dis.readUTF();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            cursor = bais.getPos();
        }
    }

}
