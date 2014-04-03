package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.ExecutorServiceUtils;
import it.unisa.dia.gas.plaf.jpbc.util.concurrent.PoolExecutor;
import sun.nio.ch.ThreadPool;

import java.util.concurrent.ExecutorService;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2MatrixSampleD extends MP12HLP2SampleD {

    protected static ExecutorService tp = ExecutorServiceUtils.getNewFixedThreadPool();


    protected MatrixField outputField;

    public MP12HLP2MatrixSampleD(MatrixField outputField) {
        this.outputField = outputField;
    }

    public MP12HLP2MatrixSampleD() {
        this.outputField = null;
    }


    @Override
    public Element processElements(Element... input) {
        final Matrix U = (Matrix) input[0];

        final Matrix result;
        if (outputField != null) {
            result = outputField.newElement();
        } else
            throw new IllegalStateException();

        PoolExecutor pool = new PoolExecutor(tp);
        for (int i = 0, length = result.getN(); i < length; i++) {
            final int finalI = i;
            pool.submit(new Runnable() {
                public void run() {
//            System.out.println("i = " + finalI);
                    result.setColAt(finalI, MP12HLP2MatrixSampleD.super.processElements(U.columnAt(finalI)));
                }
            });
        }
        pool.awaitTermination();

//        for (int i = 0, length = result.getN(); i < length; i++) {
//            System.out.println("i = " + i);
//                    result.setColAt(i, MP12HLP2MatrixSampleD.super.processElements(U.columnAt(i)));
//        }


        return result;
    }
}
