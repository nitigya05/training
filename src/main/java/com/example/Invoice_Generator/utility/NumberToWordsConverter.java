package com.example.Invoice_Generator.utility;
import java.math.BigDecimal;

public class NumberToWordsConverter {

    private static final String[] tensNames = {
            "", " ten", " twenty", " thirty", " forty", " fifty", " sixty", " seventy", " eighty", " ninety"
    };

    private static final String[] numNames = {
            "", " one", " two", " three", " four", " five", " six", " seven", " eight", " nine", " ten", " eleven",
            " twelve", " thirteen", " fourteen", " fifteen", " sixteen", " seventeen", " eighteen", " nineteen"
    };

    private static String convertLessThanOneThousand(int number) {
        String current;

        if (number % 100 < 20) {
            current = numNames[number % 100];
            number /= 100;
        } else {
            current = numNames[number % 10];
            number /= 10;

            current = tensNames[number % 10] + current;
            number /= 10;
        }
        if (number == 0) return current;
        return numNames[number] + " hundred" + current;
    }

    public static String convert(long number) {
        if (number == 0) {
            return "zero";
        }

        String snumber = Long.toString(number);

        // pad with "0" for consistency
        String mask = "000000000000";
        snumber = String.format("%12s", snumber).replace(' ', '0');

        int billions = Integer.parseInt(snumber.substring(0, 3));
        int millions  = Integer.parseInt(snumber.substring(3, 6));
        int hundredThousands = Integer.parseInt(snumber.substring(6, 9));
        int thousands = Integer.parseInt(snumber.substring(9, 12));

        String tradBillions = (billions > 0) ? convertLessThanOneThousand(billions) + " billion " : "";
        String tradMillions = (millions > 0) ? convertLessThanOneThousand(millions) + " million " : "";
        String tradHundredThousands = (hundredThousands > 0)
                ? (hundredThousands == 1 ? "one thousand " : convertLessThanOneThousand(hundredThousands) + " thousand ")
                : "";
        String tradThousand = convertLessThanOneThousand(thousands);

        return (tradBillions + tradMillions + tradHundredThousands + tradThousand).trim();
    }

    public static String convertToWords(BigDecimal amount) {
        long rupees = amount.longValue();  // Extract rupees
        int paisa = amount.remainder(BigDecimal.ONE).multiply(BigDecimal.valueOf(100)).intValue();  // Extract paisa

        // Convert rupees and paisa to words
        String rupeesInWords = capitalizeFirstLetter(convert(rupees));
        String paisaInWords = (paisa > 0) ? " and " + convert(paisa) + " Paisa" : "";

        // Construct the final string with INR, Rupees, and Paisa
        return "INR " + rupeesInWords + " Rupees" + paisaInWords + " Only.";
    }

    // Helper method to capitalize the first letter of the words
    private static String capitalizeFirstLetter(String input) {
        if (input == null || input.isEmpty()) {
            return input;
        }
        return Character.toUpperCase(input.charAt(0)) + input.substring(1);
    }
}
