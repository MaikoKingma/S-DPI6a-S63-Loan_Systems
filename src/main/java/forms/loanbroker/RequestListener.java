package forms.loanbroker;

import shared.bank.BankInterestRequest;
import shared.loan.LoanRequest;

import javax.jms.*;

/**
 * Created by Maiko on 1-4-2017.
 */
public class RequestListener implements MessageListener {
    private LoanBrokerFrame frame;

    public RequestListener(LoanBrokerFrame frame) {
        this.frame = frame;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage)
        {
            try {
                String value = ((TextMessage) message).getText();
                LoanRequest loanRequest = new LoanRequest();
                loanRequest.fillFromCommaSeperatedValue(value);
                frame.add(loanRequest);
                BankInterestRequest request = new BankInterestRequest(loanRequest.getAmount(), loanRequest.getTime());
                frame.sendBankInterestRequest(request);
                frame.add(loanRequest, request);
            }
            catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
