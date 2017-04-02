package shared.bank;

import shared.IMessage;

/**
 *
 * This class stores all information about an request from a bank to offer
 * a loan to a specific client.
 */
public class BankInterestRequest implements IMessage {

    private int amount; // the requested loan amount
    private int time; // the requested loan period

    public BankInterestRequest() {
        super();
        this.amount = 0;
        this.time = 0;
    }

    public BankInterestRequest(int amount, int time) {
        super();
        this.amount = amount;
        this.time = time;
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
        return " amount=" + String.valueOf(amount) + " time=" + time;
    }

    @Override
    public String getCommaSeperatedValue() {
        return amount + "," + time;
    }

    @Override
    public void fillFromCommaSeperatedValue(String value) {
        String[] array = value.split(",");
        if (array.length != 2)
            throw new IllegalArgumentException();

        amount = Integer.parseInt(array[0]);
        time = Integer.parseInt(array[1]);
    }
}
