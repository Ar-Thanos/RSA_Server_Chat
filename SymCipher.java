
public interface SymCipher
{
	// Return an array of bytes that represent the key for the cipher
	public byte [] getKey();

	// Encode the string using the key and return the result as an array of
	// bytes.  Note that you will need to convert the String to an array of bytes
	public byte [] encode(String S);

	// Decrypt the array of bytes and generate and return the corresponding String.
	public String decode(byte [] bytes);
}
