package shared.loan;

import shared.IMessage;

/**
 *
 * This class stores all information about a bank offer
 * as a response to a client loan request.
 */
public class LoanReply implements IMessage {

        private double interest; // the interest that the bank offers
        private String bankID; // the unique quote identification

    public LoanReply() {
        super();
        this.interest = 0;
        this.bankID = "";
    }
    public LoanReply(double interest, String quoteID) {
        super();
        this.interest = interest;
        this.bankID = quoteID;
    }

    public double getInterest() {
        return interest;
    }

    public void setInterest(double interest) {
        this.interest = interest;
    }

    public String getQuoteID() {
        return bankID;
    }

    public void setQuoteID(String quoteID) {
        this.bankID = quoteID;
    }
    
    @Override
    public String toString(){
        return " interest="+String.valueOf(interest) + " quoteID="+String.valueOf(bankID);
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
