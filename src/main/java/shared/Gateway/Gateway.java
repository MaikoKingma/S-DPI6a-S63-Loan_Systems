package shared.Gateway;

/**
 * Created by Maiko on 8-4-2017.
 */
public abstract class Gateway {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiver;

    public Gateway(String senderChannel, String receiverChannel) {
        sender = new MessageSenderGateway(senderChannel);
        receiver = new MessageReceiverGateway(receiverChannel);
    }
}
