package com.example.tpjakarta.blockchain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BlockchainTests {
    private Blockchain blockchain;

    @BeforeEach
    void setUp() {
        blockchain = new Blockchain();
    }

    @Test
    void testGenesisBlock() {
        assertEquals(1, blockchain.getChain().size());
        assertEquals("0", blockchain.getChain().get(0).previousHash);
    }

    @Test
    void testAddBlock() {
        blockchain.addBlock("Test Data");
        assertEquals(2, blockchain.getChain().size());
        assertEquals(blockchain.getChain().get(0).hash, blockchain.getChain().get(1).previousHash);
    }

    @Test
    void testChainValidity() {
        blockchain.addBlock("Block 1");
        blockchain.addBlock("Block 2");
        assertTrue(blockchain.isChainValid());
    }

    @Test
    void testCorruptedChain() {
        blockchain.addBlock("Block 1");
        blockchain.getChain().get(1).data = "Donnée modifiée";
        assertFalse(blockchain.isChainValid());
    }

    @Test
    void testMiningIntegrity() {
        blockchain.addBlock("Bloc Miné");
        String hash = blockchain.getChain().get(1).hash;
        assertTrue(hash.startsWith("00"));
    }
}
