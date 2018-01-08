package callout;

import numbers.NumbersProvider;
import org.apache.log4j.Logger;
import org.asteriskjava.manager.AuthenticationFailedException;
import org.asteriskjava.manager.ManagerConnection;
import org.asteriskjava.manager.ManagerConnectionFactory;
import org.asteriskjava.manager.TimeoutException;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.response.ManagerResponse;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.UUID;

import static java.lang.Thread.sleep;

public class CallOut {
    private String host = "localhost";
    private String userName = "manager";
    private String password = "Vika_Ruban";
    private ManagerConnection managerConnection;
    private final String CALL_ID = UUID.randomUUID().toString();
    private Properties properties;
    private String propertiesFilePath = "/var/outcalls/conf/calls.conf";

    private static final Logger LOGGER = Logger.getLogger(ClassName.getCurrentClassName());
    private String channel;
    private String context;
    private String extension;
    private String numbersSource;
    private NumbersProvider numbersProvider;

    public CallOut() {
        ManagerConnectionFactory managerConnectionFactory = new ManagerConnectionFactory(host, userName, password);
        managerConnection = managerConnectionFactory.createManagerConnection();
        try {
            setProperties();
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
        numbersProvider = new NumbersProvider();
    }

    private void setProperties() throws IOException {
        properties = new Properties();
        FileInputStream inputStream = new FileInputStream(new File(propertiesFilePath));
        properties.load(inputStream);
        channel = properties.getProperty("channel");
        LOGGER.info("Channel: " + channel + " is loaded");
        context = properties.getProperty("context");
        LOGGER.info("Context: " + context + " is loaded");
        extension = properties.getProperty("extension");
        LOGGER.info("Extension: " + extension + " is loaded");
        numbersSource = properties.getProperty("numbersSource");
        LOGGER.info("Numbers source: " + numbersSource + " is loaded");
    }

    public void run() throws AuthenticationFailedException, TimeoutException, IOException, InterruptedException {

        numbersProvider.setNumbersSource(new File(numbersSource));

        OriginateAction originateAction = new OriginateAction();
        originateAction.setChannel(channel + "/" + numbersProvider.getNumber());
        originateAction.setContext(context);
        originateAction.setExten(extension);
        originateAction.setPriority(1);
        originateAction.setCallerId("111111");
        originateAction.setVariable("Call_ID" ,CALL_ID);

        HangupAction hangupAction = new HangupAction();


        managerConnection.login();

        ManagerResponse response = managerConnection.sendAction(originateAction);
        LOGGER.info("Call_Id: " + CALL_ID);
        LOGGER.info(response.getResponse() + ": " + response.getMessage());

        sleep(1000);

        managerConnection.sendAction(hangupAction);

        managerConnection.logoff();
    }

    public static void main(String[] args) {
        while (true) {
            try {

                new CallOut().run();
                sleep(2000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
