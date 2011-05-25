package gui.Actions;

import java.awt.event.ActionEvent;

import javax.swing.*;

/**
 * Cette action lorsqu'executee affiche une boite de dialogue qui affiche une
 * petite phrase d'information sur le programme.
 * 
 * @author Damien, Maarten
 * 
 */
public class AboutAction extends AbstractAction {
	public AboutAction() {
		super("About");
		putValue(SHORT_DESCRIPTION,
				"Shows a Dialog with some information about the program.");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JOptionPane
				.showMessageDialog(
						null,
						"This program was made for a computer science project, and does not qualify for everyday use!"
								+ "\n\nThis program may be open-source, but advanced programmers might get a BIG headache while reading the code... so beware" +
										"\n\nCreated by:        Damien Engels and Maarten Sap",
						"About the program", JOptionPane.INFORMATION_MESSAGE);
	}

}
