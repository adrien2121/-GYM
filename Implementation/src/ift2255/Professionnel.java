package ift2255;

import java.io.Serializable;

/**
 * Cette classe herite de la classe abstraite Compte et implemente Serializable pour permettre de sauvergarder l'etat
 * des instances Professionnel.
 *
 * @see Compte
 * @see Membre
 * @see Serializable
 */
public class Professionnel extends Compte implements Serializable {

	public Professionnel(String nom, int id,
						 String adresse, String ville, String province, String codePostal,
						 String courriel) {
		super(nom, id, adresse, ville, province, codePostal, courriel);
	}

}
