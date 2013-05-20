package it.unisa.dia.gas.plaf.jpbc.mm.clt13.parameters;

/**
 * @author Angelo De Caro (angelo.decaro@gmail.com)
 * @since 1.3.0
 */
public class CTL13InstanceParameters {

    public static CTL13InstanceParameters TOY =
            new CTL13InstanceParameters(
                    757, // eta
                    165, // n
                    10, // alpha
                    160, // ell
                    27, //rho
                    12, // delta
                    5, // kappa
                    80, //beta
                    16, // theta
                    160 // bound
            );

    public static CTL13InstanceParameters SMALL =
            new CTL13InstanceParameters(
                    1779, // eta
                    540, // n
                    80, // alpha
                    160, // ell
                    41, //rho
                    23, // delta
                    6, // kappa
                    80, //beta
                    16, // theta
                    160 // bound
            );

    protected int eta, n, alpha, ell, rho, delta, deltaSquare, kappa, beta, theta, bound;

    public CTL13InstanceParameters(int eta, int n, int alpha, int ell,
                                   int rho, int delta, int kappa, int beta,
                                   int theta, int bound) {
        this.eta = eta;
        this.n = n;
        this.alpha = alpha;
        this.ell = ell;
        this.rho = rho;
        this.delta = delta;
        this.deltaSquare = delta * delta;
        this.kappa = kappa;
        this.beta = beta;
        this.theta = theta;
        this.bound = bound;
    }

    public int getEta() {
        return eta;
    }

    public int getN() {
        return n;
    }

    public int getAlpha() {
        return alpha;
    }

    public int getEll() {
        return ell;
    }

    public int getRho() {
        return rho;
    }

    public int getDelta() {
        return delta;
    }

    public int getDeltaSquare() {
        return deltaSquare;
    }

    public int getKappa() {
        return kappa;
    }

    public int getBeta() {
        return beta;
    }

    public int getTheta() {
        return theta;
    }

    public int getBound() {
        return bound;
    }

    public CTL13InstanceParameters setKappa(int kappa) {
        this.kappa = kappa;

        return this;
    }

    @Override
    public String toString() {
        return "CTL13InstanceParameters{" +
                "eta=" + eta +
                ", n=" + n +
                ", alpha=" + alpha +
                ", ell=" + ell +
                ", rho=" + rho +
                ", delta=" + delta +
                ", deltaSquare=" + deltaSquare +
                ", kappa=" + kappa +
                ", beta=" + beta +
                ", theta=" + theta +
                ", bound=" + bound +
                '}';
    }
}
