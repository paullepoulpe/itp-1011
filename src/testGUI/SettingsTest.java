/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package testGUI;

import settings.GeneralSettings;
import gui.FenetreSettings;

public class SettingsTest {
	public static void main(String[] args) {
		GeneralSettings.readFromFile();
		new FenetreSettings(null).showDialog();
	}
}
