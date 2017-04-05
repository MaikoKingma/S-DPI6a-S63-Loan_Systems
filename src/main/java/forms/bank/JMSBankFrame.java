package forms.bank;

import java.awt.*;
import java.awt.event.*;
import java.util.Properties;

import javax.jms.*;
import javax.naming.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import shared.bank.*;
import shared.request.RequestReply;


public class JMSBankFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField tfReply;
	private DefaultListModel<RequestReply<BankInterestRequest, BankInterestReply>> listModel = new DefaultListModel<RequestReply<BankInterestRequest, BankInterestReply>>();
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					JMSBankFrame frame = new JMSBankFrame();
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
	public JMSBankFrame() {
		setTitle("JMS Bank - ABN AMRO");
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
		gbc_scrollPane.gridwidth = 5;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.gridx = 0;
		gbc_scrollPane.gridy = 0;
		contentPane.add(scrollPane, gbc_scrollPane);
		
		JList<RequestReply<BankInterestRequest, BankInterestReply>> list = new JList<RequestReply<BankInterestRequest, BankInterestReply>>(listModel);
		scrollPane.setViewportView(list);
		
		JLabel lblNewLabel = new JLabel("type reply");
		GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
		gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
		gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
		gbc_lblNewLabel.gridx = 0;
		gbc_lblNewLabel.gridy = 1;
		contentPane.add(lblNewLabel, gbc_lblNewLabel);
		
		tfReply = new JTextField();
		tfReply.setText("1");
		GridBagConstraints gbc_tfReply = new GridBagConstraints();
		gbc_tfReply.gridwidth = 2;
		gbc_tfReply.insets = new Insets(0, 0, 0, 5);
		gbc_tfReply.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfReply.gridx = 1;
		gbc_tfReply.gridy = 1;
		contentPane.add(tfReply, gbc_tfReply);
		tfReply.setColumns(10);
		
		JButton btnSendReply = new JButton("send reply");
		btnSendReply.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				RequestReply<BankInterestRequest, BankInterestReply> rr = list.getSelectedValue();
				double interest = Double.parseDouble((tfReply.getText()));
				BankInterestReply reply = new BankInterestReply(interest,"ABN AMRO");
				if (rr!= null && reply != null){
					rr.setReply(reply);
	                list.repaint();
					sendReply(reply, rr.getCorrelationId());
				}
			}
		});
		GridBagConstraints gbc_btnSendReply = new GridBagConstraints();
		gbc_btnSendReply.anchor = GridBagConstraints.NORTHWEST;
		gbc_btnSendReply.gridx = 4;
		gbc_btnSendReply.gridy = 1;
		contentPane.add(btnSendReply, gbc_btnSendReply);

		listenToRequests();
	}

	public void add(BankInterestRequest bankInterestRequest, String CorrolationId) {
		RequestReply rr = new RequestReply<>(bankInterestRequest, null);
		rr.setCorrelationId(CorrolationId);
		listModel.addElement(rr);
	}

	public void sendReply(BankInterestReply reply, String CorrelationId)
	{
		Connection connection; // to connect to the ActiveMQ
		Session session; // session for creating messages, producers and

		Destination sendDestination; // reference to a queue destination
		MessageProducer producer; // for sending messages

		try {
			Properties props = new Properties();
			props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
			props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");

			// connect to the Destination called “bankInterestReplyQueue”
			// queue or topic: “queue.bankInterestReplyQueue”
			props.put(("queue.bankInterestReplyQueue"), "bankInterestReplyQueue");

			Context jndiContext = new InitialContext(props);
			ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
					.lookup("ConnectionFactory");
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// connect to the sender destination
			sendDestination = (Destination) jndiContext.lookup("bankInterestReplyQueue");
			producer = session.createProducer(sendDestination);

			// create a text message containing the request
			Message msg = session.createTextMessage(reply.getCommaSeperatedValue());
			msg.setJMSCorrelationID(CorrelationId);
			// send the message
			producer.send(msg);

		} catch (NamingException | JMSException e) {
			e.printStackTrace();
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

			// connect to the Destination called “bankInterestRequestQueue”
			// queue or topic: “queue.bankInterestRequestQueue”
			props.put(("queue.bankInterestRequestQueue"), " bankInterestRequestQueue");

			Context jndiContext = new InitialContext(props);
			ConnectionFactory connectionFactory = (ConnectionFactory) jndiContext
					.lookup("ConnectionFactory");
			connection = connectionFactory.createConnection();
			session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

			// connect to the receiver destination
			receiveDestination = (Destination) jndiContext.lookup("bankInterestRequestQueue");
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

	public void processRequestMessage(Message msg) {
		if (msg instanceof TextMessage)
		{
			try {
				String value = ((TextMessage) msg).getText();
				BankInterestRequest bankInterestRequest = new BankInterestRequest();
				bankInterestRequest.fillFromCommaSeperatedValue(value);
				add(bankInterestRequest, msg.getJMSCorrelationID());
			}
			catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
