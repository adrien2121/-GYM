package ift2255;

import ift2255.formateur.CalendarFormat;

import java.io.Serializable;
import java.util.*;

/**
 * Cette classe stocke les donnees necessaires pour representer un service et les operations de base necessaires pour
 * manipuler l'instance Service et les instances Seances si necessaire.
 *
 * @see Seance
 * @see CalendarFormat
 * @see ift2255.formateur.NumberFormat
 * @see Calendar
 * @see Serializable
 */
public class Service implements Serializable {

    private String nomService;
    private int codeService;
    private List<Seance> listeSeances;
    private TreeMap<Integer, Integer> compteurSeance; // < proID, nbSeance > On garde un compteur pour assigner un code
                                                     // de séance unique.

    public Service(String nomService, int codeService) {
        this.nomService = nomService;
        this.codeService = codeService;
        this.listeSeances = new ArrayList<>();
        compteurSeance = new TreeMap<>();
    }

    /**
     * Getter pour une instance Seance de la liste de seances listeSeances.
     * @param codeSeance le code de seance par lequel la seance est identifiee.
     * @return Retourne une instance Seance.
     */
    public Seance getSeance(int codeSeance) {
        for (Seance seance : listeSeances) {
            if (seance.getCodeSeance() == codeSeance) {
                return seance;
            }
        }
        return null;
    }

    /**
     * Getter pour l'attribut listeSeances.
     * @return Retourne un List<Seance>.
     */
    public List<Seance> getListeSeances() {
        return listeSeances;
    }

    /**
     * Getter pour l'attribut codeService.
     * @return Retourne le code de service en int.
     */
    public int getCodeService() {
        return codeService;
    }

    /**
     * Getter pour l'attribut nomService.
     * @return Retourne le nom du service en String.
     */
    public String getNomService() {
        return nomService;
    }

    /**
     * Getter pour le nombre de seances pour le service.
     * @return Retourne la taille de la liste de seances en int.
     */
    public int getNbSeances() {
        return listeSeances.size();
    }

    /**
     * Cette methode ajoute une seance a la liste des seances du service et incremente le nombre de service que
     * fournit un professionnel. Si le nombre incremente atteint 1000, il faut recommencer a 0.
     * @param seance une instance de Seance qui est ajoute à la liste de seances
     */
    public void addSeance(Seance seance) {

        listeSeances.add(seance);

        Integer nbSeance = compteurSeance.get(seance.getProID());

        if (nbSeance == null) {
            compteurSeance.put(seance.getProID(), 1);
            return;
        }

        if (nbSeance + 1 > 99) { // Garder à trois chiffres.
            nbSeance = 0;
        }

        compteurSeance.put(seance.getProID(), nbSeance + 1);
    }

    /**
     * Cette méthode recupere le nombre de seances fournis par le professionnel de l'arbre binaire TreeMap.
     * @param idPro le numero unique du professionnel pour aller chercher la valeur de retour.
     * @return un int pour le nombre de seances qu'a fourni le professionnel
     */
    public int nbSeanceParPro(int idPro) {

        Integer nbSeance = compteurSeance.get(idPro);

        return nbSeance == null ? 0 : nbSeance;
    }

    /**
     * Cette methode retourne le String qui affiche les donnees de la seance.
     * @return Retourne un String qui contient les donnees a afficher.
     */
    public String affichage() {
        String result = "Nom du service: " + nomService + "\n" +
                        "Code du service: " + codeService + "\n";

        if (listeSeances.size() == 0) {
            result += "Aucune séance offerte pour ce service.";

            return result;
        }

        result += "Liste de séances offertes pour ce service: " + "\n\n";

        for (Seance seance : listeSeances) {
            result += "  Code de la séance: " + seance.getCodeSeance() + "\n" +
                      "  Date et heure de la séance: " + CalendarFormat.dateToStr(seance.getDateHeureSeance()) + " " +
                                CalendarFormat.heureSansSecToStr(seance.getDateHeureSeance()) + "\n" +
                      "  Nombre d'inscriptions restantes: " + seance.getCapaciteDispo() + "\n\n";
        }

        return result;
    }
}
