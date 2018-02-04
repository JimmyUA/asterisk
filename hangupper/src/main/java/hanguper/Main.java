package hanguper;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.DefaultAgiServer;

import java.io.IOException;

public class Main {
    private static final Logger LOGGER = Logger.getLogger(ClassName.getCurrentClassName());


    public static void main(String[] args) {
    DefaultAgiServer defaultAgiServer = new DefaultAgiServer();
        try {
            defaultAgiServer.startup();
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
    }

}
