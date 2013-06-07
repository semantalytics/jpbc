package it.unisa.dia.gas.plaf.jpbc.util.io.sector;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.0.0
 */
public interface Disk<S extends Sector> {

    S getSectorAt(int index);

    S getSector(String key);

}
