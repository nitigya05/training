package com.example.Invoice_Generator.uploadfile;

import com.example.Invoice_Generator.domain.TransportDetails;
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
public class TransportDetailsItemReader implements ItemStreamReader<TransportDetails> {

    private List<TransportDetails> transportDetailsList;
    private int nextTransportDetailIndex;
    private String filePath;  // Add this to accept the file path

    @Autowired
    private FileUploadService excelService;

    @Override
    public TransportDetails read() throws IOException {
        if (isNotInitialized()) {
            transportDetailsList = readExcelFile(filePath);
        }

        TransportDetails nextTransportDetail = null;

        if (nextTransportDetailIndex < transportDetailsList.size()) {
            nextTransportDetail = transportDetailsList.get(nextTransportDetailIndex);
            nextTransportDetailIndex++;
        } else {
            nextTransportDetailIndex = 0;
            transportDetailsList = null;
        }

        return nextTransportDetail;
    }

    private boolean isNotInitialized() {
        return this.transportDetailsList == null;
    }

    private List<TransportDetails> readExcelFile(String filePath) throws IOException {  // Use the dynamic file path
        List<TransportDetails> detailsList = new ArrayList<>();

        try (FileInputStream file = new FileInputStream(new File(filePath))) {  // Use filePath here
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue; // skip header row
                }
                TransportDetails details = excelService.mapRowToTransportDetails(row);
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
