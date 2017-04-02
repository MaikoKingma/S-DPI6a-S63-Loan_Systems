package shared.loan;

import shared.IMessage;

import java.text.DecimalFormat;

/**
 *
 * This class stores all information about a
 * request that a client submits to get a loan.
 *
 */
public class LoanRequest implements IMessage {

    private int ssn; // unique client number.
    private int amount; // the ammount to borrow
    private int time; // the time-span of the loan

    public LoanRequest() {
        super();
        this.ssn = 0;
        this.amount = 0;
        this.time = 0;
    }

    public LoanRequest(int ssn, int amount, int time) {
        super();
        this.ssn = ssn;
        this.amount = amount;
        this.time = time;
    }

    public int getSsn() {
        return ssn;
    }

    public void setSsn(int ssn) {
        this.ssn = ssn;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "ssn=" + String.valueOf(ssn) + " amount=" + String.valueOf(amount) + " time=" + String.valueOf(time);
    }

    @Override
    public String getCommaSeperatedValue() {
        return ssn + "," + amount + "," + time;
    }

    @Override
    public void fillFromCommaSeperatedValue(String value) {
        String[] array = value.split(",");
        if (array.length != 3)
            throw new IllegalArgumentException();

        ssn = Integer.parseInt(array[0]);
        amount = Integer.parseInt(array[1]);
        time = Integer.parseInt(array[2]);
    }
}