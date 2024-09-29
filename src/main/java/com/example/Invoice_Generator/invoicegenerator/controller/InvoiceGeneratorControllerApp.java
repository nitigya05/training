package com.example.Invoice_Generator.invoicegenerator.controller;

import com.example.Invoice_Generator.domain.TransportDetails;
import com.example.Invoice_Generator.domain.TransportDetailsWORelationShip;
import com.example.Invoice_Generator.domain.dao.TransportDetailsRepo;
import com.example.Invoice_Generator.invoicegenerator.restcontroller.InvoiceGeneratorRestControllerApp;
import com.example.Invoice_Generator.invoicegenerator.service.InvoiceGeneratorService;
import com.example.Invoice_Generator.utility.UtilityClass;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@Controller
@RequestMapping("/invoice")
public class InvoiceGeneratorControllerApp {

    @Autowired
    private InvoiceGeneratorService service;

    @Autowired
    private TransportDetailsRepo repository;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    InvoiceGeneratorRestControllerApp invoiceGeneratorRestControllerApp;




    // Show Edit Form
    @GetMapping("/download/{id}")
    public void downloadPdfInvoiceById(HttpServletResponse response, @PathVariable("id") Long id) throws IOException, DocumentException, DocumentException {
        // Fetch the TransportDetails entity by ID
        TransportDetails transportDetails = repository.findById(id.intValue())
                .orElseThrow(() -> new IllegalArgumentException("Invalid transport ID: " + id));



        // Generate the PDF using Thymeleaf and Flying Saucer
        ByteArrayOutputStream pdfStream = UtilityClass.generateSinglePdf(transportDetails,templateEngine);

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

    @GetMapping("/list")
    public String viewProductsPage(Model model,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "50") int size) {
        Page<TransportDetailsWORelationShip> productPage = service.findPaginated(page, size);


        int startPage = Math.max(1, page - 5); // Show 2 pages before the current page
        int endPage = Math.min(productPage.getTotalPages(), page +10); // Show 2 pages after the current page

// Pass these to the model
        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "invoicelist";
    }


    // Show the update form
    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") Long id, Model model) {
        TransportDetailsWORelationShip invoice = service.findById(id);
        if (invoice == null) {
            // Handle invoice not found (you can return an error page if needed)
            return "error";
        }
        model.addAttribute("invoice", invoice);
        return "update-invoice";
    }

    // Handle the update form submission
    @PostMapping("/update/{id}")
    public String updateInvoice(@PathVariable("id") Long id, @ModelAttribute("invoice") TransportDetailsWORelationShip updatedInvoice) {
        service.updateInvoice(id, updatedInvoice);
        return "redirect:/invoices/list"; // Redirect to a list page or success page
    }

    @GetMapping("/search")
    public String searchInvoices(@RequestParam("searchTerm") String searchTerm, Model model,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<TransportDetailsWORelationShip> productPage = service.searchInvoices(searchTerm, page, size);

        int startPage = Math.max(1, page - 5); // Show 2 pages before the current page
        int endPage = Math.min(productPage.getTotalPages(), page +10); // Show 2 pages after the current page

        model.addAttribute("startPage", startPage);
        model.addAttribute("endPage", endPage);
        model.addAttribute("productPage", productPage);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        return "invoicelist";
    }


}
