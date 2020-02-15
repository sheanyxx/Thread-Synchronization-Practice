/**
 *
 * 340 Project 2
 * Shopping at the Mega store
 * Simulation without Semaphores
 *
 * @author Xin Yuan
 * All rights reserved
 * 12/14/2019
 *
 */

 import static java.lang.Thread.*;

public class Main {

    public static long time = System.currentTimeMillis();
    public static int nCustomer = 12,
                      nFloorClerk = 3,
                      nCashier = 2;

    public static FloorClerk[] floorClerks = new FloorClerk[nFloorClerk];
    public static Cashier[] cashiers = new Cashier[nCashier];
    public static Customer[] customers = new Customer[nCustomer];

    public static void main(String[] args) {
//  Uncomment below to enable the command line argument to allow changes to the nCustomer variable
//        nCustomer=Integer.parseInt(args[0]);

        try {

            for(int i = 0; i < nFloorClerk; i++){
                floorClerks[i] = new FloorClerk(i+1);
                floorClerks[i].start();
            }
            for(int i = 0; i < nCustomer; i++){
                customers[i] = new Customer(i+1);
                customers[i].start();
            }
            for(int i = 0; i < nCashier; i++){
                cashiers[i] = new Cashier(i+1);
                cashiers[i].start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public static boolean availableClerk() {
        for(int i=0; i<nFloorClerk; i++){
            if(floorClerks[i].isAlive()) return true;
        }
        return false;
    }
}
