package ift2255.Pays;

/**
 * Cette classe statique encapsule en particulier les codes des provinces canadiennes. Peut aussi stocker autres donnees
 * necessaires.
 */
public class Canada {

    private static String[] codeProvinces = {"AB", "BC", "ON", "MB", "NB",
            "NS", "PE", "SK", "NL", "QC"};

    /**
     * Cette methode determine si le String envoyee correspondant a un des codes de province.
     * @param str un String
     * @return Retourne un booleen.
     */
    public static boolean isProvinceCanadienCode(String str) {

        if (str.length() > 2) return false; // Si plus que 2 caractères.

        // Trouver si le string est égale à un des codes de province.
        for (int i = 0; i < codeProvinces.length; i++) {
            if (codeProvinces[i].equals(str)) {
                return true;
            }
        }

        // Sinon, il ne correspond à aucun code de province.
        return false;
    }

    /**
     * Cette methode retourne l'affichage des codes de province et le nom de la province approprie.
     * @return Retourne un String pour l'affichage.
     */
    public static String affichageCode() {
        return "Voici les codes des provinces: \n\n" +
                "   AB - Alberta\n"+
                "   BC - Colombie-Britannique / British Columbia\n" +
                "   MB - Manitoba\n" +
                "   NB - Nouveau-Brunswick / New Brunswick\n" +
                "   NL - Terre-Neuve-et-Labrador / Newfoundland and Labrador\n" +
                "   NS - Nouvelle-Écosse / Nova Scotia\n" +
                "   ON - Ontario\n" +
                "   PE - Île-du-Prince-Édouard / Prince Edward Island\n" +
                "   QC - Québec / Quebec\n" +
                "   SK - Saskatchewan";
    }
}
