import java.math.BigInteger;
import java.util.Map;
import java.util.Random;

public class Voter {
    private final BigInteger vote;
    private final ShamirsSecretSharing sss;
    public Voter(boolean voteYes, ShamirsSecretSharing sss) {
        this.vote = voteYes ? BigInteger.ONE : BigInteger.ONE.negate();
        this.sss = sss;
    }
    public Map<BigInteger, BigInteger> castVote() {
        return sss.generateShares(vote);
    }
}
