package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public class PairingStreamParser {

    private Pairing pairing;
    private byte[] buffer;
    private int offset;

    private int cursor;


    public PairingStreamParser(Pairing pairing, byte[] buffer, int offset) {
        this.pairing = pairing;
        this.buffer = buffer;
        this.offset = offset;

        this.cursor = offset;
    }


    public int getCursor() {
        return cursor;
    }

    public void resetCursor() {
        this.cursor = offset;
    }


    public Element[] load(Pairing.PairingFieldIdentifier... ids) {
        Element[] elements = new Element[ids.length];

        for (int i = 0; i < ids.length; i++) {
            Pairing.PairingFieldIdentifier id = ids[i];
            elements[i] = pairing.getField(id).newElement();
            cursor += elements[i].setFromBytes(buffer, cursor);
        }

        return elements;
    }


    public Element[] load(Pairing.PairingFieldIdentifier id, int count) {
        Element[] elements = new Element[count];

        Field field = pairing.getField(id);
        for (int i = 0; i < count; i++) {
            elements[i] = field.newElement();
            cursor += elements[i].setFromBytes(buffer, cursor);
        }

        return elements;
    }

    public Element[] loadG1(int count) {
        Element[] elements = new Element[count];

        Field field = pairing.getG1();
        for (int i = 0; i < count; i++) {
            elements[i] = field.newElement();
            cursor += elements[i].setFromBytes(buffer, cursor);
        }

        return elements;
    }


    public Element load(Pairing.PairingFieldIdentifier id) {
        Field field = pairing.getField(id);
        Element element = field.newElement();
        cursor += element.setFromBytes(buffer, cursor);

        return element;
    }

    public Element loadG1() {
        Element element = pairing.getG1().newElement();
        cursor += element.setFromBytes(buffer, cursor);

        return element;
    }

    public Element loadGT() {
        Element element = pairing.getGT().newElement();
        cursor += element.setFromBytes(buffer, cursor);

        return element;
    }


    public String loadString() {
        int length = buffer[cursor++];
        String w = new String(buffer, cursor, length);
        cursor += length;

        return w;
    }
}
