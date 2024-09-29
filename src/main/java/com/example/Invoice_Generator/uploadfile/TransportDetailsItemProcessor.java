package com.example.Invoice_Generator.uploadfile;


import com.example.Invoice_Generator.domain.TransportDetails;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TransportDetailsItemProcessor implements ItemProcessor<TransportDetails, TransportDetails> {

    @Override
    public TransportDetails process(TransportDetails transportDetails) throws Exception {
        // Business logic here
        return transportDetails;
    }
}
