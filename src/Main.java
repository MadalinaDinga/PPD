import utils.Polynomial;
import utils.PolynomialOperations;
import utils.Util;
import java.util.Scanner;

public class Main {
    public static void printMenu() {
        System.out.println("USUAL MULTIPLICATION:");
        System.out.println("1-Serial");
        System.out.println("2-Parallel");
        System.out.println("KARATSUBA:");
        System.out.println("3-Serial");
        System.out.println("4-Parallel");
        System.out.println("0-Exit");
    }

    public static void main(String[] args) {
        Polynomial polynomial1 = null;
        Polynomial polynomial2 = null;
        Polynomial pR = null;

        long startTime, stopTime, execTime;
        Scanner keyboard = new Scanner(System.in);
        int option = 0;
        try {
            polynomial1 = Util.getRandomPolynomial(1000);
            polynomial2 = Util.getRandomPolynomial(1000);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        System.out.println("Polynomial1: \n" + polynomial1.toString());
        System.out.println("Polynomial2: \n" + polynomial2.toString());

        while (true) {
            printMenu();
            System.out.println("Your option:");
            option = keyboard.nextInt();
            if (option == 1) {
                startTime = System.currentTimeMillis();
                pR = PolynomialOperations.multiplySerial(polynomial1, polynomial2);
                System.out.println("Result: " + pR.toString());
                stopTime = System.currentTimeMillis();
                execTime = stopTime - startTime;
                System.out.println("Time of execution: " + execTime);
            }
            if (option == 2) {
                startTime = System.currentTimeMillis();
                pR = PolynomialOperations.multiplyPolynomialParallel(polynomial1, polynomial2);
                stopTime = System.currentTimeMillis();
                execTime = stopTime - startTime;
                System.out.println("Result: " + pR.toString());
                System.out.println("Time of execution: " + execTime);
            }
            if(option==3){
                startTime = System.currentTimeMillis();
                pR = PolynomialOperations.multiplyKaratsubaSerial(polynomial1, polynomial2);
                stopTime = System.currentTimeMillis();
                execTime = stopTime - startTime;
                System.out.println("Result: " + pR.toString());
                System.out.println("Time of execution: " + execTime);
            }
            if(option==4){
                startTime = System.currentTimeMillis();
                pR = PolynomialOperations.multiplyKaratsubaParallel(polynomial1, polynomial2);
                System.out.println("Result: " + pR.toString());
                stopTime = System.currentTimeMillis();
                execTime = stopTime - startTime;
                System.out.println("Time of execution: " + execTime);
            }
            if (option == 0) {
                break;
            }
        }
    }

}
