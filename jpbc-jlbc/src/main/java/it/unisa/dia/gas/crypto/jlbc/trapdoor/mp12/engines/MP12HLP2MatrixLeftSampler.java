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
public class MP12HLP2MatrixLeftSampler extends MP12HLP2LeftSampler {

    protected static final ExecutorService EXECUTOR_SERVICE = ExecutorServiceUtils.getNewFixedThreadPool();


    protected MatrixField outputField;

    public MP12HLP2MatrixLeftSampler(MatrixField outputField) {
        this.outputField = outputField;
    }

    public MP12HLP2MatrixLeftSampler() {
    }

    @Override
    public Element processElements(Element... input) {
        final Matrix M = (Matrix) input[0];
        final Matrix U = (Matrix) input[1];

        final Matrix result;
        if (outputField != null) {
            result = outputField.newElement();
        } else {
            result = new MatrixField<Field>(
                        pk.getParameters().getRandom(),
                        pk.getZq(),
                        pk.getM() + M.getM(), U.getM()
            ).newElement();
        }

        PoolExecutor pool = new PoolExecutor(EXECUTOR_SERVICE);
        for (int i = 0, length = U.getM(); i < length; i++) {
            final int finalI = i;
            pool.submit(new Runnable() {
                public void run() {
                    result.setColAt(finalI, MP12HLP2MatrixLeftSampler.super.processElements(M, U.columnAt(finalI)));
                }
            });
        }
        pool.awaitTermination();

        return result;
    }
}
