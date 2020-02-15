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

public class Cashier extends Thread{
    int cashierId;  // two cashiers: 1 takes cash, 2 takes credit cards
    public Queue<Customer> registerLine = new LinkedList<>();
    public static long time = System.currentTimeMillis();
    public static int numOfCustomerPaidInCash=0, numOfCustomerPaidInCreditCard=0;
    public static Semaphore mutex = new Semaphore(1,true);

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

    Cashier(int id){
        this.cashierId=id;
        setName("Cashier " + this.cashierId);
    }

    @Override
    public void run(){
        try{
            while(true){
                msg("is waiting for the customer");
                mutex.acquire();
                if(numOfCustomerPaidInCash+numOfCustomerPaidInCreditCard==Main.nCustomer) {
                    mutex.release();
                    msg(" is ready to close the store because all customer have paid.");
                    break;
                }
                if(this.cashierId==1){  // cash register
                    Main.availableCustomerOnCashLine.acquire();
                    assistCustomer();
                    ++numOfCustomerPaidInCash;
                    print("Number of customer assisted by "+ getName()+": " + numOfCustomerPaidInCash);
                    Main.availableCashRegister.release();
                    mutex.release();
                }
                if(this.cashierId==2){  // credit card register
                    Main.availableCustomerOnCreditCardLine.acquire();
                    assistCustomer();
                    ++numOfCustomerPaidInCreditCard;
                    print("Number of customer assisted by "+ getName()+": " + numOfCustomerPaidInCreditCard);
                    Main.availableCreditCardRegister.release();
                    mutex.release();
                }
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    public void assistCustomer(){
        try {
            if (customersInLine()){
                Customer c = getCustomer();
                msg("is serving " + c.getName());
                c.pays();
                Main.boardingPass.release();
                print("Number of customer paid: " + c.numOfPaidCustomer);
                msg("gave " + c.getName() + " the receipt and the boarding pass");
            }
        } catch(Exception e) { e.printStackTrace(); }
    }

    public boolean customersInLine() { return registerLine.size() != 0; }

    public Customer getCustomer() { return registerLine.remove(); }

    public static void print(Object o){ System.out.println("["+(System.currentTimeMillis()-time)+"] "+o); }
}
