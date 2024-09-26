package com.example.Invoice_Generator.invoicegenerator.service;

import com.example.Invoice_Generator.domain.dao.TransportDetailsRepo;
import com.example.Invoice_Generator.domain.TransportDetails;
import com.example.Invoice_Generator.domain.dao.UserDetailsRepo;
import jakarta.servlet.http.HttpServletResponse;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class InvoiceGeneratorService {

    @Autowired
    private TransportDetailsRepo repository;
    @Autowired
    private UserDetailsRepo userDetailsRepo;


    public void exportJasperReport(HttpServletResponse response) throws JRException, IOException {
        List<TransportDetails> address = repository.findAll();
        //Get file and compile it
        File file = ResourceUtils.getFile("C:/Users/nitig/Downloads/Invoice-Generatorw/Invoice-Generator/src/main/resources/hkjgfds.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(address);
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Simplifying Tech");
        //Fill Jasper report
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        //Export report
        JasperExportManager.exportReportToPdfStream(jasperPrint,response.getOutputStream());
    }


    public List<TransportDetails> findTransportDetailsAll() {
        return (List<TransportDetails>) repository.findAll();
    }

    public List<TransportDetails> findTransportDetailsbyUserId(int userid) {
        return (List<TransportDetails>) repository.findByUserDetails(userDetailsRepo.findById(userid));
    }
}