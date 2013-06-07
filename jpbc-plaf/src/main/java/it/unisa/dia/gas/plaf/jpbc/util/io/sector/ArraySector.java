package it.unisa.dia.gas.plaf.jpbc.util.io.sector;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public interface ArraySector<T> extends Sector {

    int getSize();

    T getAt(int index);

    void setAt(int index, T value);


    T getAt(String label);

    void setAt(String label, T value);

}
