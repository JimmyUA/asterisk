package callout;

import numbers.NumberTurnOverer;
import numbers.NumbersProvider;
import org.apache.log4j.Logger;
import org.asteriskjava.fastagi.DefaultAgiServer;
import org.asteriskjava.manager.*;
import org.asteriskjava.manager.action.HangupAction;
import org.asteriskjava.manager.action.OriginateAction;
import org.asteriskjava.manager.event.*;
import org.asteriskjava.manager.response.ManagerResponse;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
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
        LOGGER.info("Start calls out!");
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


        OriginateAction originateAction = new OriginateAction();
        String number = "0";
        if (numbersProvider.getNumbers().size() > 0) {
            number = numbersProvider.getNumber();
        }

        String ani = NumberTurnOverer.turnNumberOver(number);
        LOGGER.info("Number to call: " + number);
        originateAction.setChannel(channel + "/" + number);
        originateAction.setExten(extension);
        originateAction.setContext(context);
        originateAction.setPriority(1);
        originateAction.setCallerId(ani);
        originateAction.setVariable("Call_ID", CALL_ID);
        originateAction.setApplication("Agi");
        originateAction.setData("agi://localhost/hanguper.agi," + number);

        ManagerResponse response = managerConnection.sendAction(originateAction);

        LOGGER.info("Call_Id: " + CALL_ID);
        LOGGER.info(response.getResponse() + ": " + response.getMessage());


    }

    public void setManagerListener() {
        managerConnection.addEventListener(new ManagerEventListener() {
            public void onManagerEvent(ManagerEvent event) {
                if (event instanceof RtcpReceivedEvent) {
                    LOGGER.warn(((RtcpReceivedEvent) event).getFromAddress());
                } else if (event instanceof BridgeEvent) {
                    LOGGER.warn(((BridgeEvent) event).getCallerId1());
                } else if (event instanceof AgentConnectEvent) {
                    LOGGER.warn(((AgentConnectEvent) event).getVariables());
                } else if (event instanceof ConnectEvent) {
                    LOGGER.warn("Connect event");
                   LOGGER.info (( event).toString());
                } else if (event instanceof ParkedCallEvent) {
                    LOGGER.warn(((ParkedCallEvent) event).getUniqueId());
                } else if (event instanceof QueueEntryEvent) {
                    LOGGER.warn(((QueueEntryEvent) event).getCallerId());
                } else if (event instanceof QueueEvent) {
                    LOGGER.warn(((QueueEvent) event).getChannel());
                }
            }
        });
    }

    public void loginToManager() throws IOException, AuthenticationFailedException, TimeoutException {
        managerConnection.login();
    }

    public void stopConnection() {
        managerConnection.logoff();
    }

    public void setNumbersProvider() {
        numbersProvider.setNumbersSource(new File(numbersSource));
    }

    public static void main(String[] args) {

        CallOut callOut = new CallOut();
        callOut.setNumbersProvider();
        callOut.setManagerListener();
        try {
            callOut.loginToManager();
            while (true) {
            try {
                callOut.run();
                sleep(2000);
            } catch (Exception e) {
                LOGGER.info(e.getMessage());
                LOGGER.info(Arrays.deepToString(e.getStackTrace()));
                LOGGER.info(e.getClass());
            }
            }
        } catch (AuthenticationFailedException e) {
            LOGGER.info(e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            callOut.stopConnection();
        }
    }
}
