package com.example.Invoice_Generator.invoicegenerator.controller;

import com.example.Invoice_Generator.domain.TransportDetails;
import com.example.Invoice_Generator.domain.dao.TransportDetailsRepo;
import com.example.Invoice_Generator.invoicegenerator.restcontroller.InvoiceGeneratorRestControllerApp;
import com.example.Invoice_Generator.invoicegenerator.service.InvoiceGeneratorService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/invoice")
public class InvoiceGeneratorControllerApp {

    @Autowired
    private InvoiceGeneratorService service;

    @Autowired
    private TransportDetailsRepo repository;

    @Autowired
    InvoiceGeneratorRestControllerApp invoiceGeneratorRestControllerApp;

    // List all TransportDetails
    @GetMapping("/list")
    public String viewTransportDetails(Model model) {
        List<TransportDetails> transportDetails = service.findTransportDetailsAll();
        model.addAttribute("transportDetails", transportDetails);
        return "invoicelist";
    }



    // Show Edit Form
    @GetMapping("/download/{id}")
    public void downloadPdfInvoiceById(HttpServletResponse response, @PathVariable("id") Long id) throws IOException, DocumentException, DocumentException {
        // Fetch the TransportDetails entity by ID
        TransportDetails transportDetails = repository.findById(id.intValue())
                .orElseThrow(() -> new IllegalArgumentException("Invalid transport ID: " + id));



        // Generate the PDF using Thymeleaf and Flying Saucer
        ByteArrayOutputStream pdfStream = invoiceGeneratorRestControllerApp.generateSinglePdf(transportDetails);

        // Set response headers for file download
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice_" + transportDetails.getInvoiceNo() + ".pdf");

        // Write the PDF to the response output stream
        response.getOutputStream().write(pdfStream.toByteArray());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }


    // Handle Edit Form Submission
    @PostMapping("/edit/{id}")
    public String updateTransportDetails(@PathVariable("id") Long id, @ModelAttribute TransportDetails transportDetails) {
        transportDetails.setInvoiceNo(id);
        repository.save(transportDetails);
        return "redirect:/";
    }

    // Handle Delete
    @GetMapping("/delete/{id}")
    public String deleteTransportDetails(@PathVariable("id") Long id) {
        TransportDetails transportDetails = repository.findById(id.intValue()).orElseThrow(() -> new IllegalArgumentException("Invalid transport ID:" + id));
        repository.delete(transportDetails);
        return "redirect:/";
    }
}
