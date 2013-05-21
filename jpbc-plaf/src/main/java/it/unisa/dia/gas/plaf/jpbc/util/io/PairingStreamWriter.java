package it.unisa.dia.gas.plaf.jpbc.util.io;

import it.unisa.dia.gas.jpbc.Element;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class PairingStreamWriter {

    private ByteArrayOutputStream baos;
    private DataOutputStream dos;

    public PairingStreamWriter(int size) {
        this.baos = new ByteArrayOutputStream(size);
        this.dos = new DataOutputStream(baos);
    }


    public byte[] getBytes() {
        return baos.toByteArray();
    }

    public void writeString(String s) throws IOException {
        dos.writeUTF(s);
    }

    public void writeElement(Element element) throws IOException {
        dos.write(element.toBytes());
    }

    public void writeBytesFixedLength(byte[] bytes, int length) throws IOException {
        dos.writeInt(bytes.length);

        int valueLengthInBytes = length - 4;

        if (bytes.length < valueLengthInBytes) {
            byte[] result = new byte[valueLengthInBytes];
            System.arraycopy(bytes, 0, result, 0, bytes.length);
            bytes = result;
        }

        dos.write(bytes);
    }

    public void writeBytes(byte[] bytes) throws IOException {
        dos.write(bytes);
    }
}
