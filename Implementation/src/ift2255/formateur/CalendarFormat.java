package ift2255.formateur;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Cette classe statique permet de convertir des dates en String vers des objets Calendar et vice versa.
 * Le format d'une date en String est defini dès le debut. Le programmeur/developpeur est invite à le modifier si
 * necessaire.
 */
public class CalendarFormat {

    private final static SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy"),
                                          formatDateHeure = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss"),
                                          formatHeure = new SimpleDateFormat("HH:mm:ss"),
                                          formatHeureSansSec = new SimpleDateFormat("HH:mm");

    /**
     * Cette methode retourne un String selon un objet Calendar en format JJ-MM-YYYY
     * @param calDate un instance Calendar
     * @return Retourne un String.
     */
    public static String dateToStr(Calendar calDate) {
        return formatDate.format(calDate.getTime()); // Retourne "JJ-MM-YYYY"
    }

    /**
     * Cette methode retourne un String qui represente un objet Calendar par le format specifie par l'attribut
     * formateurTemps ci-dessous.
     * @param date un objet Calendar qui contient en particulier une date et un temps HH:MM:SS
     * @return String representant l'heure et la date.
     */
    public static String dateHeureToStr(Calendar date) {
        return formatDateHeure.format(date.getTime()); // Retourne "JJ-MM-YYYY HH:MM:SS"
    }

    /**
     * Cette methode va parser une date en String en format JJ-MM-AAAA en un instance de Calendar.
     * @param date un String en format JJ-MM-AAAA
     * @return un instance de Calendar
     */
    public static Calendar getCalendarObj(String date) {

        String[] dateStr = date.split("-");
        int year = Integer.parseInt(dateStr[2]),
                month = Integer.parseInt(dateStr[1]) - 1, // Jan = 0, Feb = 1, ..., Dec = 11
                day = Integer.parseInt(dateStr[0]);

        return new GregorianCalendar(year, month, day);
    }

    /**
     * Convertir HH:MM en un Calendar object qui a comme but seul de representer l'heure. Le format la discretion du
     * developpeur.
     * @param heure un string en format HH:MM
     * @return Retourne un objet Calendar qui represente uniquement l'heure et les minutes passes en parametre.
     */
    public static Calendar getCalObjPourHeure(String heure) {
        String[] heureStr = heure.split(":");
        int heure24 = Integer.parseInt(heureStr[0]),
                minutes = Integer.parseInt(heureStr[1]);

        return new GregorianCalendar(0, 0, 0, heure24, minutes);
    }

    /**
     * Methode permet representer l'heure et les minutes d'un objet Calendar en String en format 24h
     * @param date l'objet Calendar d'ou on extrait l'heure et la minute.
     * @return Retourne un String qui represente l'heure et les minutes de l'objet envoye en parametre.
     */
    public static String heureToStr(Calendar date) {
        return formatHeure.format(date.getTime());
    }

    /**
     * Methode permet representer l'heure, les minutes et les secondes d'un objet Calendar en String en format 24h.
     * @param date l'objet Calendar d'ou on extrait l'heure, la minute et les secondes.
     * @return Retourne un String qui represente l'heure, les minutes et les secondes de l'objet envoye en parametre.
     */
    public static String heureSansSecToStr(Calendar date) {
        return formatHeureSansSec.format(date.getTime());
    }

    /**
     * Cette methode permet de recuperer la valeur int associee au jour de la semaine qui est envoye en parametre
     * comme un String.
     *
     * Pour le programme,
     *<pre>
     *      Lu = lundi
     *      Ma = mardi
     *      Me = mercredi
     *      Je = jeudi
     *      Ve = vendredi
     *</pre>
     * @param day le jour de la semaine en String dans l'acronyme qui lui a ete donne.
     * @return Retourne un int qui represente le jour de la semaine selon ce qu'a ete defini pour la classe Calendar de
     * java.
     * @see Calendar
     */
    public static int getDayOfWeekInt(String day) {
        switch(day) {
            case "Lu": return Calendar.MONDAY;
            case "Ma": return Calendar.TUESDAY;
            case "Me": return Calendar.WEDNESDAY;
            case "Je": return Calendar.THURSDAY;
            case "Ve": return Calendar.FRIDAY;
            default: return -1;
        }
    }

    /**
     * Cette methode permet de calculer le nombre de jours entre deux dates exclusivement en prenant
     * leur temps en millisecondes depuis un certain jour precise dans la documentation de Calendar.
     *
     * La difference en millisecondes est ensuite divisee par le nombre de secondes par jour.
     *
     * @param debut la date de debut representee dans l'objet Calendar
     * @param fin la date de fin representee dans l'objet Calendar
     * @return Retourne le nombre de jours entre deux dates inclusivement.
     */
    public static int daysBetween(Calendar debut, Calendar fin) {
        return (int) ( (fin.getTimeInMillis() - debut.getTimeInMillis()) / (1000 * 60 * 60 * 24));
    }

    public static void main(String[] args) {
        Calendar now = new GregorianCalendar();
        now.set(2019, Calendar.JULY, 22, 20, 00);
        System.out.println(daysBetween(Calendar.getInstance(), now));


    }
}
