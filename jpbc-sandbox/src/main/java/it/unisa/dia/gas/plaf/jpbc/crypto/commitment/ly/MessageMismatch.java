/**
 * @author Costante Luca (dott.costante@gmail.com)
 * @author Giardino Daniele (dagix5@gmail.com)
 */
package it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly;

public class MessageMismatch extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message
	 */
	public MessageMismatch(String message) {
		super(message);
	}

}
