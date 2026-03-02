package com.example.tpjakarta.blockchain;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {

    private final BlockchainService blockchainService;

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    @GetMapping
    public List<Block> getBlockchain() {
        return blockchainService.getBlockchain().getChain();
    }

    @PostMapping("/mine")
    public Block mineBlock(@RequestBody Map<String, String> body) {
        String data = body.getOrDefault("data", "Nouvelle Transaction");
        blockchainService.addBlock(data);
        List<Block> chain = blockchainService.getBlockchain().getChain();
        return chain.get(chain.size() - 1);
    }

    @PostMapping("/add-ticket")
    public Block addTicket(@RequestBody Map<String, String> body) {
        blockchainService.addTicket(
                body.get("data"),
                body.get("eventId"),
                body.get("artist"),
                body.get("owner"));
        List<Block> chain = blockchainService.getBlockchain().getChain();
        return chain.get(chain.size() - 1);
    }

    @GetMapping("/validate")
    public Map<String, Object> validate() {
        boolean isValid = blockchainService.getBlockchain().isChainValid();
        return Map.of("isValid", isValid, "message",
                isValid ? "La chaine est intègre." : "ATTENTION: La chaine est corrompue !");
    }
}
