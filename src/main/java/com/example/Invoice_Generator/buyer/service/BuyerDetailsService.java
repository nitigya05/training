package com.example.Invoice_Generator.buyer.service;


import com.example.Invoice_Generator.domain.dao.BuyerDetailsRepo;
import com.example.Invoice_Generator.domain.BuyerDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BuyerDetailsService {

    @Autowired
    private BuyerDetailsRepo buyerDetailsRepository;

    // Create or Update
    public BuyerDetails saveOrUpdateBuyer(BuyerDetails buyerDetails) {
        buyerDetails.setCreatedAt(LocalDateTime.now());
        return buyerDetailsRepository.save(buyerDetails);
    }

    // Read all
    public List<BuyerDetails> getAllBuyers() {
        return buyerDetailsRepository.findAll();
    }

    // Read by ID
    public Optional<BuyerDetails> getBuyerById(int buyerId) {
        return buyerDetailsRepository.findById(buyerId);
    }

    // Delete by ID
    public void deleteBuyer(int buyerId) {
        buyerDetailsRepository.deleteById(buyerId);
    }
}

