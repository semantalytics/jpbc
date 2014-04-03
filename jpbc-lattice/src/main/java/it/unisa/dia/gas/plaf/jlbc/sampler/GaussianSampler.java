package it.unisa.dia.gas.plaf.jlbc.sampler;

import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import org.apfloat.Apfloat;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public interface GaussianSampler<E> extends Sampler<E> {

    GaussianSampler<E> setCenter(Apfloat center);

    E sample(Apfloat center);

}
