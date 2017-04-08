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

        receiver.setListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                processMessage(message);
            }
        });
    }

    protected abstract void processMessage(Message message);
}
