import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.asteriskjava.manager.action.OriginateAction;

public class HelloAgiScript extends BaseAgiScript {
    public void service(AgiRequest agiRequest, AgiChannel agiChannel) throws AgiException {
        answer();
        agiRequest.getCallerIdName();
        streamFile("welcome");
        hangup();


    }
}
