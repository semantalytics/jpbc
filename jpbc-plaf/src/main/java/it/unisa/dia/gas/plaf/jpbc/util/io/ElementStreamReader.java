package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;

import java.io.DataInputStream;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @since 2.0.0
 */
public class ElementStreamReader {

    protected byte[] buffer;
    protected int offset;

    protected int cursor;

    protected DataInputStream dis;
    protected ExByteArrayInputStream bais;


    public ElementStreamReader(byte[] buffer, int offset) {
        this.buffer = buffer;
        this.offset = offset;

        this.cursor = offset;

        this.bais = new ExByteArrayInputStream(buffer, offset, buffer.length - offset);
        this.dis = new DataInputStream(bais);
    }


    public void reset() {
        this.cursor = this.offset;
    }

    public Element readElement(Field field) {
        Element e = field.newElementFromBytes(buffer, cursor);
        jump(field.getLengthInBytes(e));

        return e;
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

    public int readInt() {
        try {
            return dis.readInt();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            cursor = bais.getPos();
        }
    }


    protected void jump(int length) {
        cursor += length;
        bais.skip(length);
    }

}
