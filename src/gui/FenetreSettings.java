/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.List;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.util.Arrays;
import java.util.Comparator;

import javax.swing.*;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import settings.GeneralSettings;

public class FenetreSettings extends JDialog implements ActionListener,
		WindowListener {
	private GeneralSettings settings = new GeneralSettings();
	private JTextField numwant = new JTextField(),
			nbMaxPeerHandler = new JTextField(),
			nbMaxRequests = new JTextField(),
			peerResponseDelay = new JTextField(),
			downloadFolderPath = new JTextField();
	private JCheckBox encryptionEnabled = new JCheckBox();
	private Color[] couleursValues = { Color.ORANGE, Color.BLUE, Color.RED,
			Color.GRAY, Color.GREEN };
	private String[] couleursNoms = { "Orange", "Bleu", "Rouge", "Gris", "Vert" };
	private JComboBox couleurs = new JComboBox(couleursNoms);
	private JButton valider = new JButton("Valider"),
			restoreDefault = new JButton("Valeurs par default"),
			browse = new JButton("Choisir...");

	public FenetreSettings(JFrame f) {
		super(f, "Parametres");
		this.setLocationRelativeTo(f);
		this.setResizable(false);
		this.setAlwaysOnTop(true);
		this.setEnabled(false);
		this.addWindowListener(this);
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		valider.addActionListener(this);
		restoreDefault.addActionListener(this);
		browse.addActionListener(this);

		arrange();
	}

	private void arrange() {
		JPanel principal = new JPanel(new BorderLayout(20, 0));
		this.setContentPane(principal);

		JPanel param = new JPanel(new GridLayout(0, 2));

		param.add(new Label("Choisir le dossier de telechargement par default"));
		JPanel choixDossier = new JPanel(new BorderLayout());
		choixDossier.add(downloadFolderPath, BorderLayout.WEST);
		choixDossier.add(browse, BorderLayout.EAST);
		param.add(choixDossier);

		param.add(new Label("Parametre numwant pour les trackers"));
		param.add(numwant);

		param.add(new Label("Nombre maximum de pairs actifs"));
		param.add(nbMaxPeerHandler);

		param.add(new Label("Nombre maximum de requetes de blocs simultanees"));
		param.add(nbMaxRequests);

		param.add(new Label("Delai avant de couper la connection avec un pair"));
		param.add(peerResponseDelay);

		param.add(new Label("Enclencher l'encryption"));
		param.add(encryptionEnabled);

		param.add(new Label("Couleur des barres de telechargement"));
		param.add(couleurs);

		JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));

		boutons.add(restoreDefault);
		boutons.add(valider);

		principal.add(param, BorderLayout.NORTH);
		principal.add(boutons, BorderLayout.SOUTH);

		setVisual();
		this.pack();

	}

	public void showDialog() {
		this.getOwner().setEnabled(false);
		this.setEnabled(true);
		this.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		Object source = e.getSource();
		if (source == browse) {
			JFileChooser fileChooser = new JFileChooser(
					GeneralSettings.DOWNLOADING_FOLDER);
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			int i = fileChooser.showDialog(this, "Choisir!");
			if (i == JFileChooser.APPROVE_OPTION) {
				GeneralSettings.DOWNLOADING_FOLDER = fileChooser
						.getSelectedFile();
			}
		}
		if (source == restoreDefault) {
			settings.restoreDefaultValues();
		}
		if (source == valider) {
			getValues();
			close();
		}

		setVisual();
	}

	private void getValues() {
		boolean probleme = false;

		try {
			File file = new File(downloadFolderPath.getText());
			if (file.isDirectory() && file.exists()) {
				GeneralSettings.DOWNLOADING_FOLDER = file;
			} else {
				probleme = true;
			}
		} catch (Exception e) {
			probleme = true;
		}

		try {
			GeneralSettings.NUMWANT = Integer.parseInt(numwant.getText());
		} catch (Exception e) {
			probleme = true;
		}
		try {
			GeneralSettings.NB_MAX_PEERHANDLERS = Integer
					.parseInt(nbMaxPeerHandler.getText());
		} catch (Exception e) {
			probleme = true;
		}
		try {
			GeneralSettings.NB_MAX_REQUESTS = Integer.parseInt(nbMaxRequests
					.getText());
		} catch (Exception e) {
			probleme = true;
		}
		try {
			GeneralSettings.PEER_RESPONSE_DELAY = Integer
					.parseInt(peerResponseDelay.getText());
		} catch (Exception e) {
			probleme = true;
		}

		GeneralSettings.ENCRYPTION_ENABLED = encryptionEnabled.isSelected();

		GeneralSettings.PROGRESS_COLOR = couleursValues[couleurs
				.getSelectedIndex()];

		if (probleme) {
			JOptionPane.showConfirmDialog(this,
					"Plusieurs champs ont ete mal remplis", "Attention",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
		}

	}

	private void setVisual() {
		downloadFolderPath.setText(GeneralSettings.DOWNLOADING_FOLDER
				.getAbsolutePath());
		numwant.setText(GeneralSettings.NUMWANT + "");
		nbMaxPeerHandler.setText(GeneralSettings.NB_MAX_PEERHANDLERS + "");
		nbMaxRequests.setText(GeneralSettings.NB_MAX_REQUESTS + "");
		peerResponseDelay.setText(GeneralSettings.PEER_RESPONSE_DELAY + "");

		encryptionEnabled.setSelected(GeneralSettings.ENCRYPTION_ENABLED);

		Color couleurCourante = GeneralSettings.PROGRESS_COLOR;
		int index = 0;
		for (index = 0; index < couleursValues.length; index++) {
			if (couleursValues[index].equals(couleurCourante)) {
				break;
			}
		}
		couleurs.setSelectedIndex(index);

	}

	public void close() {
		this.getOwner().setEnabled(true);
		this.setVisible(false);
		this.setEnabled(false);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		int n = JOptionPane
				.showConfirmDialog(
						this,
						"Si vous fermez cette fenetre, les parametres ne seront pas sauvegardes !",
						"Attention", JOptionPane.OK_CANCEL_OPTION);
		if (n == JOptionPane.OK_OPTION) {
			close();
		} else {

		}

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent arg0) {
		// TODO Auto-generated method stub

	}

}
