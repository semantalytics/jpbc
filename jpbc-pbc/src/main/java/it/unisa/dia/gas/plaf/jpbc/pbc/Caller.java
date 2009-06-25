package it.unisa.dia.gas.plaf.jpbc.pbc;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
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
        PBCLibrary INSTANCE = (PBCLibrary) Native.loadLibrary("jpbc-pbc", PBCLibrary.class);

        void pairingInit(Pointer pairing, String bug, int len);

        void pairingClear(Pointer pairing);

        void elementInitG1(Pointer element, Pointer pairing);

        void elementClear(Pointer element);
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

        PBCLibrary pbcLibrary = PBCLibrary.INSTANCE;

        pbcLibrary.pairingInit(pairing.getPointer(), params, length);

        PointerByReference g1 = new PointerByReference();
        pbcLibrary.elementInitG1(g1.getValue(), pairing.getPointer());
        pbcLibrary.elementClear(g1.getValue());

        pbcLibrary.pairingClear(pairing.getPointer());
    }
}
