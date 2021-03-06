import org.junit.Assert;
import org.junit.Test;
import torrent.Torrent;
import torrent.piece.Piece;
import torrent.tracker.TrackerInfo;

import java.io.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

public class TorrentTest2 {

	@Test
	public void testTorrentContents() throws IOException {
		Torrent torrent = new Torrent(new File("data/LePetitPrince.torrent"));

		assertEquals("st_exupery_le_petit_prince.pdf", torrent.getMetainfo()
				.getFileName());
		assertEquals(
				"Le petit prince est l'oeuvre la plus connue d'Antoine de Saint-Exupery",
				torrent.getMetainfo().getComment());
		assertEquals("Transmission/2.20b3 (11793)", torrent.getMetainfo()
				.getCreatedBy());
		assertEquals(40, torrent.getPieces().length);

		Piece[] pieces = torrent.getPieces();
		assertNotNull(pieces);
		// assertFalse(pieces.isEmpty());
		assertEquals(40, pieces.length);

		torrent.massAnnounce();

		TrackerInfo[] trackers = torrent.getTrackers();
		assertNotNull(trackers);
		// assertFalse(trackers.isEmpty());
		assertEquals(2, trackers.length);
		assertEquals("http://icsinsrv1.epfl.ch:6969/", trackers[0]
				.getUrlAnnounce());
		assertEquals("http://127.0.0.1:6969/", trackers[1].getUrlAnnounce());
	}

	@Test
	public void testReadFromFileGivenDataFile() throws IOException {
		Torrent torrent = new Torrent(new File("data/LePetitPrince.torrent"));
		// Torrent torrent = new Torrent(new File("data/LePetitPrince.torrent"),
		// new File("data/st_exupery_le_petit_prince.pdf"), false);
		// torrent.setWritingEnabled(false);
		//
		// assertTrue(torrent.isWritten());
		torrent.readFromFile();
		assertEquals("All pieces must have been read from the data file",
				100.0, torrent.getCompleteness(), 0.0);
		assertTrue("All pieces must have been read from the data file", torrent
				.isComplete());
	}

	@Test
	public void testReadFromFileNoDataFile() throws IOException {
		Torrent torrent = new Torrent(new File("data/LePetitPrince.torrent"));
		// torrent.setWritingEnabled(false);

		// assertFalse(torrent.isWritten());

		assertEquals("No pieces should have been read", 0.0, torrent
				.getCompleteness(), 0.0);
		assertFalse("No pieces should have been read", torrent.isComplete());
	}

	// @Test
	// public void testReadFromFileWrongDataFile() throws IOException {
	// Torrent torrent = new Torrent(new File("data/LePetitPrince.torrent"),
	// new File("data/LePetitPrince.torrent"), false);
	// torrent.setWritingEnabled(false);
	//
	// assertFalse(torrent.isWritten());
	//
	// assertEquals("No pieces should have been read", 0.0, torrent
	// .getCompleteness(), 0.0);
	// assertFalse("No pieces should have been read", torrent.isComplete());
	// }

	@Test
	public void testWriteToFile() throws IOException, NoSuchAlgorithmException {
		Torrent torrent = new Torrent(new File("data/LePetitPrince.torrent"));
		// torrent.setWritingEnabled(true);

		int pieceLength = torrent.getPieces()[0].getSizeTab();
		int pieceCount = torrent.getPieces().length;

		List<Integer> indices = new ArrayList<Integer>();
		for (int i = 0; i < pieceCount; i++) {
			indices.add(i);
		}
		Collections.shuffle(indices);
		Assert.assertEquals(pieceCount, indices.size());
		Assert.assertEquals(pieceLength, 1 << 15);

		RandomAccessFile file = new RandomAccessFile(
				"data/st_exupery_le_petit_prince.pdf", "r");
		for (int index : indices) {
			Piece piece = torrent.getPieces()[index];

			// do the second block first, if any
			int secondBlockSize = piece.getSizeTab() - Piece.BLOCK_SIZE;
			if (secondBlockSize > 0) {
				file.seek(index * pieceLength + Piece.BLOCK_SIZE);
				byte[] block = new byte[secondBlockSize];
				file.read(block);
				piece.feed(Piece.BLOCK_SIZE, block);
			}

			// now do the first block
			{
				int firstBlockSize = Math.min(Piece.BLOCK_SIZE, piece
						.getSizeTab());
				file.seek(index * pieceLength);
				byte[] block = new byte[firstBlockSize];
				file.read(block);
				piece.feed(0, block);
			}
		}
		file.close();

		File outputFile = File.createTempFile(
				"test_st_exupery_le_petit_prince", ".pdf");
		outputFile.deleteOnExit();

		torrent.writeToFile();
		assertTrue("The written file must be identical to the data file",
				checkMD5checksum("0D41D08C0D908F000B2040E9080090980EC0F8427E",
						outputFile));

	}

	private boolean checkMD5checksum(String original, File file)
			throws NoSuchAlgorithmException, IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		InputStream is = new FileInputStream(file);
		try {
			is = new DigestInputStream(is, md);
		} finally {
			is.close();
		}

		return original.equals(toHexString(md.digest()));
	}

	private String toHexString(byte[] input) {
		StringBuilder sb = new StringBuilder();
		for (byte b : input) {
			if (b < 16)
				sb.append("0");
			sb.append(Integer.toHexString(0xFF & b).toUpperCase());
		}
		return sb.toString();
	}
}
