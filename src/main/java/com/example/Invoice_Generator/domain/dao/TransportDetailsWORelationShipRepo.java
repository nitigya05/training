package com.example.Invoice_Generator.domain.dao;

import com.example.Invoice_Generator.domain.TransportDetailsWORelationShip;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TransportDetailsWORelationShipRepo extends JpaRepository<TransportDetailsWORelationShip, Long> {

 @Query("SELECT t FROM TransportDetailsWORelationShip t " +
         "WHERE CAST(t.invoiceNo AS string) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
         "OR LOWER(t.clientName) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
         "OR LOWER(t.origin) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
         "OR LOWER(t.destination) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
         "OR LOWER(t.vehicleNo) LIKE LOWER(CONCAT('%', :searchTerm, '%')) " +
         "OR LOWER(t.driverName) LIKE LOWER(CONCAT('%', :searchTerm, '%'))")
 Page<TransportDetailsWORelationShip> searchByMultipleColumns(@Param("searchTerm") String searchTerm, Pageable pageable);

 Page<TransportDetailsWORelationShip> findAll(Pageable pageable);
}