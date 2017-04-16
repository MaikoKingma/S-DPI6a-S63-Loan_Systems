package forms.bank;

import shared.bank.BankInterestRequest;

/**
 * Created by Maiko on 16-4-2017.
 */
public class Request {
    private String corrolationId;
    private BankInterestRequest bankInterestRequest;
    private int aggregationId;

    public Request(String corrolationId, BankInterestRequest bankInterestRequest, int aggregationId) {
        this.corrolationId = corrolationId;
        this.bankInterestRequest = bankInterestRequest;
        this.aggregationId = aggregationId;
    }

    public String getCorrolationId() {
        return corrolationId;
    }

    public BankInterestRequest getBankInterestRequest() {
        return bankInterestRequest;
    }

    public int getAggregationId() {
        return aggregationId;
    }
}
