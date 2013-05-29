package it.unisa.dia.gas.plaf.jpbc.util.mt;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class MTUtils {

    static ExecutorService executorService;
    static {
//        executorService = Executors.newCachedThreadPool();
        executorService = Executors.newFixedThreadPool(
                Runtime.getRuntime().availableProcessors() * 8
        );
    }

}
