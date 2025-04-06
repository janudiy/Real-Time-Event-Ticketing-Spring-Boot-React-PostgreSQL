package com.example.demo.service;

import com.example.demo.dtos.LogMessage;
import com.example.demo.handlers.TicketWebSocketHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Service
public class TicketService {

    private final BlockingQueue<String> ticketPool = new LinkedBlockingQueue<>(100);

//    public TicketService() {
//        // Start producing tickets in the background
//        new Thread(this::produceTickets).start();
//    }
//
//    public void produceTickets() {
//        try {
//            int ticketId = 1;
//            while (true) {
//                String ticket = "Ticket-" + ticketId++;
//                ticketPool.put(ticket); // Add ticket to the pool
//                System.out.println("Produced: " + ticket);
//                Thread.sleep(5000); // Simulate production delay
//            }
//        } catch (InterruptedException e) {
//            Thread.currentThread().interrupt();
//        }
//    }
//
//    public String purchaseTicket() {
//        String ticket;
//        if ((ticket = ticketPool.poll()) != null) {
//            return "Purchased: " + ticket;
//        } else {
//            return "No tickets available. Please wait for more tickets to be produced.";
//        }
//    }
private final Object lock = new Object(); // Shared lock for synchronization

    private int productionBatchSize = 10; // Tracks tickets produced in current batch
    private int consumptionBatchSize = 10; // Tracks tickets consumed in current batch
    private int ticketCapacity = 10;
    private int releasedTickets = 0;

    private final ObjectMapper objectMapper = new ObjectMapper();
    @Autowired
    private TicketWebSocketHandler webSocketHandler;
    public void setProductionBatchSize(int productionBatchSize) {
        this.productionBatchSize = productionBatchSize;
    }

    public void setConsumptionBatchSize(int consumptionBatchSize) {
        this.consumptionBatchSize = consumptionBatchSize;
    }
    public void setTicketCapacity(int ticketCapacity) {
        this.ticketCapacity = ticketCapacity;
    }
    // Start vendors to produce tickets
    public void startVendors() {
        // Vendor 1 producing tickets every 2 seconds
        new Thread(() -> produceTickets("Vendor-1")).start();

        // Vendor 2 producing tickets every 3 seconds
        new Thread(() -> produceTickets("Vendor-2")).start();
    }

    // Start buyers to consume tickets
    public void startBuyers() {
        // Buyer 1 purchasing tickets every 1 second
        new Thread(() -> purchaseTickets("Buyer-1")).start();

        // Buyer 2 purchasing tickets every 1.5 seconds
        new Thread(() -> purchaseTickets("Buyer-2")).start();
    }

    // Produce tickets by a specific vendor at a given interval

    private void produceTickets(String vendorName) {
        try {
            int ticketId = 1;
            while (true) {
                synchronized (lock) {
                    while (ticketPool.size() >= productionBatchSize) {
//                        log(vendorName + " is waiting for buyers to consume tickets...");
                        lock.wait();
                    }

                    for (int i = 0; i < productionBatchSize; i++) {
                        if (releasedTickets < ticketCapacity) {
                            String ticket = vendorName + "-Ticket-" + ticketId++;
                            ticketPool.put(ticket);
                            releasedTickets ++;
                            log("PRODUCTION", vendorName + " produced " + ticket + "Available ticket count: " + ticketPool.size());
                            Thread.sleep(2000);
                        }
                    }
                    lock.notifyAll(); // Notify buyers
                }
                Thread.sleep(2000); // Simulate production delay
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log("PRODUCTION", vendorName + " stopped producing tickets.");
        }
    }

    // Purchase tickets by a specific buyer at a given interval
    private void purchaseTickets(String buyerName) {
        try {
            while (true) {
                synchronized (lock) {
                    while (ticketPool.size() < consumptionBatchSize) {
//                        log(buyerName + " is waiting for vendors to produce tickets...");
                        lock.wait();
                    }

                    for (int i = 0; i < consumptionBatchSize; i++) {
                        String ticket = ticketPool.poll();
                        if (ticket != null) {
                            log("PURCHASE", buyerName + " purchased " + ticket + "Available ticket count: " + ticketPool.size());
                        }
                    }
                    lock.notifyAll(); // Notify vendors
                }
                Thread.sleep(2000); // Simulate consumption delay
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log("PURCHASE", buyerName + " stopped consuming tickets.");
        }
    }

    // Log messages via WebSocket
    private void log(String type, String message) {
        try {
            String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
            LogMessage logMessage = new LogMessage(type, message, ticketPool.size(), releasedTickets, timestamp);
            String jsonMessage = objectMapper.writeValueAsString(logMessage); // Convert to JSON
            webSocketHandler.broadcastMessage(jsonMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
