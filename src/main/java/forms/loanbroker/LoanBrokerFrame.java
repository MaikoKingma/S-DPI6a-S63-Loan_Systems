package forms.loanbroker;

import java.awt.*;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import javax.jms.*;
import shared.bank.*;
import shared.loan.LoanReply;
import shared.loan.LoanRequest;

public class LoanBrokerFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private DefaultListModel<JListLine> listModel = new DefaultListModel<JListLine>();
	private JList<JListLine> list;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					LoanBrokerFrame frame = new LoanBrokerFrame();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	/**
	 * Create the frame.
	 */
	public LoanBrokerFrame() {
		setTitle("Loan Broker");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		GridBagLayout gbl_contentPane = new GridBagLayout();
		gbl_contentPane.columnWidths = new int[]{46, 31, 86, 30, 89, 0};
		gbl_contentPane.rowHeights = new int[]{233, 23, 0};
		gbl_contentPane.columnWeights = new double[]{1.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE};
		gbl_contentPane.rowWeights = new double[]{1.0, 0.0, Double.MIN_VALUE};
		contentPane.setLayout(gbl_contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.gridwidth = 7;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		list = new JList<JListLine>(listModel);
		scrollPane.setViewportView(list);

		listenToRequests();
		listenToReplys();
	}
	
	private JListLine getRequestReply(LoanRequest request){
		for (int i = 0; i < listModel.getSize(); i++){
			JListLine rr =listModel.get(i);
			if (rr.getLoanRequest() == request){
				return rr;
			}
		}

		return null;
	}

	private JListLine getRequestReply(String CorrolationId)
	{
		for (int i = 0; i < listModel.getSize(); i++){
			JListLine rr =listModel.get(i);
			if (rr.getCorrolationId() == CorrolationId){
				return rr;
			}
		}

		return null;
	}
	
	public void add(LoanRequest loanRequest, String CorrolationId){
		JListLine rr = new JListLine(loanRequest);
		rr.setCorrolationId(CorrolationId);
		listModel.addElement(rr);
	}

	public void add(LoanRequest loanRequest,BankInterestRequest bankRequest){
		JListLine rr = getRequestReply(loanRequest);
		if (rr!= null && bankRequest != null){
			rr.setBankRequest(bankRequest);
            list.repaint();
		}		
	}
	
	public void add(String CorrolationId, BankInterestReply bankReply){
		JListLine rr = getRequestReply(CorrolationId);
		if (rr!= null && bankReply != null){
			rr.setBankReply(bankReply);
            list.repaint();
		}		
	}

	private void listenToRequests()
	{
		Connection connection; // to connect to the JMS
		Session session; // session for creating consumers

		Destination receiveDestination; //reference to a queue destination
		MessageConsumer consumer = null; // for receiving messages

		try {
			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
					"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

			// connect to the Destination called “loanRequestQueue”
			// queue or topic: “queue.loanRequestQueue”
			props.put(("queue.loanRequestQueue"), " loanRequestQueue");

			Context jndiContext = new InitialContext(props);
			ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
					.lookup("ConnectionFactory");
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// connect to the receiver destination
			receiveDestination = (Destination) jndiContext.lookup("loanRequestQueue");
			consumer = session.createConsumer(receiveDestination);

			connection.start(); // this is needed to start receiving messages

		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}


		try {
			consumer.setMessageListener(new MessageListener() {

											@Override
											public void onMessage(Message msg) {
												processRequestMessage(msg);
											}
										});

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void listenToReplys()
	{
		Connection connection; // to connect to the JMS
		Session session; // session for creating consumers

		Destination receiveDestination; //reference to a queue destination
		MessageConsumer consumer = null; // for receiving messages

		try {
			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY,
					"org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

			// connect to the Destination called “bankInterestReplyQueue”
			// queue or topic: “queue.bankInterestReplyQueue”
			props.put(("queue.bankInterestReplyQueue"), "bankInterestReplyQueue");

			Context jndiContext = new InitialContext(props);
			ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
					.lookup("ConnectionFactory");
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// connect to the receiver destination
			receiveDestination = (Destination) jndiContext.lookup("bankInterestReplyQueue");
			consumer = session.createConsumer(receiveDestination);

			connection.start(); // this is needed to start receiving messages

		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}


		try {
			consumer.setMessageListener(new MessageListener() {

				@Override
				public void onMessage(Message msg) {
					processReplyMessage(msg);
				}
			});

		} catch (JMSException e) {
			e.printStackTrace();
		}
	}

	public void sendBankInterestRequest(BankInterestRequest request, String CorrelationId)
	{
		Connection connection; // to connect to the ActiveMQ
		Session session; // session for creating messages, producers and

		Destination sendDestination; // reference to a queue destination
		MessageProducer producer; // for sending messages

		try {
			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

			// connect to the Destination called “bankInterestRequestQueue”
			// queue or topic: “queue.bankInterestRequestQueue”
			props.put(("queue.bankInterestRequestQueue"), "bankInterestRequestQueue");

			Context jndiContext = new InitialContext(props);
			ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
					.lookup("ConnectionFactory");
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// connect to the sender destination
			sendDestination = (Destination) jndiContext.lookup("bankInterestRequestQueue");
			producer = session.createProducer(sendDestination);

			// create a text message containing the request
			Message msg = session.createTextMessage(request.getCommaSeperatedValue());
			msg.setJMSCorrelationID(CorrelationId);
			// send the message
			producer.send(msg);
			System.out.println("<<< CorrolationId: " + msg.getJMSCorrelationID() + " Message: " + ((TextMessage)msg).getText());

		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
	}

	public void sendLoanReply(LoanReply reply, String CorrolationId)
	{
		Connection connection; // to connect to the ActiveMQ
		Session session; // session for creating messages, producers and

		Destination sendDestination; // reference to a queue destination
		MessageProducer producer; // for sending messages

		try {
			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

			// connect to the Destination called “loanReplyQueue”
			// queue or topic: “queue.loanReplyQueue”
			props.put(("queue.loanReplyQueue"), "loanReplyQueue");

			Context jndiContext = new InitialContext(props);
			ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
					.lookup("ConnectionFactory");
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// connect to the sender destination
			sendDestination = (Destination) jndiContext.lookup("loanReplyQueue");
			producer = session.createProducer(sendDestination);

			// create a text message containing the request
			Message msg = session.createTextMessage(reply.getCommaSeperatedValue());
			msg.setJMSCorrelationID(CorrolationId);
			// send the message
			producer.send(msg);
			System.out.println("<<< CorrolationId: " + msg.getJMSCorrelationID() + " Message: " + ((TextMessage)msg).getText());

		} catch (NamingException | JMSException e) {
			e.printStackTrace();
		}
	}

	private void processRequestMessage(Message msg)
	{
		try {
			String value = ((TextMessage) msg).getText();
			System.out.println(">>> CorrolationId: " + msg.getJMSCorrelationID() + " Message: " + value);
			LoanRequest loanRequest = new LoanRequest();
			loanRequest.fillFromCommaSeperatedValue(value);
			add(loanRequest, msg.getJMSCorrelationID());
			BankInterestRequest request = new BankInterestRequest(loanRequest.getAmount(), loanRequest.getTime());
			sendBankInterestRequest(request, msg.getJMSCorrelationID());
			add(loanRequest, request);
		}
		catch (JMSException e) {
			e.printStackTrace();
		}
	}

	private void processReplyMessage(Message msg) {
		if (msg instanceof TextMessage)
		{
			try {
				String value = ((TextMessage) msg).getText();
				System.out.println(">>> CorrolationId: " + msg.getJMSCorrelationID() + " Message: " + value);
				BankInterestReply bankInterestReply = new BankInterestReply();
				bankInterestReply.fillFromCommaSeperatedValue(value);
				add(msg.getJMSCorrelationID(), bankInterestReply);

				LoanReply reply = new LoanReply(bankInterestReply.getInterest(), bankInterestReply.getQuoteId());
				sendLoanReply(reply, msg.getJMSCorrelationID());
			}
			catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
