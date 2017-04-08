package forms.loanclient;

import com.sun.istack.NotNull;
import shared.Gateway.*;
import shared.loan.*;
import shared.request.RequestReply;

import javax.jms.*;
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
        sender.send(sender.createTextMessage(request.getCommaSeperatedValue(), corrolationId));
    }

    @Override
    protected void processMessage(Message message) {
        if (message instanceof TextMessage)
        {
            try {
                String value = ((TextMessage) message).getText();
                System.out.println(">>> CorrolationId: " + message.getJMSCorrelationID() + " Message: " + value);
                LoanReply loanReply = new LoanReply();
                loanReply.fillFromCommaSeperatedValue(value);
                LoanRequest request = loanRequests.get(message.getJMSCorrelationID());
                frame.addReply(request, loanReply);
            }
            catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    @NotNull
    private String getCorrolationId()
    {
        Id++;
        return name + Integer.toString(Id);
    }
}
