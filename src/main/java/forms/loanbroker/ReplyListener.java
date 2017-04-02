package forms.loanbroker;

import shared.bank.BankInterestReply;
import shared.loan.LoanReply;

import javax.jms.*;

/**
 * Created by Maiko on 2-4-2017.
 */
public class ReplyListener implements MessageListener {
    private LoanBrokerFrame frame;

    public ReplyListener(LoanBrokerFrame frame) {
        this.frame = frame;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage)
        {
            try {
                String value = ((TextMessage) message).getText();
                BankInterestReply bankInterestReply = new BankInterestReply();
                bankInterestReply.fillFromCommaSeperatedValue(value);
                frame.add(bankInterestReply);

                LoanReply reply = new LoanReply(bankInterestReply.getInterest(), bankInterestReply.getQuoteId());
                frame.sendLoanReply(reply);
            }
            catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
