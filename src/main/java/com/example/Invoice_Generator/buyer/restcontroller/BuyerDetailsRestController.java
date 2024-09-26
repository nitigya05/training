package com.example.Invoice_Generator.buyer.restcontroller;


import com.example.Invoice_Generator.buyer.service.BuyerDetailsService;
import com.example.Invoice_Generator.domain.BuyerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/buyers")
public class BuyerDetailsRestController {

    @Autowired
    private BuyerDetailsService buyerDetailsService;

    // Create or Update Buyer
    @PostMapping
    public ResponseEntity<BuyerDetails> createOrUpdateBuyer(@RequestBody BuyerDetails buyerDetails) {
        BuyerDetails savedBuyer = buyerDetailsService.saveOrUpdateBuyer(buyerDetails);
        return ResponseEntity.ok(savedBuyer);
    }

    // Get all buyers
    @GetMapping
    public List<BuyerDetails> getAllBuyers() {
        return buyerDetailsService.getAllBuyers();
    }

    // Get a buyer by ID
    @GetMapping("/{id}")
    public ResponseEntity<BuyerDetails> getBuyerById(@PathVariable int id) {
        Optional<BuyerDetails> buyerDetails = buyerDetailsService.getBuyerById(id);
        return buyerDetails.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Delete a buyer by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBuyer(@PathVariable int id) {
        buyerDetailsService.deleteBuyer(id);
        return ResponseEntity.noContent().build();
    }
}
