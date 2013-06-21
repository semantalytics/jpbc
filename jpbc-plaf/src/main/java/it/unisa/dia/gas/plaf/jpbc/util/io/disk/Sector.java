package it.unisa.dia.gas.plaf.jpbc.util.io.disk;

import java.nio.ByteBuffer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public interface Sector {

    enum Mode {INIT, READ}


    int getLengthInBytes();

    Sector mapTo(Mode mode, ByteBuffer buffer);

}
