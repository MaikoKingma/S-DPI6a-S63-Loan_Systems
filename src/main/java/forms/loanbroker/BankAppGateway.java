package forms.loanbroker;

import shared.gateway.*;
import shared.bank.*;

import javax.jms.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class BankAppGateway extends Gateway {
    private LoanBrokerFrame frame;

    public BankAppGateway(LoanBrokerFrame frame) {
        super("bankIntrestRequestQueue", "bankIntrestReplyQueue");
        this.frame = frame;
    }

    public void sendBankRequest(BankInterestRequest request, String corrolationId) {
        sender.send(sender.createTextMessage(request, corrolationId));
    }

    @Override
    protected void processMessage(Message message) {
        if (message instanceof TextMessage)
        {
            try {
                String value = ((TextMessage) message).getText();
                System.out.println(">>> CorrolationId: " + message.getJMSCorrelationID() + " Message: " + value);
                BankInterestReply bankInterestReply = new BankInterestReply();
                bankInterestReply.fillFromCommaSeperatedValue(value);
                frame.add(message.getJMSCorrelationID(), bankInterestReply);
            }
            catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
