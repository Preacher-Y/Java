
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;

public class LoginForm extends JFrame {

    // Create Labels for Login Form
    public static JLabel userNameLabel = new JLabel("Username: ");
    public static JLabel passwordLabel = new JLabel("Password: ");

    // Create Inputs
    public static JTextField userNameTxt = new JTextField();
    public static JPasswordField passwordTxt = new JPasswordField();

    // Button Login
    public static JButton loginBtn = new JButton("Login");

    // Constructor for LoginForm
    public LoginForm() {
        // This is setting the title for the Frame
        super("Login Form");
        // Press x in corner it close the operation
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // Setting Size (Width, Height)
        setSize(400, 200);
        // Set it In Center
        setLocationRelativeTo(null);
        
        // Panel that will be having the Login Form
        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

        // Adjusting the Fonts
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        userNameTxt.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordTxt.setFont(new Font("Arial", Font.PLAIN, 18));
        loginBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        loginBtn.setBackground(Color.BLUE);
        loginBtn.setForeground(Color.WHITE);

        // Adding the Item(Labels, inputs, button) on the panel
        loginPanel.add(userNameLabel);
        loginPanel.add(userNameTxt);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordTxt);
        loginPanel.add(loginBtn);

        // Adding the Panel to the Form
        add(loginPanel);
        
        // Adding the action Listener Basically to listen of the Click event
        loginBtn.addActionListener(e -> authenticate());
    }

    // Authentication Logic / Function
    private void authenticate() {
        // Retrieve inputs for the text fields 
        String username = userNameTxt.getText().trim();
        String password = new String(passwordTxt.getPassword());
        
        // Validate if they are filled
        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All Field must be filled");
            return;
        }

        // Exception handling using Try and catch
        try ( // Creating the Connection using the DbConfig class
            Connection con = DbConfig.getConnection();
            Statement stm = con.createStatement()) {
            
            // Query to be used to retrieve User Data
            String query = "SELECT id, name, username FROM students WHERE username='" + username.replace("'", "''")
                    + "' and password='" + password.replace("'", "''") + "'";
            
            // .replace("'", "''") this will be 1'OR => 1''OR this would cause some syntax error
            
            // Creating the Set/table containing the Result or data pulled from the database
            ResultSet rs = stm.executeQuery(query);
            
            // Checking if the user info where retrieved
            if(rs.next()){
                // Storing User Data info into the variables 
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String userName = rs.getString("username");

                // User Instance that will act as the Logged In User.
                User user = new User(id, name, userName);

                // Message to show the User Logged in successfully
                JOptionPane.showMessageDialog(this, "Login successful! Welcome " + name);

                // Navigating to the Borrowing Form
                new BorrowingForm(user).setVisible(true);

                // Closing the Login Form
                dispose();
            } else {
                // If there so data in the table retrieved
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}