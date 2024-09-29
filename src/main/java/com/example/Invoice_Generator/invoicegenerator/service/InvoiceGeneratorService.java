package com.example.Invoice_Generator.invoicegenerator.service;

import com.example.Invoice_Generator.domain.TransportDetails;
import com.example.Invoice_Generator.domain.TransportDetailsWORelationShip;
import com.example.Invoice_Generator.domain.dao.TransportDetailsRepo;
import com.example.Invoice_Generator.domain.dao.TransportDetailsWORelationShipRepo;
import com.example.Invoice_Generator.domain.dao.UserDetailsRepo;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class InvoiceGeneratorService {

    @Autowired
    private TransportDetailsRepo transportDetailsRepo;
    @Autowired
    private UserDetailsRepo userDetailsRepo;
    @Autowired
    private TransportDetailsWORelationShipRepo transportDetailsWORelationShipRepo;


    public void exportJasperReport(HttpServletResponse response) throws JRException, IOException {
        List<TransportDetails> address = transportDetailsRepo.findAll();
        //Get file and compile it
        File file = ResourceUtils.getFile("C:/Users/nitig/Downloads/Invoice-Generatorw/Invoice-Generator/src/main/resources/hkjgfds.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(address);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Simplifying Tech");
        //Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        //Export report
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }


    public List<TransportDetails> findTransportDetailsAll() {
        return (List<TransportDetails>) transportDetailsRepo.findAll();
    }

    public List<TransportDetails> findTransportDetailsbyUserId(int userid) {
        return (List<TransportDetails>) transportDetailsRepo.findByUserDetails(userDetailsRepo.findById(userid));
    }

    public Page<TransportDetailsWORelationShip> findPaginated(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo - 1, pageSize);
        return transportDetailsWORelationShipRepo.findAll(pageable);
    }


    public TransportDetailsWORelationShip findById(Long id) {
        Optional<TransportDetailsWORelationShip> optionalInvoice = transportDetailsWORelationShipRepo.findById(id);
        return optionalInvoice.orElse(null);
    }

    public void updateInvoice(Long id, TransportDetailsWORelationShip updatedInvoice) {
        Optional<TransportDetailsWORelationShip> existingInvoiceOpt = transportDetailsWORelationShipRepo.findById(id);
        if (existingInvoiceOpt.isPresent()) {
            TransportDetailsWORelationShip existingInvoice = existingInvoiceOpt.get();
            // Update fields as needed
            existingInvoice.setClientName(updatedInvoice.getClientName());
            existingInvoice.setOrigin(updatedInvoice.getOrigin());
            existingInvoice.setDestination(updatedInvoice.getDestination());
            existingInvoice.setNetAmount(updatedInvoice.getNetAmount());
            // Add other fields to update as needed

            transportDetailsWORelationShipRepo.save(existingInvoice);
        }
    }

    public Page<TransportDetailsWORelationShip> searchInvoices(String searchTerm, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return transportDetailsWORelationShipRepo.searchByMultipleColumns(searchTerm, pageable);
    }
}