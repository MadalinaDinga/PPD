package utils;

import java.util.Random;

/**
 * Created by Madalina Dinga on 24-Nov-17.
 */
public class Util {
    // Generates a random polynomial of a given degree.
    public static Polynomial getRandomPolynomial(int degree) throws PolynomialException{
        Random rand = new Random();

        if (degree < 0) {
            throw new PolynomialException("Invalid degree specification");
        }

        int[] coefficients = new int[degree + 1];

        for (int i = 0; i <= degree; ++i) {
            coefficients[i] = rand.nextInt(5) + 1;
            if (coefficients[i] == 0) {
                coefficients[i] = rand.nextInt(5) + 1;
            }
        }

        return new Polynomial(coefficients);
    }
}
