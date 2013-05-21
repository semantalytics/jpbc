package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.io.DataInputStream;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
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


    public Element[] load(Pairing.PairingFieldIdentifier... ids) {
        Element[] elements = new Element[ids.length];

        for (int i = 0; i < ids.length; i++) {
            Pairing.PairingFieldIdentifier id = ids[i];
            elements[i] = pairing.getField(id).newElement();
            int length = elements[i].setFromBytes(buffer, cursor);
            cursor += length;
            bais.skip(length);
        }

        return elements;
    }

    public Element[] load(Pairing.PairingFieldIdentifier id, int count) {
        Element[] elements = new Element[count];

        Field field = pairing.getField(id);
        for (int i = 0; i < count; i++) {
            elements[i] = field.newElement();
            int length = elements[i].setFromBytes(buffer, cursor);
            cursor += length;
            bais.skip(length);
        }

        return elements;
    }

    public Element[] loadG1(int count) {
        Element[] elements = new Element[count];

        Field field = pairing.getG1();
        for (int i = 0; i < count; i++) {
            elements[i] = field.newElement();
            int length = elements[i].setFromBytes(buffer, cursor);
            cursor += length;
            bais.skip(length);
        }

        return elements;
    }


    public Element load(Pairing.PairingFieldIdentifier id) {
        Field field = pairing.getField(id);
        Element element = field.newElement();
        int length = element.setFromBytes(buffer, cursor);
        cursor += length;
        bais.skip(length);

        return element;
    }

    public Element loadG1() {
        Element element = pairing.getG1().newElement();
        int length = element.setFromBytes(buffer, cursor);
        cursor += length;
        bais.skip(length);

        return element;
    }

    public Element loadGT() {
        Element element = pairing.getGT().newElement();
        int length = element.setFromBytes(buffer, cursor);
        cursor += length;
        bais.skip(length);

        return element;
    }


    public String loadString() {
        try {
            return dis.readUTF();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            cursor = bais.getPos();
        }
    }

}
