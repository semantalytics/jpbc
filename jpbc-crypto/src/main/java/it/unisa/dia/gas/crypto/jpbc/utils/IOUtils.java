package it.unisa.dia.gas.crypto.jpbc.utils;

import it.unisa.dia.gas.jpbc.Element;

import java.io.*;
import java.math.BigInteger;
import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IOUtils {

    public static void writeElement(DataOutputStream output, Element element) throws IOException {
        writeByteArray(output, (element != null) ? element.toBytes() : null);
    }

    public static void writeBigInteger(DataOutputStream output, BigInteger bigInteger) throws IOException {
        writeByteArray(output, (bigInteger != null) ? bigInteger.toByteArray() : null);
    }

    public static void writeByteArray(DataOutputStream out, byte[] bytes) throws IOException {
        if (bytes == null) {
//            System.out.println("length = " + 0);
            Arrays.toString(bytes);
            out.writeInt(0);
        } else {
//            System.out.println("length = " + bytes.length);
//            System.out.println("bytes = " + Arrays.toString(bytes));
            out.writeInt(bytes.length);
            out.write(bytes);
        }
    }

    public static byte[] toBytes(Object o) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            ObjectOutputStream objectOutput = new ObjectOutputStream(out);

            objectOutput.writeObject(o);

            objectOutput.flush();
            objectOutput.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return out.toByteArray();
    }


    public static byte[] readByteArray(DataInputStream in) throws IOException {
        int length = in.readInt();
        switch (length) {
            case 0:
//                System.out.println("length = " + 0);
                return new byte[0];
            case -1:
                throw new EOFException();
            default:
//                System.out.println("length = " + length);
                if (length > 0) {
                    byte[] result = new byte[length];
                    in.readFully(result);
//                    System.out.println("bytes = " + Arrays.toString(result));
                    return result;
                }
                throw new IOException("array length cannot less than zero");
        }
    }

    public static BigInteger readBigInteger(DataInputStream in) throws IOException {
        byte[] bytes = readByteArray(in);
        if (bytes.length == 0)
            return null;
        
        return new BigInteger(bytes);
    }

    public static <T> T fromBytes(Class<T> clazz, byte[] bytes) throws IOException {
        try {
            ObjectInputStream objectInput = new ObjectInputStream(new ByteArrayInputStream(bytes));
            return (T) objectInput.readObject();
        } catch (Exception e) {
            throw new IOException(e);
        }
    }


}
