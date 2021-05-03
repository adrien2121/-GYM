package ift2255;

import ift2255.formateur.NumberFormat;
import java.io.Serializable;

/**
 * Cette classe abstraite encapsule les donnees necessaires pour creer un compte professionnel ou membre. Les donnees
 * necessaires sont:
 *
 * <pre>
 *     		Nom
 *     		Numero unique
 *     		Adresse
 *     		Ville
 *     		Province
*     		Code postal
 *     		Courriel
 * </pre>
 * @see Membre
 * @see Professionnel
 * @see Serializable
 * @see CentreDonnee
 * @see NumberFormat
 */
public class Compte implements Serializable {

	private String nom; // 25 char
	private int ID; // 9 chiffres

	private String adresse; // 25 char
	private String ville; // 14 char
	private String province; // 2 char
	private String codePostal; // 6 char

	private String courriel;

	public Compte(String nom, int id,
				  String adresse, String ville, String province, String codePostal,
				  String courriel) {
		this.nom = nom;
		this.ID = id;

		this.adresse = adresse;
		this.ville = ville;
		this.province = province;
		this.codePostal = codePostal;

		this.courriel = courriel;
	}


	/**
	 * Getter pour attribut nom.
	 * @return Retourne le String qui est contenu dans l'attribut nom.
	 */
	public String getNom() {
		return nom;
	}

	/**
	 * Getter pour attribut id.
	 * @return Retourne le int contenu dans l'attribut id.
	 */
	public int getID() {
		return ID;
	}

	/**
	 * Getter pour l'attribut adresse.
	 * @return Retourne le String contenu dans l'attribut adresse.
	 */
	public String getAdresse() {
		return adresse;
	}

	/**
	 * Getter pour l'attribut province.
	 * @return Retourne le String contenu dans l'attribut province.
	 */
	public String getProvince() {
		return province;
	}

	/**
	 * Getter pour l'attribut ville.
	 * @return Retourne le String contenu dans l'attribut ville.
	 */
	public String getVille() {
		return ville;
	}

	/**
	 * Getter pour l'attribut codePostal.
	 * @return Retourne le String contenu dans l'attribut codePostal.
	 */
	public String getCodePostal() {
		return codePostal;
	}

	/**
	 * Getter pour l'attribut courriel.
	 * @return Retourne le String contenu dans l'attribut courriel.
	 */
	public String getCourriel() {
		return courriel;
	}

	/**
	 * Setter pour l'attribut nom.
	 * @param nom un String ayant au plus 25 caracteres.
	 */
	public void setNom(String nom) {
		this.nom = nom;
	}

	/**
	 * Setter pour l'attribut adresse.
	 * @param adresse un String ayant au plus 25 caracteres.
	 */
	public void setAdresse(String adresse) {
		this.adresse = adresse;
	}

	/**
	 * Setter pour l'attribut codePostal.
	 * @param codePostal un String ayant au plus 6 caracteres.
	 */
	public void setCodePostal(String codePostal) {
		this.codePostal = codePostal;
	}

	/**
	 * Setter pour l'attribut courriel.
	 * @param courriel un String.
	 */
	public void setCourriel(String courriel) {
		this.courriel = courriel;
	}

	/**
	 * Setter pour l'attribut province.
	 * @param province un String ayant au plus 2 caracteres.
	 */
	public void setProvince(String province) {
		this.province = province;
	}

	/**
	 * Setter pour l'attribut ville.
	 * @param ville un String ayant au plus 14 caracteres.
	 */
	public void setVille(String ville) {
		this.ville = ville;
	}

	/**
	 * Cette methode retourne l'affichage texte approprie montrer les donnees du compte.
	 * @return Retourne un String.
	 */
	public String affichage() {
		return "Nom: " + nom + "\n" +
				"Numéro unique: " + NumberFormat.versNeufChar(ID) + "\n" +
				"Courriel: " + courriel + "\n" +
				"Adresse: " + adresse + "\n" +
				"Ville: " + ville + "\n" +
				"Province: " + province + "\n" +
				"Code postal: " + codePostal + "\n";
	}
}
