import java.util.*;

public class Cashier extends Thread{
    int cashierId;  // two cashiers: 1 takes cash, 2 takes credit cards
    public Queue<Customer> registerLine = new LinkedList<>();
    public static long time = System.currentTimeMillis();
    static int numOfCustomerPaid=0;

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

    Cashier(int id){
        this.cashierId=id;
        setName("Cashier " + this.cashierId);
    }

    @Override
    public synchronized void run(){
        try{

            while(numOfCustomerPaid != Main.nCustomer ) {
                assistCustomer();
            }
            sleep(100000000);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void assistCustomer(){
        try {
            if (!customersInLine())  msg("is waiting for the customer");
            while (!customersInLine()) {
                if(numOfCustomerPaid==Main.nCustomer) break;
            } // wait (busy waiting) for customers to arrive
            //CS
            sleep(1000);
            if (customersInLine()){
                Customer c = getCustomer();
                msg("is serving " + c.getName());
                c.pays();
                numOfCustomerPaid++;
                // Once the customer is at the cashier, the customer will reset their priority back to the default value.
                currentThread().setPriority(Thread.NORM_PRIORITY);
                msg("gave " + c.getName() + " the receipt");
                print("# of customer paid: ");
                print(numOfCustomerPaid);
            }

            sleep(2500);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized boolean customersInLine() {
        return registerLine.size() != 0;
    }

    public synchronized Customer getCustomer() {
        return registerLine.remove();
    }

    public static void print(Object o){
        System.out.println(o);
    }
}
