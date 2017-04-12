package forms.bank;

import shared.gateway.*;
import shared.bank.*;

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
         sender.send(sender.createTextMessage(reply, requests.get(request)));
    }

    @Override
    protected void processMessage(String message, String CorrelationId) {
        BankInterestRequest bankInterestRequest = new BankInterestRequest();
        bankInterestRequest.fillFromCommaSeperatedValue(message);
        requests.put(bankInterestRequest, CorrelationId);
        frame.add(bankInterestRequest);
    }
}
