package com.example.Invoice_Generator.uploadfile.service;


import com.example.Invoice_Generator.domain.InvoiceDetails;
import com.example.Invoice_Generator.domain.dao.BankDetailsRepo;
import com.example.Invoice_Generator.domain.dao.BuyerDetailsRepo;
import com.example.Invoice_Generator.domain.dao.TransportDetailsRepo;
import com.example.Invoice_Generator.domain.dao.UserDetailsRepo;
import jakarta.transaction.Transactional;
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

    @Transactional
    public  List<InvoiceDetails> saveExcelData(MultipartFile file) {
        List<InvoiceDetails> invoiceDetails = new ArrayList<>();
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
                InvoiceDetails details = mapRowToTransportDetails(currentRow);

                transportDetailsRepository.save(details);
                invoiceDetails.add(details);
            }

            workbook.close();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read Excel file", e);
        }
        return invoiceDetails;
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

    public InvoiceDetails mapRowToTransportDetails(Row row) {
        InvoiceDetails invoiceDetails = new InvoiceDetails();

        try {
            // Skip cell 0 as it is the serial number (SR NO)

            // Mapping each cell in the row to the corresponding InvoiceDetails fields
            invoiceDetails.setInvoiceNo((long) row.getCell(1).getNumericCellValue()); // Invoice No
            invoiceDetails.setInvoiceDate(row.getCell(2).getLocalDateTimeCellValue().toLocalDate()); // Invoice Date
            invoiceDetails.setDateOfService(row.getCell(3).getLocalDateTimeCellValue().toLocalDate()); // Date of Service
            invoiceDetails.setBuyer_id((int) row.getCell(4).getNumericCellValue()); // Company Code
            invoiceDetails.setUserDetails(userDetailsRepo.getReferenceById(1));
            // Set Buyer Details using company code
            invoiceDetails.setBuyerDetails(buyerDetailsRepo.getById(invoiceDetails.getBuyer_id()));

            // Billing To (Assuming cell 5 contains the billing information)
            invoiceDetails.setBillingTo(row.getCell(5).getStringCellValue());

            // GST Applicable (Assuming cell 6 contains GST applicability)
            invoiceDetails.setGstApplicable(row.getCell(6).getStringCellValue());

            // GST No (Assuming cell 7 contains GST number)
            invoiceDetails.setGstNo(row.getCell(7).getStringCellValue());

            // Client Name (Assuming cell 8 contains the client name)
            invoiceDetails.setClientName(row.getCell(8).getStringCellValue());

            // Client Address (Assuming cell 9 contains the client address)
            invoiceDetails.setClientAddress(row.getCell(9).getStringCellValue());

            // Origin (Assuming cell 10 contains the origin)
            invoiceDetails.setOrigin(row.getCell(10).getStringCellValue());

            // Destination (Assuming cell 11 contains the destination)
            invoiceDetails.setDestination(row.getCell(11).getStringCellValue());

            // No of Days (Assuming cell 12 contains the number of days)
            invoiceDetails.setNoOfDays((int) row.getCell(12).getNumericCellValue());

            // Vehicle No (Assuming cell 13 contains the vehicle number)
            invoiceDetails.setVehicleNo(row.getCell(13).getStringCellValue());

            // Vehicle Type (Assuming cell 14 contains the vehicle type)
            invoiceDetails.setVehicleType(row.getCell(14).getStringCellValue());

            // Driver Name (Assuming cell 15 contains the driver's name)
            invoiceDetails.setDriverName(row.getCell(15).getStringCellValue());

            // Set the remaining numeric fields using BigDecimal
            invoiceDetails.setTotalHrs(getBigDecimalFromCell(row.getCell(16))); // Total Hrs
            invoiceDetails.setActualHrs(getBigDecimalFromCell(row.getCell(17))); // Actual Hrs
            invoiceDetails.setExtraHrs(getBigDecimalFromCell(row.getCell(18))); // Extra Hrs
            invoiceDetails.setBasicKm(getBigDecimalFromCell(row.getCell(19))); // Basic Km
            invoiceDetails.setBaseFare(getBigDecimalFromCell(row.getCell(20))); // Base Fare
            invoiceDetails.setActualKm(getBigDecimalFromCell(row.getCell(21))); // Actual Km
            invoiceDetails.setExtraKm(getBigDecimalFromCell(row.getCell(22))); // Extra Km
            invoiceDetails.setExtraPerKmRate(getBigDecimalFromCell(row.getCell(23))); // Extra Per Km Rate
            invoiceDetails.setExtraPerHrRate(getBigDecimalFromCell(row.getCell(24))); // Extra Per Hr Rate
            invoiceDetails.setExtraHrRate(getBigDecimalFromCell(row.getCell(25))); // Extra Hr Rate
            invoiceDetails.setExtraKmRate(getBigDecimalFromCell(row.getCell(26))); // Extra Km Rate
            invoiceDetails.setFoodAllowance(getBigDecimalFromCell(row.getCell(27))); // Food Allowance
            invoiceDetails.setTollAndParking(getBigDecimalFromCell(row.getCell(28))); // Toll and Parking
            invoiceDetails.setParking(getBigDecimalFromCell(row.getCell(29))); // Parking
            invoiceDetails.setNetAmount(getBigDecimalFromCell(row.getCell(30))); // Net Amount
            invoiceDetails.setCgstPercent(getBigDecimalFromCell(row.getCell(31))); // CGST %
            invoiceDetails.setSgstPercent(getBigDecimalFromCell(row.getCell(32))); // SGST %
            invoiceDetails.setIgstPercent(getBigDecimalFromCell(row.getCell(33))); // IGST %
            invoiceDetails.setCgst(getBigDecimalFromCell(row.getCell(34))); // CGST
            invoiceDetails.setSgst(getBigDecimalFromCell(row.getCell(35))); // SGST
            invoiceDetails.setIgst(getBigDecimalFromCell(row.getCell(36))); // IGST
            invoiceDetails.setGstAmount(getBigDecimalFromCell(row.getCell(37))); // GST Amount
            invoiceDetails.setTotalAmountIncludingGst(getBigDecimalFromCell(row.getCell(38))); // Total Amount Including GST
            invoiceDetails.setAdvance(getBigDecimalFromCell(row.getCell(39))); // Advance
            invoiceDetails.setBalance((int) row.getCell(40).getNumericCellValue()); // Balance
            invoiceDetails.setGstApplicable1(row.getCell(41).getStringCellValue()); // GST Applicable 1

            // Set created and updated timestamps
            invoiceDetails.setCreatedAt(LocalDateTime.now());
            invoiceDetails.setCreatedBy("1"); // Hardcoded createdBy value
            invoiceDetails.setUpdatedAt(LocalDateTime.now());
            invoiceDetails.setUpdatedBy("2"); // Hardcoded updatedBy value

            // Calculate grand net amount in words
            invoiceDetails.setGrandNetAmountInwords();

        } catch (Exception e) {
            System.out.println("Error while mapping row to InvoiceDetails: " + e.getMessage());
        }

        return invoiceDetails;
    }



    private BigDecimal getBigDecimalFromCell(Cell cell) {
        if (cell == null) {
            return BigDecimal.ZERO;
        }

        // Handle the cell based on its type
        switch (cell.getCellType()) {
            case NUMERIC:
                return BigDecimal.valueOf(cell.getNumericCellValue());
            case FORMULA:
                // Directly get the cached value of the formula
                if (cell.getCachedFormulaResultType() == CellType.NUMERIC) {
                    return BigDecimal.valueOf(cell.getNumericCellValue());
                } else {
                    return BigDecimal.ZERO;
                }
            default:
                return BigDecimal.ZERO;
        }
    }



}
