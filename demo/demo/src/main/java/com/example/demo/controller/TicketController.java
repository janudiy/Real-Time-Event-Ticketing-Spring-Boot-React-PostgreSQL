package com.example.demo.controller;
//
//import com.example.demo.service.TicketService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequestMapping("/api")
//public class TicketController {
//
//    private final TicketService ticketService;
//
//    @Autowired
//    public TicketController(TicketService ticketService) {
//        this.ticketService = ticketService;
//    }
//
//    @PostMapping("/start")
//    public String startSimulation(@RequestParam int tickets,
//                                  @RequestParam long productionInterval,
//                                  @RequestParam long consumptionInterval) {
//        ticketService.produceTickets(tickets, productionInterval);
//        ticketService.consumeTickets(consumptionInterval);
//        return "Ticket simulation started!";
//    }
//}

import com.example.demo.dtos.BuyerRequest;
import com.example.demo.dtos.VendorRequest;
import com.example.demo.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class TicketController {

    private final TicketService ticketService;

    @Autowired
    public TicketController(TicketService ticketService) {
        this.ticketService = ticketService;
    }

    // Start the vendors
    @PostMapping("/start-service")
    public String startVendors(@RequestBody VendorRequest request) {
        ticketService.setProductionBatchSize(request.getBatchSize());
        ticketService.setTicketCapacity(request.getTicketCapacity());
        ticketService.setConsumptionBatchSize(request.getConsumeBatchSize());
        ticketService.startVendors();
        ticketService.startBuyers();
        return "Simulation has been started";
    }

    // Start the buyers
//    @PostMapping("/start-buyers")
//    public String startBuyers(@RequestBody BuyerRequest request) {
//        ticketService.setConsumptionBatchSize(request.getBatchSize());
//        ticketService.startBuyers();
//        return "Buyers started purchasing tickets.";
//    }
}
