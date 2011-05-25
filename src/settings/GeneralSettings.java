package settings;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

import javax.swing.UIDefaults;

/**
 * Cette classe encapsule les diverses "constantes" utilisees dans le
 * telechargement des torrents ainsi que les differentes options visuelles.
 * 
 * @author Damien, Maarten
 * 
 */
public class GeneralSettings {
	public static Integer NUMWANT = 50;
	public static Boolean ENCRYPTION_ENABLED = false;
	public static Integer NB_MAX_PEERHANDLERS = 100;
	public static Integer NB_MAX_REQUESTS = 15;
	public static Integer MAX_NUM_OF_CURRENT_PIECES = 15;
	public static Long PEER_RESPONSE_DELAY = 1000l;
	public static Integer SYMMETRIC_KEY_SIZE = 128;
	public static Integer RSA_KEY_SIZE = 128;
	public static File DOWNLOADING_FOLDER = new File(System
			.getProperty("user.home"), "Downloads");

	public GeneralSettings() {
		restoreDefaultValues();
	}

	/**
	 * Methode permettant de mettre tous les champs a leur valeur par defaut
	 */
	public static void restoreDefaultValues() {
		NUMWANT = 50;
		ENCRYPTION_ENABLED = false;
		NB_MAX_PEERHANDLERS = 100;
		NB_MAX_REQUESTS = 15;
		MAX_NUM_OF_CURRENT_PIECES = 50;
		PEER_RESPONSE_DELAY = 100l;
		SYMMETRIC_KEY_SIZE = 128;
		RSA_KEY_SIZE = 128;
		DOWNLOADING_FOLDER = new File(System.getProperty("user.home"),
				"Downloads");
	}

	/**
	 * Cette methode permet de sauvegarder les parametres personalises sur le
	 * disque dans un fichier "settings.dat". Les donnes sont stockees sous
	 * forme de {@link HashMap}shMap
	 */
	public static void writeOnFile() {
		File settingsFile = new File("settings.dat");
		if (settingsFile.exists()) {
			settingsFile.delete();
		}

		try {
			settingsFile.createNewFile();
		} catch (IOException e) {
			System.err
					.println("Problem encountered while writing settings on disk. Default values will be used on startup.");
		}

		try {
			ObjectOutputStream out = new ObjectOutputStream(
					new FileOutputStream(settingsFile, false));
			HashMap<String, Object> parametres = new HashMap<String, Object>();
			parametres.put("NUMWANT", NUMWANT);
			parametres.put("ENCRYPTION_ENABLED", ENCRYPTION_ENABLED);
			parametres.put("NB_MAX_PEERHANDLERS", NB_MAX_PEERHANDLERS);
			parametres.put("NB_MAX_REQUESTS", NB_MAX_REQUESTS);
			parametres.put("MAX_NUM_OF_CURRENT_PIECES",
					MAX_NUM_OF_CURRENT_PIECES);
			parametres.put("PEER_RESPONSE_DELAY", PEER_RESPONSE_DELAY);
			parametres.put("SYMMETRIC_KEY_SIZE", SYMMETRIC_KEY_SIZE);
			parametres.put("RSA_KEY_SIZE", RSA_KEY_SIZE);
			parametres.put("DOWNLOADING_FOLDER", DOWNLOADING_FOLDER);
			out.writeObject(parametres);
		} catch (Exception e) {
			System.err
					.println("Probleme de sauvegarde des parametres, le paramtetres par default seront charges au prochain lancement du programme");
		}

	}

	/**
	 * Si un fichier de parametres personalises existe, cette methode va les
	 * lire.
	 */
	public static void readFromFile() {
		File settingsFile = new File("settings.dat");
		if (!settingsFile.exists()) {
			System.err
					.println("There is no settings file, default values will be used");
		} else {

			try {
				ObjectInputStream in = new ObjectInputStream(
						new FileInputStream(settingsFile));
				Object object = in.readObject();
				if (object instanceof HashMap<?, ?>) {
					HashMap<String, Object> parametres = (HashMap<String, Object>) object;
					NUMWANT = (Integer) parametres.get("NUMWANT");
					ENCRYPTION_ENABLED = (Boolean) parametres
							.get("ENCRYPTION_ENABLED");
					NB_MAX_PEERHANDLERS = (Integer) parametres
							.get("NB_MAX_PEERHANDLERS");
					NB_MAX_REQUESTS = (Integer) parametres
							.get("NB_MAX_REQUESTS");
					MAX_NUM_OF_CURRENT_PIECES = (Integer) parametres
							.get("MAX_NUM_OF_CURRENT_PIECES");
					PEER_RESPONSE_DELAY = (Long) parametres
							.get("PEER_RESPONSE_DELAY");
					DOWNLOADING_FOLDER = (File) parametres
							.get("DOWNLOADING_FOLDER");
					RSA_KEY_SIZE = (Integer) parametres.get("RSA_KEY_SIZE");
					SYMMETRIC_KEY_SIZE = (Integer) parametres
							.get("SYMMETRIC_KEY_SIZE");
				}
			} catch (Exception e) {
				System.err
						.println("Problem while reading settings file. Default values will be used.");
				restoreDefaultValues();
			}
		}
	}

}
