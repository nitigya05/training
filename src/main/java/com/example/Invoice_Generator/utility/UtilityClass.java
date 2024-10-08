package com.example.Invoice_Generator.utility;

import com.example.Invoice_Generator.domain.InvoiceDetails;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class UtilityClass {

    public static String saveZipFileLocally(List<InvoiceDetails> invoiceDetails, SpringTemplateEngine templateEngine) throws IOException, DocumentException {
       System.out.println("inside saveZipFileLocally "+ invoiceDetails.size());
        // Define a custom date-time format without colons
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");

        // Get the current timestamp in the valid format
        String timestamp = LocalDateTime.now().format(formatter);

        // Define the local path to save the ZIP file
        String zipFilePath = "C:/temp/invoices_" + timestamp + ".zip";
        // Create the directory if it doesn't exist
        Path directoryPath = Paths.get("C:/temp");
        if (!Files.exists(directoryPath)) {
            Files.createDirectories(directoryPath);
        }

        // Save the ZIP file locally
        try (FileOutputStream fileOut = new FileOutputStream(zipFilePath);
             ZipOutputStream zipOut = new ZipOutputStream(fileOut)) {

            // Iterate through the list of TransportDetails and generate PDFs
            for (int i = 0; i < invoiceDetails.size(); i++) {
                // Generate a single PDF for each transport detail
                ByteArrayOutputStream pdf = generateSinglePdf(invoiceDetails.get(i),templateEngine);

                // Add the PDF to the zip file with a unique name
                String pdfFileName = "invoice" + invoiceDetails.get(i).getInvoiceNo() + ".pdf";
                addPdfToZip(pdfFileName, pdf, zipOut);
            }

            zipOut.finish();
        }

        return zipFilePath;
    }
    public static ByteArrayOutputStream generateSinglePdf(InvoiceDetails invoiceDetails, SpringTemplateEngine templateEngine) throws IOException, DocumentException {
        // Create a Thymeleaf context and set variables

        Context context = new Context();
        context.setVariable("invoiceDetails", invoiceDetails);

        // Render the Thymeleaf template into HTML
        String renderedHtmlContent = templateEngine.process("invoice", context);

        // Convert HTML to PDF using Flying Saucer
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocumentFromString(renderedHtmlContent);
        renderer.layout();
        renderer.createPDF(outputStream, false);
        renderer.finishPDF();

        return outputStream;
    }


    public static  void sendZipToClient(HttpServletResponse response, String zipFilePath) throws IOException {
        // Set response content type to ZIP
        response.setContentType("application/zip");
        response.setHeader("Content-Disposition", "attachment; filename=Invoice"+LocalDateTime.now()+".zip");

        // Send the local ZIP file to the client
        try (InputStream fileInputStream = new FileInputStream(zipFilePath)) {
            org.apache.commons.io.IOUtils.copy(fileInputStream, response.getOutputStream());
            response.getOutputStream().flush();
        }
    }

    public static void addPdfToZip(String fileName, ByteArrayOutputStream pdfContent, ZipOutputStream zipOut) throws IOException {
        // Create a new ZipEntry for each PDF file
        ZipEntry zipEntry = new ZipEntry(fileName);
        zipOut.putNextEntry(zipEntry);

        // Write the PDF content to the zip entry
        ByteArrayInputStream inputStream = new ByteArrayInputStream(pdfContent.toByteArray());
        org.apache.commons.io.IOUtils.copy(inputStream, zipOut);
        zipOut.closeEntry();
    }
}
