package shared.Gateway;

import javax.jms.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class MessageReceiverGateway {
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;

    public MessageReceiverGateway(String channelName) {
        //ToDo
    }

    public void setListener(MessageListener listener) {
        //ToDo
    }
}
