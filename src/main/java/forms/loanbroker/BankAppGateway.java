package forms.loanbroker;

import shared.gateway.*;
import shared.bank.*;

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
    protected void processMessage(String message, String CorrelationId) {
        BankInterestReply bankInterestReply = new BankInterestReply();
        bankInterestReply.fillFromCommaSeperatedValue(message);
        frame.add(CorrelationId, bankInterestReply);
    }
}
