package ift2255;

import ift2255.formateur.CalendarFormat;
import ift2255.formateur.NumberFormat;

import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Cette classe enregistre des données reliees a l'inscription sur un fichier. A chaque semaine, les fichiers sont
 * enregistres dans differents directoires. Le nomSeance des fichiers comporte le code de la seance et la date de la seance.
 */
public class Confirmation implements Enregistrement {

    private final static String directoryPath =
            System.getProperty("user.dir") + "\\resources\\Enregistrement\\Confirmation\\";

    /**
     * Cette methode enregistre les donnees necessaires lors de la confirmation de la presence d'un membre a une seance.
     * On se sert du constructeur FileWriter(pathFile:String, append:boolean) ou append veut dire qu'on veut ajouter du
     * texte au texte original dans le fichier.
     *
     * On append les donnees une a la fois de maniere hardcoded.
     *
     * L'hierarchie des fichiers est: Enregistrement -> Confirmation
     *                                               -> lundi de la semaine -> codeSeance_dateSeance
     * ou les dates sont ecrites dans ce format (JJ-MM-YYYY).
     *
     * @param membre Pour le numero unique
     * @param seance Pour les codes service et seance, le numero unique du professionnel
     * @param tempsActuel Pour la date et l'heure actuel de l'enregistrement.
     * @see Inscription
     */
    public static void enregistrer(Membre membre, Seance seance, Calendar tempsActuel) {

        Calendar temp = (Calendar) tempsActuel.clone(); // Éviter de changer la valeur par référence.
        temp.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // --- CHEMIN AU DOSSIER DE LA SEMAINE ---
        String dossierSemaine = directoryPath + CalendarFormat.dateToStr(temp) + "\\";
        File dossier = new File(dossierSemaine);
        if (!dossier.exists()) { // Créer le dossier s'il n'existe pas encore.
            dossier.mkdirs();
        }

        // --- CHEMIN AU FICHIER DE LA SEANCE POUR CE JOUR ---
        String path = dossierSemaine +
                seance.getCodeSeance() + "_" + CalendarFormat.dateToStr(tempsActuel) +
                ".txt";

        // --- ENREGISTRER LES DONNÉES ---
        BufferedWriter writer;
        File fichierSeance = new File(path);
        if (fichierSeance.exists()) {
            fichierSeance.setWritable(true);
        }

        try {
            writer = new BufferedWriter(new FileWriter(fichierSeance, true)); // True pour append

            writer.write(CalendarFormat.dateHeureToStr(tempsActuel) + "\n");
            writer.write(NumberFormat.versNeufChar(seance.getProID()) + "\n");
            writer.write(NumberFormat.versNeufChar(membre.getID()) + "\n");
            writer.write(NumberFormat.versSeptChar(seance.getCodeService()) + "\n");
            writer.write(NumberFormat.versSeptChar(seance.getCodeSeance()) + "\n");
            writer.write("Commentaires:\n");
            writer.newLine();

            fichierSeance.setReadOnly();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
