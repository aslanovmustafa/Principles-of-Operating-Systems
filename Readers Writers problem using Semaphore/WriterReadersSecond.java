//this is simple version of the rest of the code.

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.Scanner;

public class WriterReadersSecond {
	static int readerCount = 0;
	static int writerCount = 0;
    static Semaphore x = new Semaphore(1);
    static Semaphore y = new Semaphore(1);
    static Semaphore z = new Semaphore(1);
    static Semaphore rsem = new Semaphore(1);
    static Semaphore wsem = new Semaphore(1);
    static Scanner sc = new Scanner(System.in);
    static boolean append;

    static class Read implements Runnable {
        @Override
        public void run() {
            try {
                z.acquire();
                rsem.acquire();
                x.acquire();
                readerCount++;
                if (readerCount == 1) wsem.acquire();
                x.release();
                rsem.release();
                z.release();

                System.out.println("Thread "+Thread.currentThread().getName() + " is READING. " + " 		Number of readers: " + readerCount);
                read();
                Thread.sleep(1500);
                System.out.println("Thread "+Thread.currentThread().getName() + " has FINISHED READING. ");
                
                x.acquire();
                readerCount--;
                System.out.println("					Number of readers left: " + readerCount);
                if (readerCount == 0) wsem.release();
                x.release();

            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    
        private void read() {
            BufferedReader br = null;
            Random rgen = new Random();
            try {
              int num1 = 1 + rgen.nextInt(3);
              if (num1 == 1) {
                br = new BufferedReader(new FileReader("C:\\Users\\Hp\\eclipse-workspace\\as1\\src\\file1"));
              } else if (num1 == 2) {
                br = new BufferedReader(new FileReader("C:\\Users\\Hp\\eclipse-workspace\\as1\\src\\file2"));
              } else if (num1 == 3) {
                br = new BufferedReader(new FileReader("C:\\Users\\Hp\\eclipse-workspace\\as1\\src\\file3"));
              }
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

    static class Write implements Runnable {
        @Override
        public void run() {
            try {
                y.acquire();
                writerCount++;
                if(writerCount == 1) rsem.acquire();
                y.release();
                wsem.acquire();
                System.out.println("Thread "+Thread.currentThread().getName() + " is WRITING. " + "		Number of writers: " + writerCount
                		+ "\nTo finish writing, type 0");
                write();
                Thread.sleep(2500);
                System.out.println("Thread "+Thread.currentThread().getName() + " has finished WRITING. ");
                wsem.release();
                y.acquire();
                writerCount--;
                System.out.println("					Number of writers left: " + writerCount);
                if (writerCount == 0) rsem.release();
                y.release();
            } catch (InterruptedException e) {
                System.out.println(e.getMessage());
            }
        }
    
        private void write() {
            try {
              append = true;
              PrintWriter pw = new PrintWriter(
                  new FileWriter("C:\\Users\\Hp\\eclipse-workspace\\as1\\src\\file1", append));
              PrintWriter pw1 = new PrintWriter(
                  new FileWriter("C:\\Users\\Hp\\eclipse-workspace\\as1\\src\\file2", append));
              PrintWriter pw2 = new PrintWriter(
                  new FileWriter("C:\\Users\\Hp\\eclipse-workspace\\as1\\src\\file3", append));
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

    public static void main(String[] args) throws Exception {
        Read read = new Read();
        Write write = new Write();
        Thread t1 = new Thread(read);
        t1.setName("thread1");
        Thread t2 = new Thread(read);
        t2.setName("thread2");
        Thread t3 = new Thread(write);
        t3.setName("thread3");
        Thread t4 = new Thread(read);
        t4.setName("thread4");
        t3.start();
        t1.start();
        t2.start();
        t4.start();
    }
}
