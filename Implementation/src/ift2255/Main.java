package ift2255;

import ift2255.Pays.Canada;
import ift2255.formateur.CalendarFormat;
import ift2255.formateur.NumberFormat;

import java.io.*;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Calendar.MONDAY;

public class Main {

    private static CentreDonnee centreDonnee;

    // Où se trouve l'état précédent du système. Sauf si c'est la première exécution du programme.
    private static File systemFile = new File(System.getProperty("user.dir") +
            "\\resources\\system\\");

    /**
     * Cette methode affiche le menu principal.
     * @see #main(String[])
     * @see #newLine()
     * @author Tina
     */
    private static void printMenu() {
        System.out.println("Bienvenue dans #GYM");
        newLine();

        System.out.println("----- CU de gérant -----");
        System.out.println("1 pour créer un nouveau compte membre");
        System.out.println("2 pour créer un nouveau compte professionnel");
        System.out.println("3 pour consulter le Répertoire des Services");
        System.out.println("4 pour créer un nouveau service");
        System.out.println("5 pour créer une nouvelle seance de service");
        System.out.println("6 pour consulter la liste des inscriptions à une séance");
        System.out.println("7 pour la gestion d'un compte");
        System.out.println("8 pour la gestion d'une séance de service");
        System.out.println("9 pour rédiger le rapport de gérant de la semaine passée");
        System.out.println("10 pour consulter le rapport de gérant de la semaine passée");
        newLine();

        System.out.println("----- CU de l'application mobile uniquement -----");
        newLine();
        System.out.println("--- Version membre ---");
        System.out.println("11 pour connecter sur l'application mobile comme membre");
        System.out.println("12 pour s'inscrire à une séance");
        System.out.println("13 pour consulter le rapport membre le plus récent");
        newLine();

        System.out.println("--- Version professionnel uniquement ---");
        System.out.println("14 pour connecter sur l'application mobile comme professionnel");
        System.out.println("15 pour confirmer un membre à une séance");
        System.out.println("16 pour consulter le rapport professionnel le plus récent");
        newLine();

        System.out.println("----- QUITTER -----");
        System.out.println("17 pour quitter");
        newLine();

        System.out.println("----- Autres -----");
        System.out.println("18 pour valider un numéro unique");
        System.out.println("19 pour exécuter la procédure comptable");
        newLine();
    }

    /**
     * Methode qui permet a un membre de s'inscrire a une seance
     * On affiche le repertoire et on laisse le membre choisir le service.
     * Une fois le service choisit, le membre choisit la seance offerte par ce service
     * On ajoute le membre a la seance, si la capacite n'est pas maximale, on enregistre
     * les donnees du membre pour la seance
     * @see CentreDonnee
     * @see Service
     * @see Seance
     * @see Inscription
     * @param sc prend les entrees de l'utilisateur
     * @author Adrien
     */
    public static void inscrireSeance(Scanner sc) {

        String userInput;

        System.out.println("Entrez votre numero de membre");
        userInput = sc.nextLine();
        newLine();

        int codeMembre = Integer.parseInt(userInput);
        Membre membre = centreDonnee.getMembre(codeMembre);

        // On regarde si le membre est valide
        if (centreDonnee.validerMembre(codeMembre) == false) {
            System.out.println("Membre non valide");
            return;
        }

        System.out.println("----- Répertoire des Services -----");
        newLine(); newLine();

        if (centreDonnee.getNbServices() == 0) {
            System.out.println ("Aucun Service disponible.");
            return;
        } else {
            System.out.println(centreDonnee.affichageRepertoire());
        }
        newLine();

        System.out.println("Écrivez le code du service dont vous voulez vous" +
                " inscrire");
        userInput = sc.nextLine();
        newLine();
        int codeService = Integer.parseInt(userInput);

        // On obtient le service a partir du code tappé
        Service service = centreDonnee.getService (codeService);

        // On montre les séances offertes pour le service
        System.out.println("Séances offertes pour ce service");
        newLine();

        System.out.println(service.affichage());
        newLine();

        System.out.println("Écrivez le code de la séance dont vous voulez vous " +
                "inscrire");
        userInput = sc.nextLine();
        newLine();

        int codeSeance = Integer.parseInt(userInput);

        // On obtient la séance à partir du code tappé
        Seance seance = service.getSeance(codeSeance);
        System.out.println(seance.affichage());
        newLine();

        System.out.println("Écrivez <ok> pour confirmer l'inscription");
        userInput = sc.nextLine();
        newLine();

        if(userInput.equals("ok")) {
            Calendar maintenant = Calendar.getInstance();

            if (seance.inscrire(codeMembre) == false) {

                if (seance.isInscrit(codeMembre)) System.out.println("Membre est déjà inscrit à la séance.");
                else System.out.println ("Capacité maximale atteinte");

                newLine();
                return;
            }

            Inscription.enregistrer(membre, seance, maintenant);

            afficherFraisService(codeService, codeSeance);
            newLine();

        }
    }

    /**
     * cette methode affiche la liste des membre inscrit a une seance
     * @see CentreDonnee
     * @param codeSeance
     * @param codeService
     * @author Soumaila
     */
    public static void consulterInscrption(int codeSeance, int codeService) {
    	centreDonnee.getSeance(codeService, codeSeance).afficherMembre(centreDonnee);
    }

    /**
     * Pour que le membre puisse utiliser l'aplication mobile
     * il doit se loguer et cette fonction return true si le courriel est valide 
     * et false si le courriel est invalide
     * @param mail un String
     * @return un boolean pour dire si le courriel est valide ou pas
     * @see CentreDonnee
     * @author Soumaila
     */
    public static boolean loginMembre(String mail) {

        if (centreDonnee.getListeMembres() == null) {
            return false;
        }
    	LinkedList<Membre> me=centreDonnee.getListeMembres();
    	for(int m=0; m<me.size();m++) {
    		if(me.get(m).getCourriel().equalsIgnoreCase(mail)) {
    			System.out.println("reussi");
    			return true;
    		}
    		
    	}
    	System.out.println("mail non valide");
    	return false;
    }

    /**
     * Pour que le professionnel puisse utiliser l'aplication mobile
     * il doit se loguer et cette fonction return true si le courriel est valide
     * et false si le courriel est invalide
     * @param sc un Scanner
     * @author Tina
     */
    public static void loginPro(Scanner sc) {

        String userInput;

        while (true) {
            System.out.println("Entrez le courriel du professionnel:");
            userInput = sc.nextLine();
            newLine();

            if (courrielValide(userInput)) {

                Professionnel pro = centreDonnee.getProLogin(userInput);

                if (pro == null) {
                    System.out.println("Aucun compte trouvé.");
                    newLine();
                } else {
                    System.out.println(pro.affichage());
                    newLine();
                }

                taperEnter("retourner au menu principal", sc);

                break;
            }

            System.out.println("Entrée n'est pas un courriel.");
            newLine();

            if (optionRetourMenu(sc)) {
                return;
            }
        }

    }

    /**
     * Avant de pouvoir assister a une seance, le membre doit confirmer
     * sa presence.
     * On regarde d'abord si la seance dont le membre veut assister
     * existe. Ensuite, on verifie si le membre est bien inscrit
     * a cette seance. Si un des deux n'est pas vrai, le membre
     * se verra l'acces refuse. Si les deux sont vrais, le membre
     * peut assister a la seance
     * @see CentreDonnee
     * @see Seance
     * @param sc prend les entrees de l'utilisateur
     * @author Adrien
     */
    public static void confirmerPresence(Scanner sc) {

        String userInput;

        System.out.println("Écrivez le numero du membre");
        userInput = sc.nextLine();
        int codeMembre = Integer.parseInt(userInput);
        Membre membre = centreDonnee.getMembre(codeMembre);

        if (centreDonnee.validerMembre(codeMembre) == false) {
            System.out.println("Membre non valide");
            newLine();
            return;
        }

        System.out.println("Entrez le code du service");
        userInput = sc.nextLine();
        newLine();
        int codeService = Integer.parseInt(userInput);

        System.out.println("Entrez le code de la séance");
        userInput = sc.nextLine();
        newLine();
        int codeSeance = Integer.parseInt(userInput);

        System.out.println("Confirmation...");
        newLine();
        Seance seance = centreDonnee.getSeance(codeService, codeSeance);

        // regarde si la séance existe
        if (seance == null) {
            System.out.println("Séance non trouvé, accès refusé");
            newLine();
            return;
        }

        // regarde si le membre est bien inscrit
        if (seance.confirmer(codeMembre) == false) {
            System.out.println("Membre n'est pas inscrit à cette séance");
            System.out.println("Accès refusé");
            newLine();
        } else {
            System.out.println("Présence confirmé !");
            System.out.println("Accès à la séance autorisé");
            Confirmation.enregistrer(membre, seance, Calendar.getInstance());
            newLine();
        }
    }

    /**
     * Cette methode statique permet de simuler la consultation du Repertoire des Services. Il faut afficher le
     * repertoire, donc la liste des services disponibles. Le programme attend ensuite que l'utilisateur entre le code
     * de service. Le Centre de Donnees retourne le service associe au code entre.
     * <br><br>
     * Si le service n'existe pas, le programme va prompter a l'utilisateur de re-entrer un autre code de service.
     * Si le service existe, l'utilisateur a le choix de sortir de la consultation ou de continuer pour chercher une
     * seance.
     * <br><br>
     * S'il desire sortir de la consultation, on sort de la methode. S'il desire rester dans la consultation, la liste
     * des seances du service choisi est affichee ensuite. L'utilisateur entre le code de séance approprie. Le Centre
     * de Donnees va recuperer la seance appropriee et le programme va afficher toutes les informations de la seance.
     * <br><br>
     * Le programme retourne au menu principal.
     * @param sc le scanner qui est utilise pour prendre les entrees de l'utilisateur.
     * @see #gererSeance(Scanner)
     * @see #newLine()
     * @see CentreDonnee
     * @see Service
     * @see Seance
     * @author Tina
     */
    public static void consulterRepertoire(Scanner sc) {

        String userInput;

        // --- Header ---
        System.out.println("----- Répertoire des Services -----");
        newLine();

        // --- Cas de base ---
        if (centreDonnee.getNbServices() == 0) { // Aucun service à afficher.
            System.out.println("Aucun service disponible.");
            newLine();

            return;
        } else { // Au moins un service à afficher.
            System.out.println(centreDonnee.affichageRepertoire());
        }
        newLine();

        System.out.println("Entrez <ok> pour retourner. Sinon <Enter>");
        userInput = sc.nextLine();
        newLine();

        if (userInput.equals("ok")) return;

        // --- Chercher le service associé au code entré ---
        int codeService;
        Service service;

        while (true) {
            System.out.println("Entrez le code de service à 7 chiffres à consulter:");
            userInput = sc.nextLine();
            newLine();

            codeService = Integer.parseInt(userInput);
            service = centreDonnee.getService(codeService);

            if (service != null) { // Tant que le code de service est invalide, continuer à demander pour un code.

                // --- Afficher les séances avec les informations de bases ---
                System.out.println(service.affichage());
                newLine();

                break;
            }

            if (userInput.equals("1")) { // Sortir de la consultation
                return;
            } // else continuer la consultation

            System.out.println("Le service n'existe pas.");
            newLine();
        }

        // --- Si on cherchait seulement le service ---
        System.out.println("Entrez <ok> pour sortir de la consultation. Sinon <Enter> pour continuer.");
        userInput = sc.nextLine();
        newLine();

        if (userInput.equals("ok")) return;

        // --- Chercher la séance associée au code entré ---
        int codeSeance;
        Seance seance;

        while(true) {
            System.out.println("Entrez le code de séance à 7 chiffres à consulter:");
            userInput = sc.nextLine();
            newLine();

            codeSeance = Integer.parseInt(userInput);
            seance = service.getSeance(codeSeance);

            if (seance == null) { // La séance n'existe pas. Demander d'entrer un code valide.
                System.out.println("La séance n'existe pas.");
                newLine();
                continue;
            } // else continuer la consultation

            break;
        }

        // --- Afficher les informations sur la séance ---
        System.out.println(seance.affichage());
        newLine();
    }

    /**
     * Cette methode execute le procedure comptable
     * @param testing un booleen
     * @see ProcedureComptable#executerProc(CentreDonnee, Calendar)
     * @author Tina
     */
    public static void executerProcCompt(boolean testing) {
        Calendar now = Calendar.getInstance();
        if (now.get(Calendar.DAY_OF_WEEK) == 7 || testing) { // Past friday midnight.
            ProcedureComptable.executerProc(centreDonnee, now);
        }
    }

    /**
     * Cette methode prend chaque montant de seance du service, les additionne ensemble et affiche le total des frais.
     * @param codeService on donne le code de service et affiche
     * @see CentreDonnee
     * @see Service
     * @see Seance
     * @author Soumaila
     */
    public static void afficherFraisService(int codeService, int codeSeance) {
        Seance seance = centreDonnee.getSeance(codeService, codeSeance);
        System.out.println(seance.getFrais());
    }

    /**
     * Cette methode va rediger le rapport gerant associe aux activites de la semaine derniere.
     * @see ProcedureComptable#makeRapportGerant(Calendar)
     * @author Tina
     */
    public static void executerRapportGer() {

        Calendar semainePassee = Calendar.getInstance();

        semainePassee.add(Calendar.DAY_OF_MONTH, -7); // Aller chercher le rapport de la semaine passée.
        semainePassee.add(Calendar.DAY_OF_WEEK, MONDAY);

        try {
            ProcedureComptable.makeRapportGerant(semainePassee);
        } catch (NullPointerException e) {
            System.out.println("C'est soit la première semaine d'ouverture ou il ne s'est rien passé... En tout cas, il"
                    + " n'y a aucun rapport disponible pour l'instant.");
        }
    }

    /**
     * Cette methode va afficher le rapport de gerant de la semaine passee.
     * @param sc un Scanner
     * @see ProcedureComptable#getRapportGerant(Calendar)
     * @author Tina
     */
    public static void consulterRapportGerant(Scanner sc) {
        newLine();
        System.out.println("-----------------------------------");
        newLine();

        ProcedureComptable.getRapportGerant(getSemaineDerniere());

        newLine();
        System.out.println("-----------------------------------");
        newLine();
    }

    /**
     * Cette methode va afficher le rapport de professionnel de la semaine passee.
     * @see ProcedureComptable#getRapportPro(Calendar, int)
     * @author Tina
     */
    public static void consulterRapportPro(Scanner sc) {

        System.out.println("Entrez le numéro unique de professionnel:");
        String userInput = sc.next();
        newLine();

        int id = Integer.parseInt(userInput);
        if (!centreDonnee.validerMembre(id)) {
            System.out.println("Le numéro unique de professionnel n'est pas valide.");
            System.out.println("Retour au menu principal");
            newLine();
        }

        newLine();
        System.out.println("-----------------------------------");
        newLine();

        ProcedureComptable.getRapportPro(getSemaineDerniere(), id);

        newLine();
        System.out.println("-----------------------------------");
        newLine();
    }

    /**
     * Cette methode va afficher le rapport de membre de la semaine passee.
     * @see ProcedureComptable#getRapportMembre(Calendar, int)
     */
    public static void consulterRapportMembre(Scanner sc) {

        System.out.println("Entrez le numéro unique de membre:");
        String userInput = sc.next();
        newLine();

        int id = Integer.parseInt(userInput);
        if (!centreDonnee.validerMembre(id)) {
            System.out.println("Le numéro unique de membre n'est pas valide.");
            System.out.println("Retour au menu principal");
            newLine();
        }

        newLine();
        System.out.println("-----------------------------------");
        newLine();

        ProcedureComptable.getRapportMembre(getSemaineDerniere(), id);

        newLine();
        System.out.println("-----------------------------------");
        newLine();
    }

    /**
     * Cette methode valide un numero unique d'un membre donnee par l'utilisateur.
     * @param sc un instance Scanner
     * @author Tina
     */
    public static void validerNumero(Scanner sc) {

        String userInput;
        boolean pourMembre = true;

        System.out.println("1 pour valider un numéro de membre");
        System.out.println("2 pour valider un numéro de professionnel");
        userInput = sc.nextLine();
        newLine();

        if (userInput.equals("2")) pourMembre = false;

        System.out.println("Entrez le numéro unique:");
        userInput = sc.nextLine();
        newLine();

        int id = Integer.parseInt(userInput);
        Compte compte;

        if (pourMembre) compte = centreDonnee.getMembre(id);
        else compte = centreDonnee.getPro(id);

        if (compte == null) {
            System.out.println("Numéro invalide.");
            newLine();
        } else {

            if (pourMembre && ((Membre) compte).isSuspended()) {
                System.out.println("Numéro de membre est suspendu.");
                newLine();
            } else {
                System.out.println("Numéro valide.");
                newLine();

                System.out.println(compte.affichage());
                newLine();
            }
        }
    }

    /**
     * Cette methode statique a comme but de creer un compte pour soit un professionnel ou un membre. Le type de compte
     * a creer est determine par le parametre booleen creerMembre.
     * <br><br>
     * Tout d'abord, il faut collectionner les donnees necessaires pour creer le compte. Quelques donnees permettent
     * de re-essayer s'il y a un probleme d'entre. L'option de retourner au menu principal est donnee durant ces
     * problemes.
     * <br><br>
     * Le compte est cree, puis afficher les donnees du compte. Le paiement d'adhesion doit etre fait et est uniquement
     * <b>simule</b> pour ressembler a la realite.
     *<br><br>
     * La methode termine en affichant le courriel et le numero unique nouvellement assigne au nouveau membre. Le
     * programme retourne au menu principal.
     *
     * @param sc le scanner qui est utilise pour prendre les entrees de l'utilisateur.
     * @param creerMembre un booleen qui indique si l'appel est pour creer un compte de membre. Si non, c'est pour pro.
     * @see Compte
     * @see Professionnel
     * @see Membre
     * @see Canada
     * @see #courrielValide(String)
     * @see #codePostalValide(String)
     * @see #cutStringToLimit(String, int)
     * @see #newLine()
     * @author Tina
     */
    public static void creerCompte(Scanner sc, boolean creerMembre) {

        // --- Voici les données nécessaires à récupérer de l'utilisateur ---
        String nom, courriel, adresse, ville, province, codePostal;

        String userInput;

        // --- Le nom du membre/professionnel ---
        while (true) {

            if (creerMembre) System.out.println("Entrer le nom du nouveau membre:");
            else System.out.println("Entrer le nom du nouveau professionnel:");
            userInput = sc.nextLine();
            newLine();

            nom = cutStringToLimit(userInput, 25);
            if (allLettresSpace(nom)) break;

            System.out.println("Le nom ne peut seulement contenir des lettres ou espace.");
            newLine();

            if (optionRetourMenu(sc)) {
                return;
            }
        }

        // --- L'adresse du membre/professionnel ---
        System.out.println("Entrer son adresse:");
        userInput = sc.nextLine();
        adresse = cutStringToLimit(userInput, 25);
        newLine();

        // --- La ville du membre/professionnel ---
        System.out.println("Entrer la ville de son adresse:");
        userInput = sc.nextLine();
        ville = cutStringToLimit(userInput, 14);
        newLine();

        // --- La province du membre/professionnel ---
        while (true) {
            System.out.println("Entrer le code de la province à deux lettres:");
            System.out.println("(Entrez <aide> pour connaître les codes des provinces.)");
            userInput = sc.nextLine();
            newLine();

            if (userInput.equals("aide")) {
                System.out.println(Canada.affichageCode());
                newLine();

                userInput = sc.nextLine();
                newLine();
            }

            if (Canada.isProvinceCanadienCode(userInput)) {
                province = userInput;
                break;
            }

            System.out.println("La province entrée n'est pas valide.");
            newLine();

            if (optionRetourMenu(sc)) {
                return;
            }
        }

        // --- Le code postal du membre/professionnel ---
        while (true) {

            System.out.println("Entrer le code postal en format LCLCLC (L pour letter et C pour chiffre) :");
            userInput = sc.nextLine();
            newLine();

            if (codePostalValide(userInput)) {
                codePostal = userInput;
                break;
            }

            System.out.println("Le code postal entré n'est pas valide.");
            newLine();

            if (optionRetourMenu(sc)) {
                return;
            }
        }

        // --- Le courriel du membre/professionnel ---
        while (true) {
            System.out.println("Entrer son courriel:");
            userInput = sc.nextLine();
            newLine();

            if (courrielValide(userInput)) { // Si le courriel entré est valide.
                courriel = userInput;
                break;
            }

            // Sinon, invalide.
            System.out.println("Courriel n'est pas valide.\n");
            newLine();

            if (optionRetourMenu(sc)) {
                return;
            }
        }

        Compte compte = centreDonnee.creerCompte(creerMembre, nom, adresse, ville, province, codePostal, courriel);

        System.out.println("Compte est créé!");
        newLine();

        // --- Afficher les informations dans le nouveau compte ---
        System.out.println(compte.affichage());
        newLine();

        // --- Aviser que les changements se font via le menu principal ---
        System.out.println("Pour modifier des données, passer par la sélection \"Gérer un compte\" " +
                "dans le menu principal.");
        newLine();

        // --- Paiement d'adhésion pour un membre SIMULATION SEULEMENT ---
        if (creerMembre) {
            taperEnter("procéder au paiment", sc);
            paiement();
        }

        // --- Afficher le courriel et le numéro unique ---
        System.out.println("Voici le courriel et le numéro unique:");
        System.out.println(compte.getCourriel());
        System.out.println(NumberFormat.versNeufChar(compte.getID()));
        newLine();
    }

    /**
     * Cette methode sert a creer une seance. Il faut recuperer toutes les donnees necessaires pour creer une seance.
     * Apres que les donnees ont ete recueillies, le Centre de Donnees les reçoit pour creer la/les seances. Le nombre
     * de seances creees depend de la recurrence hebdomadaire et la periode de service couverte entre deux dates.
     * <br><br>
     * Pour le code de service, l'agent ou le professionnel n'est pas tenu compte de connaitre le code de service. Il
     * est possible de faire une consultation du Repertoire des Services si necessaire pour aller recuperer le code de
     * service approprie.
     * <br><br>
     * La/les seance(s) est/sont creee(s) via la methode dans le Centre de Donnes creerSeances(...).
     * La confirmation est affichee et le programme retourner au menu principal.
     *
     * @param sc le scanner qui est utilise pour prendre les entrees de l'utilisateur.
     * @see #getJour(String[])
     * @see #aideEntrerJour()
     * @see #newLine()
     * @see CentreDonnee#creerSeances(Calendar, int, int, Calendar, Calendar, Calendar, int[], double, boolean, int, String)
     * @see Service
     * @see Seance
     * @see CalendarFormat#getCalendarObj(String)
     * @see CalendarFormat#getCalObjPourHeure(String)
     * @see CalendarFormat#getDayOfWeekInt(String)
     * @author Tina
     */
    public static void creerSeance(Scanner sc) {

        // --- Les données nécessaires à entrer ---
        Calendar dateDebutService, dateFinService, heureService;
        int[] recurrenceService;
        double frais;
        boolean fraisParInscription = false;
        int capacMax;
        int idPro, codeService;
        String commentaires;

        String userInput;

        // --- Numéro unique du professionnel ---
        while (true) {

            System.out.println("Entrez le numéro unique à 9 chiffres du professionnel:");
            userInput = sc.nextLine();
            newLine();

            try {
                idPro = Integer.parseInt(userInput);
                break;

            } catch (NumberFormatException e) {
                System.out.println("Entrée non valide.");
                newLine();

                if (optionRetourMenu(sc)) {
                    return;
                }
            }
        }

        // --- Cherhcer le compte professionnel ---
        if (!centreDonnee.validerPro(idPro)) { // Si le numéro n'est pas valide, retourner au menu principal.
            System.out.println("Le numéro de professionnel n'est pas valide.");
            newLine();

            System.out.println("----- Annulé -----");
            System.out.println("Retour au menu principal.");
            newLine();

            return;
        }

        // --- Option d'aller chercher le code de service dans le Répertoire des Services ---
        System.out.println("Entrez <ok> pour chercher le code service dans le " +
                "Répertoire des Services. Ou <Enter> pour continuer");
        userInput = sc.nextLine();
        newLine();

        // --- Pour consulter le Répertoire ---
        if (userInput.equals("ok")) {
            consulterRepertoire(sc); // Les informations sur le service et ses séances va être afficher.
        }

        // --- Le code de service ---
        while(true) {
            System.out.println("Entrez le code de service:");
            userInput = sc.nextLine();
            newLine();

            codeService = Integer.parseInt(userInput);

            // Tant que le code de service est invalide, demandez pour un autre code.
            if (centreDonnee.serviceExiste(codeService)) {
                break;
            } // else continuer la création de séance.

            System.out.println("Le service associé au code entré n'existe pas.");
            newLine();

            if (optionRetourMenu(sc)) {
                return;
            }
        }

        // --- La récurrence hebdomadaire ---
        System.out.println("Entrez le(s) jour(s) de la semaine que le service est offert:");
        System.out.println("(Entrez <aide> pour de l'aide sur comment procéder)");
        userInput = sc.nextLine();
        newLine();

        // - Option d'aide pour entrer la récurrence hebdomadaire -
        if (userInput.equals("aide")) {
            aideEntrerJour();
            userInput = sc.nextLine();
            newLine();
        }

        // --- Traduire les mots-clés des jours en int selon la classe Calendar ---
        String[] selectionJour = userInput.split(" ");
        recurrenceService = getJour(selectionJour);

        // --- La date de début de service ---
        System.out.println("Entrez la date de début du service en format JJ-MM-AAAA :");
        userInput = sc.nextLine();
        newLine();

        dateDebutService = CalendarFormat.getCalendarObj(userInput);

        // --- La date de fin de service ---
        System.out.println("Entrez la date de fin du service en format JJ-MM-AAAA:");
        userInput = sc.nextLine();
        newLine();

        dateFinService = CalendarFormat.getCalendarObj(userInput);

        // --- L'heure de service ---
        System.out.println("Entrez l'heure de service en format HH:SS (24h) :");
        userInput = sc.nextLine();
        newLine();

        heureService = CalendarFormat.getCalObjPourHeure(userInput);

        // --- Les frais de service ---
        System.out.println("Entrez les frais de séances (max. 999.99) :");
        userInput = sc.nextLine();
        newLine();

        frais = Double.parseDouble(userInput);

        // --- Les frais sont par inscription ou par service ? ---
        System.out.println("Entrez <ok> pour appliquer les frais pour chaque inscription. Sinon tapez <Enter>.");
        userInput = sc.nextLine();
        newLine();

        if (userInput.equals("ok")) {
            fraisParInscription = true;
        }

        // --- La capacité maximale d'inscription à la séance ---
        System.out.println("Entrez la capacité maximale de la séance (max. 30) :");
        userInput = sc.nextLine();
        newLine();

        capacMax = Integer.parseInt(userInput);

        // --- Commentaires ---
        System.out.println("Commentaires (100 char max.)");
        userInput = sc.nextLine();
        newLine();

        if (userInput.length() == 0) commentaires = "";
        else commentaires = cutStringToLimit(userInput, 100);

        // --- Envoyer les données au Centre de Données ---
        centreDonnee.creerSeances(Calendar.getInstance(), idPro, codeService,
                dateDebutService, dateFinService, heureService,
                recurrenceService, frais, fraisParInscription, capacMax, commentaires);

        System.out.println("Les séances ont été créées et ajoutées pour le service.");
        newLine();

        System.out.println("Pour modifier des données, passer par la sélection \"Gérer une séance de service\"" +
                "dans le menu principal.");
        newLine();
    }

    /**
     * Cette methode va cree un instance de Service puis l'ajouter dans la liste des servies dans le Repertoire des
     * Services
     * @param sc un instance Scanner
     * @author Tina
     */
    public static void creerService(Scanner sc) {

        String userInput;

        System.out.println("Entrez le nom du service:");
        userInput = sc.nextLine();
        newLine();

        String nomService = cutStringToLimit(userInput, 20); // 20 caracteres

        Service nouveauService = centreDonnee.creerService(nomService);

        System.out.println("Nouveau service ajouté");
        newLine();

        System.out.println(nouveauService.affichage());
        newLine();
    }

    /**
     * Cette methode statique demande en premier si c'est pour creer un compte membre ou pour gerer un compte deja
     * existant.
     * <br><br>
     * Pour supprimer un compte membre existant, il faut simplement le supprimer du Centre de Donnees.
     * Pour un compte membre existant, les options de donnees pouvant etre modifiees sont affichees. L'utilisateur doit
     * selectionner quelle donnee modifier. Apres avoir selectionne, l'utilisateur entre la nouvelle donnee et cette
     * derniere est enregistree dans l'instance Membre par la methode setter appropriee.
     * <br><br>
     * Pour creer un compte membre, il suffit d'appeler a la methode creerCompte avec le booleen true.
     * <br><br>
     * L'utilisateur a egalement l'option de quitter le prcessus pour retourner au menu principal.
     * @param sc un instance Scanner
     * @see #creerCompte(Scanner, boolean)
     * @see Membre
     * @see CentreDonnee#removeMembre(int)
     * @see #newLine()
     * @see #mauvaisCommande()
     * @see #cutStringToLimit(String, int)
     */
    public static void gererCompte(Scanner sc) {

        String userInput;
        boolean compteMembre = true; // On veut aller voir un compte membre.

        while (true) { // Bouclez tant que la première commande ne passe pas.

            System.out.println("1 pour la gestion d'un compte membre");
            System.out.println("2 pour la gestion d'un compte professionnel");
            newLine();

            userInput = sc.nextLine();

            if (userInput.equals("2")) compteMembre = false; // On veut aller voir un compte professionnel

            System.out.println("1 pour gérer un compte qui existe");
            System.out.println("2 pour créer un nouveau compte");
            System.out.println("3 pour retourner au menu principal");
            newLine();
            userInput = sc.nextLine();

            if (userInput.equals("1")) { // Gérer un compte membre qui existe

                // --- Numéro unique du membre ---

                System.out.println("Entrez le numéro unique associé au compte");
                userInput = sc.nextLine();
                newLine();

                int id = Integer.parseInt(userInput);
                Compte compte;

                if (compteMembre) {
                    compte = centreDonnee.getMembre(id);
                } else {
                    compte = centreDonnee.getPro(id);
                }

                if (compte == null) { // Si le compte n'existe pas, retourner au menu principal.
                    System.out.println("Le compte n'existe pas. Retournez au menu principal.");
                    newLine();
                    return;
                }

                System.out.println(compte.affichage());
                newLine();

                // --- Gérer un compte existant ---
                while (true) {

                    System.out.println("1 pour modifier le compte");
                    System.out.println("2 pour supprimer le compte");
                    newLine();

                    userInput = sc.nextLine();

                    if (userInput.equals("1")) { // Modifier le compte.

                        System.out.println("Choisissez l'information à modifier:");
                        System.out.println("1 pour le nom");
                        System.out.println("2 pour le courriel");
                        System.out.println("3 pour l'adresse complet");
                        newLine();

                        userInput = sc.nextLine();
                        String selection = userInput;
                        newLine();

                        if (selection.equals("1")) { // Modifier le nom.
                            System.out.println("Entrez la nouvelle donnée:");
                            userInput = cutStringToLimit(sc.nextLine(), 25);
                            String nom = cutStringToLimit(userInput, 25);
                            compte.setNom(nom);
                        }

                        else if (selection.equals("2")) { // Modifier le courriel.
                            System.out.println("Entrez la nouvelle donnée:");
                            userInput = sc.nextLine();
                            if (courrielValide(userInput)) compte.setCourriel(userInput);
                        }

                        else if (selection.equals("3")) { // Modifier l'adresse complet

                            // --- Adresse ---
                            System.out.println("Entrer l'adresse:");
                            userInput = sc.nextLine();
                            String adresse = cutStringToLimit(userInput, 25);
                            newLine();

                            // --- Ville ---
                            System.out.println("Entrer la ville de son adresse:");
                            userInput = sc.nextLine();
                            String ville = cutStringToLimit(userInput, 14);
                            newLine();

                            // --- Province ---
                            System.out.println("Entrer la province de la ville:");
                            userInput = sc.nextLine();
                            String province = cutStringToLimit(userInput, 2);
                            newLine();

                            // --- Code postal ---
                            System.out.println("Entrer le code postal:");
                            userInput = sc.nextLine();
                            String codePostal = cutStringToLimit(userInput, 6);
                            newLine();

                            compte.setAdresse(adresse);
                            compte.setVille(ville);
                            compte.setProvince(province);
                            compte.setCodePostal(codePostal);
                        }

                        else { // Boucle secondaire pour un autre entrée.
                            System.out.println("Commande n'existe pas.");
                            continue;
                        }
                    }

                    else if (userInput.equals("2")) { // Supprimer le compte

                        if (compteMembre) centreDonnee.removeMembre(id);
                        else centreDonnee.removePro(id);

                        System.out.println("Le compte a été supprimé.");
                        newLine();
                    }

                    else { // Retourner au boucle principale. Recommencer le processus.
                        System.out.println("Commande n'existe pas.");
                        newLine();
                        continue;
                    }

                    return;
                }

            }

            else if (userInput.equals("2")) { // Créer un nouveau compte.
                creerCompte(sc, true);
                newLine();
            }

            else if (userInput.equals("3")){ // Retourner au menu principal.
                newLine();
                return;
            }

            else {
                mauvaisCommande();
            }

        }
    }

    /**
     * Cette methode statique demande en premier si c'est pour creer une seance ou pour gerer une seance deja existante.
     * <br><br>
     * Pour supprimer une seance existante, il faut simplement le supprimer du Centre de Donnees.
     * Pour une seance existante, l'utilisateur a le choix de consulter le Repertoire des Services pour recuperer les
     * codes service et seance. On recupere l'instance Seance approprie du Centre de Donnees et l'utilisateur doit
     * selectionner la donnee a modifier. Aprws avoir selectionne la donnee a modifier, l'utilisateur entre la nouvelle
     * donnee et est enregistree a la seance. Retourner au menu principal.
     * <br><br>
     * Pour creer une seance, il suffit simplement d'appeler a la methode statique creerSeance().
     * <br><br>
     * L'utilisateur a egalement l'option de quitter le processus pour retourner au menu principal.
     *
     * @param sc un instance Scanner
     * @see #creerSeance(Scanner)
     * @see #consulterRepertoire(Scanner)
     * @see CentreDonnee#removeSeance(int, int)
     * @see #newLine()
     * @see #taperEnter(String, Scanner)
     * @author Tina
     */
    public static void gererSeance(Scanner sc) {

        String userInput;

        while (true) { // Bouclez tant que la première commande ne passe pas.

            // --- Options de gestion ---
            System.out.println("1 pour gérer une séance de service qui existe");
            System.out.println("2 pour créer une nouvelle séance de service");
            System.out.println("3 pour retourner au menu principal");
            newLine();

            userInput = sc.nextLine();

            if (userInput.equals("1")) { // Gérer une séance de service qui existe

                int codeService, codeSeance;

                // --- Option d'aller chercher les codes dans le Répertoire de Services ---
                System.out.println("Entrez <ok> pour chercher les codes service et séance dans le " +
                        "Répertoire des Services");
                userInput = sc.nextLine();

                if (userInput.equals("ok")) {
                    consulterRepertoire(sc); // Affiche les données de la séance.
                }

                // --- Code de service ---
                System.out.println("Entrez le code de service associé à la séance:");
                userInput = sc.nextLine();
                codeService = Integer.parseInt(userInput);
                newLine();

                // --- Code de séance ---
                System.out.println("Entrez le code de la séance:");
                userInput = sc.nextLine();
                codeSeance = Integer.parseInt(userInput);
                newLine();

                // Si la séance n'existe, on retourne au menu principal.
                if (!centreDonnee.seanceExiste(codeService, codeSeance)) { // Si la séance n'existe pas.
                    System.out.println("Le séance n'existe pas. Retourner au menu principal.");
                    newLine();
                    return;
                }

                // --- Chercher l'instance de séance appropriée ---
                Seance seance = centreDonnee.getSeance(codeService, codeSeance); // Accéder à la séance.
                System.out.println(seance.affichage());
                newLine();

                // --- Modifier ou supprimer la séance ---
                while (true) {

                    System.out.println("1 pour modifier la séance");
                    System.out.println("2 pour supprimer la séance");
                    newLine();

                    if (userInput.equals("1")) { // Modifier la séance.

                        System.out.println("Choisissez l'information à modifier:");
                        System.out.println("1 pour la date de séance");
                        System.out.println("2 pour l'heure de séance");
                        System.out.println("3 pour la capacité maximale");
                        System.out.println("4 pour les frais de service");

                        newLine();

                        userInput = sc.nextLine();
                        String selection = userInput;
                        newLine();

                        if (selection.equals("1")) { // Modifier la date de séance.
                            System.out.println("Entrez la nouvelle date de séance JJ-MM-YYYY :");
                            userInput = sc.nextLine();
                            newLine();

                            Calendar dateSeance = CalendarFormat.getCalendarObj(userInput);
                            seance.setDateSeance(dateSeance);
                        }

                        else if (selection.equals("2")) { // Modifier l'heure de séance.
                            System.out.println("Entrez la nouvelle heure de séance HH:MM (format 24h) :");
                            userInput = sc.nextLine();
                            newLine();

                            Calendar heureSeance = CalendarFormat.getCalObjPourHeure(userInput);
                            seance.setHeureSeance(heureSeance);
                        }

                        else if (selection.equals("3")) { // Modifier la capacité maximale.
                            System.out.println("Entrer la nouvelle donnée:");
                            userInput = sc.nextLine();
                            newLine();

                            int capMax = Integer.parseInt(userInput);
                            seance.setMaxCapacite(capMax);
                        }

                        else if (selection.equals("4")) { // Modifier les frais de service.

                            while (true) { // Les nouveaux frais de service

                                System.out.println("Entrer le nouveau montant ###.## (max. 999.99) :");
                                userInput = sc.nextLine();
                                newLine();

                                double frais = Double.parseDouble(userInput);
                                if (frais > 999.99) { // Re-demander tant que la condition n'est pas respectée.
                                    System.out.println("Montant trop haute. Entrez un nouveau montant s.v.p.");
                                    newLine();
                                    continue;
                                }

                                seance.setFrais(frais);
                                break;
                            }

                            while (true) { // Les frais sont par inscriptions ou par service?

                                System.out.println("Les frais sont-ils par inscirptions?");
                                System.out.println("1 pour oui");
                                System.out.println("2 pour non");
                                newLine();

                                userInput = sc.nextLine();
                                if (userInput.equals("1")) seance.setFraisParInscription();
                                else if (userInput.equals("2")) seance.setFraisParService();
                                else {
                                    mauvaisCommande();
                                    continue;
                                }

                                break;
                            }

                        }

                        else { // Mauvaise commande. Re-demander pour une commande.
                            mauvaisCommande();
                            continue;
                        }
                    }

                    else if (userInput.equals("2")) { // Supprimer la séance de service
                        centreDonnee.removeSeance(codeService, codeSeance);
                        System.out.println("La séance de service a été supprimé.");
                        newLine();
                    }

                    else { // Retourner au boucle principale. Recommencer le processus.
                        mauvaisCommande();
                        continue;
                    }

                    return;
                }

            }

            else if (userInput.equals("2")) { // Créer une nouvelle séance de service.
                creerSeance(sc);
                newLine();
            }

            else if (userInput.equals("3")){ // Retourner au menu principal.
                newLine();
                return;
            }

            else {
                mauvaisCommande();
            }

        }
    }

    /**
     * Cette methode simule un processus de paiement pour l'adhesion d'un membre au #GYM. Le standard est 15 secondes
     * pour payer peu importe la methode de paiement. Elle fait appel a Thread.sleep(long) pour simuler l'attente
     * de confirmation de paiement.
     * @see Thread#sleep(long)
     */
    public static void paiement() {

        System.out.println("Permettez quelques secondes pour que la transaction se complète...");
        System.out.println("Ne tapez aucune commande.");
        try {
            Thread.sleep(10 * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Transaction complétée!");
        newLine();
    }

    /**
     * Cette methode retourne la date du lundi de la semaine passee.
     * @return un instance Calendar
     * @author Tina
     */
    private static Calendar getSemaineDerniere() {
        Calendar semaineDerniere = Calendar.getInstance();

        semaineDerniere.add(Calendar.DAY_OF_WEEK, -7); // Aller à la semaine passée
        semaineDerniere.set(Calendar.DAY_OF_WEEK, MONDAY); // Aller au lundi.

        return semaineDerniere;
    }

    /**
     * Cette methode permet l'option de retourner au menu principal lorsqu'un utilisateur execute une methode statique.
     * @param sc une instance Scanner
     * @return Retourne un booleen qui indique si l'utilisateur veut retourner au menu principal.
     * @author Tina
     */
    private static boolean optionRetourMenu(Scanner sc) {

        // --- Option de retourner au menu principal ---
        System.out.println("Entrez <ok> pour retourner au menu principal. Sinon <Enter> pour continuer.");
        newLine();

        String userInput = sc.nextLine();
        newLine();
        if (userInput.equals("ok")) {
            return true;
        } else { // Enter ou tout autre input à part "ok".
            return false;
        }
    }

    /**
     * Cette methode convertit le tableau de jour en String selon le format choisi dans la methode aideEntrerJour()
     * en tableau de int selon le standard de Calendar.DAY_OF_WEEK.
     *
     * @param jours le tableau de jour en String a convertir en tableau de int.
     * @return Retourne un tableau de int qui represente les jours de la semaine selon Calendar.DAY_OF_WEEK.
     * @see #creerSeance(Scanner)
     * @see CalendarFormat#getDayOfWeekInt(String)
     * @author Tina
     */
    private static int[] getJour(String[] jours) {

        int[] result = new int[jours.length];

        for (int i = 0; i < jours.length; i++) {
            int jour = CalendarFormat.getDayOfWeekInt(jours[i]);
            if (jour != -1) result[i] = jour;
        }

        int min, minIdx;
        int temp;
        for (int i = 0; i < jours.length; i++) {

            min = result[i];
            minIdx = i;

            for (int j = i + 1; j < jours.length; j++) {
                if (result[j] < min) {
                    min = result[j];
                    minIdx = j;
                }
            }

            temp = result[i];
            result[i] = min;
            result[minIdx] = temp;
        }

        return result;
    }

    /**
     * Cette methode affiche le message d'aide pour entrer les jours de la semaine. Surtout utilisee pour la methode
     * statique creerSeance(Scanner).
     *
     * @see #creerSeance(Scanner)
     * @see #newLine()
     * @author Tina
     */
    private static void aideEntrerJour() {
        System.out.println("  Lu pour lundi\n  Ma pour mardi\n  Me pour mercredi\n" +
                           "  Je pour jeudi\n  Ve pour Vendredi");
        System.out.println("Pour entrer plusieurs jours, mettez un espace simple entre les jours.");
        System.out.println("Ex. Vous voulez entrer le lundi et le mardi. Entrez: lu ma");
        newLine();
    }

    /**
     * Cette methode afficher un message qui dit de tapez sur le bouton <Enter> pour faire quelque chose. Ce quelque
     * chose dépend du parametre String pour.
     * @param pour le type d'evenement qui se passe apres que l'utilisateur tape <Enter>
     * @see #newLine()
     * @author Tina
     */
    private static void taperEnter(String pour, Scanner sc) {
        System.out.println("Tapez <Enter> pour " + pour + ".");
        newLine();
        sc.nextLine();
    }

    /**
     * Cette methode affiche qu'une commande entree n'est pas valide ou n'existe pas.
     * @see #newLine()
     * @author Tina
     */
    private static void mauvaisCommande() {
        System.out.println("Commande n'existe pas.");
        newLine();
    }

    /**
     * Cette methode permet de verifier selon un pattern si le paramètre envoye est un courriel.
     *
     * @param courriel un String qui est ou n'est pas un courriel.
     * @return Retourne un booleen qui indique si le parametre en String est un courriel.
     * @see Pattern#matches(String, CharSequence)
     * @see Pattern#compile(String)
     * @see Pattern#matcher(CharSequence)
     * @author Tina
     */
    private static boolean courrielValide(String courriel) {

        /*
        source:
        https://www.developpez.net/forums/d34321/java/general-java/apis/java-util/pattern-regex-v-rifier-email-valide/

        Équivalent à Pattern.match("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$", courriel);

        En utilisant la méthode ci-dessous, le pattern est compilé une seule fois. Alors que la méthode ci-dessus va
        appelant à la méthode plusieurs fois et va évaluer le pattern envoyé.
        */
        Pattern pattern = Pattern.compile("^[_a-z0-9-]+(\\.[_a-z0-9-]+)*@[a-z0-9-]+(\\.[a-z0-9-]+)+$");
        Matcher matcher = pattern.matcher(courriel);
        boolean match = matcher.matches();

        if (match) return true;
        else return false;
    }

    /**
     * Cette methode vérifie si un String est valide comme code postal en format LCLCLC où L est une lettre et C est un
     * chiffre.
     * @param codePostal un String
     * @return Retourne un booleen.
     * @see #isChiffre(char)
     * @see #isLettre(char)
     * @author Tina
     */
    private static boolean codePostalValide(String codePostal) {

        if (codePostal.length() > 6) return false; // Plus que 6 caractères.

        for (int i = 0; i < 6; i++) {

            char c = codePostal.charAt(i);

            // LNLNLN -> lettre et nombre alterne.
            if (i % 2 == 0 && !isLettre(c)) { // Si une lettre.
                return false;

            } else if (i % 2 == 1 && !isChiffre(c)) { // Si un nombre.
                return false;

            }
        }

        return true;
    }

    /**
     * Cette methode verifie si un caractere est une lettre.
     * @param c un caractere
     * @return Retourne un booleenà
     * @author Tina
     */
    private static boolean isLettre(char c) {
        return Character.isLetter(c);
    }

    /**
     * Cette methode verifie si le caractere est un chiffre.
     * @param c un caractere
     * @return Retourne un booleen.
     * @exception NumberFormatException pour verifier si c'est un nombre.
     * @author Tina
     */
    private static boolean isChiffre(char c) {
        try {
            Integer.parseInt(c + "");
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * Cette methode verifie si toutes les caracteres d'un String sont des lettres ou un espace.
     * @param str un String
     * @return Retourne un booleen.
     * @see #isLettre(char)
     * @author Tina
     */
    private static boolean allLettresSpace(String str) {

        if (str.length() > str.length()) return false;

        Pattern pattern = Pattern.compile("^[ A-Za-z]+$");
        Matcher matcher = pattern.matcher(str);
        boolean match = matcher.matches();

        if (match) return true;
        else return false;
    }

    /**
     * Cette methode permet de sauter une ligne. Les alternatives sont "\n" ou directement System.out.println. Pour
     * le but de rendre le code moins encombre par ces alternatives communes, cette methode est creee.
     * @author Tina
     */
    private static void newLine() {
        System.out.println(); System.out.println();
    }

    /**
     * Cette methode affiche le message qui indique que la longueur d'un entree de texte depasse une limite n donnee
     * en parametre.
     * @param n la limite de la longueur du texte.
     * @see #cutStringToLimit(String, int)
     * @see #newLine()
     * @author Tina
     */
    private static void afficherCharLimit(int n) {
        System.out.println("ATTENTION: L'entrée excède les " + n + " caractères!");
        newLine();
    }

    /**
     * Cette methode verifie si le texte envoye en parametre est de longueur plus petit ou egale a maxLength. Si oui,
     * elle retourne le texte. Sinon, un message est affiche pour prevenir que la longueur depasse la limite
     * et elle retourne le substring(0, maxLength) du texte.
     * @see #afficherCharLimit(int)
     *
     * @param str le texte String a verifier.
     * @param maxLength la limite de la longueur du texte
     * @return Retourne le substring(0, maxLength) du texte ou le texte complet.
     * @author Tina
     */
    private static String cutStringToLimit(String str, int maxLength) {
        if (str.length() > maxLength) {
            afficherCharLimit(maxLength);
            return str.substring(0, maxLength);
        } else {
            return str;
        }
    }

    /**
     * Cette methode initialise le Centre de Donnée au tout debut de l'execution du programme. Si c'est la premiere
     * qu'on execute le programme, il faut instancier le Centre de Donnee puisqu'il n'aura pas de fichier system.
     * Sinon, il faut aller lire le fichier system et recuperer l'etat precedent du systeme.
     * @author Tina
     */
    private static void initSystem() {

        if (!systemFile.exists()) {
            centreDonnee = new CentreDonnee();
        } else {

            try {
                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(systemFile));

                centreDonnee = new CentreDonnee();
                centreDonnee.setListeMembres((LinkedList<Membre>)ois.readObject());
                centreDonnee.setListePros((LinkedList<Professionnel>)ois.readObject());
                centreDonnee.setRepertoire((LinkedList<Service>)ois.readObject());

                ois.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Cette methode permet de sauvegarder l'etat du systeme avant que le programme arrete d'executer.
     * @author Tina
     */
    private static void saveSystem() {

        try {

            if (systemFile.exists()) {
                systemFile.delete();
            }

            systemFile.createNewFile();

            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(systemFile));
            oos.writeObject(centreDonnee.getListeMembres());
            oos.writeObject(centreDonnee.getListePros());
            oos.writeObject(centreDonnee.getRepertoire());
            oos.close();

            systemFile.setReadOnly();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }

    }

    public static void main(String[] args) throws IOException, ClassNotFoundException {

        initSystem();

        Scanner sc = new Scanner(System.in);
        String userInput;

        while(true) {

            executerProcCompt(false); // Vérifier chaque fois si c'est vendredi minute. Si oui, on exécute le procédure.

            printMenu();

            userInput = sc.nextLine();
            newLine();

            try {
                if (userInput.equals("1")) { // Créer un nouveau compte membre
                    creerCompte(sc, true);
                }

                else if (userInput.equals("2")) { // Créer un nouveau compte professionnel
                    creerCompte(sc, false);
                }

                else if (userInput.equals("3")) { // Consulter le Répertoire des Services
                    consulterRepertoire(sc);
                }

                else if (userInput.equals("4")) { // Créer un nouveau service
                    creerService(sc);
                }

                else if (userInput.equals("5")) { // Créer une nouvelle séance de service
                    creerSeance(sc);
                }

                else if (userInput.equals("6")) { // Consulter la liste des inscriptions à une séance
                	System.out.println("Entrer code seance un espace et code service");
                	String li= sc.nextLine();
                	String [] t;
                	t=li.split(" ");
                	consulterInscrption(Integer.parseInt(t[0]),Integer.parseInt(t[1]));
                }

                else if (userInput.equals("7")) { // Gérer un compte membre
                    gererCompte(sc);
                }

                else if (userInput.equals("8")) { // Gérer une séance de service
                    gererSeance(sc);
                }

                else if (userInput.equals("9")) { // Rédiger le rapport de gérant.
                    executerRapportGer();
                }

                else if (userInput.equals("10")) { // pour consulter le rapport de gérant de la semaine passée.
                    consulterRapportGerant(sc);
                }

                else if (userInput.equals("11")) { // pour connecter sur l'application mobile comme membre
                	System.out.println("Entrer le mail pour etre connecte");
                	String o=sc.nextLine();
                	loginMembre(o);
                }

                else if (userInput.equals("12")) { // pour s'inscrire à une séance
                    inscrireSeance(sc);
                }

                else if (userInput.equals("13")) { // pour consulter le rapport membre le plus récent
                    consulterRapportMembre(sc);
                }

                else if (userInput.equals("14")) { // pour connecter sur l'application mobile comme professionnel
                    loginPro(sc);
                }

                else if (userInput.equals("15")) { // pour confirmer un membre à une séance
                    confirmerPresence(sc);
                }

                else if (userInput.equals("16")) { // pour consulter le rapport professionnel le plus récent
                    consulterRapportPro(sc);
                }

                else if (userInput.equals("17")) { // Quitter le programme
                    saveSystem();
                }

                else if (userInput.equals("18")) { // Valider un numéro unique d'un membre
                    validerNumero(sc);
                }

                else if (userInput.equals("19")) {
                    executerProcCompt(true);
                }

                else { // Autres
                    mauvaisCommande();
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Un erreur s'est produit. Veuillez contacter l'équipe de maintenance.");
            }


            taperEnter("retourner au menu principal", sc);
        }
    }
}

