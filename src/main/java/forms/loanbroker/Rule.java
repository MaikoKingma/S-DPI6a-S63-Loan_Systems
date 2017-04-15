package forms.loanbroker;

/**
 * Created by Maiko on 15-4-2017.
 */
public class Rule {
    private String bankName;
    private Integer minLoanAmount;
    private Integer maxLoanAmount;
    private Integer minLoanTime;
    private Integer maxLoanTime;

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

     public boolean checkLoanAmount(int loanAmount) {
        if ((minLoanAmount == null || loanAmount >= minLoanAmount) && (maxLoanAmount == null || loanAmount <= maxLoanAmount)) {
            return true;
        }
        return false;
     }

     public boolean checkLoanTime(int loanTime) {
        if ((minLoanTime == null || loanTime >= minLoanTime) && (maxLoanTime == null || loanTime <= maxLoanTime)) {
            return true;
        }
        return false;
     }
}
