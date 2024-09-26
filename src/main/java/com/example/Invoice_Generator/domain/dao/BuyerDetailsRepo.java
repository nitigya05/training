package com.example.Invoice_Generator.domain.dao;

import com.example.Invoice_Generator.domain.BuyerDetails;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyerDetailsRepo extends JpaRepository<BuyerDetails,Integer> {
}