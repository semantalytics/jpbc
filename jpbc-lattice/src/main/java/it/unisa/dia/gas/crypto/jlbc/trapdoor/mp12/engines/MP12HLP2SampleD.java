package it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.engines;

import it.unisa.dia.gas.crypto.cipher.ElementCipher;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2PrivateKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2PublicKeyParameters;
import it.unisa.dia.gas.crypto.jlbc.trapdoor.mp12.params.MP12HLP2SampleParameters;
import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.Matrix;
import it.unisa.dia.gas.jpbc.Vector;
import it.unisa.dia.gas.plaf.jlbc.field.floating.FloatingField;
import it.unisa.dia.gas.plaf.jlbc.sampler.Sampler;
import it.unisa.dia.gas.plaf.jlbc.sampler.ZGaussianCOVSampler;
import it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils;
import it.unisa.dia.gas.plaf.jpbc.field.vector.MatrixField;
import it.unisa.dia.gas.plaf.jpbc.field.vector.VectorField;
import it.unisa.dia.gas.plaf.jpbc.util.math.Cholesky;
import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.apfloat.Apint;
import org.bouncycastle.crypto.AsymmetricCipherKeyPair;
import org.bouncycastle.crypto.CipherParameters;

import java.security.SecureRandom;

import static it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils.ITWO;
import static it.unisa.dia.gas.plaf.jlbc.util.ApfloatUtils.newApfloat;

/**
 * @author Angelo De Caro (jpbclib@gmail.com)
 */
public class MP12HLP2SampleD extends MP12PLP2SampleD {

    protected MP12HLP2PublicKeyParameters pk;
    protected MP12HLP2PrivateKeyParameters sk;

    protected Element p, wMat, barWMat;


    public ElementCipher init(CipherParameters param) {
        AsymmetricCipherKeyPair keyPair = ((MP12HLP2SampleParameters) param).getKeyPair();

        pk = (MP12HLP2PublicKeyParameters) keyPair.getPublic();
        sk = (MP12HLP2PrivateKeyParameters) keyPair.getPrivate();

        // Init the Primitive Lattice Sampler
        super.init(pk);

        // Compute covariance matrix COV
        int barM = 2 * n;
        int w = n * k;

        SecureRandom random = sk.getParameters().getRandom();
        MatrixField<FloatingField> ff = new MatrixField<FloatingField>(random, new FloatingField(random), barM + w);

        Apfloat n = newApfloat(pk.getParameters().getN());
        Apfloat k = newApfloat(pk.getK());

        // Compute s1R = ((2\sqrt(n)) * (\sqrt(2n) + \sqrt(nk) + (t=1)) \ \sqrt(2\pi)
        Apfloat s1R = ApfloatMath.sqrt(n).multiply(ITWO).multiply(
                ApfloatMath.sqrt(n.multiply(ITWO)).add(ApfloatMath.sqrt(n.multiply(k))).add(ApfloatUtils.IONE)
        ).divide(ApfloatMath.sqrt(ApfloatUtils.pi().multiply(ITWO)));

        Apfloat s1Rsquare = ApfloatUtils.square(s1R);

        Apfloat sQuare = s1Rsquare.add(ApfloatUtils.IONE)
                .multiply(ApfloatUtils.ISIX)
                .multiply(ApfloatUtils.square(ApfloatUtils.IFIVE));

        Element rSquare = ff.getTargetField().newElement(pk.getGaussianParameter()).square();
        final Element aSquare = ff.getTargetField().newElement(pk.getGaussianParameter()).halve().square();
        final Element sSquare = ff.getTargetField().newElement(sQuare);

        System.out.println("s1R = " + ApfloatUtils.toString(s1R));
        System.out.println("s1Rsquare = " + ApfloatUtils.toString(s1Rsquare));
        System.out.println("sSquare = " + sSquare);
        System.out.println("rSquare = " + rSquare);
        System.out.println("aSquare = " + aSquare);

        Matrix cov = ff.newElement()
                .setSubMatrixFromMatrixAt(0, 0, sk.getR().mulByTranspose())
                .setSubMatrixFromMatrixAt(0, barM, sk.getR())
                .setSubMatrixFromMatrixTransposeAt(barM, 0, sk.getR())
                .setSubMatrixToIdentityAt(barM, barM, w);
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
        Matrix chol = Cholesky.cholesky(cov);

//        System.out.println("chol = " + chol);

        Sampler<Vector> sampler = new ZGaussianCOVSampler(random, chol, sk.getR().getTargetField());
        Vector p = sampler.sample();

        // Offline phase

        // Sample p in \Z^{\bar m + w}
        Element p1 = p.subVectorTo(barM);
        Element p2 = p.subVectorFrom(barM);

        this.p = p;
        this.barWMat = pk.getBarA().mul(p1.sub(sk.getR().mul(p2)));
        this.wMat = pk.getG().mul(p2);

        return this;
    }

    public Element processElements(Element... input) {
        // Online phase
        Element u = input[0];

        // Compute syndrom w
        Element v = u.duplicate().sub(pk.getA().mul(p));

        // Compute x
        Element z2 = super.processElements(v);
        Element z1 = sk.getR().mul(z2);

        return p.add(VectorField.union(z1, z2));
    }

}
