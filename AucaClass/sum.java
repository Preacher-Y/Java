package AucaClass;

import java.util.Scanner;

public class sum {

    public static void main(String[] args) {
        Scanner num = new Scanner(System.in);
        System.out.println("Enter the first number: ");
        int a = num.nextInt();
        System.out.println("Enter the second number: ");
        int b = num.nextInt();

        int sum = a+b;
        System.out.println("The sum of "+a+" and "+b+" is: "+sum);
        num.close();
    }
}