package callslistener;

import org.asteriskjava.fastagi.DefaultAgiServer;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
    DefaultAgiServer defaultAgiServer = new DefaultAgiServer();
        defaultAgiServer.startup();
    }

}
