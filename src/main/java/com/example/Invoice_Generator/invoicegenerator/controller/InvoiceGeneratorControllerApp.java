package com.example.Invoice_Generator.invoicegenerator.controller;

import com.example.Invoice_Generator.domain.InvoiceDetails;
import com.example.Invoice_Generator.domain.InvoiceDetailsWORelationShip;
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
        InvoiceDetails invoiceDetails = repository.findById(id.intValue())
                .orElseThrow(() -> new IllegalArgumentException("Invalid transport ID: " + id));



        // Generate the PDF using Thymeleaf and Flying Saucer
        ByteArrayOutputStream pdfStream = UtilityClass.generateSinglePdf(invoiceDetails,templateEngine);

        // Set response headers for file download
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "attachment; filename=invoice_" + invoiceDetails.getInvoiceNo() + ".pdf");

        // Write the PDF to the response output stream
        response.getOutputStream().write(pdfStream.toByteArray());
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }


    // Handle Edit Form Submission
    @PostMapping("/edit/{id}")
    public String updateTransportDetails(@PathVariable("id") Long id, @ModelAttribute InvoiceDetails invoiceDetails) {
        invoiceDetails.setInvoiceNo(id);
        repository.save(invoiceDetails);
        return "redirect:/";
    }




    // Handle Delete
    @GetMapping("/delete/{id}")
    public String deleteTransportDetails(@PathVariable("id") Long id) {
        InvoiceDetails invoiceDetails = repository.findById(id.intValue()).orElseThrow(() -> new IllegalArgumentException("Invalid transport ID:" + id));
        repository.delete(invoiceDetails);
        return "redirect:/";
    }

    @GetMapping("/list")
    public String viewProductsPage(Model model,
                                   @RequestParam(defaultValue = "1") int page,
                                   @RequestParam(defaultValue = "50") int size) {
        Page<InvoiceDetailsWORelationShip> productPage = service.findPaginated(page, size);


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
    public String showUpdateForm(@PathVariable("id") long id, Model model) {
        // Fetch the invoice details by id
        InvoiceDetailsWORelationShip invoiceDetails = service.findById(id);

        if (invoiceDetails == null) {
            throw new IllegalArgumentException("Invalid invoice Id:" + id);
        }

        model.addAttribute("invoiceDetails", invoiceDetails);
        return "update-invoice";
    }

    @PostMapping("/update")
    public String updateInvoiceDetails(@ModelAttribute("invoiceDetails") InvoiceDetailsWORelationShip invoiceDetails) {
        // Update the invoice details in the database
        service.save(invoiceDetails);
        return "redirect:/invoice/list";  // Redirect to a list or confirmation page
    }

    @GetMapping("/search")
    public String searchInvoices(@RequestParam("searchTerm") String searchTerm, Model model,
                                 @RequestParam(value = "page", defaultValue = "0") int page,
                                 @RequestParam(value = "size", defaultValue = "10") int size) {
        Page<InvoiceDetailsWORelationShip> productPage = service.searchInvoices(searchTerm, page, size);

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
