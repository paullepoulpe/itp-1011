/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package testGUI;

import javax.swing.UIManager;

import settings.GeneralSettings;
import gui.FenetreSettings;

public class SettingsTest {
	public static void main(String[] args) {
		try{
			UIManager.setLookAndFeel(UIManager.getInstalledLookAndFeels()[3].getClassName());
		} catch (Exception e) {
			// TODO: handle exception
		}
		GeneralSettings.readFromFile();
		new FenetreSettings(null).showDialog();
	}
}
