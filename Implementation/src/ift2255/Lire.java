package ift2255;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Cette classe sert a prendre le chemin vers un fichier qu'on peut lire et lit les contenus du fichier ligne par ligne.
 * Chaque ligne est stockee dans un tableau dynamique, puis ce dernier est retourne via la methode lireFichier().
 */
public class Lire {

	/**
	 * Cette methode prend un chemin vers un fichier a lire en String et decoupe le contenu du fichier par ligne pour
	 * les
	 * mettre dans un tableau dynamique.
	 * @param path le chemin vers le fichier a lire en String
	 * @return un ArrayList<String> des lignes du fichier.
	 */
	public static ArrayList<String> lireFichier(String path) throws IOException {

		ArrayList<String> tokens = new ArrayList<>();

		@SuppressWarnings("resource")
		BufferedReader reader = new BufferedReader(new FileReader(path));
		String line;

		while((line = reader.readLine()) != null){
			tokens.add(line);
		}

		return tokens;
	}
}
