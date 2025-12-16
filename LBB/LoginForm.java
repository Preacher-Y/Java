
import java.awt.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.*;

public class LoginForm extends JFrame {

    public static JLabel userNameLabel = new JLabel("Username: ");
    public static JLabel passwordLabel = new JLabel("Password: ");

    public static JTextField userNameTxt = new JTextField();
    public static JPasswordField passwordTxt = new JPasswordField();

    public static JButton loginBtn = new JButton("Login");

    public LoginForm() {
        super("Login Form");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        JPanel loginPanel = new JPanel(new GridLayout(3, 2, 10, 10));
        loginPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        userNameLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        userNameTxt.setFont(new Font("Arial", Font.PLAIN, 18));
        passwordTxt.setFont(new Font("Arial", Font.PLAIN, 18));
        loginBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        loginPanel.add(userNameLabel);
        loginPanel.add(userNameTxt);
        loginPanel.add(passwordLabel);
        loginPanel.add(passwordTxt);
        loginBtn.setBackground(Color.BLUE);
        loginBtn.setForeground(Color.WHITE);
        loginPanel.add(loginBtn);

        add(loginPanel);

        loginBtn.addActionListener(e -> authenticate());
    }

    private void authenticate() {
        String username = userNameTxt.getText().trim();
        String password = new String(passwordTxt.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "All Field must be filled");
            return;
        }

        try (Connection con = DbConfig.getConnection();
                Statement stm = con.createStatement()) {

            String query = "SELECT id, name, username FROM students WHERE username='" + username.replace("'", "''")
                    + "' and password='" + password.replace("'", "''") + "'";
            
            ResultSet rs = stm.executeQuery(query);

            if(rs.next()){
                int id = rs.getInt("id");
                String name = rs.getString("name");
                String userName = rs.getString("username");

                User user = new User(id, name, userName);
                JOptionPane.showMessageDialog(this, "Login successful! Welcome " + name);
                new BorrowingForm(user).setVisible(true);
                dispose();
            } else {
                JOptionPane.showMessageDialog(this, "Invalid username or password");
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage());
        }
    }
}