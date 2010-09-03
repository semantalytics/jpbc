package it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

/**
 * Keys Object. It contains the public and the trapdoor key.
 */
public class CommitmentKeys {

	/**
	 * Public key
	 */
	private ArrayList<Element> pk;

	/**
	 * Trapdoor key
	 */
	private Element tk;

	/**
	 * Get the trapdoor key
	 */
	public Element getTk() {
		return tk.duplicate();
	}

	/**
	 * Set the trapdoor key
	 * 
	 * @param tk
	 *            trapdoor key
	 */
	public void setTk(Element tk) {
		this.tk = tk;
	}

	/**
	 * Get the public key
	 */
	public ArrayList<Element> getPk() {
		return pk;
	}

	/**
	 * Set the public key
	 * 
	 * @param pk
	 *            public key
	 */
	public void setPk(ArrayList<Element> pk) {
		this.pk = pk;
	}
}
