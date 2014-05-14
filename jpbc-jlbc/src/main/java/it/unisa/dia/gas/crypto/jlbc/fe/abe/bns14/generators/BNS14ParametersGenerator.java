package it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.generators;

import it.unisa.dia.gas.crypto.jlbc.fe.abe.bns14.params.BNS14Parameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.utils.MP12P2Utils;
import it.unisa.dia.gas.plaf.jlbc.sampler.UniformOneMinusOneSampler;

import java.security.SecureRandom;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class BNS14ParametersGenerator {
    private SecureRandom random;
    private int ell;
    private int depth;


    public BNS14ParametersGenerator(SecureRandom random, int ell, int depth) {
        this.random = random;
        this.ell = ell;
        this.depth = depth;
    }


    public BNS14Parameters generateParameters() {
        int n = 16;
        return new BNS14Parameters(
                random,
                ell,
                n,
                64,
                MP12P2Utils.getLWENoiseSampler(random, n),
                new UniformOneMinusOneSampler(random)
        );
    }
}
