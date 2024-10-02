package com.example.Invoice_Generator.uploadfile.service;


import com.example.Invoice_Generator.domain.TransportDetails;
import com.example.Invoice_Generator.domain.dao.BankDetailsRepo;
import com.example.Invoice_Generator.domain.dao.BuyerDetailsRepo;
import com.example.Invoice_Generator.domain.dao.TransportDetailsRepo;
import com.example.Invoice_Generator.domain.dao.UserDetailsRepo;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class FileUploadService {

    @Autowired
    private TransportDetailsRepo transportDetailsRepository;

    @Autowired
    private UserDetailsRepo userDetailsRepo;

    @Autowired
    private BankDetailsRepo bankDetailsRepo;

    @Autowired
    private BuyerDetailsRepo buyerDetailsRepo;

    public  List<TransportDetails> saveExcelData(MultipartFile file) {
        List<TransportDetails> transportDetails= new ArrayList<>();
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = new XSSFWorkbook(inputStream);
            Sheet sheet = workbook.getSheetAt(0);
            Iterator<Row> rows = sheet.iterator();

            // Skip header row if there is any
            if (rows.hasNext()) {
                rows.next();
            }

            while (rows.hasNext()) {
                Row currentRow = rows.next();
                if (isRowEmpty(currentRow)) {
                    continue; // Skip this row and move to the next one
                }
                System.out.println("Processing row: " + currentRow.getRowNum());
                TransportDetails details = mapRowToTransportDetails(currentRow);
                transportDetailsRepository.save(details);
                transportDetails.add(details);
            }

            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }
        return transportDetails;
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true; // The row itself is null
        }

        // Iterate over each cell in the row and check if it's empty or null
        for (int cellNum = row.getFirstCellNum(); cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);

            if (cell != null && cell.getCellType() != CellType.BLANK) {
                switch (cell.getCellType()) {
                    case STRING:
                        if (!cell.getStringCellValue().trim().isEmpty()) {
                            return false; // Non-empty string found
                        }
                        break;
                    case NUMERIC:
                        return false; // Numeric value found
                    case BOOLEAN:
                        return false; // Boolean value found
                    case FORMULA:
                        return false; // Formula found
                    default:
                        break;
                }
            }
        }

        return true; // The row is empty if no non-empty cells are found
    }



    public TransportDetails mapRowToTransportDetails(Row row) {
  System.out.println("inside mapRowToTransportDetails" +row.getCell(2).getLocalDateTimeCellValue().toLocalDate());
        TransportDetails transportDetails = new TransportDetails();
//        details.setSrNo(row.getCell(0).getStringCellValue());
//        details.setInvoiceNo(row.getCell(1).getStringCellValue());
        transportDetails.setDate(row.getCell(2).getLocalDateTimeCellValue().toLocalDate());
        transportDetails.setBuyer_id((int) row.getCell(3).getNumericCellValue());
        transportDetails.setClientName(row.getCell(4).getStringCellValue());
        transportDetails.setOrigin(row.getCell(5).getStringCellValue());
        transportDetails.setDestination(row.getCell(6).getStringCellValue());
        transportDetails.setNoOfDays((int) row.getCell(7).getNumericCellValue());
        transportDetails.setVehicleNo(row.getCell(8).getStringCellValue());
        transportDetails.setVehicleType(row.getCell(9).getStringCellValue());
        transportDetails.setDriverName(row.getCell(10).getStringCellValue());
        transportDetails.setCreatedAt(LocalDateTime.now());
        transportDetails.setTotalHrs(new BigDecimal(row.getCell(11).getNumericCellValue()));
        transportDetails.setActualHrs(new BigDecimal(row.getCell(12).getNumericCellValue()));
        transportDetails.setExtraHrs(new BigDecimal(row.getCell(13).getNumericCellValue()));
        transportDetails.setBasicKm(new BigDecimal(row.getCell(14).getNumericCellValue()));
        transportDetails.setBaseFare(new BigDecimal(row.getCell(15).getNumericCellValue()));
        transportDetails.setActualKm(new BigDecimal(row.getCell(16).getNumericCellValue()));
        transportDetails.setExtraKm(new BigDecimal(row.getCell(17).getNumericCellValue()));
        transportDetails.setPerKmRate(new BigDecimal(row.getCell(18).getNumericCellValue()));
        transportDetails.setPerHrRate(new BigDecimal(row.getCell(19).getNumericCellValue()));
        transportDetails.setExtraHrRate(new BigDecimal(row.getCell(20).getNumericCellValue()));
        transportDetails.setExtraKmRate(new BigDecimal(row.getCell(21).getNumericCellValue()));
        transportDetails.setTollAndParking(new BigDecimal(row.getCell(22).getNumericCellValue()));
        transportDetails.setNetAmount(new BigDecimal(row.getCell(23).getNumericCellValue()));
        transportDetails.setAdvance(new BigDecimal(row.getCell(24).getNumericCellValue()));
        transportDetails.setBalance(new BigDecimal(row.getCell(25).getNumericCellValue()));
        transportDetails.setCreatedBy("1");
        transportDetails.setUserDetails(userDetailsRepo.getById(1));
        transportDetails.setBuyerDetails(buyerDetailsRepo.getById(transportDetails.getBuyer_id()));
        transportDetails.setCreatedBy("1");
        transportDetails.setTaxAmount();
        transportDetails.setGrandNetAmount();
        transportDetails.setGrandNetAmountInwords();
        return transportDetails;
    }
}
