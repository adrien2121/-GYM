package ift2255;

import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class LoginTest {

    // variables "globales" dont les deux tests auront besoin
    static CentreDonnee centreDonnee = new CentreDonnee();

    Membre membre1 =  (Membre) centreDonnee.creerCompte
            (true, "Bob", "3200 Jean-Brillant", "Montreal",
            "Qc", "H34R3T", "bobleponge@hotmail.com");

    Membre membre2 = (Membre) centreDonnee.creerCompte
            (true, "Patrick", "3200 Jean-Brillant", "Montreal",
            "Qc", "H34R3T", "patrickletoile@hotmail.com");

    Professionnel pro1 = (Professionnel) centreDonnee.creerCompte
            (false, "Bonjour", "3200 Jean-Brillant", "montreal",
                    "Qc", "H34R3T", "bonjour@hotmail.com");

    Professionnel pro2= (Professionnel) centreDonnee.creerCompte
            (false, "Allo", "3200 Jean-Brillant", "montreal",
                    "Qc", "H34R3T", "allo@hotmail.com");

    // premier service
    Service service1 = centreDonnee.creerService("Zoomba");

    @Test
    public void loginMembre() {

        // test si bon nombre de membres
        assertEquals(4, centreDonnee.getNbCompte());

        // test si les informations du membre sont corrects
        assertSame(membre1, centreDonnee.getMembre(membre1.getID()));
        assertSame(1, membre1.getID());
        assertSame("patrickletoile@hotmail.com", membre2.getCourriel());
        assertSame("Montreal", membre1.getVille());

        // cas ou membre n'existe pas
        assertEquals(null, centreDonnee.getMembre(8976));


        // test si la validation marche correctement
        assertTrue(centreDonnee.validerMembre(1));
        assertFalse(centreDonnee.validerMembre(6929));
        assertTrue(centreDonnee.validerMembre(2));
        assertFalse(centreDonnee.validerMembre(345678));
    }

    @Test
    public void afficherFraisService() {

        // test si on recoit le bon service
        assertEquals("Zoomba", service1.getNomService());
        assertEquals(0, service1.getNbSeances());

        // setup pour le temps de la seance
        Calendar debut = Calendar.getInstance();
        debut.add(Calendar.DAY_OF_MONTH, 1);
        Calendar fin = Calendar.getInstance();
        fin.add(Calendar.DAY_OF_MONTH, 14);
        Calendar heure = Calendar.getInstance();
        heure.set(Calendar.HOUR_OF_DAY, 8);
        heure.set(Calendar.MINUTE, 30);
        int[] recurrence = {Calendar.MONDAY, Calendar.TUESDAY};

        centreDonnee.creerSeances(Calendar.getInstance(), pro1.getID(),
                service1.getCodeService(), debut, fin, heure, recurrence, 135.5, false,
                3, "");
        Seance zoomba1 = service1.getListeSeances().get(0);

        // test pour voir si on a les bon frais
        assertEquals(135.5, zoomba1.getFrais());
        assertNotEquals(99999, zoomba1.getFrais());

        // setup pour la deuxieme seance
        Calendar depart = Calendar.getInstance();
        depart.add(Calendar.DAY_OF_MONTH, 1);
        Calendar arrivee = Calendar.getInstance();
        arrivee.add(Calendar.DAY_OF_MONTH, 14);
        Calendar horaire = Calendar.getInstance();
        horaire.set(Calendar.HOUR_OF_DAY, 14);
        horaire.set(Calendar.MINUTE, 00);
        int[] rec = {Calendar.THURSDAY, Calendar.FRIDAY};

        centreDonnee.creerSeances(Calendar.getInstance(), pro1.getID(), service1.getCodeService(),
                depart, arrivee, horaire, rec, 135.5, false, 4, "");
        Seance zoomba2 = service1.getListeSeances().get(1);

        // test pour voir si on a les bon frais
        assertEquals(135.5, zoomba2.getFrais());
        assertNotEquals(271, zoomba2.getFrais());

        // test pour voir si on retrouve les seances dans le centre de donnee
        assertEquals(zoomba1, centreDonnee.getSeance(service1.getCodeService(), zoomba1.getCodeSeance()));
        assertEquals(service1, centreDonnee.getService(service1.getCodeService()));
        assertEquals(null, centreDonnee.getSeance(1234, 5674));
        assertEquals(null, centreDonnee.getService(1243534));
    }
}
