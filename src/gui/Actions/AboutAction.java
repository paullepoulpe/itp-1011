package gui.Actions;

import java.awt.event.ActionEvent;

import javax.swing.*;

public class AboutAction extends AbstractAction {
	public AboutAction() {
		super("About");
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		JOptionPane
				.showMessageDialog(
						null,
						"This program was made for a computer science project, and does not qualify for everyday use!",
						"About the program", JOptionPane.INFORMATION_MESSAGE);
	}

}
