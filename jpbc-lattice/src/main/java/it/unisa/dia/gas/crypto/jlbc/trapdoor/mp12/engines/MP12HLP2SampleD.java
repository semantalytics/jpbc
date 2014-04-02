package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2SampleParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.utils.LatticeUtils;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jlbc.field.floating.FloatingField;
import it.unisa.dia.gas.plaf.jlbc.sampler.DiscreteGaussianCOVSampler;
import it.unisa.dia.gas.plaf.jpbc.sampler.Sampler;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import it.unisa.dia.gas.plaf.jpbc.util.math.Cholesky;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;

import static it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.utils.LatticeUtils.getSSquare;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2SampleD extends MP12PLP2SampleD {

    protected static Map<CipherParameters, Matrix> covs = new HashMap<CipherParameters, Matrix>();


    protected MP12HLP2PublicKeyParameters pk;
    protected MP12HLP2PrivateKeyParameters sk;

    protected Sampler<Vector> offlineSampler;


    public ElementCipher init(CipherParameters param) {
        AsymmetricCipherKeyPair keyPair = ((MP12HLP2SampleParameters) param).getKeyPair();

        pk = (MP12HLP2PublicKeyParameters) keyPair.getPublic();
        sk = (MP12HLP2PrivateKeyParameters) keyPair.getPrivate();

        // Init the Primitive Lattice Sampler
        super.init(pk);

        // Init offline sampler
        SecureRandom random = sk.getParameters().getRandom();
        Matrix cov = covs.get(sk);
        if (cov == null) {
            cov = computeCOV2(random, 2 * n, n * k);
            covs.put(sk, cov);
        }

        offlineSampler = new DiscreteGaussianCOVSampler(random, cov, sk.getR().getTargetField(), LatticeUtils.RRP);

        return this;

    }

    public Element processElements(Element... input) {
        // Online phase
        Element u = input[0];

        // sample perturbation
        Element[] perturbation = samplePerturbation();
        Element p = perturbation[0];
        Element offset = perturbation[1];

        // Compute syndrome w
        Element v = u.duplicate().sub(offset);
//        Element v = u;

        // Compute x
        Element z2 = super.processElements(v);
        Element z1 = sk.getR().mul(z2);

        return p.add(VectorField.union(z1, z2));
//        return VectorField.union(z1, z2);
    }


    Element p, o;

    protected Element[] samplePerturbation() {
        // TODO: must be new every time!
//        if (p == null) {
        Element p = offlineSampler.sample();
        Element o = pk.getA().mul(p);
//        }

        return new Element[]{p, o};
    }

    protected Matrix computeCOV(SecureRandom random, int n, int m) {
        // Compute covariance matrix COV
        MatrixField<FloatingField> ff = new MatrixField<FloatingField>(random, new FloatingField(random), n + m);

        final Element sSquare = ff.getTargetField().newElement(getSSquare(n, m));
        final Element aSquare = ff.getTargetField().newElement(LatticeUtils.RRP_SQUARE);
        Element rSquare = ff.getTargetField().newElement(LatticeUtils.TWO_RRP_SQUARE);

        Matrix cov = ff.newElement()
                .setSubMatrixFromMatrixAt(0, 0, sk.getR().mulByTranspose())
                .setSubMatrixFromMatrixAt(0, n, sk.getR())
                .setSubMatrixFromMatrixTransposeAt(n, 0, sk.getR())
                .setSubMatrixToIdentityAt(n, n, m);
        cov.mul(rSquare);

        // Construct \Sigma_P = s^2 I - COV
        cov.transform(new Matrix.Transformer() {
            public void transform(int row, int col, Element e) {
                e.negate();
                if (row == col)
                    e.add(sSquare);
            }
        });

        // Construct \Sigma_P - a^2 I
        cov.transform(new Matrix.Transformer() {
            public void transform(int row, int col, Element e) {
                if (row == col)
                    e.sub(aSquare);
            }
        });

        // Compute Cholesky decomposition
        cov = Cholesky.cholesky(cov);
//        System.out.println("cov = " + cov);

        return cov;
    }

    protected Matrix computeCOV2(SecureRandom random, int n, final int m) {
        // Compute covariance matrix COV
        MatrixField<FloatingField> ff = new MatrixField<FloatingField>(random, new FloatingField(random), n + m);

        Element sSquare = ff.getTargetField().newElement(getSSquare(n, m));
        Element rSquare = ff.getTargetField().newElement(LatticeUtils.TWO_RRP_SQUARE);
        Element aSquare = ff.getTargetField().newElement(LatticeUtils.RRP_SQUARE);

        Element b = sSquare.duplicate().sub(rSquare).sub(aSquare);
        final Element sqrtB = b.duplicate().sqrt();
        final Element rSquarePlusOneOverB = rSquare.duplicate().add(b.duplicate().invert());
        final Element sSquareMinusASquare = sSquare.duplicate().sub(aSquare);

        Matrix cov = ff.newElement()
                .setSubMatrixToIdentityAt(0, 0, m)
                .setSubMatrixFromMatrixAt(m, 0, sk.getR())
                .setSubMatrixFromMatrixAt(m, m, sk.getR().mulByTranspose());


//        System.out.println("cov = " + cov);

        // scale upper left square by sqrtB
        cov.transform(new Matrix.Transformer() {
            public void transform(int row, int col, Element e) {
                if (row == col && row < m)
                    e.div(sqrtB);
            }
        });

        // scale bottom left square by sqrtB
        cov.transform(new Matrix.Transformer() {
            public void transform(int row, int col, Element e) {
                if (row >= m && col < m)
                    e.div(sqrtB);
            }
        });

        // Construct \Sigma_P = s^2 I - COV
        cov.transform(new Matrix.Transformer() {
            public void transform(int row, int col, Element e) {
                e.mul(rSquarePlusOneOverB);

                e.negate();
                if (row == col && row >= m)
                    e.add(sSquareMinusASquare);
            }
        });


        // Compute Cholesky decomposition
        cov = Cholesky.cholesky2(cov, m, m);

//        System.out.println("cov = " + cov.toStringSubMatrix(m, m));


//        System.out.println("cov = " + cov);

        return cov;
    }


}
