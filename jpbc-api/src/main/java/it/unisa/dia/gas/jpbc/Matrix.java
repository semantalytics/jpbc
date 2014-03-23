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


    Vector<E> rowAt(int row);

    Vector<E> columnAt(int col);

    Matrix<E> setRowAt(int row, Element rowElement);

    Matrix<E> setColAt(int col, Element colElement);


    Matrix<E> setSubMatrixToIdentityAt(int row, int col, int n);

    Matrix<E> setSubMatrixFromMatrixAt(int row, int col, Element e);

    Matrix<E> setSubMatrixFromMatrixTransposeAt(int row, int col, Element e);

    Matrix<E> mulByTranspose();

    Matrix<E> transform(Transformer transformer);

    public boolean isSymmetric();


    public static interface Transformer {

        public void transform(int row, int col, Element e);

    }
}

