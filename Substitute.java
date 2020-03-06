import java.security.SecureRandom;
public class Substitute implements SymCipher{

    private byte[] keys; 
    private byte[] decod;
    public Substitute(){
       keys = new byte[256];
       decod = new byte[256];
       int j = 0;
       for(int i = 0; i<=255; i++){
          keys[i] = (byte)i;
       }
       RandomShuffle shuffle = new RandomShuffle(keys); //Class I made to "randomly" shuffle bytes
       keys = shuffle.getRandomShuffle();  
       for(int i =0; i<=255;i++){
           int b = (int)(keys[i]+256)%256; //To get original byte back and make decode array
           //System.out.println("My b is: "+b+" and my i is "+i);
           decod[b] = (byte)i;
       }
    }

    public Substitute(byte[] bites){
        keys = bites;
        decod = new byte[256];
        for(int i =0; i<=255;i++){
            int b = (int)(keys[i]+256)%256;
            //System.out.println("My b is: "+b+" and my i is "+i);
            decod[b] = (byte)i;
        }
    }
//Found a good shuffle algorithm; shuffle data structure so it has a random order. remove them in order from data structure and place them into my array as i go
//Insert all possible bytes into arraylist with 256 bytes in order. Shuffle em. 

    public byte [] getKey(){
        return keys;
    }

	// Encode the string using the key and return the result as an array of
	// bytes.  Note that you will need to convert the String to an array of bytes
	// prior to encrypting it.  Also note that String S could have an arbitrary
	// length, so your cipher may have to "wrap" when encrypting.
	public byte [] encode(String S){ //Encode 
        byte[] message = S.getBytes();
        int j = 0;
        for(int i = 0; i<message.length;i++){
            int b = (int)(message[i] + 256)%256;
            message[i]=keys[b]; //Switch message byte to key byte
        }
        System.out.print("The encrypted array of bytes: ");
        for(int i=0;i<message.length;i++){
            System.out.print(message[i]+" ");
        }
        //System.out.println("\neff");
        return message;
    }

	// Decrypt the array of bytes and generate and return the corresponding String.
	public String decode(byte [] bytes){
        for(int i =0; i<bytes.length;i++){
            int b = (int)(bytes[i] + 256)%256; //Switch back to message byte
            System.out.println(b);
            bytes[i] = decod[b];
        }
        System.out.println("\nDecoded bytes: ");
            for(int i=0; i<bytes.length;i++){
                System.out.print(bytes[i]+" ");
            }
        String s = new String(bytes);
        return s;
    }

}