package it.unisa.dia.gas.plaf.jpbc.util.io.sector;

import java.nio.ByteBuffer;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public interface Sector {

    int getLengthInBytes();

    Sector mapTo(ByteBuffer buffer);

}
