package com.example.demo.dtos;

public class LogMessage {
    private String type; // e.g., "production", "consumption", "info"
    private String message;
    private String timestamp;
    private int availableTicketCount;
    private int releasedTicketCount;

    public LogMessage(String type, String message, int availableTicketCount, int releasedTicketCount, String timestamp) {
        this.type = type;
        this.message = message;
        this.availableTicketCount = availableTicketCount;
        this.releasedTicketCount = releasedTicketCount;
        this.timestamp = timestamp;
    }

    // Getters and setters
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
    public int getAvailableTicketCount() {
        return availableTicketCount;
    }
    public void setAvailableTicketCount(int availableTicketCount) {
        this.availableTicketCount = availableTicketCount;
    }
    public int getReleasedTicketCount() {
        return releasedTicketCount;
    }
    public void setReleasedTicketCount(int releasedTicketCount) {
        this.releasedTicketCount = releasedTicketCount;
    }
}
