package communicator;

import callslistener.AgiListener;
import org.asteriskjava.fastagi.DefaultAgiServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Applicatio {
    public static void main(String[] args) throws IOException {
        SpringApplication.run(Applicatio.class, args);
    }
}
