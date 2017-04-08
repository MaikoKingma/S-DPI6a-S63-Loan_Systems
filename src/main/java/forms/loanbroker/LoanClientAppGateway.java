package forms.loanbroker;

import shared.gateway.*;
import shared.loan.*;

import javax.jms.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class LoanClientAppGateway extends Gateway {
    private LoanBrokerFrame frame;

    public LoanClientAppGateway(LoanBrokerFrame frame) {
        super("loanReplyQueue", "loandRequestQueue");
        this.frame = frame;
    }

    public void sendLoanReply(LoanReply reply, String corrolationId) {
        sender.send(sender.createTextMessage(reply.getCommaSeperatedValue(), corrolationId));
    }

    @Override
    protected void processMessage(Message message) {
        try {
            String value = ((TextMessage) message).getText();
            System.out.println(">>> CorrolationId: " + message.getJMSCorrelationID() + " Message: " + value);
            LoanRequest loanRequest = new LoanRequest();
            loanRequest.fillFromCommaSeperatedValue(value);
            frame.add(loanRequest, message.getJMSCorrelationID());
        }
        catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
