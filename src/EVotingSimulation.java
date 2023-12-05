import java.math.BigInteger;
import java.util.*;

public class EVotingSimulation {
    public static void main(String[] args) {
        int k = 3;
        int n = 5;
        int numVoters = 10;
        ShamirsSecretSharing sss = new ShamirsSecretSharing(k, n);
        Map<BigInteger, BigInteger> authorityShares = new HashMap<>();
        for (int i = 0; i < numVoters; i++) {
            boolean voteYes = new Random().nextBoolean();
            Voter voter = new Voter(voteYes, sss);
            Map<BigInteger, BigInteger> shares = voter.castVote();
            shares.forEach((key, value) -> authorityShares.merge(key, value, BigInteger::add));
        }
        BigInteger result = sss.combineShares(authorityShares);

        if(result.signum() == 1) {
            System.out.println("Candidate 1 won!");
        } else {
            System.out.println("Candidate 2 won!");
        }

    }
}


