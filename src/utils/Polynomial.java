package utils;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by Madalina Dinga on 24-Nov-17.
 */
public final class Polynomial {
    // The degree of the polynomial.
    private int degree;
    // The array of coefficients. Its length should always be degree + 1.
    // For example a polynomial with degree 0 has a single coefficient.
    private int[] coefficients;

    public Polynomial(int[] _coefficients) throws PolynomialException{
        if (_coefficients == null || _coefficients.length == 0)
            throw new PolynomialException("Invalid coefficients specification!");

        this.degree = _coefficients.length - 1;
        this.coefficients = _coefficients;
    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
    }

    public int[] getCoefficients() {
        return coefficients;
    }

    public void setCoefficients(int[] coefficients) {
        this.coefficients = coefficients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Polynomial p=(Polynomial) o;

        if (degree != p.degree) return false;

        return Arrays.equals(coefficients, p.coefficients);
    }

    @Override
    public int hashCode() {
        int result = degree;
        result = 31 * result + Arrays.hashCode(coefficients);
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb=new StringBuilder();
        sb.append("Degree "+degree+"-> ");

        for(int i=0;i<=this.degree;i++){
            sb.append(this.coefficients[i]+"x^"+i+"+ ");
        }

        return sb.toString();
    }

    // Shifts a polynomial to the left by a given offset.
    // Equivalent to multiplying with "X^offset".
    // For example, X^2 shifted by offset 3 becomes X^5.
    public static Polynomial Shift (Polynomial p, int offset) throws PolynomialException {
        if (offset < 0)
          throw new PolynomialException("Invalid offset specification!");

        int coefficients[] = new int[p.degree + 1 + offset];;
        for(int i=0;i<=p.getDegree();i++){
            coefficients[offset+i]=p.coefficients[i];
        }

        Polynomial shiftedPolynomial=null;
        try {
            shiftedPolynomial=new Polynomial(coefficients);
        }
        catch(PolynomialException e){
            System.out.println(e.getMessage());
        }
        return shiftedPolynomial;
    }
}
