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

import java.util.concurrent.Semaphore;

public class Customer extends Thread{
    private int customerId;
    public static int numOfPaidCustomer=0, numOfCustomerDeparted=0;
    public static Semaphore mutex = new Semaphore(1,true),
                            mutex1 = new Semaphore(1,true),
                            mutex2 = new Semaphore(1,true),
                            mutex3 = new Semaphore(1,true);
    private boolean hasSlip = false, hasPaid = false;
    public static long time = System.currentTimeMillis();

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

    Customer(int id){
        this.customerId=id;
        setName("Customer " + this.customerId);
    }

    @Override
    public void run(){
        try{
            msg("has walked in the mega showroom");
            browsingTheStore();
            makingDecision();
            getSlipFromClerk();
            msg(" is deciding the payment method ");
            Main.paymentLine.acquire();
            msg(" got on payment line");
            goToCashier( (int)((Math.random()*2)+1) ); // randomly choose either 1 (cash) or 2 (credit card)
            msg(" has paid, waiting in the cafe");
            Main.boardingPass.acquire();
            waitingToLeave();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void browsingTheStore() {
        try {
            //  browses around for a while (sleep(random time))
            msg("is browsing the store");
            this.sleep( (long)((Math.random()*2000)+1000) );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  deciding to purchase the item (yield() twice)
    public void makingDecision() {
        try {
            msg("is deciding on purchasing the items");
            sleep((long)((Math.random()*2000)+1000));
            msg("has decided the item that they want to purchase");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getSlipFromClerk() {
        try {
            if(!this.hasSlip) {
                mutex.acquire();
                Main.availableCustomerOnSlipLine.release();
                FloorClerk.line.add(this);
                msg("is waiting for an available clerk to get a slip");
                Main.availableClerk.acquire();
                mutex.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  decide to pay in either 1 cash or 2 credit (determined randomly)
    //  get on the corresponding line.
    //  go to the cashier to pay
    public void goToCashier(int paymentMethod) {
        try {
            if(!this.hasPaid) {
                // 1 for cash
                if (paymentMethod == 1) {
                    mutex1.acquire();
                    msg("is paying in cash, getting on the cash line ");
                    Main.availableCustomerOnCashLine.release();
                    Main.cashiers[0].registerLine.add(this);
                    msg("is waiting for the cash register to be available");
                    Main.availableCashRegister.acquire();
                    mutex1.release();
                }
                // 2 for credit card
                if (paymentMethod == 2) {
                    mutex2.acquire();
                    msg("is paying with credit card, getting on the credit card line ");
                    Main.availableCustomerOnCreditCardLine.release();
                    Main.cashiers[1].registerLine.add(this);
                    msg("is waiting for the credit card register to be available");
                    Main.availableCreditCardRegister.acquire();
                    mutex2.release();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  group to mini_size. → board the minibus
    // → departure → ( closing time ) for all the threads.
    // the last bus departs when all customers are left
    public void waitingToLeave() {
        try {
            if(numOfPaidCustomer % Main.miniSize == 0){
                for(int i=0; i<Main.miniSize;i++) {
                    Main.busGroup.release();
                }
            } else if(numOfPaidCustomer == Main.nCustomer){
                for(int i=0; i<numOfPaidCustomer%Main.miniSize;i++) {
                    Main.busGroup.release();
                }
            } else{
                msg(" is waiting for the bus to be full ");
                Main.busGroup.acquire();
                msg(" is ready to leave, bus is full ");
                mutex3.acquire();
                numOfCustomerDeparted++;
                if (numOfCustomerDeparted == Main.nCustomer) {
                    print("Last bus is departed. Store is closed.");
                }
                mutex3.release();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getsSlip() { this.hasSlip=true; }

    public void pays(){
        this.hasPaid=true;
        numOfPaidCustomer++;
    }

    public static void print(Object o){ System.out.println("["+(System.currentTimeMillis()-time)+"] "+o); }

}
