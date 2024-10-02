package com.example.Invoice_Generator.domain;

import com.example.Invoice_Generator.utility.NumberToWordsConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "transport_details")
public class TransportDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "transprt_details_seq")
    @SequenceGenerator(name = "transprt_details_seq", sequenceName = "transprt_details_seq", allocationSize = 1)
    private Long invoiceNo;

    @Column(nullable = false)
    private LocalDate date;

    @Column
    private String clientName;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column
    private Integer noOfDays;

    @Column
    private String vehicleNo;

    @Column
    private String vehicleType;

    @Column
    private String driverName;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalHrs;

    @Column(precision = 10, scale = 2)
    private BigDecimal actualHrs;

    @Column(precision = 10, scale = 2)
    private BigDecimal extraHrs;

    @Column(precision = 10, scale = 2)
    private BigDecimal basicKm;

    @Column(precision = 10, scale = 2)
    private BigDecimal baseFare;

    @Column(precision = 10, scale = 2)
    private BigDecimal actualKm;

    @Column(precision = 10, scale = 2)
    private BigDecimal extraKm;

    @Column(precision = 10, scale = 2)
    private BigDecimal perKmRate;

    @Column(precision = 10, scale = 2)
    private BigDecimal perHrRate;

    @Column(precision = 10, scale = 2)
    private BigDecimal extraHrRate;

    @Column(precision = 10, scale = 2)
    private BigDecimal extraKmRate;

    @Column(precision = 10, scale = 2)
    private BigDecimal tollAndParking;

    @Column(precision = 10, scale = 2)
    private BigDecimal netAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal advance;

    @Column(precision = 10, scale = 2)
    private BigDecimal balance;

    @Transient
    private int buyer_id;
/*
    @Column
    private int user_id;*/

    @OneToOne
    @JoinColumn(name = "buyer_id")
    private BuyerDetails buyerDetails;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserDetails userDetails;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private String updatedBy;

    @Column
    private BigDecimal rate=new BigDecimal(2.5);


    @Column
    private BigDecimal cgst;
    @Column
    private BigDecimal sgst;

    @Column
    private BigDecimal taxAmount;

    @Column
    private BigDecimal grandNetAmount;

    @Column
    private String grandNetAmountInwords;

    public void setTaxAmount() {

        this.cgst = this.netAmount.multiply(rate).divide(new BigDecimal(100));
        this.sgst = this.netAmount.multiply(rate).divide(new BigDecimal(100));;
        this.taxAmount=this.cgst.add(this.sgst);

    }

    public void setGrandNetAmount() {
        this.grandNetAmount=this.taxAmount.add(this.netAmount);
    }

    public void setGrandNetAmountInwords() {
        if(this.grandNetAmountInwords ==null)
            this.grandNetAmountInwords = NumberToWordsConverter.convertToWords(this.grandNetAmount);
    }
}