package com.example.Invoice_Generator.uploadfile;

import com.example.Invoice_Generator.domain.TransportDetails;
import com.example.Invoice_Generator.domain.dao.TransportDetailsRepo;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransportDetailsItemWriter implements ItemWriter<TransportDetails> {

    @Autowired
    private TransportDetailsRepo transportDetailsRepository;


    @Override
    public void write(Chunk<? extends TransportDetails> chunk) throws Exception {
        transportDetailsRepository.saveAll(chunk);

    }
}
