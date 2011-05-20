package settings;

import java.awt.Color;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;

public class GeneralSettings {
	public static Integer NUMWANT = 50;
	public static Boolean ENCRYPTION_ENABLED = false;
	public static Integer NB_MAX_PEERHANDLERS = 100;
	public static Integer NB_MAX_REQUESTS = 15;
	public static Integer MAX_NUM_OF_CURRENT_PIECES = 15;
	public static Long PEER_RESPONSE_DELAY = 100l;
	public static Color PROGRESS_COLOR = Color.ORANGE;
	public static File DOWNLOADING_FOLDER = new File(
			System.getProperty("user.home"), "Downloads");

	public GeneralSettings() {
		restoreDefaultValues();
	}

	public static void restoreDefaultValues() {
		NUMWANT = 50;
		ENCRYPTION_ENABLED = false;
		NB_MAX_PEERHANDLERS = 100;
		NB_MAX_REQUESTS = 15;
		MAX_NUM_OF_CURRENT_PIECES = 50;
		PEER_RESPONSE_DELAY = 100l;
		PROGRESS_COLOR = Color.ORANGE;
		DOWNLOADING_FOLDER = new File(System.getProperty("user.home"),
				"Downloads");
	}

	public static void writeOnFile() {
		File settingsFile = new File("settings.dat");
		if (settingsFile.exists()) {
			settingsFile.delete();
		}

		try {
			settingsFile.createNewFile();
		} catch (IOException e) {
			System.err
					.println("Probleme de sauvegarde des paramètres, le paramtètres par default seront chargés au prochain lancement du programme");
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
			parametres.put("PROGRESS_COLOR", PROGRESS_COLOR);
			parametres.put("DOWNLOADING_FOLDER", DOWNLOADING_FOLDER);
			out.writeObject(parametres);
		} catch (Exception e) {
			System.err
					.println("Probleme de sauvegarde des paramètres, le paramtètres par default seront chargés au prochain lancement du programme");
		}

	}

	public static void readFromFile() {
		File settingsFile = new File("settings.dat");
		if (!settingsFile.exists()) {
			System.err
					.println("Le fichier des parametres n'existe pas, les paramètres par defaut seront utilisés");
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
					PROGRESS_COLOR = (Color) parametres.get("PROGRESS_COLOR");
					DOWNLOADING_FOLDER = (File) parametres
							.get("DOWNLOADING_FOLDER");
				}
			} catch (Exception e) {
				System.err
						.println("Probleme lors de la lecture des anciens paramètres, les paramètres par defaut seront utilisés");
			}
		}
	}
}
