package numbers;


import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class NumbersProviderTest {

    private NumbersProvider numbersProvider;
    private File sourseFile = new File("D:/numbers.txt");
    private String number = "1111111111\r\n222222222";

    @Before
    public void setUp() throws Exception {
        numbersProvider = new NumbersProvider();
        fillFile();
        numbersProvider.setNumbersSource(sourseFile);
    }

    private void fillFile() {
        FileOutputStream outputStream = null;
        try {
            outputStream = new FileOutputStream(sourseFile);
            outputStream.write(number.getBytes());
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }finally {
            try {
                outputStream.flush();
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    public void getsCorrectNumbers() throws Exception {
        assertEquals("1111111111", numbersProvider.getNumber());
        assertEquals("222222222", numbersProvider.getNumber());
        assertEquals("1111111111", numbersProvider.getNumber());


    }
}
