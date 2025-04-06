package com.example.demo.dtos;

public class VendorRequest {
    private int batchSize;
    private int ticketCapacity;
    private int consumeBatchSize;

    public int getBatchSize() {
        return batchSize;
    }

    public int getTicketCapacity() {
        return ticketCapacity;
    }
    public int getConsumeBatchSize() {
        return consumeBatchSize;
    }

    public void setBatchSize(int batchSize) {
        this.batchSize = batchSize;
    }
}
