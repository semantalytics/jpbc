package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public interface DFATransition {

    int getFrom();

    Character getReading();

    int getTo();

}
