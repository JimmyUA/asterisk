package numbers;

import callout.ClassName;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class NumbersProvider {
    private File numbersSource;
    private List<String> numbers;
    private int counter;

    private static final Logger LOGGER = Logger.getLogger(ClassName.getCurrentClassName());

    public void setNumbersSource(File numbersSource) {
        this.numbersSource = numbersSource;
        numbers = new ArrayList<String>();
        fillNumbersList();
    }

    private void fillNumbersList() {
        counter = 0;
        String numbersFile;
        try {
              numbersFile = parseFile();
        } catch (IOException e){
            LOGGER.error(e.getMessage());
            throw new RuntimeException(e);
        }
        String[] numbersArray = numbersFile.split("\r\n");
        numbers.addAll(Arrays.asList(numbersArray));
    }

    private String parseFile() throws IOException {
        FileInputStream fileInputStream = new FileInputStream(numbersSource);
        StringBuilder result = new StringBuilder();
        int symbol;
        while ((symbol = fileInputStream.read()) != -1){
            result.append((char) symbol);
        }
        return result.toString();
    }

    public String getNumber(){
        if (counter == numbers.size()){
            counter = 0;
        }
        return numbers.get(counter++);
    }
}
