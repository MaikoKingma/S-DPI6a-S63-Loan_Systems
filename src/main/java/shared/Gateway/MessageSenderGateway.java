package shared.Gateway;

import javax.jms.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class MessageSenderGateway {
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageProducer producer;

    public MessageSenderGateway(String channelName) {
        //ToDo
    }

    public Message createTextMessage(String body) {
        //ToDo
        return null;
    }

    public void send(Message msg) {
        //ToDo
    }
}
