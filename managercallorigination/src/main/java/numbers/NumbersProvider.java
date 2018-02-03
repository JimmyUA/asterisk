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
    private String lineSeparator = getLineSeparator();

    public NumbersProvider() {
        numbers = new ArrayList<>();
    }

    public List<String> getNumbers() {
        return numbers;
    }

    private String getLineSeparator() {
        String osName = System.getProperty("os.name");
        LOGGER.info("OS: " + osName);
        if (osName.contains("Windows")){
            return "\r\n";
        } else {
            return "\r\n";
        }
    }

    private static final Logger LOGGER = Logger.getLogger(ClassName.getCurrentClassName());

    public void setNumbersSource(File numbersSource) {
        this.numbersSource = numbersSource;
        numbers = new ArrayList<>();
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
        String[] numbersArray = numbersFile.split(lineSeparator);
       LOGGER.info( Arrays.deepToString(numbersArray));
        numbers.addAll(Arrays.asList(numbersArray));
        LOGGER.info(numbers);

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

    public static void main(String[] args) {
        System.out.println(System.getProperty("os.name"));
    }
}
