package com.example.tpjakarta.blockchain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Blockchain {
    private final List<Block> chain;
    private int difficulty = 2;

    public Blockchain() {
        this.chain = new ArrayList<>();
        File file = new File("blockchain.json");

        if (file.exists() && file.length() > 0) {
            loadFromJson("blockchain.json");
        }

        if (chain.isEmpty()) {
            Block genesisBlock = new Block(0, "Genesis Block - Billetterie", "0");
            genesisBlock.mineBlock(difficulty);
            chain.add(genesisBlock);
            exportAsJson("blockchain.json");
        }
        System.out.println("Blockchain opérationnelle (" + chain.size() + " blocs)");
    }

    private void loadFromJson(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            List<Block> loadedChain = mapper.readValue(new File(filename),
                    mapper.getTypeFactory().constructCollectionType(List.class, Block.class));
            this.chain.addAll(loadedChain);
        } catch (IOException e) {
            System.err.println("Erreur de chargement JSON : " + e.getMessage());
        }
    }

    public void addBlock(String data) {
        Block previousBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(chain.size(), data, previousBlock.hash);
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
        exportAsJson("blockchain.json");
    }

    public void addTicketBlock(String data, String eventId, String artist, String status, String owner) {
        Block previousBlock = chain.get(chain.size() - 1);
        Block newBlock = new Block(chain.size(), data, previousBlock.hash);
        newBlock.setTicketInfo(eventId, artist, status, owner);
        newBlock.mineBlock(difficulty);
        chain.add(newBlock);
        exportAsJson("blockchain.json");
    }

    public Boolean isChainValid() {
        for (int i = 1; i < chain.size(); i++) {
            if (!isBlockValid(i))
                return false;
        }
        return true;
    }

    public boolean isBlockValid(int index) {
        if (index < 0 || index >= chain.size())
            return false;

        Block currentBlock = chain.get(index);

        if (!currentBlock.hash.equals(currentBlock.calculateHash()))
            return false;

        if (index > 0) {
            Block previousBlock = chain.get(index - 1);
            if (!previousBlock.hash.equals(currentBlock.previousHash))
                return false;
        }

        String hashTarget = new String(new char[difficulty]).replace('\0', '0');
        if (!currentBlock.hash.substring(0, difficulty).equals(hashTarget))
            return false;

        return true;
    }

    public void exportAsJson(String filename) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.enable(SerializationFeature.INDENT_OUTPUT);
        try {
            mapper.writeValue(new File(filename), chain);
            System.out.println("Blockchain exportée avec succès dans : " + filename);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void displayChain() {
        System.out.println("\n--- ÉTAT DE LA BLOCKCHAIN ---");
        for (Block block : chain) {
            System.out.println("Index : " + block.index);
            System.out.println("Données : " + block.data);
            if (block.owner != null)
                System.out.println("Propriétaire : " + block.owner);
            System.out.println("Hash : " + block.hash);
            System.out.println("Previous Hash : " + block.previousHash);
            System.out.println("-----------------------------");
        }
    }

    public void consensusPoS(String validator, double stake) {
        System.out.println("Validation PoS par " + validator + " avec une mise de " + stake + " jetons.");
        addBlock("Validation PoS par " + validator);
    }

    public void consensusPBFT(int totalNodes, int positiveVotes) {
        double ratio = (double) positiveVotes / totalNodes;
        if (ratio > 0.66) {
            System.out.println("Consensus PBFT atteint (" + (int) (ratio * 100) + "%). Ajout du bloc.");
            addBlock("Validation PBFT - Accord réseau");
        } else {
            System.out.println("Consensus PBFT échoué. Pas assez de votes.");
        }
    }

    public void consensusPoA(String authorityName, boolean isAuthorized) {
        if (isAuthorized) {
            System.out.println("Autorité " + authorityName + " a validé le bloc.");
            addBlock("Validation PoA par " + authorityName);
        } else {
            System.out.println("Action refusée : " + authorityName + " n'est pas une autorité valide.");
        }
    }

    public List<Block> getChain() {
        return chain;
    }
}
