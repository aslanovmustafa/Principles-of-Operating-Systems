//increased sleep time

import java.util.Random;
public class RandomSleep {
    public void doSleep() {
    	  Random rnd = new Random();
    	  int rndInt = rnd.nextInt(3000-1000)+1000; //randomly assigns time to sleep
    	  try {
			Thread.sleep(rndInt);
		} catch (InterruptedException e) {}
      
  }
}
