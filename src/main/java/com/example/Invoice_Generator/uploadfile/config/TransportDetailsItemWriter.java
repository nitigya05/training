package com.example.Invoice_Generator.uploadfile.config;

import com.example.Invoice_Generator.domain.InvoiceDetails;
import com.example.Invoice_Generator.domain.dao.TransportDetailsRepo;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

@Component
public class TransportDetailsItemWriter implements ItemWriter<InvoiceDetails> {

    @Autowired
    private TransportDetailsRepo transportDetailsRepository;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void write(Chunk<? extends InvoiceDetails> chunk) throws Exception {
        // Convert the chunk to a List
        List<InvoiceDetails> invoiceDetailsList = (List<InvoiceDetails>) chunk.getItems();

        // Save all items to the repository
        transportDetailsRepository.saveAll(invoiceDetailsList);

        // Use the list for further processing
        //UtilityClass.saveZipFileLocally(transportDetailsList, templateEngine);
    }
}
