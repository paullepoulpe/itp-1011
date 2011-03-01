import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Random;

import org.junit.BeforeClass;
import org.junit.Test;

import torrent.piece.Piece;

public class PieceTest {

    private static MessageDigest shaDigest;

    @BeforeClass
    public static void setup() {
        try {
            shaDigest = MessageDigest.getInstance("SHA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Your Java runtime doesn't support SHA-1 digests - that's incredible but we can't continue.", e);
        }
    }

    @Test
    public void testCheckCorrectData() {
        byte[] data = new byte[Piece.BLOCK_SIZE * 4];
        Random r = new Random();
        r.nextBytes(data);
        
        byte[] hash = shaDigest.digest(data);
        Piece piece = new Piece(2, data.length, hash);

        assertNull(piece.getData());
        assertEquals(0, piece.getDownloadCompleteness());

        piece.feed(Piece.BLOCK_SIZE * 0, Arrays.copyOfRange(data, Piece.BLOCK_SIZE * 0, Piece.BLOCK_SIZE * 1));
        assertEquals(25, piece.getDownloadCompleteness());
        
        piece.feed(Piece.BLOCK_SIZE * 3, Arrays.copyOfRange(data, Piece.BLOCK_SIZE * 3, Piece.BLOCK_SIZE * 4));
        assertEquals(50, piece.getDownloadCompleteness());
        
        piece.feed(Piece.BLOCK_SIZE * 1, Arrays.copyOfRange(data, Piece.BLOCK_SIZE * 1, Piece.BLOCK_SIZE * 2));
        assertEquals(75, piece.getDownloadCompleteness());
        
        piece.feed(Piece.BLOCK_SIZE * 2, Arrays.copyOfRange(data, Piece.BLOCK_SIZE * 2, Piece.BLOCK_SIZE * 3));
        assertEquals(100, piece.getDownloadCompleteness());
        
        assertTrue(piece.isComplete());
        assertTrue(piece.check());
        assertArrayEquals(data, piece.getData());
    }

    @Test
    public void testCheckIncorrectData() throws Exception {
        byte[] data = "Dummy data Dummy data 252".getBytes("UTF-8");
        byte[] hash = shaDigest.digest(data);
        Piece piece = new Piece(2, 25, hash);

        byte[] block = "Dummy data Dummy data 253".getBytes("UTF-8");
        piece.feed(0, block);

        // the check is done automatically when the piece is 'completed'
        assertEquals(0, piece.getDownloadCompleteness());
        assertTrue(!piece.isComplete());
    }
}
