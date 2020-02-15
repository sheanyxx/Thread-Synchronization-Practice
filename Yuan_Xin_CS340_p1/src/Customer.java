
public class Customer extends Thread{
    private int customerId;
    private boolean hasSlip = false, hasPaid = false;
    public static long time = System.currentTimeMillis();

    public void msg(String m) {
        System.out.println("["+(System.currentTimeMillis()-time)+"] "+getName()+": "+m);
    }

    Customer(int id){
        this.customerId=id;
        setName("Customer " + this.customerId);
    }

    public void assignSlip(){
        this.hasSlip=true;
    }

    public void pays(){
        this.hasPaid=true;
    }

    @Override
    public synchronized void run(){
        try{
            msg("has walked in the mega showroom");
            startShopping();
            sleep(1000);
            goToCashier( (int)((Math.random()*2)+1) );
            sleep(1000);
            while(!hasPaid) { } // wait till paid

            for(int i = 0; i < Main.nFloorClerk; i++) {
                if(Main.floorClerks[i].isAlive())  Main.floorClerks[i].interrupt();
            }
            while(Main.cashiers[0].numOfCustomerPaid!=Main.nCustomer){} // wait until all customer has paid
            for(int i = 0; i < Main.nCashier; i++) {
                if(Main.cashiers[i].isAlive()) Main.cashiers[i].interrupt();
            }
            waitingToLeave();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public synchronized void startShopping() {
        try {
            //  browses around for a while (sleep(random time))
            msg("is browsing the store");
            this.sleep( (long)((Math.random()*8000)+1000) );

            makingDecision();
            getSlipFromClerk();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //  deciding to purchase the item (yield() twice)
    public synchronized void makingDecision() {
        try {
            msg("is deciding on purchasing the items");
            yield();
            yield();
            sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
        msg("has decided the item that they want to purchase");
    }


    public synchronized void getSlipFromClerk() {
        try {
            if(!hasSlip) FloorClerk.line.add(this);
            msg("is looking for the next available clerk to get a slip");
            if(!Main.availableClerk()) msg("is waiting for an available clerk");
            while(!Main.availableClerk()){ }
            sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //  decide to pay in either 1 cash or 2 credit (determined randomly) and will get on the corresponding line.
    //  go to the cashier to pay:  increase their priority (use getPriority( ), sleep(short time), setPriority( )).
    public void goToCashier(int paymentMethod) {
        try {
            if(paymentMethod==1){
                msg("is paying in cash, getting on the cash line ");
                // go to cashier 1
                Main.cashiers[0].registerLine.add(this);
            }
            if(paymentMethod==2){
                msg("is paying with credit card, getting on the credit card line ");
                // go to cashier 2
                Main.cashiers[1].registerLine.add(this);
            }
            currentThread().setPriority(Thread.MAX_PRIORITY);
            // paying...
            sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //  Once paid, each customer joins another customer; then leaves in sequential order.
    //  Customer N joins with customer N-1, customer N1 joins with N-2, …, customer 2 joins with customer 1 (use join( ), isAlive( )).
    //  Customer 1 will not join with any other customer;
    //  before Customer 1 leaves. announce to the floor clerks and cashiers that it is closing time
    //  by waking them up (call interrupt() on these threads).
    public void waitingToLeave() {
        try {
            msg(" is waiting to leave ");
            // join the previous customer use a queue
            // use join( ), isAlive( )
            for(int i = 1; i<Main.nCustomer; i++) {
                Customer cust = Main.customers[i];
                if (cust!=null&&cust.isAlive()) cust.join();
            }
            sleep(5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
