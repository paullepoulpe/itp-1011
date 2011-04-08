package test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;

import torrent.Torrent;
import torrent.messages.*;

public class BitfieldTest {
	public static void main(String[] args) {
		BitField monBF = new BitField(new Torrent(new File(
				"data/eminem.torrent")));
		Serveur serv = new Serveur(12345);
		serv.start();
		Socket socket = null;
		DataOutputStream output = null;
		boolean connect = false;
		while (!connect) {
			try {
				socket = new Socket(InetAddress.getByName("127.0.0.1"), 12345);
				output = new DataOutputStream(socket.getOutputStream());
				connect = true;
			} catch (IOException e) {
				connect = false;
				socket = null;
			}
		}
		monBF.send(output);

	}
}

class Serveur extends Thread {
	int port;

	public Serveur(int port) {
		this.port = port;
	}

	public void run() {
		try {
			ServerSocket socket = new ServerSocket(this.port);
			Socket sock = socket.accept();
			DataInputStream input = new DataInputStream(sock.getInputStream());
			while (true) {
				if (input.available() > 0) {
					Message message = new MessageReader(input).readMessage();
					try {
						System.out.println(Arrays.toString(((BitField) message)
								.getPosessedPieces()));
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}
}
