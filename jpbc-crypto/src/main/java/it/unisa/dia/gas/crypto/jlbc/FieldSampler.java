package it.unisa.dia.gas.crypto.jlbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Polynomial;

import java.math.BigInteger;
import java.util.Random;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class FieldSampler extends Sampler {

    public FieldSampler(Random random, int sigma) {
        super(random, sigma);
    }

    public Element sampleDFromField(Field<Polynomial> field) {
        Polynomial element = field.newElement();

        BigInteger[] coefficients = sampleD(element.getDegree());
        for (int i = 0; i < element.getDegree(); i++) {
            element.getCoefficient(i).set(coefficients[i]);
        }

        return element;
    }

}
