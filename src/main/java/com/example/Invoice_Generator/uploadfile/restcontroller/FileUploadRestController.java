package com.example.Invoice_Generator.uploadfile.restcontroller;

import com.example.Invoice_Generator.uploadfile.service.FileUploadService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.io.File;

@RestController
@RequestMapping("/api/invoice")
public class FileUploadRestController {

    @Autowired
    private FileUploadService excelService;

    @Autowired
    private SpringTemplateEngine templateEngine;

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;


    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            long startTime= System.currentTimeMillis();
            // Get the temp directory and ensure it exists
            String tmpDir = System.getProperty("java.io.tmpdir");
            if (tmpDir == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Temp directory not available.");
            }

            // Create a temporary file
            File convFile = new File(tmpDir + "/" + file.getOriginalFilename());

            // Check if the uploaded file is not null
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Empty file uploaded.");
            }

            // Transfer the file to the server's temp directory
            file.transferTo(convFile);

            JobParameters jobParameters = new JobParametersBuilder()
                    .addString("filePath", convFile.getAbsolutePath())  // Ensure this path is correct
                    .addLong("startAt", System.currentTimeMillis())
                    .toJobParameters();

            // Run the batch job
            jobLauncher.run(job, jobParameters);
            long endTime = System.currentTimeMillis();
            System.out.println("Processing time: " + (endTime - startTime) + " ms");
            return ResponseEntity.ok("File uploaded and batch process started.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload and process the file.");
        }
    }

}
