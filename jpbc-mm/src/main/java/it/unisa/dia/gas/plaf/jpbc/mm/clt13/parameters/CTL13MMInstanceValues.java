package it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters;

import it.unisa.dia.gas.jpbc.PairingParameters;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMInstanceValues {
    private PairingParameters parameters;


    public CTL13MMInstanceValues(PairingParameters parameters) {
        this.parameters = parameters;
    }


    public CTL13MMInstanceParameters getInstanceParameters() {
        return (CTL13MMInstanceParameters) parameters.getObject("params");
    }

    public BigInteger getX0() {
        return parameters.getBigInteger("x0");
    }

    public BigInteger getY() {
        return parameters.getBigInteger("y");
    }

    public BigInteger getZ() {
        return parameters.getBigInteger("z");
    }

    public BigInteger getZInv() {
        return parameters.getBigInteger("zInv");
    }

    public BigInteger getPzt() {
        return parameters.getBigInteger("pzt");
    }

    public BigInteger getXspAt(int index) {
        return parameters.getBigIntegerAt("xsp", index);
    }

    public BigInteger getCrtCoefficientAt(int index) {
        return parameters.getBigIntegerAt("crtCoefficients", index);
    }

    public BigInteger getXsAt(int level, int index) {
        return parameters.getBigIntegerAt("xs" + level, index);
    }

    public BigInteger getGsAt(int index) {
        return parameters.getBigIntegerAt("gs", index);
    }

    public BigInteger getPsAt(int index) {
        return parameters.getBigIntegerAt("ps", index);
    }
}
