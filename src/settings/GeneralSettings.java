package settings;

import java.awt.Color;
import java.io.File;

public class GeneralSettings {
	public static int NUMWANT = 50;
	public static boolean ENCRYPTION_ENABLED = true;
	public static int NB_MAX_PEERHANDLERS = 150;
	public static int NB_MAX_REQUESTS = 15;
	public static long PEER_RESPONSE_DELAY = 100;
	public static Color PROGRESS_COLOR = Color.ORANGE;
	public static File DOWNLOADING_FOLDER = new File(System
			.getProperty("user.home"), "Downloads");

	public GeneralSettings() {
		restoreDefaultValues();
	}

	public static void restoreDefaultValues() {
		NUMWANT = 50;
		ENCRYPTION_ENABLED = false;
		NB_MAX_PEERHANDLERS = 150;
		NB_MAX_REQUESTS = 15;
		PEER_RESPONSE_DELAY = 100;
		PROGRESS_COLOR = Color.ORANGE;
		DOWNLOADING_FOLDER = new File(System.getProperty("user.home"),
				"Downloads");
	}
}
