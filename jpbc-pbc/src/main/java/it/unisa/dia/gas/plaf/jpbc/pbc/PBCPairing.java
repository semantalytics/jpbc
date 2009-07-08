package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibrary;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCPairingType;

import java.math.BigInteger;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class PBCPairing implements Pairing {
    protected PBCPairingType pairing;

    protected PBCG1Field g1Field;
    protected PBCG2Field g2Field;
    protected PBCGTField gTField;
    protected PBCZrField zRField;


    public PBCPairing(CurveParams curveParams) {
        // Init pairing...

        String buf = curveParams.toString(" ");

        pairing = new PBCPairingType();
        PBCLibrary.INSTANCE.pbc_pairing_init_inp_buf(pairing, buf, buf.length());

        // Init fields

        initFields();
    }


    public boolean isSymmetric() {
        return PBCLibrary.INSTANCE.pbc_pairing_is_symmetric(pairing) == 0;
    }

    public BigInteger getPhikonr() {
        throw new IllegalStateException("Not Implemented yet!!!");
    }

    public Field getG1() {
        return g1Field;
    }

    public Field getG2() {
        return g2Field;
    }

    public Field getGT() {
        return gTField;
    }

    public Field getZr() {
        return zRField;
    }

    public Element pairing(Element g1, Element g2) {
        PBCElement out = (PBCElement) gTField.newElement();

        PBCLibrary.INSTANCE.pbc_pairing_apply(out.getValue(), ((PBCElement) g1).getValue(), ((PBCElement) g2).getValue(), pairing);

        return out;
    }

    public Element finalPow(Element element) {
        throw new IllegalStateException("Not Implemented yet!!!");
    }


    @Override
    protected void finalize() throws Throwable {
        g1Field = null;
        g2Field = null;
        gTField = null;
        zRField = null;
        
        PBCLibrary.INSTANCE.pbc_pairing_clear(pairing);
    }

    protected void initPairing() {

    }

    protected void initFields() {
        g1Field = new PBCG1Field(pairing);
        g2Field = new PBCG2Field(pairing);
        gTField = new PBCGTField(pairing);
        zRField = new PBCZrField(pairing);
    }
}
