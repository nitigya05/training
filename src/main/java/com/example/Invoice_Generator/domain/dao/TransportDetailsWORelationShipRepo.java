package com.example.Invoice_Generator.domain.dao;

import com.example.Invoice_Generator.domain.InvoiceDetailsWORelationShip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportDetailsWORelationShipRepo extends JpaRepository<InvoiceDetailsWORelationShip, Long> {

 Page<InvoiceDetailsWORelationShip> findAll(Pageable pageable);
}