package forms.loanbroker;

import shared.gateway.*;
import shared.loan.*;

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
    protected void processMessage(String message, String CorrelationId) {
        LoanRequest loanRequest = new LoanRequest();
        loanRequest.fillFromCommaSeperatedValue(message);
        frame.add(loanRequest, CorrelationId);
    }
}
