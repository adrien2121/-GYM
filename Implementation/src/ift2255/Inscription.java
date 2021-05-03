package ift2255;

import ift2255.formateur.CalendarFormat;
import ift2255.formateur.NumberFormat;

import java.io.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Cette classe enregistre des donnees reliees a l'inscription sur un fichier. A chaque semaine, les fichiers sont
 * enregistres dans differents directoires. Le nomSeance des fichiers comporte le code de la seance et la date actuelle.
 */
public class Inscription implements Enregistrement {

    // System.getProperties("user.dir") retourne \Implémentation\
    private final static String directoryPath =
            System.getProperty("user.dir") + "\\resources\\Enregistrement\\Inscription\\";

    /**
     * Cette methode enregistre les donnees necessaires lors l'inscription d'un membre a une seance.
     * On se sert du constructeur FileWriter(pathFile:String, append:boolean) ou append veut dire qu'on veut ajouter du
     * texte au texte original dans le fichier.
     *
     * On append les donnees une a la fois de maniere hardcoded.
     *
     * L'hierarchie des fichiers est: Enregistrement -> Inscription
     *                                               -> lundi de la semaine -> codeSeance_dateSeance
     * ou les dates sont ecrites dans ce format (JJ-MM-YYYY).
     *
     * @param membre Pour le numero unique
     * @param seance Pour les codes service et seance, le numero unique du professionnel, et la date de seance.
     * @param tempsActuel Pour la date et l'heure actuel de l'enregistrement.
     * @see Confirmation
     */
    public static void enregistrer(Membre membre, Seance seance, Calendar tempsActuel) {

        Calendar temp = (Calendar) tempsActuel.clone(); // On ne veut pas modifier la valeur par référence.
        temp.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY); // On veut avoir la date du lundi de la semaine.

        // --- CHEMIN AU DOSSIER DE LA SEMAINE ---
        String path = directoryPath + CalendarFormat.dateToStr(temp) + "\\";
        File dossier = new File(path);
        if (!dossier.exists()) { // Si le dossier n'existe pas, il faut le créer. Sinon, BufferedWriter lance un excep.
            dossier.mkdirs();
        }

        // --- CHEMIN AU FICHIER DE LA SEANCE POUR CE JOUR ---
        path += seance.getCodeSeance() + "_" + CalendarFormat.dateToStr(tempsActuel) +
                ".txt";

        // --- ENREGISTRER LES DONNÉES ---
        BufferedWriter writer;
        File fichierSeance = new File(path);
        if (fichierSeance.exists()) {
            fichierSeance.setWritable(true);
        }

        try {
            writer = new BufferedWriter(new FileWriter(fichierSeance, true)); // Initialise. True pour append.

            writer.write(CalendarFormat.dateHeureToStr(tempsActuel) + "\n");
            writer.write(CalendarFormat.dateToStr(seance.getDateHeureSeance()) + "\n");
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
