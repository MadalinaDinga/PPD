package utils;

import java.util.concurrent.RecursiveTask;

/**
 * Created by Madalina Dinga on 24-Nov-17.
 */
//must implement the method compute()
public class KaratsubaTask extends RecursiveTask<Polynomial> {
    private Polynomial x;
    private Polynomial y;

    public KaratsubaTask(Polynomial x, Polynomial y) {
        this.x = x;
        this.y = y;
    }

    protected Polynomial compute() {
        if (x.getDegree() <= 100 || y.getDegree() <= 100) {
            return PolynomialOperations.multiplySerial(x, y);
        }

        int length = Math.max(x.getDegree(), y.getDegree());
        length = length / 2;
        int[] low1 = new int[length];
        int[] high1 = new int[x.getDegree() + 1 - length];
        int[] low2 = new int[length];
        int[] high2 = new int[y.getDegree() + 1 - length];

        for (int i = 0; i < length; i++) {
            low1[i] = x.getCoefficients()[i];
            low2[i] = y.getCoefficients()[i];
        }

        for (int j = 0; j < length; j++) {
            high1[j] = x.getCoefficients()[j + length];
            high2[j] = y.getCoefficients()[j + length];
        }

        Polynomial p1l = null;
        Polynomial p1h = null;
        Polynomial p2l = null;
        Polynomial p2h = null;
        try {
            p1l = new Polynomial(low1);
            p1h = new Polynomial(high1);
            p2l = new Polynomial(low2);
            p2h = new Polynomial(high2);
        } catch (PolynomialException e) {
            System.out.println(e.getMessage());
        }

        KaratsubaTask r0 = new KaratsubaTask(p1l, p2l);
        KaratsubaTask r1 = new KaratsubaTask(PolynomialOperations.Add(p1l, p1h), PolynomialOperations.Add(p2l, p2h));
        KaratsubaTask r2 = new KaratsubaTask(p1h, p2h);

        r0.fork();
        r1.fork();
        r2.fork();

        Polynomial r00 = r0.join();
        Polynomial r01 = r1.join();
        Polynomial r02 = r2.join();

        //Polynomial result=null;
        //try{
        ///  result=Polynomial.Add(Polynomial.Add(Polynomial.Shift(r02,(2*length)),Polynomial.Shift(Polynomial.Substract(Polynomial.Substract(r01,r02),r00),length)),r00);
        //}
        //catch(Exception e){
        //  System.out.println(e.getMessage());
        //}
        //return Polynomial.Add(Polynomial.Add(Polynomial.Shift(r02,(2*length)),Polynomial.Shift(Polynomial.Substract(Polynomial.Substract(r01,r02),r00),length)),r00);
        try {
            return PolynomialOperations.Add(
                    PolynomialOperations.Add(
                            Polynomial.Shift(r02, 2 * length),
                            Polynomial.Shift(PolynomialOperations.Substract(PolynomialOperations.Substract(r01, r02), r01), length)), r00);
        } catch (PolynomialException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

}
