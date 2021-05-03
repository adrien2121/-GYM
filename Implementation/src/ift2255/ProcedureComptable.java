package ift2255;

import ift2255.formateur.CalendarFormat;
import ift2255.formateur.NumberFormat;

import java.io.*;
import java.util.*;

/**
 * Cette classe permet d'executer le procedure comptable et le rapport gerant. Elle peut aussi retourner un texte String
 * des rapports generes dans le passe.
 *
 * <br><br>
 *
 * <h3>FICHIER Confirmation</h3>
 * <pre>
 *     Chaque bloc de confirmation consiste de 6 lignes de donnees et une ligne vide.
 *
 *     (Temps actuel)                   jj-mm-aaaa hh:mm:ss
 *     (Numero du professionn)          xxxxxxxxx
 *     (Numero du membre)               xxxxxxxxx
 *     (Code du service)                xxxxxxx
 *     (Code de la seance)              xxxxxxx
 *     (Commentaires)                   100 char max.
 *
 * </pre>
 * <br>
 *
 * <h3>FICHIER makeTEF</h3>
 *
 * <pre>
 *      Chaque professionnel ayant fourni un service la semaine aura un fichier makeTEF qui leur est approprie.
 *
 *          Transfert electronique de fond
 *
 *          Nom:xxx
 *          Numero unique:xxxxxxxxx
 *          Frais a transferer pour les services: xxx.xx
 *
 *</pre>
 *<br>
 *
 * <h3>FICHIER Rapport/Gerant</h3>
 *
 * <pre>
 *      Chaque semaine, un rapport synthese de gerant est genere. Voici le header.
 *
 *          Rapport de synthese du gerant du jj-mm-aaaa
 *
 *      Chaque bloc dans le rapport de gerant contient des blocs. Chaque professionnel a payer va generer un bloc dans le
 *      rapport.
 *
 *          Liste de comptables payables
 *
 *                  Nom:
 *                  Numero unique:
 *                  Frais a transferer pour les services:
 *                  Nombre total de services fournis:
 *
 *      Puis, e la fin du rapport de gerant, on voit un resume des donnees.
 *
 *          Resume
 *
 *                  Nombre de professionnel ayant fourni un ou des service(s) cette semaine:
 *                  Nombre de services fournis cette semaine:
 *                  Total des frais pour l'ensemble de la semaine:
 *</pre>
 *<br>
 *
 *  <h3>FICHIER Rapport/Professionnel</h3>
 *  <pre>
 *
 *      Chaque semaine, un rapport est genere pour le professionnel. Si le fichier rapport de la semaine n'a pas encore
 *      ete initialise/cree, il faut rédiger le bloc header.
 *
 *          Rapport professionnel du <semaine jj-mm-aaaa>
 *
 *          Nom du professionnel:
 *          Numero unique:
 *          Adresse:
 *          Ville:
 *          Province:
 *          Code postal:
 *
 *
 *          Liste de service(s) fourni(s) par le professionnel
 *
 *      Pour le reste du rapport, il faut lister les services fournis par le professionnel. Pour chaque service, il faut
 *      rediger le bloc ci-dessous.
 *
 *                  Date de service:
 *                  Date d'enregistrement de confirmation:
 *                  Nom du membre:
 *                  Numero du membre:
 *                  Code du service:
 *                  Code de la séance:
 *                  Montant a payer (jusqu'a 999.99$), si facture par inscription, sinon un seul montant est affiche.
 *
 *  </pre>
 *<br>
 *
 *  <h3>FICHIER Rapport/Membre</h3>
 *  <pre>
 *      Chaque semaine, un rapport est genere pour le membre. Si le fichier rapport de la semaine n'a pas encore ete
 *      initialise/cree, il faut rediger le bloc header.
 *
 *          Rapport membre du <semaine jj-mm-aaaa>
 *
 *          Nom du membre:
 *          Numero unique:
 *          Adresse:
 *          Ville:
 *          Province:
 *          Code postal:
 *
 *          Liste de services fourni au membre
 *
 *      Pour le reste du rapport, il faut lister les services qui leur ont ete fournis. Pour chaque service, il faut
 *      rediger le bloc ci-dessous.
 *
 *             Date de seance:
 *             Nom du professionnel:
 *             Nom du service:
 *
 *  </pre>
 */
public class ProcedureComptable {

    private static CentreDonnee systemCentral; // Référence

    private static TreeMap<Integer, Double> frais; // < idPro, frais >, les frais total pour chaque professionnel
    private static TreeMap<Integer, Integer> nbServices; // < idPro, nbServices >, le nb de services fournis par chacun
    private static TreeSet<Integer> hasRapport; // < idCompte >, on y met tous les comptes qui ont déjà un fichier
                                                // rapport approprié pour la semaine

    // Les chemins vers les fichiers à écrire
    private final static String confirmationPath = System.getProperty("user.dir") +
                                        "\\resources\\Enregistrement\\Confirmation\\",

                                rapportGerantPath = System.getProperty("user.dir") +
                                        "\\resources\\Rapport\\Gerant\\",

                                rapportProPath = System.getProperty("user.dir") +
                                        "\\resources\\Rapport\\Professionnel\\",

                                rapportMembrePath = System.getProperty("user.dir") +
                                        "\\resources\\Rapport\\Membre\\",

                                TEFpath = System.getProperty("user.dir") + "\\resources\\TEF\\";

    private final static String fileExt = ".txt";

    /**
     * Cette methode initialise l'execution de l'entierete du procedure comptable. On passe par chaque fichier de
     * confirmation et on appelle lireFichier avec parametre le chemin au fichier.
     *
     * Puis, on genere les fichiers makeTEF pour chaque professionnel. Finalement, le rapport de gérant est genere.
     *
     * @param system le Centre de Donnees qui contient les donnees necessaires pour generer les fichiers.
     * @param dateActuelle la date actuelle pour calculer le lundi de la semaine.
     */
    public static void executerProc(CentreDonnee system, Calendar dateActuelle) {

        systemCentral = system;
        frais = new TreeMap<>();
        nbServices = new TreeMap<>();
        hasRapport = new TreeSet<>();

        dateActuelle.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        File dossierSemaine = new File(confirmationPath + CalendarFormat.dateToStr(dateActuelle));
        System.out.println(dossierSemaine.getName());

        if (!dossierSemaine.exists()) {
            return;
        }

        File[] fichiers = dossierSemaine.listFiles();

        for (File fichier : fichiers) {
            System.out.println(fichier.getName());
            lireFichier(fichier.getPath(), dateActuelle);
        }

        makeTEF(dateActuelle);
        makeRapportGerant(dateActuelle);
    }

    /**
     * Cette methode itere sur chaque fichier de confirmation. Pour chacun de ces fichiers, on accumule le nombre de
     * services que chaque professionnel a fourni et le total des frais a payer a chacun.
     * @param path le chemin vers le fichier de confirmation
     */
    private static void lireFichier(String path, Calendar semaine) {

        int idPro, idMembre, codeService, codeSeance;

        try {
            ArrayList<String> contenu = Lire.lireFichier(path); // Les lignes du fichier.

            // --- DONNÉES UTILES POUR CHAQUE ITÉRATION ---
            idPro = Integer.parseInt(contenu.get(1)); // Professionnel en charge de la séance.
            Professionnel pro = systemCentral.getPro(idPro);

            codeService = Integer.parseInt(contenu.get(3)); // Code du service.
            String nomService = systemCentral.getService(codeService).getNomService();

            codeSeance = Integer.parseInt(contenu.get(4)); // Code de la séance.
            Seance seance = systemCentral.getSeance(codeService, codeSeance); // La séance elle-mêmeà

            // --- ACCUMULER LE NOMBRE DE SERVICES PAR LE PROFESSIONNEL ---
            if (nbServices.containsKey(idPro)) {
                int nbService = nbServices.get(idPro);
                nbServices.put(idPro, nbService + 1);

            } else { // Le premier service enregistré de la semaine dans Confirmation.
                nbServices.put(idPro, 1); // Initialisé.
            }

            // --- BLOC DE CONFIRMATION ---
            boolean isLastInscription = false;
            boolean paid = false;
            for (int i = 0; i < contenu.size(); i += 7) { // Pour chaque bloc de confirmation (= 7 lignes).

                String dateEnreg = contenu.get(i);
                idMembre = Integer.parseInt(contenu.get(i + 2));

                Membre membre = systemCentral.getMembre(idMembre);

                if (i == contenu.size() - 1) {
                    isLastInscription = true;
                }

                makeRapportPro(idPro, pro, idMembre, membre.getNom(),
                        semaine, dateEnreg,
                        codeService, seance, isLastInscription);

                makeRapportMembre(idMembre, membre, pro.getNom(), semaine,
                        seance.getDateHeureSeance(), nomService);

                // --- ACCUMULER LES FRAIS DU PROFESSIONNEL ---
                double fraisService;

                if (!paid) { // Pas encore payé pour la séance.

                    if (frais.containsKey(idPro)) {
                        fraisService = frais.get(idPro);
                        frais.put(idPro, fraisService + seance.getFrais());

                    } else { // Le premier service enregistré de la semaine dans Confirmation.
                        frais.put(idPro, seance.getFrais());
                    }

                    // Si c'est un seul montant, paid = true pour éviter de facturer plusieurs fois.
                    // Si c'est un frais par inscription, paid = false toujours.
                    if (!seance.isFraisParInscription()) paid = true;
                }

            }



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette methode redige un bloc de rapport professionnel. Si le dossier du membre n'existe pas, il faut le creer.
     * Pour le fichier rapport correspondant a la semaine, s'il n'est pas encore cree, il faut rediger le bloc
     * d'informations generales en premier. Puis, il faut rediger une liste par service fourni par le professionnel et
     * des blocs de membres pour chaque bloc de service.
     * @param idPro le numero unique du professionnel.
     * @param pro l'instance Professionnel pour recuperer le nom et d'autres informations.
     * @param idMembre le numero unique du membre.
     * @param nomMembre le nom du membre.
     * @param semaine l'instance Calendar qui represente le lundi de la semaine.
     * @param dateEnreg un String pour la date d'enregistrement de la confirmation de presence du membre a la séance.
     * @param codeService le code de service
     * @param seance l'instance Seance pour recuperer les donnees necessaires.
     * @param isLastInscription le booleen pour si c'est le dernier membre de la seance.
     * @see Seance
     * @see Professionnel
     * @see Calendar
     * @see CalendarFormat
     * @see NumberFormat
     */
    private static void makeRapportPro(int idPro, Professionnel pro, int idMembre, String nomMembre,
                                       Calendar semaine, String dateEnreg,
                                       int codeService, Seance seance, boolean isLastInscription) {

        try {
            // --- RÉDIGER RAPPORT PROFESSIONNEL ---
            String pathname = rapportProPath + NumberFormat.versNeufChar(idPro) + "\\";
            File dossier = new File(pathname);
            if (!dossier.exists()) {
                dossier.mkdirs();
            }

            pathname += CalendarFormat.dateToStr(semaine) + fileExt + "\\";

            File rapport = new File(pathname);
            if (rapport.exists()) {
                rapport.setWritable(true);
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(pathname, true));

            if (!hasRapport.contains(idPro)) {

                writer.write("Rapport professionnel du" + CalendarFormat.dateToStr(semaine) + "\n");
                writer.newLine();
                writer.write("  Nom du professionnel: " + pro.getNom() + "\n");
                writer.write("  Numero unique: " + NumberFormat.versNeufChar(idPro) + "\n");
                writer.write("  Adresse: " + pro.getAdresse() + "\n");
                writer.write("  Ville: " + pro.getVille() + "\n");
                writer.write("  Province: " + pro.getProvince() + "\n");
                writer.write("  Code postal: " + pro.getCodePostal() + "\n");
                writer.newLine(); writer.newLine();
                writer.write("Liste de service(s) fourni(s) par le professionnel" + "\n");
                writer.newLine();

                hasRapport.add(idPro);
            }

            writer.write("  Date de service: " +
                    CalendarFormat.dateToStr(seance.getDateHeureSeance()) + "\n");
            writer.write("  Date d'enregistrement de confirmation: " +
                    dateEnreg + "\n");
            writer.write("  Nom du membre: " + nomMembre + "\n");
            writer.write("  Numero du membre: " + NumberFormat.versNeufChar(idMembre) + "\n");
            writer.write("  Code du service: " + NumberFormat.versSeptChar(codeService) + "\n");
            writer.write("  Code de la séance: " + NumberFormat.versSeptChar(seance.getCodeSeance()) + "\n");

            if (seance.isFraisParInscription()) {
                writer.write("  Montant a payer: " + NumberFormat.versDeuxDecimal(seance.getFrais()) + "\n");
            } else if (isLastInscription) {
                writer.write("  Montant a payer: " + NumberFormat.versDeuxDecimal(frais.get(idPro)) + "\n");
            }
            writer.newLine();

            rapport.setReadOnly();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Cette methode redige un bloc de rapport membre. Si le dossier du membre n'existe pas, il faut le creer. Pour le
     * fichier rapport correspondant a la semaine, s'il n'est pas encore cree, il faut rediger le bloc d'informations
     * generales en premier. Puis, il faut rediger un bloc par service fourni au membre.
     * @param membre l'objet complet Membre pour recuperer les donnees necessaires.
     * @param semaine semaine est le lundi de la semaine. On l'utilise pour ecrire la date.
     * @param dateSeance dateSeance est un objet Calendar. On l'utilise pour ecrire la date de seance.
     * @param proNom proNom est un String et on l'utilise pour ecrire le nom.
     * @param nomService nomService est un String et on l'utilise pour l'ecrire.
     * @see ProcedureComptable
     * @see Membre
     * @see Calendar
     * @see CalendarFormat
     * @see NumberFormat
     */
    private static void makeRapportMembre(int idMembre, Membre membre, String proNom,
                                          Calendar semaine, Calendar dateSeance,
                                          String nomService) {

        try {

            // --- RÉDIGER UN BLOC DE TEXTE DANS LE RAPPORT DU MEMBRE ---

            // - Le dossier rapportMembrePath/idMembre/dateSemaine -
            String pathname = rapportMembrePath + NumberFormat.versNeufChar(membre.getID()) + "\\";
            File dossier = new File(pathname);
            if (!dossier.exists()) { // Créer si le dossier n'existe pas encore.
                dossier.mkdirs();
            }

            // - Le fichier dans lequel on rédige les blocs -
            pathname += CalendarFormat.dateToStr(semaine) + fileExt + "\\";

            File fichier = new File(pathname);
            if (fichier.exists()) {
                fichier.setWritable(true);
            }

            BufferedWriter writer = new BufferedWriter(new FileWriter(fichier, true));

            // - INFOS GÉNÉRALES POUR LES NOUVEAUX FICHIERS -
            if ( !hasRapport.contains(idMembre) ) {
                writer.write("Rapport membre du " + CalendarFormat.dateToStr(semaine) + "\n");
                writer.newLine();
                writer.write("Nom du membre: " + membre.getNom() + "\n");
                writer.write("Numero unique: " + NumberFormat.versNeufChar(idMembre) + "\n");
                writer.write("Adresse: " + membre.getAdresse() + "\n");
                writer.write("Ville: " + membre.getVille() + "\n");
                writer.write("Province: " + membre.getProvince() + "\n");
                writer.write("Code postal: " + membre.getCodePostal() + "\n");
                writer.newLine();
                writer.write("Liste de services fourni au membre\n");
                writer.newLine();

                hasRapport.add(idMembre);
            }

            // - BLOC DE RAPPORT SUR LE SERVICE -
            writer.write("  Date de seance: " + CalendarFormat.dateToStr(dateSeance) + "\n");
            writer.write("  Nom du professionnel: " + proNom + "\n");
            writer.write("  Nom du service: " + nomService + "\n");
            writer.newLine();

            fichier.setReadOnly();
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette methode genere un rapport de synthese de gerant. Pour chaque professionnel, on redige un bloc qui contient
     * le nom du professionnel, son numéro unique, le montant a lui payer et le nombre de services qu'il a fourni durant
     * la semaine.
     * @param semaine le lundi de la semaine utilise pour nommer les fichiers par semaine.
     */
    public static void makeRapportGerant(Calendar semaine) {

        Set<Integer> listPros = nbServices.keySet(); // La liste de numéro unique de chaque professionnel.

        String nomPro; // Le nom du professionnel.

        int totalNbService = 0; // Accumuler le nombre total de services fournis cette semaine.
        double totalFrais = 0; // Le total des frais à payer en tout.

        BufferedWriter writer;
        try {

            String pathName = rapportGerantPath + // Le chemin au fichier du rapport de gérant de la semaine
                    CalendarFormat.dateToStr(semaine) + fileExt + "\\";

            File fichier = new File(pathName);
            if (fichier.exists()) fichier.delete();

            writer = new BufferedWriter(new FileWriter(fichier));

            // Le header du fichier et le header de la liste des comptes payables.
            writer.write("Rapport de synthese du gerant du " + CalendarFormat.dateToStr(semaine) + "\n");
            writer.newLine();
            writer.write("Liste de comptes payables\n");
            writer.newLine();

            for (Integer idPro : listPros) { // Pour chaque professionnel, rédiger un bloc.

                nomPro = systemCentral.getPro(idPro).getNom(); // Chercher le nom du professionnel.

                // Rédiger le bloc pour ce professionnel.
                writer.write("  Nom du professionnel: " + nomPro + "\n");
                writer.write("  Numero du professionnel: " + NumberFormat.versNeufChar(idPro) + "\n");
                writer.write("  Frais pour les services: " +
                        NumberFormat.versDeuxDecimal(frais.get(idPro)) + "\n");
                writer.write("  Nombre total de services fournis: " + nbServices.get(idPro) + "\n");
                writer.newLine(); writer.newLine();

                // Accumuler les valeurs globales.
                totalNbService += nbServices.get(idPro);
                totalFrais += frais.get(idPro);
            }

            // Rédiger le bloc de résumé.
            writer.write("Resume\n");
            writer.newLine();
            writer.write("  Nombre de professionnels ayant fourni un ou des services(s) cette semaine: " +
                    listPros.size() + "\n");
            writer.write("  Nombre de services fournis cette semaine: " + totalNbService + "\n");
            writer.write("  Total des frais pour l'ensemble de la semaine: " +
                    NumberFormat.versDeuxDecimal(totalFrais));

            fichier.setReadOnly();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette methode genere un fichier makeTEF pour chaque professionnel. Chaque makeTEF contient le nom du
     * professionnel, son
     * numéro unique et le montant a lui payer.
     * @param semaine le lundi de la semaine utilise pour nommer les fichiers par semaine
     */
    private static void makeTEF(Calendar semaine) {

        Set<Integer> listPros = nbServices.keySet(); // La liste de numéro unique de chaque professionnel

        String pathName; // Le chemin du fichier du professionnel.
        String nomPro; // Le nom du professionnel.

        BufferedWriter writer;
        try {

            for (Integer idPro : listPros) { // Pour chaque professionnel

                nomPro = systemCentral.getPro(idPro).getNom(); // Chercher le nom du professionel.

                pathName = TEFpath + NumberFormat.versNeufChar(idPro) + "\\";
                // Le dossier makeTEF du professionnel.

                // Vérifier si le dossier makeTEF du professionnel existe.
                File dossier = new File(pathName);
                if (!dossier.exists()) dossier.mkdirs(); // Si non, créer le dossier.

                pathName += CalendarFormat.dateToStr(semaine) + fileExt + "\\"; // Compléter le chemin avec la date.

                File rapport = new File(pathName);
                writer = new BufferedWriter(new FileWriter(rapport)); // Écrire dans ce fichier.

                // Bloc makeTEF pour le professionnel
                writer.write("Transfert electronique de fond " + CalendarFormat.dateToStr(semaine) + "\n");
                writer.newLine();
                writer.write("  Nom du professionnel: " + nomPro + "\n");
                writer.write("  Numero unique: " + NumberFormat.versNeufChar(idPro) + "\n");
                writer.write("  Frais pour les services: " + frais.get(idPro));

                rapport.setReadOnly();
                writer.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Cette methode accede au fichier rapport du membre et retourne le texte String du contenu du rapport de la semaine
     * passee.
     * @param semaineDerniere un instance Calendar qui represente le lundi de la semaine passee
     * @param idMembre un int
     * @see NumberFormat#versNeufChar(int)
     * @see CalendarFormat#dateToStr(Calendar)
     * @see Lire#lireFichier(String)
     */
    public static String getRapportMembre(Calendar semaineDerniere, int idMembre) {

        String pathToRapport = rapportMembrePath + NumberFormat.versNeufChar(idMembre) + "\\" +
                CalendarFormat.dateToStr(semaineDerniere) + "\\";

        String result = "";

        try {
            ArrayList<String> contenu = Lire.lireFichier(pathToRapport);

            for (String ligne : contenu) {
                result += ligne;
            }

            return result;

        } catch (FileNotFoundException e) {

            System.out.println("Aucun rapport trouvé pour la semaine passée");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // If unsuccessful
    }

    /**
     * Cette methode accede au fichier rapport du professionnel et retourne le texte String du contenu du rapport de la
     * semaine passee.
     * @param semaineDerniere un instance Calendar qui represente le lundi de la semaine passee
     * @param idPro un int
     * @see NumberFormat#versNeufChar(int)
     * @see CalendarFormat#dateToStr(Calendar)
     * @see Lire#lireFichier(String)
     */
    public static String getRapportPro(Calendar semaineDerniere, int idPro) {

        String pathToRapport = rapportProPath + NumberFormat.versNeufChar(idPro) + "\\" +
                CalendarFormat.dateToStr(semaineDerniere) + "\\";

        String result = "";

        try {
            ArrayList<String> contenu = Lire.lireFichier(pathToRapport);

            for (String ligne : contenu) {
                result += ligne;
            }

            return result;

        } catch (FileNotFoundException e) {

            System.out.println("Aucun rapport trouvé pour la semaine passée");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // If unsuccessful
    }

    /**
     * Cette methode accede au fichier rapport du gerant et retourne le texte String du contenu du rapport de la semaine
     * passee.
     * @param semaineDerniere un instance Calendar qui represente le lundi de la semaine passee
     * @see CalendarFormat#dateToStr(Calendar)
     * @see Lire#lireFichier(String)
     */
    public static String getRapportGerant(Calendar semaineDerniere) {

        String pathToRapport = rapportProPath + CalendarFormat.dateToStr(semaineDerniere) + "\\";

        String result = "";

        try {
            ArrayList<String> contenu = Lire.lireFichier(pathToRapport);

            for (String ligne : contenu) {
                result += ligne;
            }

            return result;

        } catch (FileNotFoundException e) {

            System.out.println("Aucun rapport trouvé pour la semaine passée");

        } catch (IOException e) {
            e.printStackTrace();
        }

        return null; // If unsuccessful
    }
}
