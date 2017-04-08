package forms.loanclient;

import shared.Gateway.*;
import shared.loan.*;

/**
 * Created by Maiko on 8-4-2017.
 */
public class LoanBrokerAppGateway {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiver;

    public void applyForLoan(LoanRequest request) {
        //ToDo
    }

    public void onLoanReplyArrived(LoanRequest request, LoanReply reply) {
        //ToDo
    }
}
