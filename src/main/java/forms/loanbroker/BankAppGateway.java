package forms.loanbroker;

import shared.gateway.*;
import shared.bank.*;

import java.util.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class BankAppGateway extends Gateway {
    private LoanBrokerFrame frame;
    private List<Rule> bankRules = new ArrayList<Rule>() {{
        add(new Rule("ING", null, 100000, null, 10));
        add(new Rule("ABNAmro", 200000, 300000, null, 20));
        add(new Rule("Rabobank", null, 250000, null, 15));
    }};

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
