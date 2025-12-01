import java.util.Scanner;

public class Main {
    static final double pi = 3.1416;
    static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {

        System.out.println("1. Rectangle");
        System.out.println("2. Triangle");
        System.out.println("3. Circle");

        int x = sc.nextInt();
        switch (x) {
            case 1:
                rectangle();
                break;
            case 2:
                triangle();
                break;
            case 3:
                circle();
                break;
        }

    }

    static void rectangle() {
        System.out.println("Input Length and Width");
        double a = sc.nextDouble();
        double b = sc.nextDouble();
        System.out.println("Area: " + a * b);
    }

    static void triangle() {
        System.out.println("Input Length and Width");
        double a = sc.nextDouble();
        double b = sc.nextDouble();
        System.out.println("Area: " + 0.5 * a * b);
    }

    static void circle() {
        System.out.println("Input Radius");
        double a = sc.nextDouble();
        System.out.println("Area: " + pi * a * a);
    }
}