//Algorithm adapted from Donald Knuth. Fairly famous shuffle algorithm that I read about.

import java.util.Random;
public class RandomShuffle{
private byte[] bytes;
    public RandomShuffle(byte[] bites){
        bytes = bites;
        shuffle();
    }
    private void shuffle(){
        Random rand = new Random();
        for(int i=bytes.length-1;i>0; i--){
            swap(i, rand.nextInt(i+1));
        }
    }
    private void swap(int i, int j){
        byte temp = bytes[i];
        bytes[i] = bytes[j];
        bytes[j] = temp;
    }
    public byte[] getRandomShuffle(){
        return bytes;
    }
}