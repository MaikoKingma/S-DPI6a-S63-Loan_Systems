package forms.bank;

import shared.gateway.*;
import shared.bank.*;

import javax.jms.*;
import java.util.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class LoanBrokerAppGateway extends Gateway {
    private List<Request> requests;
    private JMSBankFrame frame;

    public LoanBrokerAppGateway(JMSBankFrame frame, String bankName) {
        super("bankIntrestReplyQueue", bankName + "RequestQueue");
        this.frame = frame;
        requests = new ArrayList<>();
    }

    public void sendBankIntrestReply(BankInterestRequest bankInterestRequest, BankInterestReply reply) {
        for (Request request : requests) {
            if (request.getBankInterestRequest() == bankInterestRequest) {
                try {
                    Message message = sender.createTextMessage(reply, request.getCorrolationId());
                    message.setIntProperty("aggregationId", request.getAggregationId());
                    sender.send(message);
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }

    @Override
    protected void processMessage(TextMessage message, String CorrelationId) throws JMSException {
        Integer aggregationId = message.getIntProperty("aggregationId");
        if (aggregationId == null)
            return;

        BankInterestRequest bankInterestRequest = new BankInterestRequest();
        bankInterestRequest.fillFromCommaSeperatedValue(message.getText());
        requests.add(new Request(message.getJMSCorrelationID(), bankInterestRequest, aggregationId));
        frame.add(bankInterestRequest);
    }
}
