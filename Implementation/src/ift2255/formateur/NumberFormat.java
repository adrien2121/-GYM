package ift2255.formateur;

import java.text.DecimalFormat;

/**
 * Cette classe contient des methodes uniquement statiques pour permettre la conversion d'un int ou d'un double (selon
 * la methode appelee) en String selon un format demande. Pour le programme, les formats inclus sont ceux a trois
 * chiffres, sept chiffres, neuf chiffres et a deux decimales apres la virgule.
 */
public class NumberFormat {

    private static DecimalFormat deuxChif = new DecimalFormat("00"), // Trois chiffres.
                                 septChif = new DecimalFormat("0000000"), // Sept chiffres
                                 neufChif = new DecimalFormat("000000000"), // Neuf chiffres
                                 deuxDecimal = new DecimalFormat("#.##"); // Deux décimales

    /**
     * Cette methode permet de convertir un nombre ayant au plus 7 chiffres en String. Des zeros sont ajoutes au debut
     * si le nombre a moins de 7 chiffres.
     * @param n le nombre a convertir en String
     * @return Retourne un String avec possiblement des zeros en avant du chiffre.
     */
    public static String versSeptChar(int n) {
        return septChif.format(n);
    }

    /**
     * Cette methode permet de convertir un nombre ayant au plus 9 chiffres en String. Des zeros sont ajoutes au debut
     * si le nombre a moins de 9 chiffres.
     * @param n le nombre a convertir en String
     * @return Retourne un String avec possiblement des zeros en avant du chiffre.
     */
    public static String versNeufChar(int n) {
        return neufChif.format(n);
    }

    /**
     * Cette methode permet de convertir un nombre ayant au plus 3 chiffres en String. Des zeros sont ajoutes au debut
     * si le nombre a moins de 3 chiffres.
     * @param n le nombre a convertir en String
     * @return Retourne un String avec possiblement des zeros en avant du chiffre.
     */
    public static String versDeuxChar(int n) {
        return deuxChif.format(n);
    }

    /**
     * Cette methode permet de convertir un nombre ayant au plus 2 decimales en String.
     * @param n un double
     * @return Retourne le format String avec 2 decimales.
     */
    public static String versDeuxDecimal(double n) {
        return deuxDecimal.format(n);
    }

}
