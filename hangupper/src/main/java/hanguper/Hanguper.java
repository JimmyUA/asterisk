package hanguper;

import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;


public class Hanguper extends BaseAgiScript {

    private final String USER_AGENT = "Mozilla/5.0";
    private static final Logger LOGGER = Logger.getLogger(ClassName.getCurrentClassName());


    public void service(AgiRequest request, AgiChannel channel) throws AgiException {
        LOGGER.info("Working");
        String ani = request.getCallerIdNumber();
        String[] arguments = request.getArguments();
        String calledNumber = arguments[0];
        LOGGER.info("got call ANI: " + ani + ", number : " + calledNumber);

        if (isConnectedCorrect(calledNumber, ani)) {
                LOGGER.info(ani + " : " + calledNumber + " - connected");
            while (true) {
                sayPhonetic("Rashacom");
            }
        } else {
            LOGGER.info(ani + " : " + calledNumber + " - hungup");
            hangup();
        }


    }

    private boolean isConnectedCorrect(String number, String ani) {
        String request = "http://5.189.169.161:8080/contains?number=" + number + "&" + "ani=" + ani;

        try {
            URL obj = new URL(request);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            // optional default is GET
            con.setRequestMethod("GET");

            //add request header
            con.setRequestProperty("User-Agent", USER_AGENT);

            BufferedReader in = new BufferedReader(
                    new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            LOGGER.info("response is " + response.toString());
            if (response.toString().equals("yes")) {
                return true;
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(Arrays.toString(e.getStackTrace()));
        }

        return false;
    }
}
