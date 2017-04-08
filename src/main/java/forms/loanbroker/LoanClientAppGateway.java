package forms.loanbroker;

import shared.gateway.*;
import shared.loan.LoanReply;
import shared.loan.LoanRequest;

/**
 * Created by Maiko on 8-4-2017.
 */
public class LoanClientAppGateway {
    private MessageSenderGateway sender;
    private MessageReceiverGateway receiver;

    public void onLoanRequestArrived(LoanRequest request) {
        //ToDo
    }

    public void sendLoanReply(LoanRequest request, LoanReply reply) {
        //ToDo
    }
}
