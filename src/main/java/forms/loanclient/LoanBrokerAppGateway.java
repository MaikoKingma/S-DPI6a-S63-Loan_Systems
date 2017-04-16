package forms.loanclient;

import com.sun.istack.NotNull;
import shared.gateway.*;
import shared.loan.*;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class LoanBrokerAppGateway extends Gateway {
    private LoanClientFrame frame;
    private Map<String, LoanRequest> loanRequests;
    @NotNull
    private String name = "clientName";
    @NotNull
    private int Id = 0;

    public LoanBrokerAppGateway(LoanClientFrame frame) {
        super("loandRequestQueue", "loanReplyQueue");
        this.frame = frame;
        loanRequests = new HashMap<>();
    }

    public void applyForLoan(LoanRequest request) {
        String corrolationId = getCorrolationId();
        loanRequests.put(corrolationId, request);
        sender.send(sender.createTextMessage(request, corrolationId));
    }

    @Override
    protected void processMessage(TextMessage message, String CorrelationId) throws JMSException {
        LoanReply loanReply = new LoanReply();
        loanReply.fillFromCommaSeperatedValue(message.getText());
        LoanRequest request = loanRequests.get(CorrelationId);
        frame.addReply(request, loanReply);
    }

    @NotNull
    private String getCorrolationId()
    {
        Id++;
        return name + Integer.toString(Id);
    }
}
