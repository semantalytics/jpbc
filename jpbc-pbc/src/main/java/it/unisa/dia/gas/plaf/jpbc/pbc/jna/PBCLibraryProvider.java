package it.unisa.dia.gas.plaf.jpbc.pbc.jna;

import com.sun.jna.Native;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCLibraryProvider {

    private static PBCLibrary pbcLibrary;

    static {
        try {
            pbcLibrary = (PBCLibrary) Native.loadLibrary("jpbc-pbc", PBCLibrary.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static PBCLibrary getPbcLibrary() {
        return pbcLibrary;
    }

    public static boolean isAvailable() {
        return pbcLibrary != null;
    }
}
