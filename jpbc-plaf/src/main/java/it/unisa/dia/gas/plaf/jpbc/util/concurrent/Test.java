package it.unisa.dia.gas.plaf.jpbc.util.concurrent;

import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.2.2
 */
public class Test {

    static int length = 0x8FFFFFF; // 128 Mb

    public static void main(String[] args) throws Exception {
        MappedByteBuffer out =
                new RandomAccessFile("test.dat", "rw").getChannel()
                        .map(FileChannel.MapMode.READ_WRITE, 0, length);

        for (int i = 0; i < length; i++)
            out.put((byte) 'x');

        System.out.println("Finished writing");
        for (int i = length / 2; i < length / 2 + 6; i++)
            System.out.print((char) out.get(i));
    }

}
