import java.util.Random;
public class RandomSleep {
    public void doSleep() {
    	  Random rnd = new Random();
    	  int rndInt = rnd.nextInt(1000-100)+100; //randomly assigns time to sleep
    	  try {
			Thread.sleep(rndInt);
		} catch (InterruptedException e) {}
      
  }
}

