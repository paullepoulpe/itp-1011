package torrent.messages;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Classe abstraite: super classe de tous les messages "normaux" c a d pas le
 * HandShake ni le KeepAlive, mais tous les autres
 * 
 */
public abstract class Message {
	/**
	 * Contient l'algorithme a executer lorsqu'un {@link MessageVisitor} visite
	 * une type de message particulier
	 * 
	 * @param v
	 *            le {@link MessageVisitor} en question
	 */
	abstract public void accept(MessageVisitor v);

	/**
	 * Envoie le message de l'instance courante
	 * 
	 * @param output le flux de sortie par lequel on envoie le message ({@link DataOutputStream})
	 * @throws IOException
	 */
	abstract public void send(DataOutputStream output) throws IOException;
}