package forms.bank;

import shared.bank.BankInterestRequest;

import javax.jms.*;

/**
 * Created by Maiko on 2-4-2017.
 */
public class RequestListener implements MessageListener {
    private JMSBankFrame frame;

    public RequestListener(JMSBankFrame frame) {
        this.frame = frame;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage)
        {
            try {
                String value = ((TextMessage) message).getText();
                BankInterestRequest bankInterestRequest = new BankInterestRequest();
                bankInterestRequest.fillFromCommaSeperatedValue(value);
                frame.add(bankInterestRequest);
            }
            catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
