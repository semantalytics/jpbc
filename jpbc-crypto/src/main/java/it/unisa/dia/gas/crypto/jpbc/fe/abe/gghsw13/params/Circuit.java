package it.unisa.dia.gas.crypto.jpbc.fe.abe.gghsw13.params;

import java.util.Iterator;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public interface Circuit extends Iterable<Circuit.Gate> {

    int getN();

    int getQ();

    int getDepth();

    Iterator<Gate> iterator();

    Gate getGateAt(int index);

    Gate getOutputGate();


    interface Gate {

        public static enum Type {INPUT, AND, OR}

        Type getType();

        int getIndex();

        int getDepth();

        int getInputIndexAt(int index);

        Gate getInputAt(int index);

        void set(boolean value);

        boolean isSet();

        Gate evaluate();
    }


}
