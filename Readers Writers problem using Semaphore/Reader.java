//added more log info + fixed thread sleep issue

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Random;

public class Reader extends Thread {

	RandomSleep randSleep;

	public Reader() {
		randSleep = new RandomSleep();
	}

	public void run () {    
      try{
      	  synchronize.z.acquire();
      	  synchronize.rsem.acquire();
      	  synchronize.x.acquire();
      } catch(Exception e){}
      	//for more reads at the same time comment this part.
    	  synchronize.readCount++;
    	  if (synchronize.readCount == 1) {
    		  try {
    		  synchronize.wsem.acquire();}catch(Exception e){}}
    	  synchronize.x.release();
    	  synchronize.rsem.release();
    	  synchronize.z.release();
    	  System.out.println("Reader " + Thread.currentThread().getName() + " is reading");
    	  read();
    	  randSleep.doSleep();
          System.out.println("Writers Writing: " + synchronize.writeCount + " | Readers Reading: " + synchronize.readCount);
    	
          try{ synchronize.x.acquire();}
          	catch(Exception e){}
          synchronize.readCount--;
    	  System.out.println("Reader " + Thread.currentThread().getName() + " is finished reading");

      

          if (synchronize.readCount == 0) synchronize.wsem.release();
          synchronize.x.release();
          
    }

	 private void read() {
         BufferedReader br = null;
         Random rgen = new Random();
         try {
           int num = 1 + rgen.nextInt(3-1);
           switch(num) {
           case 1: {br = new BufferedReader(new FileReader("file1"));break;}
           case 2: {br = new BufferedReader(new FileReader("file2"));break;}
           case 3: {br = new BufferedReader(new FileReader("file3"));break;}
           }
//           
           while (true) {
             String line = br.readLine();
             if (line == null)
               break;
             System.out.println(line);
           }

         } catch (FileNotFoundException e) {
           System.err.println("The file you specified does not exist.");
         } catch (IOException e) {
           System.err.println("Some other IO exception occured. Message: " + e.getMessage());
         } finally {
           try {
             if (br != null)
               br.close();
           } catch (IOException e) {
             e.printStackTrace();
           }
         }
       }
}
