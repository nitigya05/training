package com.example.Invoice_Generator.domain;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "buyer_details")
public class BuyerDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "buyer_details_seq")
    @SequenceGenerator(name = "buyer_details_seq", sequenceName = "buyer_details_seq", allocationSize = 1)
    private Long buyerId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String address;

    @Column
    private String gstNo;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private String updatedBy;

    // Getters and setters
}
