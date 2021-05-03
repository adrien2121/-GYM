package ift2255;

import java.io.Serializable;

/**
 * Cette classe herite de la classe abstraite Compte et implemente Serializable pour permettre de sauvergarder l'etat
 * des instances Membre.
 *
 * @see Compte
 * @see Professionnel
 * @see Serializable
 */
public class Membre extends Compte implements Serializable {

	private boolean isSuspended = false;
	
	public Membre(String nom, int id,
				  String adresse, String ville, String province, String codePostal,
				  String courriel) {

		super(nom, id, adresse, ville, province, codePostal, courriel);
	}

	/**
	 * Methode getter de l'attribut isSuspended.
	 * @return Retourne le booleen contenu dans isSuspended.
	 */
	public boolean isSuspended() {
		return isSuspended;
	}

	/**
	 * Méthode set l'attribut isSuspended a false.
	 */
	public void suspendre() {
		isSuspended = true;
	}

	/**
	 * Méthode set l'attribut isSuspended a true.
	 */
	public void insuspendre() {
		isSuspended = false;
	}

}
