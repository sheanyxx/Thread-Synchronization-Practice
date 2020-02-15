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

import java.util.concurrent.*;

public class Main {
    // Default values
    public static int nCustomer = 15,
                      nFloorClerk = 3,
                      nCashier = 2,
                      miniSize = 4;

    // Semaphores
    public static Semaphore availableClerk = new Semaphore(nFloorClerk,true),
                            availableCashRegister = new Semaphore(1,true),
                            availableCreditCardRegister = new Semaphore(1,true),
                            availableCustomerOnSlipLine = new Semaphore(0,true),
                            paymentLine = new Semaphore(0,true),
                            availableCustomerOnCashLine = new Semaphore(0,true),
                            availableCustomerOnCreditCardLine = new Semaphore(0,true),
                            busGroup = new Semaphore(0,true),
                            boardingPass =  new Semaphore(0,true);

    public static FloorClerk[] floorClerks = new FloorClerk[nFloorClerk];
    public static Cashier[] cashiers = new Cashier[nCashier];
    public static Customer[] customers = new Customer[nCustomer];

    public static void main(String[] args) {

        try {

            // args[0] --> number of customers read as command line arguments
            if(args.length==1){
                nCustomer=Integer.parseInt(args[0]);
                customers = new Customer[nCustomer];
            }

            for(int i = 0; i < nFloorClerk; i++){
                floorClerks[i] = new FloorClerk(i+1);
                floorClerks[i].start();
            }
            for(int i = 0; i < nCashier; i++){
                cashiers[i] = new Cashier(i+1);
                cashiers[i].start();
            }
            for(int i = 0; i < nCustomer; i++){
                customers[i] = new Customer(i+1);
                customers[i].start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}