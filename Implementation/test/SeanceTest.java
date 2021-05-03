package ift2255;

import ift2255.formateur.CalendarFormat;
import org.junit.jupiter.api.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;

//import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

class SeanceTest {

    @Test
    void inscrire() {

        // ----- inscrire SETUP -----
        CentreDonnee system = new CentreDonnee();

        Professionnel premierPro = (Professionnel)
                system.creerCompte(false, "Foo",
                        "100 rue UdeM", "Montreal", "QC", "H1H1H1",
                        "foo@umontreal.ca");
        assertEquals("Foo", premierPro.getNom());
        assertEquals(1, premierPro.getID());

        assertEquals(1, system.getNbCompte());

        Membre premierMembre = (Membre)
                system.creerCompte(true, "Tina",
                        "999 rue Poly", "Montreal", "QC", "L9L9L9",
                        "tina.liu@umontreal.ca");
        assertEquals("Tina", premierMembre.getNom());
        assertEquals(2, premierMembre.getID());

        assertEquals(2, system.getNbCompte());

        Service premierService = system.creerService("Aérobie");
        assertEquals("Aérobie", premierService.getNomService());
        assertEquals(9999999, premierService.getCodeService());
        assertEquals(0, premierService.getNbSeances());

        assertEquals(true, system.validerMembre(premierMembre.getID()));
        assertEquals(false, system.validerPro(10));

        Calendar debut = new GregorianCalendar();
        debut.set(2019, Calendar.JULY, 23, 11, 00);

        Calendar fin = new GregorianCalendar();
        fin.set(2019, Calendar.JULY, 30, 11, 00);

        assertEquals(7, CalendarFormat.daysBetween(debut, fin));

        int[] recurrence = {Calendar.TUESDAY, Calendar.WEDNESDAY};

        Calendar heure = Calendar.getInstance();
        heure.set(Calendar.HOUR_OF_DAY, 11);
        heure.set(Calendar.MINUTE, 00);

        system.creerSeances(Calendar.getInstance(), premierPro.getID(), premierService.getCodeService(),
                debut, fin, heure, recurrence, 20.00, true, 5, "");

        // Verifier que les seances sont corrects.
        Seance premierSeance = premierService.getListeSeances().get(0);

        assertEquals(3, premierService.getNbSeances());
        assertEquals(20.00, premierSeance.getFrais());
        assertEquals(0, premierSeance.getNowCapacite());

        // size 0

        // cas initial
        premierSeance.inscrire(premierMembre.getID());
        assertEquals(true, premierSeance.isInscrit(premierMembre.getID())); // size 1

        // cas normal
        premierSeance.inscrire(125); // size 2

        // cas capacite max
        premierSeance.inscrire(126); // size 3
        premierSeance.inscrire(127); // size 4
        premierSeance.inscrire(128); // size 5

        premierSeance.inscrire(129); // inscription non valide
        assertEquals(false, premierSeance.isInscrit(129));
        assertEquals(false, premierSeance.isInscrit(130));

        // cas deja inscrit
        assertEquals(false, premierSeance.inscrire(premierMembre.getID()));
        assertEquals(false, premierSeance.inscrire(125));

        // capacite maximale
        assertEquals(5, premierSeance.getNowCapacite());
    }

    @Test
    void confirmer() {

        // ----- inscrire SETUP -----
        CentreDonnee system = new CentreDonnee();

        Professionnel premierPro = (Professionnel)
                system.creerCompte(false, "Foo",
                        "100 rue UdeM", "Montreal", "QC", "H1H1H1",
                        "foo@umontreal.ca");
        assertEquals("Foo", premierPro.getNom());
        assertEquals(1, premierPro.getID());

        assertEquals(1, system.getNbCompte());

        Membre premierMembre = (Membre)
                system.creerCompte(true, "Tina",
                        "999 rue Poly", "Montreal", "QC", "L9L9L9",
                        "tina.liu@umontreal.ca");
        assertEquals("Tina", premierMembre.getNom());
        assertEquals(2, premierMembre.getID());

        assertEquals(2, system.getNbCompte());

        Service premierService = system.creerService("Aérobie");
        assertEquals("Aérobie", premierService.getNomService());
        assertEquals(9999999, premierService.getCodeService());
        assertEquals(0, premierService.getNbSeances());

        assertEquals(true, system.validerMembre(premierMembre.getID()));
        assertEquals(false, system.validerPro(10));

        Calendar debut = new GregorianCalendar();
        debut.set(2019, 6, 23, 11, 00);

        Calendar fin = new GregorianCalendar();
        fin.set(2019, 6, 30, 11, 00);

        assertEquals(7, CalendarFormat.daysBetween(debut, fin));

        int[] recurrence = {Calendar.TUESDAY, Calendar.WEDNESDAY};

        Calendar heure = Calendar.getInstance();
        heure.set(Calendar.HOUR_OF_DAY, 11);
        heure.set(Calendar.MINUTE, 00);

        system.creerSeances(Calendar.getInstance(), premierPro.getID(), premierService.getCodeService(),
                debut, fin, heure, recurrence, 20.00, true, 5, "");

        // Verifier que les seances sont corrects.
        Seance premierSeance = premierService.getListeSeances().get(0),
                deuxieme = premierService.getListeSeances().get(1),
                troisieme = premierService.getListeSeances().get(2);

        Calendar sndSeance = new GregorianCalendar(), trsSeance = new GregorianCalendar();
        sndSeance.set(2019, Calendar.JULY, 24);
        trsSeance.set(2019, Calendar.JULY, 30);

        assertEquals(CalendarFormat.dateToStr(debut), CalendarFormat.dateToStr(premierSeance.getDateHeureSeance()));
        assertEquals(CalendarFormat.dateToStr(sndSeance), CalendarFormat.dateToStr(deuxieme.getDateHeureSeance()));
        assertEquals(CalendarFormat.dateToStr(trsSeance), CalendarFormat.dateToStr(troisieme.getDateHeureSeance()));


        assertEquals(3, premierService.getNbSeances());
        assertEquals(20.00, premierSeance.getFrais());
        assertEquals(0, premierSeance.getNowCapacite());

        // size 0

        // cas initial
        premierSeance.inscrire(premierMembre.getID());
        assertEquals(true, premierSeance.isInscrit(premierMembre.getID())); // size 1

        // cas normal
        premierSeance.inscrire(125); // size 2

        // cas capacite max
        premierSeance.inscrire(126); // size 3
        premierSeance.inscrire(127); // size 4
        premierSeance.inscrire(128); // size 5

        // Continuation of inscrireTest()

        assertEquals(5, premierSeance.getNowCapacite());

        // cas normal
        assertEquals(true, premierSeance.confirmer(2)); // return true. id du premierMembre.
        assertEquals(true, premierSeance.confirmer(125));

        // cas confirmation multiple
        assertEquals(false, premierSeance.confirmer(2)); // ne peut pas confirmer deux fois.
        assertEquals(false, premierSeance.confirmer(125));

        // cas non inscrit. ne peut pas confirmer un membre non inscrit
        assertEquals(false, premierSeance.confirmer(129));
        assertEquals(false, premierSeance.confirmer(130));

    }
}