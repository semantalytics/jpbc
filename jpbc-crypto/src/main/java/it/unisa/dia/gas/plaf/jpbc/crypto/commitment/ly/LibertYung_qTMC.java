/**
 * @author Costante Luca (dott.costante@gmail.com)
 * @author Giardino Daniele (dagix5@gmail.com)
 */
package it.unisa.dia.gas.plaf.jpbc.crypto.commitment.ly;

import it.unisa.dia.gas.jpbc.Element;

import java.util.ArrayList;

/**
 * Libert-Yung qTMC scheme
 */
public interface LibertYung_qTMC {
	/**
	 * Perpendicular symbol of the paper (it means "null", I guess :) )
	 */
	public static final Element PERPENDICULAR = null;

	/**
	 * Hard commitment
	 */
	public static final int HARD_COMMITMENT = 0;

	/**
	 * Soft commitment
	 */
	public static final int SOFT_COMMITMENT = 1;

	/**
	 * Takes as input a security parameter LAMPDA and the number q of messages
	 * that can be committed to in a single commitment. The output is a pair of
	 * public/private keys (pk,tk)
	 * 
	 * @param q
	 *            number of messages
	 * @return CommitmentsKeys that contains pk=(g, g1, g2, ... , gq, gq+2, ...,
	 *         g2q) and tk=(gq+1)
	 */
	public CommitmentKeys qKeygen(int q);

	/**
	 * Takes as input an ordered tuple of messages. It outputs a hard commitment
	 * C to (m1,...,mq) under the public key pk and some auxiliary state
	 * information aux.
	 * 
	 * @param Pk
	 *            public key
	 * @param messages
	 *            the q messages
	 * @return OutputCommit that contains C, V, aux= (m1,...,mq, GAMMA, TETA)
	 */
	public OutputCommit qHCom(ArrayList<Element> Pk, ArrayList<Element> messages);

	/**
	 * It is a probabilistic algorithm that generates a soft commitment and some
	 * auxiliary information aux. Such a commitment is not associated with a
	 * specific sequence of messages.
	 * 
	 * @param Pk
	 *            public key
	 * @return OutputCommit that contains C, V, aux=(TETA, GAMMA)
	 */
	public OutputCommit qSCom(ArrayList<Element> Pk);

	/**
	 * Is a randomized algorithm that takes as input the trapdoor tk and
	 * generate a q-fake commitment C and some auxiliary information aux. The
	 * commitment C is not bound to any sequence of messages. The q-fake
	 * commitment C is similar to a soft de-commitment with the differce that it
	 * can be hard-opening using the trapdoor tk.
	 * 
	 * @param Pk
	 *            public key
	 * @param tk
	 *            private key
	 * @return OutputCommit that contains C, V, aux=(GAMMA, TETA)
	 */
	public OutputCommit qFake(ArrayList<Element> Pk, Element tk);

	/**
	 * Is a Hard opening algorithm. Given the public key pk, the message m, m's
	 * index, aux=(m1,...,mq, GAMMA, TETA). It outputs a hard de-commitment
	 * PI-GREEK=(TETA, Wi)
	 * 
	 * @param PK
	 *            public key
	 * @param mi
	 *            the message m
	 * @param i
	 *            index of message m
	 * @param aux
	 *            auxilary information. aux=(m1,...,mq, GAMMA, TETA)
	 * @return Element [] PI-GREEK=(TETA, Wi)
	 * @throws MessageMismatch
	 *             The message in mi and the message in aux are different
	 */
	public Element[] qHOpen(ArrayList<Element> PK, Element mi, int i,
			ArrayList<Element> aux) throws MessageMismatch;

	/**
	 * Generate a soft-decommitment "tease" TAU of C to the message m at postion
	 * i. The variable flag indicates whether the state information aux
	 * corresponds to a hard commitment. If flag is set to Hard and m<>mi the
	 * algorithm returns null.
	 * 
	 * @param PK
	 *            public key
	 * @param message
	 *            message m
	 * @param i
	 *            position of message
	 * @param flag
	 *            true if soft, false if it's a Hard commit
	 * @param aux
	 *            auxiliary information.
	 * @return Element TAU=Wi or int the case of Hard commitment and m<>mi it
	 *         return null
	 */
	public Element qSOpen(ArrayList<Element> PK, Element message, int i,
			int flag, ArrayList<Element> aux);

	/**
	 * Is the Hard verification algorithm. Given the message m and it index. The
	 * value C, V and PI-GREEK=(TETA, Wi).
	 * 
	 * @param PK
	 *            public key
	 * @param mi
	 *            the message m
	 * @param i
	 *            index of message m
	 * @param C
	 *            value of Commitment
	 * @param V
	 *            value of V
	 * @param piGreek
	 *            value of PI-GREEK=(TETA, Wi)
	 * @return true if PIGREEK gives evidence that C is commitment to sequence
	 *         (m1,...,mq) such that mi=m. Otherwise, it ouputs false.
	 */
	public boolean qHVer(ArrayList<Element> PK, Element mi, int i, Element C,
			Element V, Element[] piGreek);

	/**
	 * Return true if TAU is a valid soft de-commitment of C to m at position i
	 * and false otherwise. If TAU is valid and C is a hard commitment, its hard
	 * opening must be to m at index i.
	 * 
	 * @param PK
	 *            public key
	 * @param mi
	 *            message m
	 * @param i
	 *            position of m
	 * @param C
	 *            value of Commitment C
	 * @param V
	 *            value of V
	 * @param tau
	 *            value of TAU=Wi
	 * @return Return true if TAU is a valid soft de-commitment of C to m at
	 *         position i and false otherwise.
	 */
	public boolean qSVer(ArrayList<Element> PK, Element mi, int i, Element C,
			Element V, Element tau);

	/**
	 * Is a Non-adaptive hard equivation algorithm. Namely, given (C,aux)=qFake,
	 * it generates a hard de-commitment PIGREEK for C at the i^th position of
	 * sequence (m1,...,mq). The algorithm is non-adaptive in that the sequence
	 * of messages has to be determined once-and-for-all before the execution of
	 * qHEquiv.
	 * 
	 * @param pk
	 *            public key
	 * @param tk
	 *            private key/trapdoor
	 * @param messages
	 *            messages=(m1,...,mq)
	 * @param i
	 *            index of message m
	 * @param aux
	 *            auxiliary information
	 * @return Element[] piGreek: PIGREEK=(TETA, Wi)
	 */
	public Element[] qHEquiv(ArrayList<Element> pk, Element tk,
			ArrayList<Element> messages, int i, ArrayList<Element> aux);

	/**
	 * Is a soft equivocation algorithm. Given the auxiliary information aux
	 * returned by (C,aux)=qFake, it creates a soft de-commitment TAU to m at
	 * position i
	 * 
	 * @param pk
	 *            public key
	 * @param tk
	 *            private key/trapdoor
	 * @param message
	 *            the message m
	 * @param i
	 *            index of message m
	 * @param aux
	 *            auxiliary information aux=(TETA, GAMMA)
	 * @return Element Wi
	 */
	public Element qSEquiv(ArrayList<Element> pk, Element tk, Element message,
			int i, ArrayList<Element> aux);

}
