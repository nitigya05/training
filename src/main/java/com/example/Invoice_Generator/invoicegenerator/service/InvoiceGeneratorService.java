package com.example.Invoice_Generator.invoicegenerator.service;

import com.example.Invoice_Generator.domain.InvoiceDetails;
import com.example.Invoice_Generator.domain.InvoiceDetailsWORelationShip;
import com.example.Invoice_Generator.domain.dao.TransportDetailsRepo;
import com.example.Invoice_Generator.domain.dao.TransportDetailsWORelationShipRepo;
import com.example.Invoice_Generator.domain.dao.UserDetailsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InvoiceGeneratorService {

    @Autowired
    private TransportDetailsRepo transportDetailsRepo;
    @Autowired
    private UserDetailsRepo userDetailsRepo;
    @Autowired
    private TransportDetailsWORelationShipRepo transportDetailsWORelationShipRepo;


    public List<InvoiceDetails> findTransportDetailsAll() {
        return (List<InvoiceDetails>) transportDetailsRepo.findAll();
    }

    public List<InvoiceDetails> findTransportDetailsbyUserId(int userid) {
        return (List<InvoiceDetails>) transportDetailsRepo.findByUserDetails(userDetailsRepo.findById(userid));
    }

    public Page<InvoiceDetailsWORelationShip> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return transportDetailsWORelationShipRepo.findAll(pageable);
    }


    public InvoiceDetailsWORelationShip findById(Long id) {
        Optional<InvoiceDetailsWORelationShip> optionalInvoice = transportDetailsWORelationShipRepo.findById(id);
        return optionalInvoice.orElse(null);
    }

    public Page<InvoiceDetailsWORelationShip> searchInvoices(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return null;
    }

    public void save(InvoiceDetailsWORelationShip invoiceDetails) {
        transportDetailsWORelationShipRepo.save(invoiceDetails);
    }
}