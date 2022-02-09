package parser;

import hash.Complex;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public class FileParserToComplex {
    /**
     * <p>
     * Format of complex numbers in the file with path {@code path}:
     * </p>
     * <p>
     * [real][space(s)][imaginary]<br/>
     * [real][space(s)][imaginary]<br/>
     * ...........................<br/>
     * [real][space(s)][imaginary]<br/>
     */
    public static Complex[] parseComplexes(String path) throws IOException {
        return Files.lines(Path.of(path))
                .map(FileParserToComplex::toComplex)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toArray(Complex[]::new);
    }

    private static Optional<Complex> toComplex(String complex) {
        String[] parts = complex.split(" ");

        if (parts.length != 2)
            return Optional.empty();

        try {
            int real = Integer.parseInt(parts[0]);
            int imaginary = Integer.parseInt(parts[1]);
            return Optional.of(new Complex(real, imaginary));
        } catch (NumberFormatException e) {
            return Optional.empty();
        }
    }
}