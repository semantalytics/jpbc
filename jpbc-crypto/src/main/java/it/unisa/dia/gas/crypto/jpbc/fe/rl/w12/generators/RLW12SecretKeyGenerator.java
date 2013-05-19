package it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.generators;

import it.unisa.dia.gas.crypto.jpbc.fe.rl.w12.params.*;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Pairing;
import it.unisa.dia.gas.plaf.jpbc.pairing.PairingFactory;
import org.bouncycastle.crypto.CipherParameters;
import org.bouncycastle.crypto.KeyGenerationParameters;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 */
public class RLW12SecretKeyGenerator {
    private RLW12SecretKeyGenerationParameters param;

    private Pairing pairing;
    private DFA dfa;

    public void init(KeyGenerationParameters param) {
        this.param = (RLW12SecretKeyGenerationParameters) param;

        this.pairing = PairingFactory.getPairing(this.param.getMasterSecretKeyParameters().getParameters().getParameters());
        this.dfa = this.param.getDfa();
    }

    public CipherParameters generateKey() {
        RLW12MasterSecretKeyParameters msk = param.getMasterSecretKeyParameters();
        RLW12PublicKeyParameters pk = param.getPublicKeyParameters();

        int ns = dfa.getNumStates();

        Element[] kStarts = new Element[2];
        Map<DFATransition, Element[]> kTransitions = new HashMap<DFATransition, Element[]>();
        Map<Integer, Element[]> kEnds = new HashMap<Integer, Element[]>();

        Element[] Ds = new Element[ns];
        for (int i = 0; i < ns; i++) {
            Ds[i] = pairing.getG1().newRandomElement().getImmutable();
        }

        // Initial state
        Element rs = pairing.getZr().newRandomElement();
        kStarts[0] = Ds[0].mul(pk.gethStart().powZn(rs));
        kStarts[1] = pk.getParameters().getG().powZn(rs);

        // Transitions
        for (int i = 0, size = dfa.getNumTransitions(); i < size; i++) {
            DFATransition DFATransition = dfa.getTransitionAt(i);

            Element rt = pairing.getZr().newRandomElement();

            kTransitions.put(
                    DFATransition,
                    new Element[]{
                            Ds[DFATransition.getFrom()].invert().mul(pk.getZ().powZn(rt)),
                            pk.getParameters().getG().powZn(rt),
                            Ds[DFATransition.getTo()].mul(pk.getHAt(DFATransition.getReading()).powZn(rt))
                    }
            );
        }

        // Final states
        Element secret = pk.getParameters().getG().powZn(msk.getAlpha().negate()).getImmutable();
        for (int i = 0, size = dfa.getNumFinalStates(); i < size; i++) {
            int finalState = dfa.getFinalStateAt(i);

            Element rf = pairing.getZr().newRandomElement();

            kEnds.put(
                    finalState,
                    new Element[]{
                        secret.mul(Ds[finalState]).mul(pk.gethEnd().powZn(rf)),
                        pk.getParameters().getG().powZn(rf)
                    }
            );
        }

        return new RLW12SecretKeyParameters(
                param.getPublicKeyParameters().getParameters(),
                dfa, kStarts, kTransitions, kEnds
        );
    }

}