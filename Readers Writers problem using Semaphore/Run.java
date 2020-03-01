import java.util.concurrent.*;
import java.util.Random;

public class Run {
  public static void main (String argv[]) {

    synchronize.x = new Semaphore(1);
    synchronize.y = new Semaphore(1);
    synchronize.z = new Semaphore(1);
    synchronize.rsem = new Semaphore(1);
    synchronize.wsem = new Semaphore(1);


    Reader R;  
    Writer W;  

    Random rand = new Random();
    int rint = rand.nextInt(10-1)+1; //makes writer appear randomly

	int i = 1;

    while(true) {
    	R = new Reader();
    	R.setName("Reader" + i);
    	R.start();
    	if(i==rint) {
    		W = new Writer();
    		W.setName("Writer");
    		W.start();
    	} 
    	i++;
    }
    
    
    
  }  
}  
