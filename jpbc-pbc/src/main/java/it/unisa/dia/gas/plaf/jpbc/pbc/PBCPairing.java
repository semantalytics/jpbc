package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.jpbc.Point;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCPairingType;

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
        if (!PBCLibraryProvider.isAvailable())
            throw new IllegalStateException("PBC support not available.");
        
        // Init pairing...
        String buf = curveParams.toString(" ");

        pairing = new PBCPairingType();
        PBCLibraryProvider.getPbcLibrary().pbc_pairing_init_inp_buf(pairing, buf, buf.length());

        // Init fields

        initFields();
    }


    public boolean isSymmetric() {
        return PBCLibraryProvider.getPbcLibrary().pbc_pairing_is_symmetric(pairing) == 0;
    }

    public Field<? extends Point> getG1() {
        return g1Field;
    }

    public Field<? extends Point> getG2() {
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

        PBCLibraryProvider.getPbcLibrary().pbc_pairing_apply(out.getValue(), ((PBCElement) g1).getValue(), ((PBCElement) g2).getValue(), pairing);

        return out;
    }


    @Override
    protected void finalize() throws Throwable {
        g1Field = null;
        g2Field = null;
        gTField = null;
        zRField = null;
        
        PBCLibraryProvider.getPbcLibrary().pbc_pairing_clear(pairing);
    }

    protected void initFields() {
        g1Field = new PBCG1Field(pairing);
        g2Field = new PBCG2Field(pairing);
        gTField = new PBCGTField(pairing);
        zRField = new PBCZrField(pairing);
    }
}
