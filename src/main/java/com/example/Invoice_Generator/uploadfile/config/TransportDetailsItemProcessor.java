package com.example.Invoice_Generator.uploadfile.config;


import com.example.Invoice_Generator.domain.InvoiceDetails;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
public class TransportDetailsItemProcessor implements ItemProcessor<InvoiceDetails, InvoiceDetails> {

    @Override
    public InvoiceDetails process(InvoiceDetails invoiceDetails) throws Exception {
        // Business logic here
        return invoiceDetails;
    }
}
