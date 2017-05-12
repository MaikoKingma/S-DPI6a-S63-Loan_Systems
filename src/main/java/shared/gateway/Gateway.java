package shared.gateway;

import javax.jms.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public abstract class Gateway {
    protected MessageSenderGateway sender;
    protected MessageReceiverGateway receiver;

    public Gateway(String senderChannel, String receiverChannel) {
        sender = new MessageSenderGateway(senderChannel);
        receiver = new MessageReceiverGateway(receiverChannel);

        System.out.println("set listner 1");
        receiver.setListener(m -> {
            System.out.println("set listner 2");
            if (m instanceof TextMessage)
            {
                try {
                    String body = ((TextMessage)m).getText();
                    System.out.println(">>> CorrolationId: " + m.getJMSCorrelationID() + " Message: " + body);
                    processMessage((TextMessage)m, m.getJMSCorrelationID());
                }
                catch (JMSException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    protected abstract void processMessage(TextMessage message, String CorrelationId) throws JMSException;
}
