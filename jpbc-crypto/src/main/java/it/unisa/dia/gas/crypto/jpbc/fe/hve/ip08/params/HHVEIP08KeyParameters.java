package it.unisa.dia.gas.crypto.jpbc.fe.hve.ip08.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class HHVEIP08KeyParameters extends AsymmetricKeyParameter implements Serializable {
    private HHVEIP08Parameters parameters;


    public HHVEIP08KeyParameters(boolean isPrivate, HHVEIP08Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public HHVEIP08Parameters getParameters() {
        return parameters;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof HHVEIP08KeyParameters)) return false;

        HHVEIP08KeyParameters that = (HHVEIP08KeyParameters) o;

        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return parameters != null ? parameters.hashCode() : 0;
    }
}