package utils;

import java.util.concurrent.ForkJoinPool;
import java.util.stream.IntStream;

/**
 * Created by Madalina Dinga on 24-Nov-17.
 */
public class PolynomialOperations {
    static Object lock = new Object();

    public static Object getLock() {
        return lock;
    }

    public static void setLock(Object lock) {
        PolynomialOperations.lock = lock;
    }

    public static ForkJoinPool getPool() {
        return pool;
    }

    public static void setPool(ForkJoinPool pool) {
        PolynomialOperations.pool = pool;
    }

    // No need for parallelization.
    public static Polynomial Add(Polynomial p1, Polynomial p2) {
        int min = Math.min(p1.getDegree(), p2.getDegree());
        int max = Math.max(p1.getDegree(), p2.getDegree());

        int[] coefficients = new int[max + 1];

        // add corresponding coefficients
        for (int i = 0; i <= min; ++i)
            coefficients[i] = p1.getCoefficients()[i] + p2.getCoefficients()[i];

        // the remaining coefficients from the higher degree polynomial are added to the result polynomial
        for (int i = min + 1; i <= max; ++i)
            if (i <= p1.getDegree())
                coefficients[i] = p1.getCoefficients()[i];
            else
                coefficients[i] = p2.getCoefficients()[i];

        Polynomial result = null;
        try {
            result = new Polynomial(coefficients);
        } catch (PolynomialException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    // No need for parallelization.
    public static Polynomial Substract(Polynomial p1, Polynomial p2)
    {
        int min = Math.min(p1.getDegree(), p2.getDegree());
        int max = Math.max(p1.getDegree(), p2.getDegree());

        int[] coefficients = new int[max + 1];

        for (int i = 0; i <= min; ++i)
            coefficients[i] = p1.getCoefficients()[i] - p2.getCoefficients()[i];

        for (int i = min + 1; i <= max; ++i)
            if (i <= p1.getDegree())
                coefficients[i] = p1.getCoefficients()[i];
            else
                coefficients[i] = -p2.getCoefficients()[i];

        // after substraction the polynomial degree has to be updated
        int degree = coefficients.length - 1;
        while (coefficients[degree] == 0 && degree > 0)
            degree--;

        // the remaining coefficients are saved into a new array
        int[] newCoeff = new int[degree + 1];

        for (int i = 0; i < degree+1; i++) newCoeff[i] = coefficients[i];

        Polynomial result=null;
        try {
            result = new Polynomial(newCoeff);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public static Polynomial multiplyKaratsubaSerial(Polynomial x,Polynomial y){
        int nr=100;
        if (x.getDegree() <= nr || y.getDegree() <= nr)
            return multiplySerial(x,y);

        int length=Math.max(x.getDegree(),y.getDegree());
        length = length/2;
        int[] low1=new int[length];
        int[] high1=new int[x.getDegree()+1-length];
        int[] low2=new int[length];
        int[] high2=new int[y.getDegree()+1-length];

        for(int i=0;i<length;i++) {
            low1[i]=x.getCoefficients()[i];
            low2[i]=y.getCoefficients()[i];
        }

        for(int j=0;j<length;j++) {
            high1[j]=x.getCoefficients()[j+length];
            high2[j]=y.getCoefficients()[j+length];
        }

        Polynomial p1l=null;
        Polynomial p1h=null;
        Polynomial p2l=null;
        Polynomial p2h=null;
        try {
            p1l = new Polynomial(low1);
            p1h = new Polynomial(high1);
            p2l = new Polynomial(low2);
            p2h = new Polynomial(high2);
        }
        catch(PolynomialException e){
            System.out.println(e.getMessage());
        }

        Polynomial result=null;

        Polynomial r0=multiplyKaratsubaSerial(p1l,p2l);
        Polynomial r1=multiplyKaratsubaSerial(Add(p1l,p1h),Add(p2l,p2h));
        Polynomial r2=multiplyKaratsubaSerial(p1h,p2h);

        try {
            result=Add(Add(Polynomial.Shift(r2,(2*length)), Polynomial.Shift(Substract(Substract(r1,r2),r0),length)),r0);
        }
        catch(PolynomialException e){
            System.out.println(e.getMessage());
        }
        return result;
    }

    public static Polynomial multiplySerial(Polynomial a, Polynomial b){
        int[] coeff=new int[a.getDegree()+b.getDegree()+1];

        for(int i=0;i<=a.getDegree();i++)
            for(int j=0;j<=b.getDegree();j++)
                coeff[i+j]+=a.getCoefficients()[i]*b.getCoefficients()[j];

        Polynomial p=null;
        try {
            p = new Polynomial(coeff);
        }
        catch(Exception e){
            System.out.println(e.getMessage());
        }

        return p;
    }

    static ForkJoinPool pool=new ForkJoinPool(3);

    public static Polynomial multiplyKaratsubaParallel(Polynomial x,Polynomial y){
        return pool.invoke(new KaratsubaTask(x, y));
    }

    public static Polynomial multiplyPolynomialParallel(Polynomial a, Polynomial b) {
        int[] coeff = new int[a.getDegree() + b.getDegree() + 1];
        Polynomial p = null;
        IntStream.range(0, a.getDegree() + 1).parallel().forEach(i -> {
            for (int j = 0; j <= b.getDegree(); j++) {
                synchronized (lock) {
                    coeff[i + j] += a.getCoefficients()[i] * b.getCoefficients()[j];
                }
            }
        });
        try {
            p = new Polynomial(coeff);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return p;
    }
}
