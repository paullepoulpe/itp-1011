/*
 *	Author:      Damien Engels
 *	Date:        17.10.2010
 */

package gui;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.swing.*;
import javax.swing.border.TitledBorder;

import settings.GeneralSettings;

/**
 * Cette classe est une boite de dialogue permettant de changer les parametres
 * de /src/settings/GeneralSettings.java
 * 
 * @author Damien, Maarten
 * 
 */
public class FenetreSettings extends JDialog implements ActionListener,
		WindowListener {
	private JTextField numwant = new JTextField(),
			nbMaxPeerHandler = new JTextField(),
			nbMaxRequests = new JTextField(),
			peerResponseDelay = new JTextField(),
			downloadFolderPath = new JTextField();
	private JCheckBox encryptionEnabled = new JCheckBox();
	private Color[] couleursValues = { Color.ORANGE, Color.BLUE, Color.RED,
			Color.GRAY, Color.GREEN };
	private String[] couleursNoms = { "Orange", "Blue", "Red", "Gray", "Green" };
	private JComboBox couleurs = new JComboBox(couleursNoms);
	private JButton valider = new JButton("Save"),
			restoreDefault = new JButton("Default Values"),
			browse = new JButton("Browse");

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

	/**
	 * Cette methode s'occupe de mettre en place tous les composants afin
	 * d'avoir une certaine ergonomie
	 */
	private void arrange() {
		JPanel principal = new JPanel(new BorderLayout(20, 0));
		this.setContentPane(principal);

		JPanel fields1 = new JPanel(new BorderLayout());
		JPanel fields2 = new JPanel(new BorderLayout());
		JPanel choixDossier = new JPanel(new BorderLayout());
		JPanel param = new JPanel(new GridLayout(0, 2, 5, 5));
		JPanel encrypt = new JPanel(new GridLayout(0, 2, 5, 5));
		JPanel visual = new JPanel(new GridLayout(0, 2, 5, 5));
		// destination folder
		choixDossier.setBorder(new TitledBorder(
				"Choose default downloading folder"));
		choixDossier.add(downloadFolderPath, BorderLayout.CENTER);
		choixDossier.add(browse, BorderLayout.EAST);
		fields1.add(choixDossier);
		// parameters
		param.setBorder(new TitledBorder("Download settings"));
		param.add(new Label("Requested number of peers for per tracker"));
		param.add(numwant);

		param.add(new Label("Maximum number of active peers"));
		param.add(nbMaxPeerHandler);

		param.add(new Label("Maximum number of simultaneous bloc requests"));
		param.add(nbMaxRequests);

		param.add(new Label("Maximum connexion delay for a peer"));
		param.add(peerResponseDelay);
		// Encryption
		encrypt.setBorder(new TitledBorder("Encryption parameters"));
		encrypt.add(new Label("Encryption"));
		encrypt.add(encryptionEnabled);
		// visual
		visual.setBorder(new TitledBorder("Interface parameters"));
		visual.add(new Label("Downloadbar color"));
		visual.add(couleurs);
		// Buttons
		JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		boutons.add(restoreDefault);
		boutons.add(valider);

		fields1.add(choixDossier, BorderLayout.NORTH);
		fields1.add(param, BorderLayout.CENTER);
		fields1.add(encrypt, BorderLayout.SOUTH);
		fields2.add(fields1,BorderLayout.CENTER);
		fields2.add(visual, BorderLayout.SOUTH);
	

		principal.add(fields2, BorderLayout.CENTER);
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
			GeneralSettings.restoreDefaultValues();
		}
		if (source == valider && getValues()) {
			close();
		}

		setVisual();
	}

	/**
	 * Cette methode s'occupe de lire les dopnnées dans les champs de texte et
	 * autre composants tout en indiquant s'il y a un probleme de compatibilite
	 * des valeurs
	 * 
	 * @return true si une ou plusieurs donnees ne sont pas dans le bon format,
	 *         false sinon
	 */
	private boolean getValues() {
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
			GeneralSettings.PEER_RESPONSE_DELAY = Long
					.parseLong(peerResponseDelay.getText());
		} catch (Exception e) {
			probleme = true;
		}

		GeneralSettings.ENCRYPTION_ENABLED = encryptionEnabled.isSelected();

		GeneralSettings.PROGRESS_COLOR = couleursValues[couleurs
				.getSelectedIndex()];

		GeneralSettings.writeOnFile();

		if (probleme) {
			JOptionPane.showConfirmDialog(this,
					"Plusieurs champs ont ete mal remplis", "Attention",
					JOptionPane.DEFAULT_OPTION, JOptionPane.ERROR_MESSAGE);
		}
		return !probleme;

	}

	/**
	 * Cette methode entre les donnes par defaut dans les champs de texte et les
	 * composants, pour que l'utilisateur puisse voir ce qu'elles sont
	 */
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
