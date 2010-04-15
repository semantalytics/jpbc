/**
 * @author Costante Luca (dott.costante@gmail.com)
 * @author Giardino Daniele (dagix5@gmail.com)
 *
 */
package it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly;

import it.unisa.dia.gas.jpbc.Element;
import it.unisa.dia.gas.jpbc.ElementPowPreProcessing;
import it.unisa.dia.gas.jpbc.Field;
import it.unisa.dia.gas.jpbc.Pairing;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;

public class Commitment implements LibertYung_qTMC {

	private Field Zr;

	private Field G;

	private Pairing pairing;

	private ElementPowPreProcessing gp;

	/**
	 * Empty Commitment constructor; you need to set the G and Zr fields with
	 * the "set" methods
	 */
	public Commitment() {
	}

	/**
	 * Commitment constructor
	 * 
	 * @param G
	 *            The G field
	 * @param Zr
	 *            The Zr field
	 */
	public Commitment(Field G, Field Zr) {
		this.Zr = Zr;
		this.G = G;
	}

	/**
	 * Commitment constructor
	 * 
	 * @param G
	 *            The G field
	 * @param Zr
	 *            The Zr field
	 * @param pairing
	 *            Pairing object for Paring-Based operations
	 */
	public Commitment(Field G, Field Zr, Pairing pairing) {
		this.Zr = Zr;
		this.G = G;
		this.pairing = pairing;
	}

	/**
	 * Get the Zr field
	 */
	public Field getZr() {
		return Zr;
	}

	/**
	 * Set the Zr field
	 * 
	 * @param zr
	 */
	public void setZr(Field zr) {
		Zr = zr;
	}

	/**
	 * Get the G field
	 */
	public Field getG() {
		return G;
	}

	/**
	 * Set the field G
	 * 
	 * @param g
	 *            field G
	 */
	public void setG(Field g) {
		G = g;
	}

	/**
	 * Get the Pairing object for Pairing-Based operations
	 */
	public Pairing getPairing() {
		return pairing;
	}

	/**
	 * Set the Pairing object for Pairing-Based operations
	 * 
	 * @param pairing
	 *            Pairing object
	 */
	public void setPairing(Pairing pairing) {
		this.pairing = pairing;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly.LibertYung_qTMC#qKeygen(int, int)
	 */
	public CommitmentKeys qKeygen(int q) {
		CommitmentKeys commkey = new CommitmentKeys();
		Element g = G.newRandomElement();
		Element alpha = Zr.newRandomElement();

		gp = g.pow();
		ElementPowPreProcessing alphap = alpha.pow();

		ArrayList<Element> key = new ArrayList<Element>();
		key.add(0, g);
		for (int i = 1; i <= (2 * q); i++) {
			Element ai = alphap.powZn(Zr.newElement(i));
			Element gi = gp.powZn(ai);
			if (i == (q + 1)) {
				commkey.setTk(gi);
				key.add(i, G.newZeroElement().setToZero());
			} else {
				key.add(i, gi);
			}
		}
		commkey.setPk(key);
		return commkey;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see libert_yung.LibertYung_qTMC#qHCom(java.util.ArrayList,
	 * java.util.ArrayList)
	 */
	public OutputCommit qHCom(ArrayList<Element> Pk, ArrayList<Element> messages) {
		OutputCommit ohc = new OutputCommit();

		Element gamma = Zr.newRandomElement();
		Element teta = Zr.newRandomElement();

		Element C = gp.powZn(teta);

		ohc.setC(C);

		Element V = gp.powZn(gamma);

		int q = messages.size() - 1;
		for (int j = 1; j <= q; j++) {
			Element current = Pk.get(q + 1 - j).duplicate().powZn(
					messages.get(j));
			V.mul(current);
		}
		ohc.setV(V);
		ArrayList<Element> aux = messages;
		aux.add(q + 1, gamma);
		aux.add(q + 2, teta);
		ohc.setAux(aux);

		return ohc;
	}

	/**
	 * Takes as input an ordered tuple of messages. It outputs a hard it.unisa.dia.gas.plaf.jpbc.crypto.commitment
	 * C to (m1,...,mq) under the public key pk and some auxiliary state
	 * information aux.
	 * 
	 * @param Pk
	 *            public key
	 * @param messages
	 *            the q messages
	 * @param random
	 *            pseudorandom generator
	 * @return OutputCommit that contains C, V, aux= (m1,...,mq, GAMMA, TETA)
	 * 
	 * @see LibertYung_qTMC#qHCom(java.util.ArrayList,
	 *      java.util.ArrayList)
	 */
	public OutputCommit qHComPr(ArrayList<Element> Pk,
			ArrayList<Element> messages, SecureRandom random) {
		OutputCommit ohc = new OutputCommit();

		Element gamma = Zr.newElement(BigInteger.valueOf(random
				.nextInt(Integer.MAX_VALUE)));
		Element teta = Zr.newElement(BigInteger.valueOf(random
				.nextInt(Integer.MAX_VALUE)));

		Element C = gp.powZn(teta);

		ohc.setC(C);

		Element V = gp.powZn(gamma);

		int q = messages.size() - 1;
		for (int j = 1; j <= q; j++) {
			Element current = Pk.get(q + 1 - j).duplicate().powZn(
					messages.get(j));
			V.mul(current);
		}
		ohc.setV(V);
		ArrayList<Element> aux = messages;
		aux.add(q + 1, gamma);
		aux.add(q + 2, teta);
		ohc.setAux(aux);

		return ohc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see libert_yung.LibertYung_qTMC#qSCom(java.util.ArrayList)
	 */
	public OutputCommit qSCom(ArrayList<Element> Pk) {
		OutputCommit osc = new OutputCommit();

		Element gamma = Zr.newRandomElement();
		Element teta = Zr.newRandomElement();
		Element g1 = Pk.get(1);

		ElementPowPreProcessing g1p = g1.pow();

		Element C = g1p.powZn(teta);
		osc.setC(C);

		Element V = g1p.powZn(gamma);
		osc.setV(V);

		ArrayList<Element> aux = new ArrayList<Element>();
		aux.add(0, teta);
		aux.add(1, gamma);
		osc.setAux(aux);
		return osc;
	}

	/**
	 * It is a probabilistic algorithm that generates a soft it.unisa.dia.gas.plaf.jpbc.crypto.commitment and some
	 * auxiliary information aux. Such a it.unisa.dia.gas.plaf.jpbc.crypto.commitment is not associated with a
	 * specific sequence of messages.
	 * 
	 * @param Pk
	 *            public key
	 * @param random
	 *            pseudorandom generator
	 * @return OutputCommit that contains C, V, aux=(TETA, GAMMA)
	 * 
	 * @see LibertYung_qTMC#qSCom(java.util.ArrayList)
	 */
	public OutputCommit qSComPr(ArrayList<Element> Pk, SecureRandom random) {
		OutputCommit osc = new OutputCommit();

		Element gamma = Zr.newElement(BigInteger.valueOf(random
				.nextInt(Integer.MAX_VALUE)));
		Element teta = Zr.newElement(BigInteger.valueOf(random
				.nextInt(Integer.MAX_VALUE)));

		Element g1 = Pk.get(1);

		ElementPowPreProcessing g1p = g1.pow();

		Element C = g1p.powZn(teta);
		osc.setC(C);

		Element V = g1p.powZn(gamma);
		osc.setV(V);

		ArrayList<Element> aux = new ArrayList<Element>();
		aux.add(0, teta);
		aux.add(1, gamma);
		osc.setAux(aux);
		return osc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see libert_yung.LibertYung_qTMC#qFake(java.util.ArrayList,
	 * it.unisa.dia.gas.jpbc.Element)
	 */
	public OutputCommit qFake(ArrayList<Element> Pk, Element tk) {
		OutputCommit ofc = new OutputCommit();

		Element gamma = Zr.newRandomElement();
		Element teta = Zr.newRandomElement();
		Element C = gp.powZn(teta);
		ofc.setC(C);

		Element V = gp.powZn(gamma);
		ofc.setV(V);

		ArrayList<Element> aux = new ArrayList<Element>();
		aux.add(0, teta);
		aux.add(1, gamma);
		ofc.setAux(aux);
		return ofc;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see libert_yung.LibertYung_qTMC#qHOpen(java.util.ArrayList,
	 * it.unisa.dia.gas.jpbc.Element, int, java.util.ArrayList)
	 */
	public Element[] qHOpen(ArrayList<Element> PK, Element mi, int i,
			ArrayList<Element> aux) throws MessageMismatch {
		Element[] piGreek = new Element[2];
		Element gamma = aux.get(aux.size() - 2);
		Element teta = aux.get(aux.size() - 1);

		if (aux.contains(mi) && (!aux.get(i).equals(mi)))
			throw new MessageMismatch(
					"The message in mi parameter is not the same of aux[i]");

		Element Wi = PK.get(i).duplicate().powZn(gamma);

		int q = aux.size() - 3;
		for (int j = 1; j <= q; j++) {
			if (j != i) {
				Element cur = PK.get(q + 1 - j + i).duplicate().powZn(
						aux.get(j));
				Wi.mul(cur);
			}
		}
		Wi.powZn(teta.duplicate().invert());
		piGreek[0] = teta;
		piGreek[1] = Wi;
		return piGreek;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see libert_yung.LibertYung_qTMC#qSOpen(java.util.ArrayList,
	 * it.unisa.dia.gas.jpbc.Element, int, boolean, java.util.ArrayList)
	 */
	public Element qSOpen(ArrayList<Element> PK, Element message, int i,
			int flag, ArrayList<Element> aux) {
		Element tau = null;

		Element gamma;
		Element teta;

		if (flag == LibertYung_qTMC.SOFT_COMMITMENT) {// soft commit
			teta = aux.get(0);
			gamma = aux.get(1);
			int q = (int) PK.size() / 2;
			Element WiA = PK.get(i).duplicate().powZn(gamma);
			Element WiB = PK.get(q).duplicate().powZn(
					message.duplicate().negate());
			WiA.mul(WiB);
			WiA.powZn(teta.duplicate().invert());
			tau = WiA;
		} else if (flag == LibertYung_qTMC.HARD_COMMITMENT) {// hard commit
			Element mi = aux.get(i);
			if (!mi.isEqual(message))
				return tau = PERPENDICULAR;
			gamma = aux.get(aux.size() - 2);
			teta = aux.get(aux.size() - 1);
			Element Wi = PK.get(i).duplicate().powZn(gamma);
			int q = aux.size() - 3;
			for (int j = 1; j <= q; j++) {
				if (j != i) {
					Element cur = PK.get(q + 1 - j + i).duplicate().powZn(
							aux.get(j));
					Wi.mul(cur);
				}
			}
			Wi.powZn(teta.duplicate().invert());
			tau = Wi;
		}

		return tau;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see libert_yung.LibertYung_qTMC#qHVer(java.util.ArrayList,
	 * it.unisa.dia.gas.jpbc.Element, int, it.unisa.dia.gas.jpbc.Element,
	 * it.unisa.dia.gas.jpbc.Element, it.unisa.dia.gas.jpbc.Element[])
	 */
	public boolean qHVer(ArrayList<Element> PK, Element mi, int i, Element C,
			Element V, Element[] piGreek) {
		Element teta = piGreek[0].duplicate();
		Element Wi = piGreek[1].duplicate();
		Element e1 = pairing.pairing(PK.get(i), V);
		Element e2 = pairing.pairing(C, Wi);
		int q = (int) PK.size() / 2;
		Element e2b = pairing.pairing(PK.get(1), PK.get(q)).powZn(mi);
		e2.mul(e2b);

		if (e1.isEqual(e2) && (C.isEqual(gp.powZn(teta))))
			return true;

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see libert_yung.LibertYung_qTMC#qSVer(java.util.ArrayList,
	 * it.unisa.dia.gas.jpbc.Element, int, it.unisa.dia.gas.jpbc.Element,
	 * it.unisa.dia.gas.jpbc.Element, it.unisa.dia.gas.jpbc.Element)
	 */
	public boolean qSVer(ArrayList<Element> PK, Element mi, int i, Element C,
			Element V, Element tau) {
		Element Wi = tau;
		if (Wi == PERPENDICULAR)
			return false;
		Element e1 = pairing.pairing(PK.get(i), V);
		Element e2a = pairing.pairing(C, Wi);
		int q = (int) PK.size() / 2;
		Element e2b = pairing.pairing(PK.get(1).duplicate(),
				PK.get(q).duplicate()).powZn(mi);
		Element e2 = e2a.mul(e2b);

		// verifica che C e V appartengono a G !!!!
		if (!(G.equals(C.getField()) && G.equals(V.getField()))) {
			return false;
		}

		if (e1.isEqual(e2)) {
			return true;
		}

		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see libert_yung.LibertYung_qTMC#qHEquiv(java.util.ArrayList,
	 * it.unisa.dia.gas.jpbc.Element, java.util.ArrayList, int,
	 * java.util.ArrayList)
	 */
	public Element[] qHEquiv(ArrayList<Element> pk, Element tk,
			ArrayList<Element> messages, int i, ArrayList<Element> aux) {
		Element[] piGreek = new Element[2];

		Element teta = aux.get(0);
		Element gamma = aux.get(1);

		Element WiA = pk.get(i).duplicate().powZn(gamma);
		Element WiB = tk.duplicate()
				.powZn(messages.get(i).duplicate().negate());
		WiA.mul(WiB);
		WiA.powZn(teta.duplicate().invert());
		piGreek[0] = teta;
		piGreek[1] = WiA;
		return piGreek;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see libert_yung.LibertYung_qTMC#qSEquiv(java.util.ArrayList,
	 * it.unisa.dia.gas.jpbc.Element, it.unisa.dia.gas.jpbc.Element, int,
	 * java.util.ArrayList)
	 */
	public Element qSEquiv(ArrayList<Element> pk, Element tk, Element message,
			int i, ArrayList<Element> aux) {

		Element teta = aux.get(0);
		Element gamma = aux.get(1);
		Element WiA = pk.get(i).duplicate().powZn(gamma);

		Element WiB = tk.duplicate().powZn(message.duplicate().negate());
		WiA = WiA.mul(WiB);
		WiA = WiA.powZn(teta.duplicate().invert());

		return WiA;

	}

}
