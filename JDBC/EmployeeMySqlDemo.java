package JDBC;

import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.Connection;

public class EmployeeMySqlDemo {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/javaclass";
        String username = "root";
        String password = "";
        
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Statement stm = connection.createStatement();

            Scanner sc = new Scanner(System.in);
            System.out.println("Enter Emplyee Id: ");
            String employeeIdPar = sc.nextLine();
            System.out.println("Enter First Name: ");
            String firstNamePar = sc.nextLine();
            System.out.println("Enter Last Name: ");
            String lastNamePar = sc.nextLine();
            System.out.println("Enter Email: ");
            String emailPar = sc.nextLine();
            System.out.println("Enter Age: ");
            int agePar = sc.nextInt();
            System.out.println("Enter Salary: ");
            int salaryPar = sc.nextInt();

            String query = "INSERT INTO EMPLOYEE (EMPLOYEE_ID, FIRST_NAME, LAST_NAME, EMAIL, AGE, SALARY) VALUES ('"+employeeIdPar+"', '"+firstNamePar+"', '"+lastNamePar+"', '"+emailPar+"', '"+agePar+"', '"+salaryPar+"')";

            int numRows = stm.executeUpdate(query);

            if(numRows>0){
                System.out.println("Data inserted successfully");
                String querySelect = "SELECT * FROM EMPLOYEE WHERE FIRST_NAME='"+firstNamePar+"' and LAST_NAME='"+firstNamePar+"'";

            ResultSet rs = stm.executeQuery(querySelect);

            while (rs.next()) {
                String employeeId=rs.getString("EMPLOYEE_ID");
                String firstName=rs.getString("FIRST_NAME");
                String lastName=rs.getString("LAST_NAME");
                String email=rs.getString("EMAIL");
                int age = rs.getInt("AGE");
                int salary = rs.getInt("SALARY");

                System.out.println("\nEmployee ID: "+employeeId);
                System.out.println("First Name: "+firstName);
                System.out.println("Last Name: "+lastName);
                System.out.println("Email: "+email);
                System.out.println("Age: "+age);
                System.out.println("Salary: "+salary);
                System.out.println("+___________________________________________+");
            }
            }else{
                System.out.println("Data not inserted");
            }

            

        } catch (Exception e) {
           e.printStackTrace();
        }
    }
}
