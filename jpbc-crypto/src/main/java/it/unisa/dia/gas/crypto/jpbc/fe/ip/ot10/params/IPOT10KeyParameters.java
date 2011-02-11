package it.unisa.dia.gas.crypto.jpbc.fe.ip.ot10.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

import java.io.Serializable;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPOT10KeyParameters extends AsymmetricKeyParameter implements Serializable {
    private IPOT10Parameters parameters;


    public IPOT10KeyParameters(boolean isPrivate, IPOT10Parameters parameters) {
        super(isPrivate);
        this.parameters = parameters;
    }


    public IPOT10Parameters getParameters() {
        return parameters;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof IPOT10KeyParameters)) return false;

        IPOT10KeyParameters that = (IPOT10KeyParameters) o;

        if (parameters != null ? !parameters.equals(that.parameters) : that.parameters != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return parameters != null ? parameters.hashCode() : 0;
    }
}