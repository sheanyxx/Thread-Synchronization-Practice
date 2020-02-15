/**
 *
 * 340 Project 2
 * Shopping at the Mega store
 * Simulation with Semaphores
 *
 * @author Xin Yuan
 * All rights reserved
 * 12/14/2019
 *
 */

import java.util.*;
import java.util.concurrent.Semaphore;

public class FloorClerk extends Thread {
    int clerkId;
    public static int numOfCustomerAssisted=0;
    public static Semaphore mutex = new Semaphore(1,true);
    public static long time = System.currentTimeMillis();
    public static Queue<Customer> line = new LinkedList<>();

    public void msg(String m) { System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m); }

    FloorClerk(int id){
        this.clerkId=id;
        setName("Floor clerk " + this.clerkId);
    }

    @Override
    public void run() {
        try {
            while(true){
                msg("is waiting for the customer");
                mutex.acquire();
                if(numOfCustomerAssisted==Main.nCustomer){
                    mutex.release();
                    msg("is ready to close the store because no customer is left to get a slip.");
                    break;
                }
                Main.availableCustomerOnSlipLine.acquire();
                serveCustomer();
                Main.availableClerk.release();
                mutex.release();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void serveCustomer() {
        try {
            if(customersInLine()){
                Customer c = getCustomer();
                msg(" is assisting " + c.getName());
                assignSlip();
                msg(" gave " + c.getName() + " a slip");
                c.getsSlip();
                numOfCustomerAssisted++;
                print("Number of customer assisted by clerks: " + numOfCustomerAssisted);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private void assignSlip() { Main.paymentLine.release(); }

    public boolean customersInLine() { return line.size() != 0; }

    public static Customer getCustomer() { return line.remove(); }

    static void print(Object o){ System.out.println("["+(System.currentTimeMillis()-time)+"] "+o); }

}
