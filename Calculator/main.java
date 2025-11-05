package Calculator;

import java.util.Scanner;

public class main {
    public static void main(String[] args) {
        
        System.out.println("+--------------------------------------------------+");
        System.out.println("|                Simple Calculator                 |");
        System.out.println("+--------------------------------------------------+");

        Scanner choice = new Scanner(System.in);
        char operator;
        System.out.println("Enter Your first Number: ");
        double num1 = choice.nextDouble();

        System.out.println("Enter Your Second Number: ");
        double num2 = choice.nextDouble();

        System.out.println("Choose an operation (+, -, *, /): ");
        operator = choice.next().charAt(0);

        double result;

        switch (operator) {
            case '+':
                result = num1 + num2;
                System.out.printf("Result: %.2f + %.2f = %.2f", num1, num2, result);
                break;
            case '-':
                result = num1 - num2;
                System.out.printf("Result: %.2f - %.2f = %.2f", num1, num2, result);
                break;
            case '*':
                result = num1 * num2;
                System.out.printf("Result: %.2f * %.2f = %.2f", num1, num2, result);
                break;
            case '/':
                if (num2 != 0) {
                    result = num1 / num2;
                    System.out.printf("Result: %.2f / %.2f = %.2f", num1, num2, result);
                } else {
                    System.out.println("Error: Division by zero is not allowed.");
                }
                break;
            default:
                System.out.println("Error: Invalid operator. Please use +, -, *, or /.");
                break;
        }
        choice.close();
    }
}
