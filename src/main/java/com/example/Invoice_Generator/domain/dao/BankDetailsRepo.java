package com.example.Invoice_Generator.domain.dao;

import com.example.Invoice_Generator.domain.BankDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BankDetailsRepo extends JpaRepository<BankDetails,Integer> {
}