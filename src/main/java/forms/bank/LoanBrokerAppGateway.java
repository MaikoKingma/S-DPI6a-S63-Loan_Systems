package forms.bank;

import shared.gateway.*;
import shared.bank.*;

import javax.jms.*;
import java.util.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class LoanBrokerAppGateway extends Gateway {
    private Map<BankInterestRequest, String> requests;
    private JMSBankFrame frame;

    public LoanBrokerAppGateway(JMSBankFrame frame) {
        super("bankIntrestReplyQueue", "bankIntrestRequestQueue");
        this.frame = frame;
        requests = new HashMap<>();
    }

    public void sendBankIntrestReply(BankInterestRequest request, BankInterestReply reply) {
         sender.send(sender.createTextMessage(reply.getCommaSeperatedValue(), requests.get(request)));
    }

    @Override
    protected void processMessage(Message message) {
        if (message instanceof TextMessage)
        {
            try {
                String value = ((TextMessage) message).getText();
                System.out.println(">>> CorrolationId: " + message.getJMSCorrelationID() + " Message: " + value);
                BankInterestRequest bankInterestRequest = new BankInterestRequest();
                bankInterestRequest.fillFromCommaSeperatedValue(value);
                requests.put(bankInterestRequest, message.getJMSCorrelationID());
                frame.add(bankInterestRequest);
            }
            catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
