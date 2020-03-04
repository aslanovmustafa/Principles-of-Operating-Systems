//added more log info

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Writer extends Thread {
 
  static boolean append;
  static Scanner sc = new Scanner(System.in);

  public Writer() {

  }  



  public void run () {
      try{
      	synchronize.y.acquire();
      } catch(Exception e){}
      synchronize.writeCount++;
      if(synchronize.writeCount == 1) {
    	  try{
    	      	synchronize.rsem.acquire();
    	      }
    	      catch(Exception e){}  
      }
      
      synchronize.y.release();
      try{
        	synchronize.wsem.acquire();
        }
        catch(Exception e){}
      System.out.println("Writer " +Thread.currentThread().getName() + " is writing"
    		  + "\nTo finish writing, type 0");
      write();   
      System.out.println("Writers Writing: " + synchronize.writeCount + " | Readers Reading: " + synchronize.readCount);
	  synchronize.wsem.release();
	  try{
	      	synchronize.y.acquire();
	      }
	      catch(Exception e){}
	  
	  synchronize.writeCount--;
	  System.out.println("Writer " + Thread.currentThread().getName() + " is finished writing");

	  if (synchronize.writeCount == 0) synchronize.rsem.release();
      synchronize.y.release();

    } 
  
  private void write() {
      try {
        append = true;
        PrintWriter pw = new PrintWriter(
            new FileWriter("file1", append));
        PrintWriter pw1 = new PrintWriter(
            new FileWriter("file2", append));
        PrintWriter pw2 = new PrintWriter(
            new FileWriter("file3", append));
        while (true) {
          String line = sc.nextLine();
          if (line.contains("0"))
            break;

          pw.println(line);
          pw1.println(line);
          pw2.println(line);
        }

        pw.close();
        pw1.close();
        pw2.close();
      } catch (IOException e) {
        e.printStackTrace();
      }

      sc.close();



}  
}
