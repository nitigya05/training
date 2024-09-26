package com.example.Invoice_Generator.domain;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "bank_details")
public class BankDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bank_details_seq")
    @SequenceGenerator(name = "bank_details_seq", sequenceName = "bank_details_seq", allocationSize = 1)
    private Long bankId;



    @Column(nullable = false)
    private String bankName;

    @Column(unique = true, nullable = false)
    private String accountNo;

    @Column(nullable = false)
    private String branch;

    @Column(unique = true, nullable = false)
    private String ifscCode;

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
