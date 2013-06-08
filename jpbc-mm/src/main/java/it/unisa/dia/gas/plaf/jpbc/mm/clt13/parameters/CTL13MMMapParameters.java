package it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters;

import it.unisa.dia.gas.jpbc.PairingParameters;
import it.unisa.dia.gas.plaf.jpbc.pairing.parameters.MapParameters;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataInput;
import it.unisa.dia.gas.plaf.jpbc.util.io.PairingDataOutput;

import java.io.*;
import java.math.BigInteger;
import java.util.Map;

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
        this(new CTL13MMInstanceParameters(parameters));
    }

    public CTL13MMMapParameters(Map<String, Object> values, CTL13MMInstanceParameters parameters) {
        super(values);
        this.parameters = parameters;
    }


    public void init() {
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
            DataOutputStream os = new DataOutputStream(new FileOutputStream(fileName));
            PairingDataOutput pdo = new PairingDataOutput(os);

            int x0Length = (parameters.getEta() * parameters.getN() + 7) / 8;
            int gLength = (parameters.getAlpha() + 7) / 8;
            int pLength = (parameters.getEta() + 7) / 8;

            pdo.writeBigInteger(getBigInteger("x0"), x0Length);
            pdo.writeBigInteger(getBigInteger("y"), x0Length);
            pdo.writeBigInteger(getBigInteger("pzt"), x0Length);
            pdo.writeBigInteger(getBigInteger("z"), x0Length);
            pdo.writeBigInteger(getBigInteger("zInv"), x0Length);

            pdo.writeBigIntegers((BigInteger[]) getObject("xsp"), x0Length);
            pdo.writeBigIntegers((BigInteger[]) getObject("crtCoefficients"), x0Length);
            pdo.writeBigIntegers((BigInteger[]) getObject("xs"), x0Length);
            pdo.writeBigIntegers((BigInteger[]) getObject("gs"), gLength);
            pdo.writeBigIntegers((BigInteger[]) getObject("ps"), pLength);

            os.flush();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public boolean load() {
        return load(String.format(
                "CTL13IP_eta_%d_n_%d_alpha_%d_ell_%d_rho_%d_delta_%d_kappa_%d_beta_%d_theta_%d_bound_%d.dat",
                parameters.getEta(), parameters.getN(), parameters.getAlpha(), parameters.getEll(),
                parameters.getRho(), parameters.getDelta(),
                parameters.getKappa(), parameters.getBeta(), parameters.getTheta(), parameters.getBound())
        );
    }

    public boolean load(String path) {
        try {
            if (!new File(path).exists())
                return false;

            DataInputStream is = new DataInputStream(new FileInputStream(path));
            PairingDataInput dos = new PairingDataInput(is);

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

            is.close();

            putObject("params", parameters);
            putObject("x0", x0);
            putObject("y", y);
            putObject("pzt", pzt);
            putObject("z", z);
            putObject("zInv", zInv);
            putObject("xsp", xsp);
            putObject("crtCoefficients", crtCoefficients);
            putObject("xs", xs);
            putObject("gs", gs);
            putObject("ps", ps);

            return true;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}