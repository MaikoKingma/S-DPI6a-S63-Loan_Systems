package shared.Gateway;

import javax.jms.*;
import javax.naming.*;
import java.util.Properties;

/**
 * Created by Maiko on 8-4-2017.
 */
public class MessageReceiverGateway {
    private Connection connection;
    private Session session;
    private Destination destination;
    private MessageConsumer consumer;

    public MessageReceiverGateway(String channelName) {
        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
        // connect to the Destination
        props.put(("queue." + channelName), channelName);

        try {

            Context jndiContext = new InitialContext(props);
            ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext.lookup("ConnectionFactory");
            connection = connectionFactory.createConnection();
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

            // connect to the receiver destination
            destination = (Destination) jndiContext.lookup(channelName);
            consumer = session.createConsumer(destination);
            connection.start(); // this is needed to start receiving messages
        } catch (NamingException | JMSException e) {
            e.printStackTrace();
        }
    }

    public void setListener(MessageListener listener) {
        try {
            consumer.setMessageListener(listener);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
