package it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params;

import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2PrivateKeyParameters;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BNS14MasterSecretKeyParameters extends BNS14KeyParameters {

    private MP12HLP2PrivateKeyParameters latticeSk;


    public BNS14MasterSecretKeyParameters(BNS14Parameters parameters, MP12HLP2PrivateKeyParameters latticeSk) {
        super(true, parameters);

        this.latticeSk = latticeSk;
    }

    public MP12HLP2PrivateKeyParameters getLatticeSk() {
        return latticeSk;
    }
}