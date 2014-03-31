package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import it.unisa.dia.gas.plaf.jlbc.sampler.ZGaussianCDTSampler;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2OneWayFunctionParameters extends AsymmetricKeyParameter {

    protected MP12HLP2PublicKeyParameters pk;
    protected Sampler<BigInteger> sampler;
    protected Field inputField, outputField;

    public MP12HLP2OneWayFunctionParameters(MP12HLP2PublicKeyParameters pk, Sampler<BigInteger> sampler) {
        super(false);

        this.pk = pk;
        this.sampler = sampler;
        this.inputField = new VectorField<Field>(
                pk.getParameters().getRandom(),
                pk.getZq(),
                pk.getParameters().getN()
        );
        this.outputField = new VectorField<Field>(
                pk.getParameters().getRandom(),
                pk.getZq(),
                pk.getM()
        );
    }

    public MP12HLP2OneWayFunctionParameters(MP12HLP2PublicKeyParameters pk) {
        super(false);

        this.pk = pk;
        this.sampler = new ZGaussianCDTSampler(pk.getParameters().getRandom(), (int) (2 * Math.sqrt(pk.getParameters().getN())));
        this.inputField = new VectorField<Field>(
                pk.getParameters().getRandom(),
                pk.getZq(),
                pk.getParameters().getN()
        );
        this.outputField = new VectorField<Field>(
                pk.getParameters().getRandom(),
                pk.getZq(),
                pk.getM()
        );
    }

    public MP12HLP2PublicKeyParameters getPk() {
        return pk;
    }

    public Sampler<BigInteger> getSampler() {
        return sampler;
    }

    public Field getInputField() {
        return inputField;
    }

    public Field getOutputField() {
        return outputField;
    }
}
