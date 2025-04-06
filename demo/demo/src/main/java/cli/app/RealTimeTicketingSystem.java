import java.util.LinkedList;
import java.util.Queue;
import java.util.Scanner;

class TicketPool {
    private final Queue<String> tickets = new LinkedList<>();
    private final int capacity;
    private int totalTicketsProduced = 0;
    private final int maxTickets;

    public TicketPool(int capacity, int maxTickets) {
        this.capacity = capacity;
        this.maxTickets = maxTickets;
    }

    // Add tickets to the pool (Producer)
    public synchronized void addTicket(int vendorId, int releasingRate) throws InterruptedException {
        while (tickets.size() + releasingRate > capacity || totalTicketsProduced >= maxTickets) {
            if (totalTicketsProduced >= maxTickets) {
                return;
            }
            System.out.println("Ticket pool is full or max tickets reached. Waiting...");
            wait();
        }

        for (int i = 0; i < releasingRate && totalTicketsProduced < maxTickets; i++) {
            String ticket = "Ticket-" + vendorId + "-" + totalTicketsProduced;
            tickets.add(ticket);
            totalTicketsProduced++;
            System.out.println("Vendor " + vendorId + " added ticket: " + ticket);
        }

        notifyAll(); // Notify consumers
    }

    // Remove tickets from the pool (Consumer)
    public synchronized String purchaseTicket(int purchasingRate) throws InterruptedException {
        while (tickets.isEmpty()) {
            System.out.println("No tickets available. Waiting for tickets...");
            wait();
        }

        StringBuilder purchasedTickets = new StringBuilder();
        for (int i = 0; i < purchasingRate && !tickets.isEmpty(); i++) {
            String ticket = tickets.poll();
            purchasedTickets.append(ticket).append(" ");
        }

        notifyAll(); // Notify producers
        return purchasedTickets.toString();
    }
}

class Vendor implements Runnable {
    private final TicketPool ticketPool;
    private final int vendorId;
    private final int releasingRate;

    public Vendor(TicketPool ticketPool, int vendorId, int releasingRate) {
        this.ticketPool = ticketPool;
        this.vendorId = vendorId;
        this.releasingRate = releasingRate;
    }

    @Override
    public void run() {
        try {
            while (true) {
                ticketPool.addTicket(vendorId, releasingRate);
                Thread.sleep(100); // Simulate ticket release delay
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Vendor " + vendorId + " interrupted.");
        }
    }
}

class Customer implements Runnable {
    private final TicketPool ticketPool;
    private final int customerId;
    private final int purchasingRate;

    public Customer(TicketPool ticketPool, int customerId, int purchasingRate) {
        this.ticketPool = ticketPool;
        this.customerId = customerId;
        this.purchasingRate = purchasingRate;
    }

    @Override
    public void run() {
        try {
            while (true) {
                String tickets = ticketPool.purchaseTicket(purchasingRate);
                System.out.println("Customer " + customerId + " bought: " + tickets);
                Thread.sleep(200); // Simulate time taken for the next purchase
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Customer " + customerId + " interrupted.");
        }
    }
}

public class RealTimeTicketingSystem {
    public static void main(String[] args) {
        Scanner sn = new Scanner(System.in);

        System.out.println("Enter total ticket count: ");
        int totalTicketCount = sn.nextInt();

        System.out.println("Enter ticket releasing rate: ");
        int ticketReleasingRate = sn.nextInt();

        System.out.println("Enter ticket purchasing rate: ");
        int ticketPurchasingRate = sn.nextInt();

        int ticketPoolCapacity = 10;
        TicketPool ticketPool = new TicketPool(ticketPoolCapacity, totalTicketCount);

        // Create vendors (producers)
        Thread vendor1 = new Thread(new Vendor(ticketPool, 1, ticketReleasingRate), "Vendor-1");
        Thread vendor2 = new Thread(new Vendor(ticketPool, 2, ticketReleasingRate), "Vendor-2");

        // Create customers (consumers)
        Thread customer1 = new Thread(new Customer(ticketPool, 1, ticketPurchasingRate), "Customer-1");
        Thread customer2 = new Thread(new Customer(ticketPool, 2, ticketPurchasingRate), "Customer-2");

        // Start all threads
        vendor1.start();
        vendor2.start();
        customer1.start();
        customer2.start();

        // Wait for producers to finish
        try {
            vendor1.join();
            vendor2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Stop consumers gracefully
        customer1.interrupt();
        customer2.interrupt();

        System.out.println("Ticketing system simulation complete.");
    }
}
