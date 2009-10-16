package it.unisa.dia.gas.plaf.jpbc.crypto.hve.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HVEKeyParameters extends AsymmetricKeyParameter implements Serializable {
    private HVEParameters parameters;


    public HVEKeyParameters(boolean isPrivate, HVEParameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public HVEParameters getParameters() {
        return parameters;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HVEKeyParameters)) return false;

        HVEKeyParameters that = (HVEKeyParameters) o;

        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return parameters != null ? parameters.hashCode() : 0;
    }
}