package it.unisa.dia.gas.plaf.jpbc.pbc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.ptr.PointerByReference;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class Caller {

    // This is the standard, stable way of mapping, which supports extensive
    // customization and mapping of Java to native types.
    public interface CLibrary extends Library {
        CLibrary INSTANCE = (CLibrary) Native.loadLibrary((Platform.isWindows() ? "msvcrt" : "c"), CLibrary.class);

        void printf(String format, Object... args);
    }

    // This is the standard, stable way of mapping, which supports extensive
    // customization and mapping of Java to native types.
    public interface PBCLibrary extends Library {
        PBCLibrary INSTANCE = (PBCLibrary) Native.loadLibrary("pbc", PBCLibrary.class);

        void pairing_init_inp_buf(PointerByReference pairing, String bug, int len);

        void pairing_clear(PointerByReference pairing);

    }


    public static void main(String[] args) {
        CLibrary.INSTANCE.printf("Hello, World\n");
        for (int i = 0; i < args.length; i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }


        PointerByReference pairing = new PointerByReference();

        String params =
                "type a\n" +
                "q 389517483806764372162075727451538192950200087543273118390202621592813077775963376258032864387\n" +
                "h 783228\n" +
                "r 497323236409786642155382248146820840100456173098092915971087118428877769660894881513471\n" +
                "exp2 288\n" +
                "exp1 144\n" +
                "sign1 1\n" +
                "sign0 -1";
        int length = params.length();

        PBCLibrary.INSTANCE.pairing_init_inp_buf(pairing, params, length);

        System.out.println("pairing = " + pairing);

        PBCLibrary.INSTANCE.pairing_clear(pairing);
    }
}
