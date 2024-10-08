package com.example.Invoice_Generator.invoicegenerator.restcontroller;

import com.example.Invoice_Generator.domain.InvoiceDetails;
import com.example.Invoice_Generator.domain.dao.TransportDetailsRepo;
import com.example.Invoice_Generator.invoicegenerator.service.InvoiceGeneratorService;
import com.example.Invoice_Generator.utility.UtilityClass;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.List;

@RestController
public class InvoiceGeneratorRestControllerApp {


    @Autowired
    private InvoiceGeneratorService service;

    @Autowired
    private TransportDetailsRepo repository;

    @Autowired
    private SpringTemplateEngine templateEngine;


    @GetMapping("/api/user/{userid}")
    public List<InvoiceDetails> getAddress(@PathVariable int  userid) {
        List<InvoiceDetails> address =  service.findTransportDetailsbyUserId(userid);
        return address;
    }

    @GetMapping("/api/user")
    public List<InvoiceDetails> getAddress() {
        List<InvoiceDetails> address =  service.findTransportDetailsAll();
        return address;
    }


    @GetMapping("/api/generate-pdf")
    public void generatePdf(HttpServletResponse response) throws IOException, DocumentException {
        // Generate the first PDF

        List<InvoiceDetails> address = repository.findAll();

        // Save the ZIP file locally
        String zipFilePath = UtilityClass.saveZipFileLocally(address,templateEngine);
        // Send the ZIP file to the client
        UtilityClass.sendZipToClient(response, zipFilePath);
    }


}