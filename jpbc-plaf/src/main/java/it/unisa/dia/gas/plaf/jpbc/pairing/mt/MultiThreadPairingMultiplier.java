package it.unisa.dia.gas.plaf.jpbc.pairing.mt;

import it.unisa.dia.gas.jpbc.*;

import java.util.concurrent.*;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public class MultiThreadPairingMultiplier implements PairingCombiner {

    static ExecutorService executorService;
    static {
        executorService = Executors.newCachedThreadPool();
    }


    private Element value;
    private Pairing pairing;

    private CompletionService<Element> pool;
    private int counter;


    public MultiThreadPairingMultiplier(Pairing pairing, Field field) {
        this(executorService, pairing, field.newOneElement());
    }

    public MultiThreadPairingMultiplier(Pairing pairing, Element element) {
        this(executorService, pairing, element);
    }

    public MultiThreadPairingMultiplier(ExecutorService service, Pairing pairing, Element value) {
        this.pairing = pairing;
        this.value = value;

        this.pool = new ExecutorCompletionService<Element>(service);
        this.counter = 0;
    }

    public PairingCombiner addPairing(final Element e1, final Element e2) {
        counter++;
        pool.submit(new Callable<Element>() {
            public Element call() throws Exception {
                return pairing.pairing(e1, e2);
            }
        });

        return this;
    }

    public PairingCombiner addPairing(final PairingPreProcessing pairingPreProcessing, final Element e2) {
        counter++;
        pool.submit(new Callable<Element>() {
            public Element call() throws Exception {
                return pairingPreProcessing.pairing(e2);
            }
        });

        return this;
    }

    public Element doFinal(){
        try{
            for(int i = 0; i < counter; i++)
                value.mul(pool.take().get());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            counter = 0;
        }
        return value;
    }

}
