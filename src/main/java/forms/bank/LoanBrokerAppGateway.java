package forms.bank;

import shared.gateway.*;
import shared.bank.*;

import javax.jms.JMSException;
import javax.jms.TextMessage;
import java.util.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class LoanBrokerAppGateway extends Gateway {
    private Map<BankInterestRequest, String> requests;
    private JMSBankFrame frame;

    public LoanBrokerAppGateway(JMSBankFrame frame, String bankName) {
        super("bankIntrestReplyQueue", bankName + "RequestQueue");
        this.frame = frame;
        requests = new HashMap<>();
    }

    public void sendBankIntrestReply(BankInterestRequest request, BankInterestReply reply) {
         sender.send(sender.createTextMessage(reply, requests.get(request)));
    }

    @Override
    protected void processMessage(TextMessage message, String CorrelationId) throws JMSException {
        BankInterestRequest bankInterestRequest = new BankInterestRequest();
        bankInterestRequest.fillFromCommaSeperatedValue(message.getText());
        requests.put(bankInterestRequest, CorrelationId);
        frame.add(bankInterestRequest);
    }
}
