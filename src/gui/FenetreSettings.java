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

import com.sun.org.apache.bcel.internal.generic.NEW;

import settings.GeneralSettings;

/**
 * Cette classe est une boite de dialogue permettant de changer les parametres
 * de {@link GeneralSettings}
 * 
 * @author Damien, Maarten
 * 
 */
public class FenetreSettings extends JDialog implements ActionListener,
		WindowListener, ItemListener {
	private JTextField numwant = new JTextField(),
			nbMaxPeerHandler = new JTextField(),
			nbMaxRequests = new JTextField(),
			sizePiecesOfInterest = new JTextField(),
			peerResponseDelay = new JTextField(),
			downloadFolderPath = new JTextField(),
			rsaKeySize = new JTextField(), symmetricKeySize = new JTextField();
	private JCheckBox encryptionEnabled = new JCheckBox();
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
		encryptionEnabled.addItemListener(this);
		rsaKeySize.setEnabled(encryptionEnabled.isSelected());
		symmetricKeySize.setEnabled(encryptionEnabled.isSelected());
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
		// destination folder
		choixDossier.setBorder(new TitledBorder(
				"Choose default downloading folder"));
		choixDossier.add(downloadFolderPath, BorderLayout.CENTER);
		choixDossier.add(browse, BorderLayout.EAST);
		fields1.add(choixDossier);
		// parameters
		param.setBorder(new TitledBorder("Download settings"));

		param.add(new JLabel("Maximum number of downloading pieces"));
		param.add(sizePiecesOfInterest);

		param.add(new JLabel("Requested number of peers for per tracker"));
		param.add(numwant);

		param.add(new JLabel("Maximum number of active peers"));
		param.add(nbMaxPeerHandler);

		param.add(new JLabel("Maximum number of simultaneous bloc requests"));
		param.add(nbMaxRequests);

		param.add(new JLabel("Maximum connexion delay for a peer"));
		param.add(peerResponseDelay);
		// Encryption
		encrypt.setBorder(new TitledBorder("Encryption parameters"));
		encrypt.add(new JLabel("Encryption"));
		encrypt.add(encryptionEnabled);
		encrypt.add(new JLabel("RSA Key Size"));
		encrypt.add(rsaKeySize);
		encrypt.add(new JLabel("Symmetric Key Size"));
		encrypt.add(symmetricKeySize);
		// Buttons
		JPanel boutons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		boutons.add(restoreDefault);
		boutons.add(valider);

		fields1.add(choixDossier, BorderLayout.NORTH);
		fields1.add(param, BorderLayout.CENTER);
		fields1.add(encrypt, BorderLayout.SOUTH);
		fields2.add(fields1, BorderLayout.CENTER);

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
			GeneralSettings.MAX_NUM_OF_CURRENT_PIECES = Integer
					.parseInt(sizePiecesOfInterest.getText());
			GeneralSettings.NB_MAX_REQUESTS = Integer.parseInt(nbMaxRequests
					.getText());
			GeneralSettings.RSA_KEY_SIZE = Integer.parseInt(rsaKeySize
					.getText());
			GeneralSettings.SYMMETRIC_KEY_SIZE = Integer
					.parseInt(symmetricKeySize.getText());
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

		GeneralSettings.writeOnFile();

		if (probleme) {
			JOptionPane.showConfirmDialog(this,
					"Several fields are not filled correctly", "Caution",
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
		sizePiecesOfInterest.setText(GeneralSettings.MAX_NUM_OF_CURRENT_PIECES
				+ "");

		encryptionEnabled.setSelected(GeneralSettings.ENCRYPTION_ENABLED);
		rsaKeySize.setText(GeneralSettings.RSA_KEY_SIZE + "");
		symmetricKeySize.setText(GeneralSettings.SYMMETRIC_KEY_SIZE + "");

	}

	public void close() {
		this.getOwner().setEnabled(true);
		this.setVisible(false);
		this.setEnabled(false);
	}

	@Override
	public void windowActivated(WindowEvent arg0) {
	}

	@Override
	public void windowClosed(WindowEvent arg0) {
	}

	@Override
	public void windowClosing(WindowEvent e) {
		int n = JOptionPane.showConfirmDialog(this,
				"If you close this dialog, your settings will not be saved!",
				"Caution", JOptionPane.OK_CANCEL_OPTION);
		if (n == JOptionPane.OK_OPTION) {
			close();
		} else {

		}

	}

	@Override
	public void windowDeactivated(WindowEvent arg0) {
	}

	@Override
	public void windowDeiconified(WindowEvent arg0) {
	}

	@Override
	public void windowIconified(WindowEvent arg0) {
	}

	@Override
	public void windowOpened(WindowEvent arg0) {
	}

	@Override
	public void itemStateChanged(ItemEvent arg0) {
		boolean enabled = encryptionEnabled.isSelected();
		rsaKeySize.setEnabled(enabled);
		symmetricKeySize.setEnabled(enabled);
	}

}
