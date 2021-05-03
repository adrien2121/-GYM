package ift2255;

import ift2255.formateur.CalendarFormat;
import ift2255.formateur.NumberFormat;

import java.io.Serializable;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Cette classe sert comme systeme qui stocke toutes les donnees necessaires au fonctionnement du #GYM. Elle stocke
 * les comptes de membres et de professionnels, ainsi que le Repertoire des Services. Chacune de ces donnees sont mises
 * dans une liste.
 * <br><br>
 * Ces donnees et leur liste sont manipulables et des insertions/supressions sont possibles. Plusieurs seances peuvent
 * etre ajoutees en meme temps pour un meme professionnel et pour un meme service.
 *
 * @see Serializable
 * @see Compte
 * @see Professionnel
 * @see Membre
 * @see Service
 * @see Seance
 */
public class CentreDonnee implements Serializable {

	private LinkedList<Professionnel> listePros;
	private LinkedList<Membre> listeMembres;
	private LinkedList<Service> repertoire;

	public CentreDonnee() {
		listePros = new LinkedList<>();
		listeMembres = new LinkedList<>();
		repertoire = new LinkedList<>();
	}

	/**
	 * Cette methode retourne un Service a partir dun code de Service fournit.
	 * Sil ny a pas eu de Service trouve, on retourne null
	 * @param codeServce un int qui est le code de Service
	 * @return Retourne une instance de Service
	 */
	public Service getService(int codeServce) {
		for (Service service : repertoire) {
			if (service.getCodeService() == codeServce) {
				return service;
			}
		}

		return null;
	}

	/**
	 * Cette methode retourne une Seance a partir du code de Service et du code
	 * de Seance. Si la Seance nest pas trouve, on retourne null
	 * @param codeService un int qui est le code de Service
	 * @param codeSeance un int qui est le code de Seance
	 * @return Retourne une instance de Seance
	 */
	public Seance getSeance(int codeService, int codeSeance) {

		for (Service service : repertoire) {
			if (service.getCodeService() == codeService) {
				return service.getSeance(codeSeance);
			}
		}

		return null;
	}

	/**
	 * Cette methode retourne un Professionnel a partir du code du
	 * Professionnel. Si le professionnel nest pas trouve, on retourne null.
	 * @param idPro un int qui est le code du pro
	 * @return Retourne une instance de Professionnel
	 */
	public Professionnel getPro(int idPro) {
		for (Professionnel pro : listePros) {
			if (pro.getID() == idPro) return pro;
		}

		return null;
	}

	/**
	 * Cette methode cherche le compte membre associe au numero unique envoye en parametre. Elle envoit null aucun
	 * compte n'est trouve.
	 * @param idMembre un int
	 * @return Retourne un instance Membre
	 */
	public Membre getMembre(int idMembre) {
		for (Membre membre : listeMembres) {
			if (membre.getID() == idMembre) return membre;
		}

		return null;
	}

	/**
	 * Cette methode cherche le compte membre associe au courriel envoye en parametre. Elle envoit null aucun compte
	 * n'est trouve.
	 * @param courriel un String
	 * @return Retourne une instance Membre
	 */
	public Membre getMembreLogin(String courriel) {

		for (Membre membre : listeMembres) {
			if (membre.getCourriel().equals(courriel)) { // Trouvé!
				return membre;
			}
		}

		return null; // Pas trouvé...
	}

	/**
	 * Cette methode cherche le compte membre associe au courriel envoye en parametre. Elle envoit null aucun compte
	 * n'est trouve.
	 * @param courriel un String
	 * @return Retourne une instance Membre
	 */
	public Professionnel getProLogin(String courriel) {

		for (Professionnel pro : listePros) {
			if (pro.getCourriel().equals(courriel)) { // Trouvé!
				return pro;
			}
		}

		return null; // Pas trouvé...
	}

	/**
	 * Cette methode retourne le nombre de compte (pro et membres) inscrits
	 * dans le centreDonnee
	 * @return Retourne nombre de comptes dans le centre de donnees
	 */
	public int getNbCompte() {
		return listeMembres.size() + listePros.size();
	}

	/**
	 * Cette methode retourne le nombre de Services offerts dans
	 * le centreDonnee
	 * @return Retournen ombre de Services dans le centre de donnees
	 */
	public int getNbServices() {
		return repertoire.size();
	}

	/**
	 * Cette methode ajoute un compte professionnel dans la liste
	 * de professionnels dans le centreDonnee
	 * @param pro un compte professionnel
	 */
	public void addPro(Professionnel pro) {
		listePros.add(pro);
	}

	/**
	 * Cette methode ajoute un compte membre dans la liste
	 * de membres dans le centreDonnee
	 * @param membre un compte membre
	 */
	public void addMembre(Membre membre) {
		listeMembres.add(membre);
	}

	/**
	 * Cette methode enleve un compte membre dans la liste des comptes
	 * professionnels dans le centreDonnee
	 * @param idMembre le code du membre
	 */
	public void removeMembre(int idMembre) {

		Iterator<Membre> iterator = listeMembres.iterator();

		Membre membre;
		while (iterator.hasNext()) {

			membre = iterator.next();
			if (membre.getID() == idMembre) {
				iterator.remove();
				return;
			}

		}
	}

	/**
	 * Cette methode enleve un compte professionnel dans la liste des comptes
	 * professionnels dans le centreDonnee
	 * @param idPro
	 */
	public void removePro(int idPro) {

		Iterator<Professionnel> iterator = listePros.iterator();

		Professionnel pro;
		while (iterator.hasNext()) {

			pro = iterator.next();
			if (pro.getID() == idPro) {
				iterator.remove();
				return;
			}

		}
	}

	/**
	 * Cette methode enleve une Seance du repertoire de Service
	 * dans le centreDonnee.
	 * @param codeService un int qui est le code de Service
	 * @param codeSeance un int qui est le code de Seance
	 */
	public void removeSeance(int codeService, int codeSeance) {

		Iterator<Service> itService = repertoire.iterator();

		Service service;
		while (itService.hasNext()) {

			service = itService.next();
			if (service.getCodeService() == codeService) {

				Iterator<Seance> itSeance = service.getListeSeances().iterator();

				Seance seance;
				while (itSeance.hasNext()) {
					seance = itSeance.next();
					if (seance.getCodeSeance() == codeSeance) {
						itSeance.remove();
						return;
					}
				}

			}

		}
	}

	/**
	 * Cette methode est un getter pour la liste des professionnels
	 * @return Retourne un LinkedList qui est la liste des professionnels
	 */
	public LinkedList<Professionnel> getListePros() {
		return listePros;
	}

	/**
	 * Cette methode est un setter pour la liste des professionnels
	 * @param listePros un LinkedList qui est la liste des professionnels
	 */
	public void setListePros(LinkedList<Professionnel> listePros) {
		this.listePros = listePros;
	}

	/**
	 * Cette methode est un getter pour la liste des membres
	 * @return Retourne un LinkedList qui est la liste des membres
	 */
	public LinkedList<Membre> getListeMembres() {
	    System.out.println("yo");
	    return listeMembres;
	}

	/**
	 * Cette methode est un setter pour la liste des membres
	 * @param listeMembres un LinkedList qui est la liste des membres
	 */
	public void setListeMembres(LinkedList<Membre> listeMembres) {
		this.listeMembres = listeMembres;
	}

	/**
	 * Cette methode est un getter pour la liste des Services dans
	 * le repertoire de service
	 * @return Retourne un LinkedList qui est la liste des services
	 */
	public LinkedList<Service> getRepertoire() {
		return repertoire;
	}

	/**
	 * Cette methode est un setter pour la liste des Services dans le
	 * repertoire des services
	 * @param repertoire un LinkedList qui est la liste des Services
	 */
	public void setRepertoire(LinkedList<Service> repertoire) {
		this.repertoire = repertoire;
	}

	/**
	 * Cette methode retourne un boolean pour dire si un compte membre
	 * est valide ou non
	 * @param idMembre un int qui est le code du membre
	 * @return Retourne un boolean disant si le membre est valide ou non
	 */
	public boolean validerMembre(int idMembre) {

		for (Membre membre : listeMembres) {
			if (membre.getID() == idMembre) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Cette methode retourne un boolean pour dire si un compte
	 * professionnel est valide ou non
	 * @param idPro un int qui est le code du professionnel
	 * @return Retourne un boolean disant si le professionnel est valide ou non
	 */
	public boolean validerPro(int idPro) {

		for (Professionnel pro : listePros) {
			if (pro.getID() == idPro) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Cette methode retourne un boolean pour dire si une seance
	 * existe ou non
	 * @param codeService un int qui est le code de Service
	 * @param codeSeance un int qui est le code de Seance
	 * @return Retourne un boolean disant si la seance existe ou non
	 */
	public boolean seanceExiste(int codeService, int codeSeance) {

		for (Service service : repertoire) {

			if (service.getCodeService() == codeService) {

				for (Seance seance : service.getListeSeances()) {
					if (seance.getCodeSeance() == codeSeance)
						return true;

				}

				return false;
			}

		}

		return false;
	}

	/**
	 * Cette methode retourne un boolean pour dire si un service
	 * existe ou non
	 * @param codeService un int qui est le code de Service
	 * @return Retourne un un boolean disant si le service existe ou non
	 */
	public boolean serviceExiste(int codeService) {
		for (Service service : repertoire) {
			if (service.getCodeService() == codeService) {
				return true;
			}
		}

		return false;
	}

	/**
	 * Cette methode permet de creer plusieurs seances fournies par un professionnel pour une periode donnee.
	 * Le nombre de seances a creer depend de debutService et de finService (= nb de semaines), et de recurrenceHeb
	 * (= nombre de fois le service est fourni durant un semaine).
	 *
	 * Il va generer un code seance selon le numero unique du professionnel, le code service et le nombre de seances
	 * que le professionnel a fourni pour le service en particulier.
	 *
	 * Ces seances sont ajoutees à la liste de seance du service approprie.
	 *
	 * @param tempsActuel l'heure et la date actuelle de la creation de la seance en JJ-MM-AAAA HH:mm:ss
	 * @param dateDebutService la date de debut du service en JJ-MM-AAAA
	 * @param dateFinService la date de fin du service en JJ-MM-AAAA
	 * @param heureService l'heure du service
	 * @param recurrenceService le(s) jour(s) de la semaine que le service est founi (le nombre de fois que le service est
	 *                      fourni par semaine)
	 * @param capacMax la capacite maximale d'inscriptions
	 * @param commentaires un commentaire ou une remarque particulier sur les seances a creer.
	 * @param idPro le numero unique du professionnel
	 * @param codeService le code de service pour lequel la methode cree les seances.
	 */
	public void creerSeances(Calendar tempsActuel, int idPro, int codeService,
							 Calendar dateDebutService, Calendar dateFinService, Calendar heureService,
							 int[] recurrenceService,
							 double frais, boolean fraisParInscription,
							 int capacMax, String commentaires) {

		Service service = this.getService(codeService);

		Seance seance;
		int nbSeanceParPro, codeSeance;

		// La date de début et l'heure de la séance.
		Calendar dateHeureSeance = new GregorianCalendar();
		dateHeureSeance.set(dateDebutService.get(Calendar.YEAR),
				dateDebutService.get(Calendar.MONTH), dateDebutService.get(Calendar.DAY_OF_MONTH),
				heureService.get(Calendar.HOUR_OF_DAY), heureService.get(Calendar.MINUTE));

		// Index du tableau de recurrence hebdomadaire.
		int j = 0;
		for (int i = 0; i < recurrenceService.length; i++) { // Chercher le jour de la date de début.

			if (recurrenceService[i] == dateDebutService.get(Calendar.DAY_OF_WEEK)) {
				j = i;
			}

		}

		boolean first = true;
		while (true) {

			nbSeanceParPro = service.nbSeanceParPro(idPro); // Le nombre de séance fourni par le professionnel pour
			codeSeance = codeSeanceGen(codeService, nbSeanceParPro, idPro); // Générer un code de séance.

			if (first) { // Premiere seance
				first = false;
			}

			else if (j != 0) { // Pas le premier jour de la recurrence

				dateHeureSeance.add(Calendar.DAY_OF_MONTH,
						recurrenceService[j] - recurrenceService[j - 1]);

			}

			else { // Le premier jour de la recurrence
				dateHeureSeance.add(Calendar.DAY_OF_MONTH,
						7 - (recurrenceService[recurrenceService.length - 1] - recurrenceService[0]));
			}

			if (CalendarFormat.daysBetween(dateHeureSeance, dateFinService) < 0) {
				break;
			}

            // Créer la séance.
            seance = new Seance(tempsActuel, idPro, codeService, codeSeance, (Calendar) dateHeureSeance.clone(),
                    capacMax, frais, fraisParInscription, commentaires);

            // Ajouter à la liste de séances du service.
            service.addSeance(seance);

            if (j == recurrenceService.length - 1) j = 0;
            else j++;
        }

	}

	/**
	 * Cette methode cree un service a partir d'un nom de service et le nombre de services dans le Repertoire.
	 * @param nomService un String
	 * @return Retourne un instance de Service
	 */
	public Service creerService(String nomService) {
		int codeService = 9999999 - getNbServices();
		Service service = new Service(nomService, codeService);
		repertoire.add(service);

		return service;
	}

	/**
	 * Cette methode cree un compte. Le type depend du parametre creerMembre qui est un booleen. Si le boolen est true,
	 * cela veut dire qu'on cree un compte de membre. Sinon, c'est un compte professionnel.
	 * @param creerMembre booleen
	 * @param nom String
	 * @param adresse String
	 * @param ville String
	 * @param province String
	 * @param codePostal String
	 * @param courriel String
	 * @return Retourne l'instance Compte cree.
	 */
	public Compte creerCompte(boolean creerMembre, String nom, String adresse,
							  String ville, String province, String codePostal, String courriel) {

		int id = getNbCompte() + 1; // Assigner un numero unique.

		Compte compte;
		if (creerMembre) {
		    compte = new Membre(nom, id, adresse, ville, province, codePostal, courriel);
		    listeMembres.add((Membre) compte);
        }
		else {
            compte = new Professionnel(nom, id, adresse, ville, province, codePostal, courriel);
            listePros.add((Professionnel) compte);
        }

		return compte;
	}

	/**
	 * Cette methode permet de generer un code de seance à 9 chiffres selon les deux derniers chiffres du numero unique
	 * du professionnel, les trois premiers chiffres du code de service et les trois chiffres du nombre de seances
	 * fournies par le professionnel pour le service particulier.
	 *
	 * @param idPro le numero unique du professionnel, utilise pour ses deux derniers chiffres.
	 * @param codeService le code de service pour les trois premiers chiffres.
	 * @param nbSeanceDeService le nombre de seances fournises par le professionnel pour le service (3 chiffres).
	 * @return Retourne le code de seance approprie à 9 chiffres.
	 */
	public int codeSeanceGen(int codeService, int nbSeanceDeService, int idPro) {

		String codeServiceStr = NumberFormat.versSeptChar(codeService),
				nbSeancesStr = NumberFormat.versDeuxChar(nbSeanceDeService),
				idProStr = NumberFormat.versNeufChar(idPro);

		String codeSeance = codeServiceStr.substring(0, 3) + nbSeancesStr + idProStr.substring(7, 9);

		return Integer.parseInt(codeSeance);
	}

	/**
	 * Cette methode affiche le repertoire de service au complet
	 * La metode indique le nombre de services offerts, et la liste de services offerts.
	 * Ensuite, la methode affiche le nom de service, le code du service et
	 * le nombre de séances pour chaque services dans le repertoire
	 * @return Retourne un String qui affiche le repertoire de service
	 * au complet
	 */
	public String affichageRepertoire() {
		String result = "Nombre de services offerts: " + repertoire.size() + "\n" +
						"Liste de services offerts: " + "\n\n";

		for (Service service : repertoire) {
			result += "  Nom du service: " + service.getNomService() + "\n" +
					  "  Code du service: " + service.getCodeService() + "\n" +
					  "  Nombre de séances pour ce service: " + service.getNbSeances() + "\n\n";
		}

		return result;
	}

}
