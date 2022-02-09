package hash;

import java.util.Random;

import static hash.HashFunctionUtil.nonNegativeSign;
import static java.lang.Math.*;

public class HashFunction {
    private final int a, b, p, m;
    private static final Random random = new Random();

    private HashFunction(int a, int b, int p, int m) {
        this.a = a;
        this.b = b;
        this.p = p;
        this.m = m;
    }

    public int hash(Complex c) {
        return (int) (((pow(a, 1) * abs(c.real()) +
                pow(a, 2) * abs(c.imaginary()) +
                pow(a, 3) * nonNegativeSign(c.real()) +
                pow(a, 4) * nonNegativeSign(c.imaginary()) +
                b) % p) % m);
    }

    public static HashFunction getRandomHashFunction(int p, int m) {
        // Z* = {1, 2, ... , p - 1}
        // Z  = {0, 1, ... , p - 1}
        // a belongs to Z*
        // b belongs to Z

        int a = random.nextInt(p - 1) + 1;
        int b = random.nextInt(p);

        return new HashFunction(a, b, p, m);
    }

    public static HashFunction getRandomHashFunction(int a, int b, int p, int m) {
        return new HashFunction(a, b, p, m);
    }
}
