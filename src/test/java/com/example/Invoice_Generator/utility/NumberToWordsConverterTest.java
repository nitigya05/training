package com.example.Invoice_Generator.utility;


import org.junit.jupiter.api.Test;

import java.math.BigDecimal;


public class NumberToWordsConverterTest {

@Test
    public  void  testdd(){
        BigDecimal d=new BigDecimal(8872.00);
        System.out.printf(""+NumberToWordsConverter.convertToWords(d));
    }
}
