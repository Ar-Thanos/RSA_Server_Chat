import java.security.SecureRandom;

public class Add128 implements SymCipher{
private byte[] keys; 
   
    public Add128(){
       keys = new byte[128];
       SecureRandom random = new SecureRandom(); 
       random.nextBytes(keys);
    }
    public Add128(byte[] bites){
        keys = bites;
    }
  
    public byte [] getKey(){
        return keys;
    }

	
	public byte [] encode(String S){
        byte[] bites = S.getBytes();
        int k = 0;
        for(int i=0; i<bites.length; i++){
            if(k>=keys.length){
                k = 0;
            }
            bites[i]+=keys[k]; //Add key to original message byte value
            k++;
        }
        System.out.print("The encrypted array of bytes: "); //Print out encryped array
        for(int i=0;i<bites.length;i++){
            System.out.print(bites[i]+" ");
        }
        System.out.println("\n");
        return bites;
    }


	public String decode(byte [] bytes){
        int k = 0;
        for(int i=0; i<bytes.length; i++){ 
            if(k>=keys.length){
                k = 0;
            }
            bytes[i]-=keys[k]; //Just subtract key now to get original back
            k++;
        }
        System.out.println("\nDecoded bytes: ");
            for(int i=0; i<bytes.length;i++){
                System.out.print(bytes[i]+" "); //Simple printing of decoded byte
            }  
            String s = new String(bytes);
            return s;
    }
}

