package it.unisa.dia.gas.plaf.jpbc.pairing.mt;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.util.concurrent.*;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public class MultiThreadMultiplier implements Multiplier {

    private Element value;
    private Pairing pairing;

    private ExecutorService service;
    private CompletionService<Element> pool;
    private int counter;


    public MultiThreadMultiplier(Pairing pairing, Field field) {
        this(pairing, field.newOneElement());
    }

    public MultiThreadMultiplier(Pairing pairing, Element value) {
        this.pairing = pairing;
        this.value = value;

        this.service = Executors.newCachedThreadPool();
        this.pool = new ExecutorCompletionService<Element>(service);
        this.counter = 0;
    }

    public Multiplier addPairing(final Element e1, final Element e2) {
        counter++;
        pool.submit(new Callable<Element>() {
            public Element call() throws Exception {
                Element result = pairing.pairing(e1, e2);

//                synchronized (value) {
//                    value.mul(result);
//                }
                return result;
            }
        });

        return this;
    }

    public Element finish(){
        try{
            for(int i = 0; i < counter; i++){
//                pool.take();
                value.mul(pool.take().get());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        counter = 0;
        return value;
    }


}
