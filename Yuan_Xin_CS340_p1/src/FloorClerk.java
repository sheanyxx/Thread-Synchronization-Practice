import java.util.*;

public class FloorClerk extends Thread {
    int clerkId;
    public static int numOfCustomerAssisted=0;
    public static long time = System.currentTimeMillis();
    public static Queue<Customer> line = new LinkedList<>();

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

    FloorClerk(int id){
        this.clerkId=id;
        setName("Floor clerk " + this.clerkId);
    }

    @Override
    public synchronized void run() {
        try {
            while(numOfCustomerAssisted != Main.nCustomer) {
                serveCustomer();
            }
            sleep(100000000);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void serveCustomer() {
        try {
            if (!customersInLine()) {
                msg("is waiting for the customer");
            }
            while (!customersInLine()) { } // wait (busy waiting) for customers to arrive
            //CS
            sleep(1500);
            if(customersInLine()){
                Customer c = getCustomer();
                msg("is serving " + c.getName()); // assign a slip
                numOfCustomerAssisted++;
                c.assignSlip();
                msg("gave " + c.getName() + " a slip");
            }
            sleep(3000);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean customersInLine() {
        return line.size() != 0;
    }

    public static synchronized Customer getCustomer() {
        return line.remove();
    }

}
