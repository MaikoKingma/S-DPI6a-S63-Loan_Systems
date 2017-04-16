package forms.loanbroker;

import shared.gateway.MessageSenderGateway;

/**
 * Created by Maiko on 15-4-2017.
 */
public class Rule {
    private String bankName;
    private Integer minLoanAmount;
    private Integer maxLoanAmount;
    private Integer minLoanTime;
    private Integer maxLoanTime;

    private MessageSenderGateway sender;

    public Rule(String bankName, Integer minLoanAmount, Integer maxLoanAmount, Integer minLoanTime, Integer maxLoanTime) {
        this.bankName = bankName;
        this.minLoanAmount = minLoanAmount;
        this.maxLoanAmount = maxLoanAmount;
        this.minLoanTime = minLoanTime;
        this.maxLoanTime = maxLoanTime;
    }

    public String getBankName() {
        return bankName;
    }

    public MessageSenderGateway getSender() {
        return sender;
    }

    public void setSender(MessageSenderGateway sender) {
        this.sender = sender;
    }

     public boolean check(int loanAmount, int loanTime) {
        if (((minLoanAmount == null || loanAmount >= minLoanAmount) &&
                (maxLoanAmount == null || loanAmount <= maxLoanAmount)) &&
                ((minLoanTime == null || loanTime >= minLoanTime) &&
                (maxLoanTime == null || loanTime <= maxLoanTime))) {
            return true;
        }
        return false;
     }
}
