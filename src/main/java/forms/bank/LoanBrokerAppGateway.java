package forms.bank;

import shared.Gateway.*;
import shared.bank.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class LoanBrokerAppGateway {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiver;

    public void sendBankIntrestReply(BankInterestRequest request, BankInterestReply reply) {
        //ToDo
    }

    public void onBankIntrestRequestArrived(BankInterestRequest request) {
        //ToDo
    }
}
