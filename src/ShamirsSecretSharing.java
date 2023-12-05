import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ShamirsSecretSharing {
    private final int k;
    private final int n;
    private final BigInteger prime;
    private final Random random = new SecureRandom();

    public ShamirsSecretSharing(int k, int n) {
        this.k = k;
        this.n = n;
        this.prime = BigInteger.probablePrime(256, random); // 256-bit prime
    }

    private BigInteger[] generateCoefficients(BigInteger secret) {
        BigInteger[] coefficients = new BigInteger[k];
        coefficients[0] = secret;

        for (int i = 1; i < k; i++) {
            BigInteger coeff;
            do {
                coeff = new BigInteger(prime.bitLength(), random);
            } while (coeff.compareTo(prime) >= 0 || coeff.compareTo(BigInteger.ZERO) == 0);
            coefficients[i] = coeff;
        }

        return coefficients;
    }

    private BigInteger evaluatePolynomial(BigInteger[] coefficients, BigInteger x) {
        BigInteger result = coefficients[k - 1];
        for (int i = k - 2; i >= 0; i--) {
            result = result.multiply(x).add(coefficients[i]).mod(prime);
        }
        return result;
    }

        public Map<BigInteger, BigInteger> generateShares(BigInteger secret) {
            BigInteger[] coefficients = generateCoefficients(secret);
            Map<BigInteger, BigInteger> shares = new HashMap<>();

            for (int i = 1; i <= n; i++) {
                BigInteger x = BigInteger.valueOf(i);
                BigInteger y = evaluatePolynomial(coefficients, x);
                shares.put(x, y);
            }

            return shares;
        }

    public BigInteger combineShares(Map<BigInteger, BigInteger> shares) {
        BigInteger secret = BigInteger.ZERO;
        for (Map.Entry<BigInteger, BigInteger> entry : shares.entrySet()) {
            BigInteger x = entry.getKey();
            BigInteger y = entry.getValue();
            BigInteger numerator = BigInteger.ONE;
            BigInteger denominator = BigInteger.ONE;
            for (Map.Entry<BigInteger, BigInteger> otherEntry : shares.entrySet()) {
                BigInteger otherX = otherEntry.getKey();
                if (!x.equals(otherX)) {
                    numerator = numerator.multiply(otherX.negate()).mod(prime);
                    denominator = denominator.multiply(x.subtract(otherX)).mod(prime);
                }
            }
            BigInteger value = y.multiply(numerator).multiply(denominator.modInverse(prime)).mod(prime);
            secret = secret.add(value).mod(prime);
        }
        return secret;
    }

    public BigInteger getPrime() {
        return prime;
    }
}
