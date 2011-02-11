package it.unisa.dia.gas.crypto.jpbc.fe.ip.ot10.params;

import it.unisa.dia.gas.jpbc.Element;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.Arrays;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class IPOT10SearchKeyGenerationParameters extends KeyGenerationParameters {

    private IPOT10PrivateKeyParameters params;
    private Element[] y;


    public IPOT10SearchKeyGenerationParameters(IPOT10PrivateKeyParameters params, Element[] y) {
        super(null, params.getParameters().getG().getField().getLengthInBytes());

        this.params = params;
        this.y = Arrays.copyOf(y, y.length);
    }

    public IPOT10PrivateKeyParameters getParameters() {
        return params;
    }

    public Element getYAt(int index) {
        return y[index];
    }

}