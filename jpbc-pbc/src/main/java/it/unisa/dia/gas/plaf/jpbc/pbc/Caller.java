package it.unisa.dia.gas.plaf.jpbc.pbc;

import com.sun.jna.*;

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
//        PBCLibrary INSTANCE = (PBCLibrary) Native.loadLibrary("jpbc-pbc", PBCLibrary.class);
        PBCLibrary INSTANCE = (PBCLibrary) Native.loadLibrary("pbc", PBCLibrary.class);

        int sizeOfPairing();

        int sizeOfElement();

        void pairing_init_inp_buf(Pointer pairing, String bug, int len);

        void pairing_clear(Pointer pointer);

        void _element_init_G1(Pointer element, Pointer pairing);

        void _element_clear(Pointer element);
    }


    public static void main(String[] args) {
        CLibrary.INSTANCE.printf("Hello, World\n");
        for (int i = 0; i < args.length; i++) {
            CLibrary.INSTANCE.printf("Argument %d: %s\n", i, args[i]);
        }

        PBCLibrary pbcLibrary = PBCLibrary.INSTANCE;
        System.out.println(pbcLibrary.sizeOfPairing());
        System.out.println(pbcLibrary.sizeOfElement());

        Pointer pairing = new Memory(pbcLibrary.sizeOfPairing());

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
        pbcLibrary.pairing_init_inp_buf(pairing, params, length);

        Pointer g1 = new Memory(pbcLibrary.sizeOfElement());
        pbcLibrary._element_init_G1(g1, pairing);
        pbcLibrary._element_clear(g1);

        pbcLibrary.pairing_clear(pairing);
    }
}
