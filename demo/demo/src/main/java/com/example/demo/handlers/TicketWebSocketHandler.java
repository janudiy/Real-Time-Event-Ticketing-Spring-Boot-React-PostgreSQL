package com.example.demo.handlers;

import com.example.demo.dtos.ConnectionResponse;
import com.example.demo.service.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class TicketWebSocketHandler extends TextWebSocketHandler {

    private final List<WebSocketSession> sessions = Collections.synchronizedList(new ArrayList<>());

//    @Override
//    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
//        System.out.println("Connected: " + session.getId());
//        sessions.add(session);
//        session.sendMessage(new TextMessage("Welcome to the ticketing system!"));
//    }
//
//    @Override
//    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
//        System.out.println("Disconnected: " + session.getId());
//        sessions.remove(session);
//    }
//
//    @Override
//    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
//        String payload = message.getPayload().toString();
//        System.out.println("Received message: " + payload);
//
//        if (payload.equalsIgnoreCase("purchase_ticket")) {
//            String result = ticketService.purchaseTicket();
//            broadcastMessage(result);
//        } else {
//            session.sendMessage(new TextMessage("Unknown command. Send 'purchase_ticket' to buy a ticket."));
//        }
//    }
//
//    public void broadcastMessage(String message) throws Exception {
//        for (WebSocketSession session : sessions) {
//            if (session.isOpen()) {
//                session.sendMessage(new TextMessage(message));
//            }
//        }
//    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessions.add(session);
        ObjectMapper objectMapper = new ObjectMapper();
        ConnectionResponse response = new ConnectionResponse("Connected", "Successfully connected to Ticket Logging System.");
        String jsonResponse = objectMapper.writeValueAsString(response);

        // Send JSON response
        session.sendMessage(new TextMessage(jsonResponse));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session);
    }

    public void broadcastMessage(String message) throws Exception {
        for (WebSocketSession session : sessions) {
            if (session.isOpen()) {
                session.sendMessage(new TextMessage(message));
            }
        }
    }
}

