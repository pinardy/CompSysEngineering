public class BankImpl {
    private int numberOfCustomers;    // the number of customers
    private int numberOfResources;    // the number of resources

    private int[] available;    // the available amount of each resource
    private int[][] maximum;    // the maximum demand of each customer
    private int[][] allocation;    // the amount currently allocated
    private int[][] need;        // the remaining needs of each customer

    public BankImpl(int[] resources, int numberOfCustomers) {
        // set the number of resources
        this.numberOfResources = resources.length;

        // set the number of customers
        this.numberOfCustomers = numberOfCustomers;

        // set the value of bank resources to available
        // how large available is depends on the how many resource types there are
        this.available = new int[numberOfResources];
        for (int i = 0; i < this.numberOfResources; i++) {
            this.available[i] = resources[i];
        }

        // set the array size for maximum, allocation, and need
        this.maximum = new int[numberOfCustomers][numberOfResources];
        this.allocation = new int[numberOfCustomers][numberOfResources];
        this.need = new int[numberOfCustomers][numberOfResources];

    }

    public int getNumberOfCustomers() {
        return this.numberOfCustomers;
    }

    public void addCustomer(int customerNumber, int[] maximumDemand) {
        // add customer, update maximum and need
        for (int i = 0; i < this.numberOfResources; i++) {
            this.maximum[customerNumber][i] = maximumDemand[i];
            this.need[customerNumber][i] = maximumDemand[i];
        }

    }

    public void getState() {
        // print the current state with a tidy format
        System.out.print("\n--- Current state --- " + "\n");

        // print available
        System.out.println("Available: ");
        for (int i = 0; i < numberOfResources; i++) {
            if (i != numberOfResources - 1) {
                System.out.print(available[i] + ", ");
            } else {
                System.out.println(available[i]);
            }
        }

        // print maximum
        System.out.println("\n" + "Maximum: ");
        for (int i = 0; i < numberOfCustomers; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                if (j != numberOfResources - 1) {
                    System.out.print(maximum[i][j] + ", ");
                } else {
                    System.out.print(maximum[i][j]);
                }
            }
            System.out.println();
        }

        // print allocation
        System.out.println("\n" + "Allocation: ");
        for (int i = 0; i < numberOfCustomers; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                if (j != numberOfResources - 1) {
                    System.out.print(allocation[i][j] + ", ");
                } else {
                    System.out.print(allocation[i][j]);
                }
            }
            System.out.println();
        }

        // print need
        System.out.println("\n" + "Need: ");
        for (int i = 0; i < numberOfCustomers; i++) {
            for (int j = 0; j < numberOfResources; j++) {
                if (j != numberOfResources - 1) {
                    System.out.print(need[i][j] + ", ");
                } else {
                    System.out.print(need[i][j]);
                }
            }
            System.out.println();
        }

    }

    public synchronized boolean requestResources(int customerNumber, int[] request) {
        // print the request
        System.out.println("\n" + "Request from customer " + String.valueOf(customerNumber + 1) + ": ");

        for (int i = 0; i < this.numberOfResources; i++) {
            if (i != numberOfResources - 1) {
                System.out.print(request[i] + ", ");
            } else {
                System.out.println(request[i]);
            }
        }

        // check if request larger than need
        for (int i = 0; i < numberOfResources; i++) {
            if (request[i] > this.need[customerNumber][i]) {
                System.out.println("\n" + "Request from customer " + String.valueOf(customerNumber + 1) +
                        "for resource " + i + " is larger than need");
                return false;
            }
        }

        // check if request larger than available
        for (int i = 0; i < numberOfResources; i++) {
            if (request[i] > this.available[i]) {
                System.out.println("\n" + "Request from customer " + String.valueOf(customerNumber + 1) +
                        "for resource " + i + " is larger than available");
                return false;
            }
        }
        // check if the state is safe
        if (this.checkSafe(customerNumber, request) == false){
            System.out.print("\n" + "Request may cause the system unsafe. Rejected.\n");
            return false;
        }

        // if it is safe, allocate the resources to customer customerNumber
        System.out.print("State is safe. Request granted." + "\n");
        for(int i = 0; i < this.numberOfResources; i++){
            this.available[i] -= request[i];
            this.allocation[customerNumber][i] += request[i];
            this.need[customerNumber][i] -= request[i];
        }
        return true;
    }

    public synchronized void releaseResources(int customerNumber, int[] release) {
        // print the release
        // release the resources from customer customerNumber
        System.out.print("\n" + "Customer " + String.valueOf(customerNumber) + " released resources: [");
        for (int i = 0; i < this.numberOfResources; i++) {
            if (i != numberOfResources - 1) {
                System.out.print(release[i] + ", ");
            } else {
                System.out.println(release[i] + "]");
            }
        }

        for (int i = 0; i < this.numberOfResources; i++) {
            this.available[i] += release[i];
            this.allocation[customerNumber][i] -= release[i];
            this.need[customerNumber][i] += release[i];
        }
    }

    // checks if the state is safe
    private synchronized boolean checkSafe(int customerNumber, int[] request) {
        // capacity of 5 for 3 resources to prevent errors
        // will need to increase if there are more than 3 resources
        int capacity = 5;

        int[] temp_avail = new int[capacity];
        int[][] temp_need = new int[capacity][capacity];
        int[][] temp_allocation = new int[capacity][capacity];
        int[] work = new int[capacity];

        boolean[] finish = new boolean[capacity];
        boolean possible;


        for (int j = 0; j < this.numberOfResources; j++) {
            temp_avail[j] = this.available[j] - request[j];
            work[j] = temp_avail[j];

            for (int i = 0; i < numberOfCustomers; i++) {
                if (i == customerNumber) {
                    temp_need[customerNumber][j] = this.need[customerNumber][j] - request[j];
                    temp_allocation[customerNumber][j] = this.allocation[customerNumber][j] + request[j];
                } else {
                    temp_need[i][j] = this.need[i][j];
                    temp_allocation[i][j] = this.allocation[i][j];
                }
            }
        }

        // finish(all) = false
        for (int i = 0; i < numberOfCustomers; i++) {
            finish[i] = false;
        }

        possible = true;

        while (possible) {
            possible = false;
            for (int i = 0; i < this.numberOfCustomers; i++) {

                // this second condition is temp_need(Ci) <= work
                boolean secondCondition = true;

                for (int j = 0; j < this.numberOfResources; j++) {
                    if (temp_need[i][j] > work[j]) {
                        secondCondition = false;
                    }
                }
                if (finish[i] == false && secondCondition) {
                    possible = true;
                    for (int j = 0; j < this.numberOfResources; j++) {
                        work[j] += temp_allocation[i][j];
                    }
                    finish[i] = true;
                }
            }
        }
        // return (finish(all) == true)
        boolean safe = true;
        for (int i = 0; i < this.numberOfCustomers; i++) {
            if (finish[i] == false) {
                safe = false;
            }
        }
        return safe;
    }

}