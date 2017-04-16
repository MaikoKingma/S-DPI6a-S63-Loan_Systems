package forms.loanbroker;

import shared.bank.BankInterestReply;

import java.util.*;

/**
 * Created by Maiko on 16-4-2017.
 */
public class Aggregation {
    private int id;
    private int expectedReplies;
    private String corrolationId;
    private List<BankInterestReply> replies;

    public Aggregation(int id, int expectedReplies, String corrolationId) {
        this.expectedReplies = expectedReplies;
        this.id = id;
        this.corrolationId = corrolationId;
        replies = new ArrayList<>();
    }

    public int getExpectedReplies() {
        return expectedReplies;
    }

    public int getId() {
        return id;
    }

    public String getCorrolationId() {
        return corrolationId;
    }

    public int getReceivedReplies() {
        return replies.size();
    }

    public void addBankInterestReply(BankInterestReply reply) {
        replies.add(reply);
    }

    public boolean repliesReceived() {
        if (replies.size() == expectedReplies) {
            return true;
        }
        return false;
    }

    public BankInterestReply getBestReply() {
        BankInterestReply bestReply = null;
        for (BankInterestReply reply : replies) {
            if (bestReply == null || bestReply.getInterest() > reply.getInterest()) {
                bestReply = reply;
            }
        }
        return bestReply;
    }
}
