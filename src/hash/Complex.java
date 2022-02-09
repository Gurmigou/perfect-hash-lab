package hash;

public record Complex(int real, int imaginary) implements Comparable<Complex> {
    @Override
    public int compareTo(Complex o) {
        int realCompared = Integer.compare(this.real, o.real);
        if (realCompared != 0)
            return realCompared;
        return Integer.compare(this.imaginary, o.imaginary);
    }

    @Override
    public String toString() {
        return "{" + real + " " + imaginary + "i}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Complex complex = (Complex) o;
        return real == complex.real && imaginary == complex.imaginary;
    }
}
