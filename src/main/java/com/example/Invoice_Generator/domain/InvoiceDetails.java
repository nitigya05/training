package com.example.Invoice_Generator.domain;

import com.example.Invoice_Generator.utility.NumberToWordsConverter;
import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "invoice_detailss")
public class InvoiceDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "invoice_no", nullable = false)
    private Long invoiceNo;

    @Column(name = "invoice_date", nullable = false)
    private LocalDate invoiceDate;

    @Column(name = "date_of_service")
    private LocalDate dateOfService;

    @Transient
    private int buyer_id;

    @OneToOne
    @JoinColumn(name = "buyer_id")
    private BuyerDetails buyerDetails;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserDetails userDetails;

    @Column(name = "billing_to", length = 255)
    private String billingTo;

    @Column(name = "gst_applicable", length = 10)
    private String gstApplicable;

    @Column(name = "gst_no", length = 50)
    private String gstNo;

    @Column(name = "client_name", length = 255)
    private String clientName;

    @Column(name = "client_address", columnDefinition = "TEXT")
    private String clientAddress;

    @Column(name = "origin", length = 255)
    private String origin;

    @Column(name = "destination", length = 255)
    private String destination;

    @Column(name = "no_of_days")
    private Integer noOfDays;

    @Column(name = "vehicle_no", length = 50)
    private String vehicleNo;

    @Column(name = "vehicle_type", length = 255)
    private String vehicleType;

    @Column(name = "driver_name", length = 255)
    private String driverName;

    @Column(name = "total_hrs", precision = 10, scale = 2)
    private BigDecimal totalHrs;

    @Column(name = "actual_hrs", precision = 10, scale = 2)
    private BigDecimal actualHrs;

    @Column(name = "extra_hrs", precision = 10, scale = 2)
    private BigDecimal extraHrs;

    @Column(name = "basic_km", precision = 10, scale = 2)
    private BigDecimal basicKm;

    @Column(name = "base_fare", precision = 10, scale = 2)
    private BigDecimal baseFare;

    @Column(name = "actual_km", precision = 10, scale = 2)
    private BigDecimal actualKm;

    @Column(name = "extra_km", precision = 10, scale = 2)
    private BigDecimal extraKm;

    @Column(name = "extra_per_km_rate", precision = 10, scale = 2)
    private BigDecimal extraPerKmRate;

    @Column(name = "extra_per_hr_rate", precision = 10, scale = 2)
    private BigDecimal extraPerHrRate;

    @Column(name = "extra_hr_rate", precision = 10, scale = 2)
    private BigDecimal extraHrRate;

    @Column(name = "extra_km_rate", precision = 10, scale = 2)
    private BigDecimal extraKmRate;

    @Column(name = "food_allowance", precision = 10, scale = 2)
    private BigDecimal foodAllowance;

    @Column(name = "toll_and_parking", precision = 10, scale = 2)
    private BigDecimal tollAndParking;

    @Column(name = "parking", precision = 10, scale = 2)
    private BigDecimal parking;

    @Column(name = "net_amount", precision = 10, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "cgst_percent", precision = 10, scale = 2)
    private BigDecimal cgstPercent;

    @Column(name = "sgst_percent", precision = 10, scale = 2)
    private BigDecimal sgstPercent;

    @Column(name = "igst_percent", precision = 10, scale = 2)
    private BigDecimal igstPercent;

    @Column(name = "cgst", precision = 10, scale = 2)
    private BigDecimal cgst;

    @Column(name = "sgst", precision = 10, scale = 2)
    private BigDecimal sgst;

    @Column(name = "igst", precision = 10, scale = 2)
    private BigDecimal igst;

    @Column(name = "gst_amount", precision = 10, scale = 2)
    private BigDecimal gstAmount;

    @Column(name = "total_amount_including_gst", precision = 10, scale = 2)
    private BigDecimal totalAmountIncludingGst;

    @Column(name = "advance", precision = 10, scale = 2)
    private BigDecimal advance;

    @Column(name = "balance")
    private Integer balance;

    @Column(name = "gst_applicable_1", length = 10)
    private String gstApplicable1;

    @Column
    private String grandNetAmountInwords;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private String createdBy;

    @Column
    private LocalDateTime updatedAt;

    @Column
    private String updatedBy;

    public void setGrandNetAmountInwords() {
        if(this.grandNetAmountInwords ==null)
            this.grandNetAmountInwords = NumberToWordsConverter.convertToWords(this.totalAmountIncludingGst);
    }
}

