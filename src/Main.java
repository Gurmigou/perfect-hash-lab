import hash.Complex;
import hash.HashTable;
import parser.FileParserToComplex;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        Complex[] complexes = FileParserToComplex.parseComplexes("src/resources/complexData.txt");

        HashTable hashTable = HashTable.create(complexes);
        System.out.println(hashTable);
    }
}
