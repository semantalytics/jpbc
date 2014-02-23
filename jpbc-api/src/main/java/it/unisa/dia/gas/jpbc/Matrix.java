package it.unisa.dia.gas.jpbc;

/**
 * TODO: 
 * @author Angelo De Caro (jpbclib@gmail.com)
 * @see Element
 * @since 2.1.0
 */
public interface Matrix <E extends Element> extends Element {

    int getN();

    int getM();

    E getAt(int row, int col);

}

