package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.plaf.jlbc.sampler.SamplerFactory;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import org.bouncycastle.crypto.CipherParameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2LeftSampler extends MP12HLP2Sampler {

    protected Sampler<BigInteger> sampler;

    public ElementCipher init(CipherParameters param) {
        super.init(param);

        // TODO: change the parameter.
        this.sampler = SamplerFactory.getInstance().getDiscreteGaussianSampler(parameters.getParameters().getRandom(), 9);

        return this;
    }

    public Element processElements(Element... input) {
        Matrix M = (Matrix) input[0];
        Element u = input[1].duplicate();

//        1. sample a random vector e2 ∈ Zm1 distributed statistically close to DZm1 ,σ ,
        Element e2 = new VectorField<Field>(parameters.getParameters().getRandom(), M.getTargetField(), M.getM()).newElementFromSampler(sampler);

//        2. run e1 ←R SamplePre(A,TA,y,σ) where y=u−(M1·e2)∈Znq,
//                note that Λyq (A) is not empty since A is rank n,
        Element y = u.sub(M.mul(e2));
        Element e1 = super.processElements(y);

//        3. output e ← (e1, e2) ∈ Zm+m1

        return VectorField.union(e1, e2);
    }


}
