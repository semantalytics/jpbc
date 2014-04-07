package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.utils.MP12P2Utils;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2OneWayFunctionParameters extends AsymmetricKeyParameter {

    protected MP12HLP2PublicKeyParameters pk;
    protected Sampler<BigInteger> sampler;
    protected Field inputField, outputField;


    public MP12HLP2OneWayFunctionParameters(MP12HLP2PublicKeyParameters pk) {
        super(false);

        this.pk = pk;
        this.sampler = MP12P2Utils.getLWENoiseSampler(pk.getParameters().getRandom(), pk.getParameters().getN());

//        MP12P2Utils.testLWENoiseSampler(pk.getParameters().getN(), pk.getK());

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
