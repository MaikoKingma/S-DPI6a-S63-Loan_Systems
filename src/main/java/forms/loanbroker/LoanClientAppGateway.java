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
        sender.send(sender.createTextMessage(reply, corrolationId));
    }

    @Override
    protected void processMessage(TextMessage message, String CorrelationId) throws JMSException {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.fillFromCommaSeperatedValue(message.getText());
        frame.add(loanRequest, CorrelationId);
    }
}
