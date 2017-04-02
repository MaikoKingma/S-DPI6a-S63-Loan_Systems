package shared.bank;

import shared.IMessage;

/**
 * This class stores information about the bank reply
 *  to a loan request of the specific client
 * 
 */
public class BankInterestReply implements IMessage {

    private double interest; // the loan interest
    private String bankID; // the unique quote Id
    
    public BankInterestReply() {
        this.interest = 0;
        this.bankID = "";
    }
    
    public BankInterestReply(double interest, String quoteId) {
        this.interest = interest;
        this.bankID = quoteId;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getQuoteId() {
        return bankID;
    }

    public void setQuoteId(String quoteId) {
        this.bankID = quoteId;
    }

    public String toString() {
        return "quote=" + this.bankID + " interest=" + this.interest;
    }

    @Override
    public String getCommaSeperatedValue() {
        return bankID + "," + interest;
    }

    @Override
    public void fillFromCommaSeperatedValue(String value) {
        String[] array = value.split(",", 2);
        if (array.length != 2)
            throw new IllegalArgumentException();

        bankID = array[0];
        interest = Double.parseDouble(array[1]);
    }
}
