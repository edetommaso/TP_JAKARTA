package com.example.tpjakarta.blockchain;

import java.security.MessageDigest;
import java.time.Instant;

public class Block {
    public int index;
    public String timestamp;
    public String data;
    public String previousHash;
    public String hash;
    public int nonce;

    public String eventId;
    public String artist;
    public String status;
    public String owner;

    public Block(int index, String data, String previousHash) {
        this.index = index;
        this.timestamp = Instant.now().toString();
        this.data = data;
        this.previousHash = previousHash;
        this.nonce = 0;
        this.hash = calculateHash();
    }

    public String calculateHash() {
        try {
            String input = index + timestamp + data + previousHash + nonce
                    + (eventId != null ? eventId : "")
                    + (artist != null ? artist : "")
                    + (status != null ? status : "")
                    + (owner != null ? owner : "");
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes("UTF-8"));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void mineBlock(int difficulty) {
        String target = new String(new char[difficulty]).replace('\0', '0');
        while (!hash.substring(0, difficulty).equals(target)) {
            nonce++;
            hash = calculateHash();
        }
        System.out.println("Block Miné !!! Hash : " + hash);
    }

    public void setTicketInfo(String eventId, String artist, String status, String owner) {
        this.eventId = eventId;
        this.artist = artist;
        this.status = status;
        this.owner = owner;
        this.hash = calculateHash();
    }
}
