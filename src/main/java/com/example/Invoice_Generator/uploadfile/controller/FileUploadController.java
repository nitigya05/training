package com.example.Invoice_Generator.uploadfile.controller;


import com.example.Invoice_Generator.uploadfile.service.ExcelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("/user/invoice")
public class FileUploadController {

    @Autowired
    private ExcelService excelService;

    // Get method to return the upload page
    @GetMapping("/upload-page")
    public String showUploadPage() {
        return "upload";  // This refers to 'upload.html' in the templates folder
    }

    // Post method to handle file upload
    @PostMapping("/upload")
    public String uploadExcelFile(@RequestParam("file") MultipartFile file, Model model) {
        if (file.isEmpty()) {
            model.addAttribute("message", "Please upload a valid Excel file.");
            return "upload";
        }

        try {
            excelService.saveExcelData(file);
            model.addAttribute("message", "File uploaded and data saved successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("message", "Failed to upload and process file.");
        }
        return "upload";
    }
}
