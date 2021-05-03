package ift2255;

import ift2255.formateur.CalendarFormat;
import ift2255.formateur.NumberFormat;

import java.io.Serializable;
import java.util.*;

/**
 * Cette classe stocke les donnees necessaires pour representer une seance. Elle permet l'inscription et la confirmation
 * d'inscription. La classe implemente Serializable pour permettre de sauvegarder l'etat des instances Seance.
 * @see Service
 * @see CalendarFormat
 * @see ift2255.formateur.NumberFormat
 * @see Calendar
 * @see Serializable
 */
public class Seance implements Serializable {

	private Calendar dateCreation; // Date que la séance a été créée.

	private int proID; // Numéro unique du professionnel.

	// Code d'identification utile pour cette séance.
    private int codeService; // 7 chiffres.
	private int codeSeance; // 7 chiffres.

	private Calendar dateHeureSeance; // Date JJ-MM-YYYY et l'heure HH:MM:SS

	private double frais; // $XXX.XX  =  3 chiffres avant la décimale et 2 après. Max = 999.99
	private boolean fraisParInscription;

	// Les inscrits et propriétés sur la liste d'inscrits.
    private int maxCapacite; // Capacité maximale.
    private int nowCapacite; // Capacité actuelle.
    private List<Integer> inscrits; // Les ID des membres inscrits à la séance.
	private boolean[] confirmes; // Un booléen pour chaque case de membre. Chaque membre est associé un index dans la
								 // liste.

	private String commentaires;

	/**
	 * Constructeur de la classe.
	 * @param proID - le numero unique a 9 chiffres du professionnel qui fournit la seance.
	 * @param codeSeance - le numéro unique a 7 chiffres de la seance.
	 * @param codeService - le numéro unique  7 chiffres du service associe à la seance.
	 * @param dateHeure - la date et heure de la séance.
	 * @param maxCapacite - la capacite maximale d'inscrits que peut accommoder la seance.
	 * @param frais - les frais à payer au professionnel pour la seance.
	 */
    public Seance(Calendar dateCreation,
				  int proID, int codeService, int codeSeance , Calendar dateHeure, int maxCapacite,
				  double frais, boolean fraisParInscription,
				  String commentaires) {
    	this.dateCreation = dateCreation;

        this.proID = proID;
        this.codeSeance = codeSeance;
        this.codeService = codeService;

        this.dateHeureSeance = dateHeure;
        this.frais = frais;
        this.fraisParInscription = fraisParInscription;

        this.nowCapacite = 0;
		this.maxCapacite = maxCapacite;
        this.inscrits = new ArrayList<>(maxCapacite);
        this.confirmes = new boolean[maxCapacite];

        this.commentaires= commentaires;
    }

    /**
     * cette methode affiche la liste de membre inscrit
     * pour une seance 
     * @see CentreDonnee
     * @see Membre
     * @param c
     */
    public void afficherMembre(CentreDonnee c) {

    	for(int i=0; i<inscrits.size();i++) {
    		Membre mm=c.getMembre(inscrits.get(i));
    		System.out.println(mm.affichage());
    	}

    }

	/**
	 * Getter pour l'attribut codeSeance.
	 * @return Retourne un int contenu dans l'attribut codeSeance.
	 */
	public int getCodeSeance() {
		return codeSeance;
	}

	/**
	 * Getter pour l'attribut codeService.
	 * @return Retourne un int contenu dans l'attribut codeService.
	 */
	public int getCodeService() {
		return codeService;
	}

	/**
	 * Getter pour l'attribut ID
	 * @return Retourne un int contenu dans l'attribut ID.
	 */
	public int getProID() {
		return proID;
	}

	/**
	 * Getter pour dateHeureSeance.
	 * @return Retourne une instance Calendar contenu dans l'attribut dateHeureSeance.
	 */
	public Calendar getDateHeureSeance() {
		return dateHeureSeance;
	}

	/**
	 * Getter pour l'attribut fraisParInscription.
	 * @return Retourne un booleen qui indique si les frais sont par inscription.
	 */
	public boolean isFraisParInscription() {
		return fraisParInscription;
	}

	/**
	 * Getter pour l'attribut frais.
	 * @return Retourne un double ayant jusqu'a 999.99 inclusivement.
	 */
	public double getFrais() {
		return frais;
	}

	/**
	 * Getter pour l'attribut nowCapacite.
	 * @return Retourne un int contenu dans l'attribut nowCapacite.
	 */
	public int getNowCapacite() {
		return nowCapacite;
	}

	/**
	 * Getter pour le nombre d'inscriptions disponibles.
	 * @return un int.
	 */
	public int getCapaciteDispo() {
		return maxCapacite - nowCapacite;
	}

	/**
	 * Methode setter pour JJ-MM-AAAA de l'attribut dateHeureSeance.
	 * @param dateSeance une instance Calendar d'ou on extrait JJ-MM-AAAA.
	 */
	public void setDateSeance(Calendar dateSeance) {
    	this.dateHeureSeance.set(Calendar.YEAR, dateSeance.get(Calendar.YEAR));
    	this.dateHeureSeance.set(Calendar.MONTH, dateSeance.get(Calendar.MONTH));
    	this.dateHeureSeance.set(Calendar.DAY_OF_MONTH, dateSeance.get(Calendar.DAY_OF_MONTH));
	}

	/**
	 * Methode setter pour l'heure et les minutes de l'attribut dateHeureSeance.
	 * @param heureSeance une instance Calendar d'ou on extrait l'heure et les minutes.
	 */
	public void setHeureSeance(Calendar heureSeance) {
    	this.dateHeureSeance.set(Calendar.HOUR_OF_DAY, heureSeance.get(Calendar.HOUR_OF_DAY));
    	this.dateHeureSeance.set(Calendar.MINUTE, heureSeance.get(Calendar.MINUTE));
	}

	/**
	 * Setter pour l'attribut maxCapacite.
	 * @param maxCapacite un int
	 */
	public void setMaxCapacite(int maxCapacite) {
		this.maxCapacite = maxCapacite;
	}

	/**
	 * Setter pour changer fraisParInscription a true.
	 */
	public void setFraisParInscription() {
		fraisParInscription = true;
	}

	/**
	 * Setter pour changer fraisParInscription a false.
	 */
	public void setFraisParService() {
		fraisParInscription = false;
	}

	/**
	 * Setter pour l'attribut frais.
	 * @param frais un double.
	 */
	public void setFrais(double frais) {
		this.frais = frais;
	}

	/**
	 * Cette methode ajoute le numero unique d'un membre a la liste des inscrits et "initialise" le booleen
	 * @param idMembre un int
	 * @return Retourne un booleen
	 */
	public boolean inscrire(int idMembre) {
    	if (nowCapacite == maxCapacite) { // Capacité maximale est atteinte. Annuler l'inscription.
    		return false;
		} else { // L'inscription est acceptée.

    		if (isInscrit(idMembre)) return false;

    		inscrits.add(nowCapacite, idMembre);
    		confirmes[nowCapacite] = false;

    		nowCapacite++;

    		return true;
		}
	}

	/**
	 * Cette methode verifie si le membre est deja inscrit.
	 * @param idMembre un int
	 * @return Retourne un boolen.
	 */
	public boolean isInscrit(int idMembre) {
		return inscrits.contains(idMembre);
	}

	/**
	 * Cette methode permet de savoir si un membre est inscrit a la séance (appartient dans la liste des inscrits) et
	 * n'a pas encore confirme sa présnce a la seance. Le dernier cas permet d'eviter la double ou plusieurs
	 * confirmations a une seance.
	 * @param idMembre le numero unique du membre.
	 * @return Retourne True si le membre est inscrits et on confirme sa presence. False si le membre est soit deja
	 * confirme ou soit il n'est pas inscrit a la seance.
	 */
	public boolean confirmer(int idMembre) {

    	int idx = inscrits.indexOf(idMembre);

    	if (idx >= 0 && !confirmes[idx]) { // Le membre est inscrit et n'a pas encore confirmé sa présence.
    		confirmes[idx] = true;
    		return true;
		} else { // Membre déjà confirmé ou non inscrit à la séance.
    		return false;
		}

	}

	/**
	 * Cette methode genere le texte pour affichage les informations generales sur la seance qui sont accessibles aux
	 * membres.
	 * @return Retourne un texte String sur les informations generales.
	 */
	public String affichage() {
    	return "Numéro unique du professionnel: " + NumberFormat.versNeufChar(proID) + "\n" +
				"Date de la séance: " + CalendarFormat.dateToStr(dateHeureSeance) + "\n" +
				"Heure de la séance: " + CalendarFormat.heureToStr(dateHeureSeance) + "\n" +
				"Code service: " + codeService + "\n" +
				"Code séance: " + codeSeance + "\n" +
				"Nombre d'inscriptions disponible: " + (maxCapacite - nowCapacite) + "\n" +
				"Frais de service: " + frais + " " +
					(fraisParInscription ? "par inscription" : "") + "\n" +
				"Commentaires: " + (commentaires.isEmpty() ? "Aucun." : commentaires) + "\n";
	}
}
