package com.example.Invoice_Generator.uploadfile;

import com.example.Invoice_Generator.domain.TransportDetails;
import com.example.Invoice_Generator.domain.dao.TransportDetailsRepo;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.List;

@Component
public class TransportDetailsItemWriter implements ItemWriter<TransportDetails> {

    @Autowired
    private TransportDetailsRepo transportDetailsRepository;
    @Autowired
    private SpringTemplateEngine templateEngine;

    @Override
    public void write(Chunk<? extends TransportDetails> chunk) throws Exception {
        // Convert the chunk to a List
        List<TransportDetails> transportDetailsList = (List<TransportDetails>) chunk.getItems();

        // Save all items to the repository
        transportDetailsRepository.saveAll(transportDetailsList);

        // Use the list for further processing
        //UtilityClass.saveZipFileLocally(transportDetailsList, templateEngine);
    }
}
