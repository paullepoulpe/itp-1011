package settings;

import gui.FenetreSettings;

import java.awt.Color;

public class GeneralSettings {
	public static int NUMWANT;
	public static boolean ENCRYPTION_ENABLED;
	public static int NB_MAX_PEERHANDLERS;
	public static int NB_MAX_REQUESTS;
	public static long PEER_RESPONSE_DELAY;
	public static Color PROGRESS_COLOR;

	public GeneralSettings() {
		restoreDefaultValues();
	}

	public void restoreDefaultValues() {
		NUMWANT = 50;
		ENCRYPTION_ENABLED = false;
		NB_MAX_PEERHANDLERS = 150;
		NB_MAX_REQUESTS = 15;
		PEER_RESPONSE_DELAY = 100;
		PROGRESS_COLOR = Color.ORANGE;
	}
}
