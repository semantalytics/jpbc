package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.PoolExecutor;

import java.util.concurrent.ExecutorService;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12PLP2MatrixSolver extends MP12PLP2Solver {

    protected static final ExecutorService EXECUTOR_SERVICE = ExecutorServiceUtils.getNewFixedThreadPool();


    protected MatrixField outputField;

    public MP12PLP2MatrixSolver(MatrixField outputField) {
        this.outputField = outputField;
    }

    public MP12PLP2MatrixSolver() {
    }

    @Override
    public Element processElements(Element... input) {
        final Matrix U = (Matrix) input[0];
        System.out.println("U = " + U);

        final Matrix result;
        if (outputField != null) {
            result = outputField.newElement();
        } else {
            result = new MatrixField<Field>(
                    parameters.getParameters().getRandom(),
                    parameters.getZq(),
                    parameters.getPreimageField().getN(), U.getM()
            ).newElement();
        }

        PoolExecutor pool = new PoolExecutor(EXECUTOR_SERVICE);
        for (int i = 0, length = U.getM(); i < length; i++) {
            final int finalI = i;
            pool.submit(new Runnable() {
                public void run() {
                    result.setColAt(finalI, MP12PLP2MatrixSolver.super.processElements(U.columnAt(finalI)));
                }
            });
        }
        pool.awaitTermination();

        return result;
    }
}
