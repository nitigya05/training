package com.example.Invoice_Generator.uploadfile.config;

import com.example.Invoice_Generator.domain.InvoiceDetails;
import com.example.Invoice_Generator.uploadfile.service.FileUploadService;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class TransportDetailsItemReader implements ItemStreamReader<InvoiceDetails> {

    private List<InvoiceDetails> invoiceDetailsList;
    private int nextTransportDetailIndex;
    private String filePath;  // Add this to accept the file path

    @Autowired
    private FileUploadService excelService;

    @Override
    public InvoiceDetails read() throws IOException {
        if (isNotInitialized()) {
            invoiceDetailsList = readExcelFile(filePath);
        }

        InvoiceDetails nextTransportDetail = null;

        if (nextTransportDetailIndex < invoiceDetailsList.size()) {
            nextTransportDetail = invoiceDetailsList.get(nextTransportDetailIndex);
            nextTransportDetailIndex++;
        } else {
            nextTransportDetailIndex = 0;
            invoiceDetailsList = null;
        }

        return nextTransportDetail;
    }

    private boolean isNotInitialized() {
        return this.invoiceDetailsList == null;
    }

    private List<InvoiceDetails> readExcelFile(String filePath) throws IOException {  // Use the dynamic file path
        List<InvoiceDetails> detailsList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(new File(filePath))) {  // Use filePath here
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // skip header row
                }
                InvoiceDetails details = excelService.mapRowToTransportDetails(row);
                detailsList.add(details);
            }

        } catch (IOException e) {
            e.printStackTrace();
            throw  e;
        }

        return detailsList;
    }

    // Add an ItemStream method to get the file path from JobParameters
    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        if (executionContext.containsKey("filePath")) {
            this.filePath = executionContext.getString("filePath");
        }
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {}

    @Override
    public void close() throws ItemStreamException {}

    public void setFilePath(String filePath) {
        this.filePath=filePath;
    }
}
