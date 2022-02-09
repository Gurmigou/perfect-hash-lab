package hash;

import java.util.stream.IntStream;

import static java.lang.Math.sqrt;

public class HashFunctionUtil {
    public static int nonNegativeSign(int x) {
        return x >= 0 ? 1 : 0;
    }

    public static boolean isPrime(int x) {
        return IntStream.rangeClosed(2, (int) sqrt(x))
                .noneMatch(i -> x % i == 0);
    }

    public static int findP(int size) {
        int p = size + 1;
        while (!isPrime(p))
            p++;
        return p;
    }
}
