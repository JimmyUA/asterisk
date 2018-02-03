package callslistener;

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


public class AgiListener extends BaseAgiScript {

    private final String USER_AGENT = "Mozilla/5.0";
    private static final Logger LOGGER = Logger.getLogger(ClassName.getCurrentClassName());

    @Override
    public void service(AgiRequest request, AgiChannel channel) throws AgiException {
        String ani = request.getCallerIdNumber();
        LOGGER.info(ani);
        String number = request.getDnid();
        LOGGER.info(number);
        LOGGER.info(request.getRequest());
        saveNumber(number, ani);
        answer();

        while (true){
            sayPhonetic("Rashacom");
        }
    }

    private void saveNumber(String number, String ani) {
        String request = "http://localhost:8080/save?number=" + number + "&" + "ani=" + ani;

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
        }catch (Exception e){
            LOGGER.error(e.getMessage());
            LOGGER.error(Arrays.toString(e.getStackTrace()));
        }
    }

}
