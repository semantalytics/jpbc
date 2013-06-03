package it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.MapParameters;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingObjectInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingObjectOutput;

import java.io.File;
import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13MMMapParameters extends MapParameters {

    protected CTL13MMInstanceParameters parameters;


    public CTL13MMMapParameters(CTL13MMInstanceParameters parameters) {
        this.parameters = parameters;
    }

    public CTL13MMMapParameters(PairingParameters parameters) {
        this.parameters = new CTL13MMInstanceParameters(parameters);
    }


    public void store() {
        store(String.format(
                "CTL13IP_eta_%d_n_%d_alpha_%d_ell_%d_rho_%d_delta_%d_kappa_%d_beta_%d_theta_%d_bound_%d.dat",
                parameters.getEta(), parameters.getN(), parameters.getAlpha(), parameters.getEll(),
                parameters.getRho(), parameters.getDelta(),
                parameters.getKappa(), parameters.getBeta(), parameters.getTheta(), parameters.getBound())
        );
    }

    public void store(String fileName) {
        try {
            PairingObjectOutput dos = new PairingObjectOutput(fileName);

            dos.writeBigInteger(getBigInteger("x0"));
            dos.writeBigInteger(getBigInteger("y"));
            dos.writeBigInteger(getBigInteger("pzt"));
            dos.writeBigInteger(getBigInteger("z"));
            dos.writeBigInteger(getBigInteger("zInv"));

            dos.writeBigIntegers((BigInteger[]) getObject("xsp"));
            dos.writeBigIntegers((BigInteger[]) getObject("crtCoefficients"));
            dos.writeBigIntegers((BigInteger[]) getObject("xs"));
            dos.writeBigIntegers((BigInteger[]) getObject("gs"));
            dos.writeBigIntegers((BigInteger[]) getObject("ps"));

            dos.flush();
            dos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public PairingParameters load() {
        return load(String.format(
                "CTL13IP_eta_%d_n_%d_alpha_%d_ell_%d_rho_%d_delta_%d_kappa_%d_beta_%d_theta_%d_bound_%d.dat",
                parameters.getEta(), parameters.getN(), parameters.getAlpha(), parameters.getEll(),
                parameters.getRho(), parameters.getDelta(),
                parameters.getKappa(), parameters.getBeta(), parameters.getTheta(), parameters.getBound())
        );
    }

    public PairingParameters load(String path) {
        try {
            if (!new File(path).exists())
                return null;

            PairingObjectInput dos = new PairingObjectInput(path);

            BigInteger x0 = dos.readBigInteger();
            BigInteger y = dos.readBigInteger();
            BigInteger pzt = dos.readBigInteger();
            BigInteger z = dos.readBigInteger();
            BigInteger zInv = dos.readBigInteger();

            BigInteger[] xsp = dos.readBigIntegers();
            BigInteger[] crtCoefficients = dos.readBigIntegers();
            BigInteger[] xs = dos.readBigIntegers();
            BigInteger[] gs = dos.readBigIntegers();
            BigInteger[] ps = dos.readBigIntegers();

            dos.close();

            put("params", parameters);
            put("x0", x0);
            put("y", y);
            put("pzt", pzt);
            put("z", z);
            put("zInv", zInv);
            put("xsp", xsp);
            put("crtCoefficients", crtCoefficients);
            put("xs", xs);
            put("gs", gs);
            put("ps", ps);

            return this;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}