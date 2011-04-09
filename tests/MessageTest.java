import java.io.*;
import java.util.Arrays;

import org.junit.*;

import static org.junit.Assert.*;
import torrent.Torrent;
import torrent.messages.*;

public class MessageTest {

	private static DataPipe myPipe;

	@Before
	public void initPipe() {
		myPipe = new DataPipe();
	}

	@Test
	public void testHandshake() {
		Handshake envoi = new Handshake(new Torrent(new File(
				"data/LePetitPrince.torrent")));
		envoi.send(myPipe.getOutput());

		Handshake recu = new Handshake(myPipe.getInput());
		assertTrue(envoi.equals(recu));
	}

	@Test
	public void testChoke() {
		Choke envoi = new Choke();
		envoi.send(myPipe.getOutput());

		Message recu = new MessageReader(myPipe.getInput()).readMessage();
		assertEquals(envoi.getClass(), recu.getClass());
	}

	@Test
	public void testUnchoke() {
		Unchoke envoi = new Unchoke();
		envoi.send(myPipe.getOutput());

		Message recu = (Unchoke) new MessageReader(myPipe.getInput())
				.readMessage();
		assertEquals(envoi.getClass(), recu.getClass());
	}

	@Test
	public void testInterested() {
		Interested envoi = new Interested();
		envoi.send(myPipe.getOutput());

		Message recu = new MessageReader(myPipe.getInput()).readMessage();
		assertEquals(envoi.getClass(), recu.getClass());
	}

	@Test
	public void testNotInterested() {
		NotInterested envoi = new NotInterested();
		envoi.send(myPipe.getOutput());

		Message recu = new MessageReader(myPipe.getInput()).readMessage();
		assertEquals(envoi.getClass(), recu.getClass());
	}

	@Test
	public void testHave() {
		Have envoi = new Have((int) (Math.random() * Integer.MAX_VALUE));
		envoi.send(myPipe.getOutput());

		Message recu = new MessageReader(myPipe.getInput()).readMessage();
		assertEquals(envoi.getClass(), recu.getClass());
		assertTrue(envoi.equals((Have) recu));
	}

	@Test
	public void testBitField() {
		BitField envoi = new BitField(new Torrent(new File(
				"data/LePetitPrince.torrent")));
		envoi.send(myPipe.getOutput());

		try {
			Message recu = new MessageReader(myPipe.getInput()).readMessage();
			assertEquals(envoi.getClass(), recu.getClass());
			assertTrue(envoi.equals((BitField) recu));
			assertFalse(envoi.hasNoPieces());
		} catch (Exception e) {
			assertTrue(envoi.hasNoPieces());
		}

	}

	@Test
	public void testRequest() {
		Request envoi = new Request((int) (Math.random() * Integer.MAX_VALUE),
				(int) (Math.random() * Integer.MAX_VALUE),
				(int) (Math.random() * Integer.MAX_VALUE));
		envoi.send(myPipe.getOutput());

		Message recu = new MessageReader(myPipe.getInput()).readMessage();
		assertEquals(envoi.getClass(), recu.getClass());
		assertTrue(envoi.equals((Request) recu));
	}

	@Test
	public void testSendBlock() {
		byte[] bloc = new byte[(int) Math.random() * 128000];
		Arrays.fill(bloc, (byte) (Math.random() * 256));
		SendBlock envoi = new SendBlock(
				(int) (Math.random() * Integer.MAX_VALUE),
				(int) (Math.random() * Integer.MAX_VALUE), bloc);
		envoi.send(myPipe.getOutput());

		Message recu = new MessageReader(myPipe.getInput()).readMessage();
		assertEquals(envoi.getClass(), recu.getClass());
		assertTrue(envoi.equals((SendBlock) recu));
	}
}

class DataPipe {
	private ByteArrayOutputStream output;

	public DataPipe() {
		output = new ByteArrayOutputStream();
	}

	public DataOutputStream getOutput() {
		return new DataOutputStream(output);
	}

	public DataInputStream getInput() {

		return new DataInputStream(new ByteArrayInputStream(
				output.toByteArray()));
	}
}
