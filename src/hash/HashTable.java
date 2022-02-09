package hash;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Objects;

import static java.lang.Math.pow;

public class HashTable {
    private final Complex[][] table;
    private final HashFunction mainHashFunc;
    private final HashFunction[] secondaryHashFuncs;
    private final int size;
    private final int p;

    private HashTable(int size, int p) {
        this.table = new Complex[size][];
        this.mainHashFunc = HashFunction.getRandomHashFunction(p, size);
        this.secondaryHashFuncs = new HashFunction[size];
        this.size = size;
        this.p = p;
    }

    public static HashTable create(Complex[] complexes) {
        // remove all null and non-unique elements
        Complex[] distinctComplexes = Arrays.stream(complexes)
                .filter(Objects::nonNull)
                .distinct()
                .toArray(Complex[]::new);

        int size = distinctComplexes.length;
        int p = HashFunctionUtil.findP(size);
        var table = new HashTable(size, p);

        table.addAll(distinctComplexes);
        return table;
    }

    private void addAll(Complex[] complexes) {
        // temporary table
        var tmpTable = new ArrayList<ArrayList<Complex>>(Collections.nCopies(size, null));

        for (var complex : complexes) {
            int primaryHash = mainHashFunc.hash(complex);

            if (tmpTable.get(primaryHash) == null) {
                var tmpTableRow = new ArrayList<Complex>();
                tmpTableRow.add(complex);
                tmpTable.set(primaryHash, tmpTableRow);
            } else {
                tmpTable.get(primaryHash).add(complex);
            }
        }

        for (int i = 0; i < size; i++) {
            if (tmpTable.get(i) == null)
                tmpTable.set(i, new ArrayList<>());

            buildSecondaryTable(i, tmpTable.get(i));
        }
    }

    private void buildSecondaryTable(int index, ArrayList<Complex> complexes) {
        int rowSize = complexes.size();

        while (true) {
            Complex[] secondaryTable = new Complex[(int) pow(rowSize, 2)];
            HashFunction secondaryHashFunc;

            if (rowSize > 1)
                secondaryHashFunc = HashFunction.getRandomHashFunction(p, rowSize);
            else
                // if secondary hash table contains one or less elements
                // there is no need in hash function, so we can take a = b = 0
                secondaryHashFunc = HashFunction.getRandomHashFunction(0, 0, p, rowSize);

            boolean isCollision = false;
            for (var complex : complexes) {
                int hash = secondaryHashFunc.hash(complex);

                // if collision happens, means that the created hash function
                // has bad coefficients and should be recreated
                if (secondaryTable[hash] != null) {
                    isCollision = true;
                    break;
                }

                secondaryTable[hash] = complex;
            }

            if (!isCollision) {
                table[index] = secondaryTable;
                secondaryHashFuncs[index] = secondaryHashFunc;
                break;
            }
        }
    }

    public boolean contains(Complex complex) {
        Objects.requireNonNull(complex);

        int primaryHash = mainHashFunc.hash(complex);
        if (primaryHash >= size)
            return false;

        int secondaryHash = secondaryHashFuncs[primaryHash].hash(complex);
        if (secondaryHash >= table[primaryHash].length)
            return false;

        Complex found = table[primaryHash][secondaryHash];
        return complex.equals(found);
    }

    @Override
    public String toString() {
        var stringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            stringBuilder
                    .append('(')
                    .append(i)
                    .append(") -> [");

            var complexRow = table[i];

            for (var complex : complexRow)
                stringBuilder.append(complex).append(",");

            if (stringBuilder.charAt(stringBuilder.length() - 1) != '[')
                stringBuilder.deleteCharAt(stringBuilder.length() - 1);

            stringBuilder.append(']').append('\n');
        }
        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        stringBuilder.append("\nSize: ").append(this.size);
        return stringBuilder.toString();
    }
}
