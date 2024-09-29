package com.example.Invoice_Generator.uploadfile.controller;


import com.example.Invoice_Generator.domain.TransportDetails;
import com.example.Invoice_Generator.uploadfile.service.FileUploadService;
import com.example.Invoice_Generator.utility.UtilityClass;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/user/invoice")
public class FileUploadController {

    @Autowired
    private FileUploadService excelService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    // Get method to return the upload page
    @GetMapping("/upload-page")
    public String showUploadPage() {
        return "upload";  // This refers to 'upload.html' in the templates folder
    }

    @PostMapping("/upload")
    public void uploadExcelFile(@RequestParam("file") MultipartFile file, HttpServletResponse response) {
        long startTime = System.currentTimeMillis();

        if (file.isEmpty()) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            try {
                response.getWriter().write("Please upload a valid Excel file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return;
        }

        try {
            // Save data from the Excel file to a list of TransportDetails
            List<TransportDetails> transportDetails = excelService.saveExcelData(file);

            // Save the ZIP file locally
            String zipFilePath = UtilityClass.saveZipFileLocally(transportDetails, templateEngine);

            // Check if the ZIP file was successfully created
            if (zipFilePath == null || zipFilePath.isEmpty()) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("Error occurred while saving ZIP file locally.");
                return;
            }

            // Send the ZIP file to the client
            UtilityClass.sendZipToClient(response, zipFilePath);

            long endTime = System.currentTimeMillis();
            System.out.println("Processing time: " + (endTime - startTime) + " ms");

        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            try {
                response.getWriter().write("Failed to upload and process file.");
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
        }
    }

}
