package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params;

import org.bouncycastle.crypto.params.AsymmetricKeyParameter;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2InverterParameters extends AsymmetricKeyParameter {

    protected MP12HLP2PublicKeyParameters pk;
    protected MP12HLP2PrivateKeyParameters sk;


    public MP12HLP2InverterParameters(MP12HLP2PublicKeyParameters pk, MP12HLP2PrivateKeyParameters sk) {
        super(true);

        this.pk = pk;
        this.sk = sk;
    }

    public MP12HLP2PublicKeyParameters getPk() {
        return pk;
    }

    public MP12HLP2PrivateKeyParameters getSk() {
        return sk;
    }
}
