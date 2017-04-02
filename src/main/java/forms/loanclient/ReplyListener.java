package forms.loanclient;

import shared.loan.LoanReply;

import javax.jms.*;

/**
 * Created by Maiko on 2-4-2017.
 */
public class ReplyListener implements MessageListener {
    private LoanClientFrame frame;

    public ReplyListener(LoanClientFrame frame) {
        this.frame = frame;
    }

    @Override
    public void onMessage(Message message) {
        if (message instanceof TextMessage)
        {
            try {
                String value = ((TextMessage) message).getText();
                LoanReply loanReply = new LoanReply();
                loanReply.fillFromCommaSeperatedValue(value);
                frame.add(loanReply);
            }
            catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }
}
