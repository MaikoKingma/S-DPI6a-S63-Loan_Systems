package forms.loanbroker;

import shared.gateway.*;
import shared.bank.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class BankAppGateway {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiver;

    public void onBankReplyArrived(BankInterestReply reply, BankInterestRequest request) {
        //ToDo
    }

    public void sendBankRequest(BankInterestRequest request) {
        //ToDo
    }
}
