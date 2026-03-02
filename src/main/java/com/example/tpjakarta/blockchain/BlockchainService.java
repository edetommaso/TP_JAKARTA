package com.example.tpjakarta.blockchain;

import org.springframework.stereotype.Service;

@Service
public class BlockchainService {
    private final Blockchain blockchain;

    public BlockchainService() {
        this.blockchain = new Blockchain();
    }

    public Blockchain getBlockchain() {
        return blockchain;
    }

    public void addBlock(String data) {
        blockchain.addBlock(data);
    }

    public void addTicket(String data, String eventId, String artist, String owner) {
        blockchain.addTicketBlock(data, eventId, artist, "VALIDE", owner);
    }
}
