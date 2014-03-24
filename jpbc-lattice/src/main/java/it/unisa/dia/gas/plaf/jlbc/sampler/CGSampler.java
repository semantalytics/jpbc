package it.unisa.dia.gas.plaf.jlbc.sampler;

import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jlbc.field.floating.FloatingElement;
import it.unisa.dia.gas.plaf.jlbc.field.floating.FloatingField;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import org.apfloat.Apfloat;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class CGSampler implements Sampler<Vector> {

    protected SecureRandom random;
    protected int precision, n;
    protected VectorField<FloatingField> vf;
    protected FloatingField ff;

    public CGSampler(SecureRandom random, int precision, int n) {
        if (random == null)
            random = new SecureRandom();

        this.random = random;
        this.precision = precision;
        this.n = n;
        this.vf = new VectorField<FloatingField>(random, (ff = new FloatingField(random, precision, 2)), n);
    }


    public Vector sample() {
        Vector<FloatingElement> v = vf.newElement();

        for (int i = 0; i < n; i++)
            v.getAt(i).set(new Apfloat(Double.toString(random.nextGaussian()), precision).toRadix(2));

        return v;
    }

}
