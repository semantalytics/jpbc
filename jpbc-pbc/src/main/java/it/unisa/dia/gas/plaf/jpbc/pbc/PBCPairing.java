package it.unisa.dia.gas.plaf.jpbc.pbc;

import it.unisa.dia.gas.jpbc.*;
import it.unisa.dia.gas.plaf.jpbc.pairing.CurveParams;
import it.unisa.dia.gas.plaf.jpbc.pbc.field.PBCG1Field;
import it.unisa.dia.gas.plaf.jpbc.pbc.field.PBCG2Field;
import it.unisa.dia.gas.plaf.jpbc.pbc.field.PBCGTField;
import it.unisa.dia.gas.plaf.jpbc.pbc.field.PBCZrField;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCLibraryProvider;
import it.unisa.dia.gas.plaf.jpbc.pbc.jna.PBCPairingPPType;
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
        pairing = new PBCPairingType(curveParams.toString(" "));

        // Init fields
        initFields();
    }


    public boolean isSymmetric() {
        return PBCLibraryProvider.getPbcLibrary().pbc_pairing_is_symmetric(pairing) == 1;
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

    public Element pairing(Element in1, Element in2) {
        PBCElement out = (PBCElement) gTField.newElement();

        PBCLibraryProvider.getPbcLibrary().pbc_pairing_apply(out.getValue(), ((PBCElement) in1).getValue(), ((PBCElement) in2).getValue(), pairing);

        return out;
    }

    public PairingPreProcessing pairing(Element in1) {
        return new PBCPairingPreProcessing(in1);
    }

    public boolean isAlmostCoddh(Element a, Element b, Element c, Element d) {
        return PBCLibraryProvider.getPbcLibrary().pbc_is_almost_coddh(
                ((PBCElement) a).getValue(),
                ((PBCElement) b).getValue(),
                ((PBCElement) c).getValue(),
                ((PBCElement) d).getValue(),
                pairing
        ) == 1;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        
        g1Field = null;
        g2Field = null;
        gTField = null;
        zRField = null;
    }

    protected void initFields() {
        g1Field = new PBCG1Field(pairing);
        g2Field = new PBCG2Field(pairing);
        gTField = new PBCGTField(pairing);
        zRField = new PBCZrField(pairing);
    }


    public class PBCPairingPreProcessing implements PairingPreProcessing {
        protected PBCPairingPPType pairingPPType;


        public PBCPairingPreProcessing(Element g1) {
            pairingPPType = new PBCPairingPPType(((PBCElement) g1).value, pairing);
        }


        public Element pairing(Element in2) {
            PBCElement out = (PBCElement) gTField.newElement();
            PBCLibraryProvider.getPbcLibrary().pbc_pairing_pp_apply(out.value, ((PBCElement) in2).value, pairingPPType);

            return out;
        }
    }
}
