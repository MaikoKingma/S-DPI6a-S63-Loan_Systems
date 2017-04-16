package forms.loanbroker;

import com.sun.istack.NotNull;
import shared.gateway.*;
import shared.bank.*;

import javax.jms.*;
import javax.xml.soap.Text;
import java.util.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class BankAppGateway {
    private List<Aggregation> aggregations;
    private MessageReceiverGateway receiver;
    @NotNull
    private int id = 0;

    private LoanBrokerFrame frame;
    private List<Rule> bankRules = new ArrayList<Rule>() {{
        add(new Rule("ING", null, 100000, null, 10));
        add(new Rule("ABNAmro", 200000, 300000, null, 20));
        add(new Rule("Rabobank", null, 250000, null, 15));
    }};

    public BankAppGateway(LoanBrokerFrame frame) {
        this.frame = frame;
        aggregations = new ArrayList<>();

        for (Rule rule : bankRules) {
            rule.setSender(new MessageSenderGateway(rule.getBankName() + "RequestQueue"));
        }
        receiver = new MessageReceiverGateway("bankIntrestReplyQueue");
        receiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                if (message instanceof TextMessage)
                {
                    try {
                        processMessage((TextMessage)message, message.getJMSCorrelationID());
                    }
                    catch (JMSException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public void sendBankRequest(BankInterestRequest request, String corrolationId) {
        int aggregationId = getAggregationID();
        int expectedReplies = 0;
        List<Rule> rulesToSend = new ArrayList<>();
        for (Rule rule : bankRules) {
            if (rule.check(request.getAmount(), request.getTime())) {
                expectedReplies++;
                rulesToSend.add(rule);
            }
        }
        aggregations.add(new Aggregation(aggregationId, expectedReplies, corrolationId));
        for (Rule rule : rulesToSend) {
            try {
                MessageSenderGateway sender = rule.getSender();
                Message message = sender.createTextMessage(request, corrolationId);
                message.setIntProperty("aggregationId", aggregationId);
                sender.send(message);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
    }

    private void processMessage(TextMessage message, String CorrelationId) throws JMSException {
        String body = message.getText();
        System.out.println(">>> CorrolationId: " + message.getJMSCorrelationID() + " Message: " + body);
        BankInterestReply bankInterestReply = new BankInterestReply();
        bankInterestReply.fillFromCommaSeperatedValue(body);
        Integer aggregationId = message.getIntProperty("aggregationId");
        if (aggregationId == null)
            return;
        Aggregation foundAggregation = null;
        for (Aggregation aggregation : aggregations) {
            if (aggregation.getId() == aggregationId) {
                foundAggregation = aggregation;
                break;
            }
        }
        if (foundAggregation != null) {
            foundAggregation.addBankInterestReply(bankInterestReply);
            if (foundAggregation.repliesReceived()){
                processAggregation(foundAggregation);
            }
        }
    }

    private void processAggregation(Aggregation aggregation) {
        aggregations.remove(aggregation);
        frame.add(aggregation.getCorrolationId(), aggregation.getBestReply());
    }

    @NotNull
    private int getAggregationID()
    {
        id++;
        return id;
    }
}
