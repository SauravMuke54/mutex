import java.util.concurrent.*;
import java.util.*;
public class Main {
  static Semaphore barber;
  static Semaphore customer;
  static Semaphore accessSeats;
  static int seats;
  static int N ;
 static void input(){
     Scanner sc = new Scanner(System.in);
      System.out.println("Enter the number of seats ");
      seats=sc.nextInt();
      System.out.println("Enter the number of customers coming ");
      N=sc.nextInt();
  }
  static void barber() {
    new Thread(() -> {
      try {
      while(true) {
        System.out.println("Barber: sleeping");
        customer.acquire();
        System.out.println("Barber: got customer");
        accessSeats.acquire();
        barber.release();
        seats++;
        accessSeats.release();
         System.out.println("Barber: cutting hair");
        Thread.sleep(1000);
         System.out.println("Barber: cutting done");
      }
    }
    catch(InterruptedException e) {}
    }).start();
  }
  static void customer(int i) {
    new Thread(() -> {
      try {
       System.out.println("Customer" +i+": checking seats");
      accessSeats.acquire();
      if(seats<=0) {
         System.out.println("Customer" +i+": no seats!");
        accessSeats.release();
        return;
      }
      seats--;
      customer.release();
      accessSeats.release();
       System.out.println("Customer" +i+": sat, seats="+seats);
      barber.acquire();
      System.out.println("Customer" +i+": having hair cut");
      }
      catch(InterruptedException e) {}
    }).start();
  }
  public static void main(String[] args) {
      input();
     System.out.println("The total number of seats is "+
        seats+"and the number of customers are "+N+" customers ...");
    barber = new Semaphore(0);
    customer = new Semaphore(0);
    accessSeats = new Semaphore(1);
    barber();
    for(int i=1; i<=N; i++) {
      sleep(1000 * Math.random());
      customer(i);
    }
  }

  static void sleep(double t) {
    try { Thread.sleep((long)t); }
    catch (InterruptedException e) {}
  }
}