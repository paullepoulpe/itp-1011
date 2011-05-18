package settings;

public class CryptoSettings {
	public static int SymetricKeySize = 128;
	public static int RSAKeySize = 128;

	public static void restoreDefaultValues() {
		SymetricKeySize = 128;
		RSAKeySize = 128;
	}
}
