package it.unisa.dia.gas.plaf.jpbc.util.io.sector;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public interface ArraySector<T> extends Sector {

    public int getSize();

    public T getAt(int index);

    public void setAt(int index, T value);

}
