package com.example.Invoice_Generator.domain.dao;

import com.example.Invoice_Generator.domain.InvoiceDetails;
import com.example.Invoice_Generator.domain.UserDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TransportDetailsRepo extends JpaRepository<InvoiceDetails, Integer> {

    // Custom query to find transport details by user ID
    List<InvoiceDetails> findByUserDetails(Optional<UserDetails> byId);
}