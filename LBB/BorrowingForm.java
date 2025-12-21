import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import java.sql.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BorrowingForm extends JFrame {
    // Variable that will store the LoggedIn User passed in the constructor
    private final User loggedInUser;

    // Creating the form items
    public static JLabel bookTitleLbl = new JLabel("Book Title:");
    public static JLabel daysLbl = new JLabel("Borrowing Days:");

    public static JTextField bookTitleTxt = new JTextField();
    public static JTextField daysTxt = new JTextField();

    public static JButton borrowbtn = new JButton("Submit"); 
    
    // Creating the table
    public static JTable borrowedBooksTable = new JTable();
    public static JTable borrowedBooksAuditTable = new JTable();

    public static JLabel booksBorrowedLbl = new JLabel("Books Borrowed: ");

    public BorrowingForm(User user){
        super("Borrowing Book Form - "+ user.name);

        // Seting the Logged in variable equal to the user passed  
        this.loggedInUser = user;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(950, 700);
        setLocationRelativeTo(null);

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBorder(new EmptyBorder(20, 20, 20, 20));
        main.setBackground(new Color(245, 245, 250));

        // Borrowing Panel
        JPanel borrowBookPanel = new JPanel(new GridLayout(3,2,20,20));
        borrowBookPanel.setBackground(Color.WHITE);
        bookTitleLbl.setFont(new Font("Arial", Font.PLAIN, 18));
        bookTitleTxt.setFont(new Font("Arial", Font.PLAIN, 18));
        daysLbl.setFont(new Font("Arial", Font.PLAIN, 18));
        daysTxt.setFont(new Font("Arial", Font.PLAIN, 18));
        borrowbtn.setFont(new Font("Arial", Font.PLAIN, 18));
        borrowbtn.setBackground(Color.GREEN);
        borrowbtn.setForeground(Color.WHITE);

        borrowbtn.addActionListener(e-> submitBorrowingRequest());

        borrowBookPanel.add(bookTitleLbl); borrowBookPanel.add(bookTitleTxt); 
        borrowBookPanel.add(daysLbl); borrowBookPanel.add(daysTxt);
        borrowBookPanel.add(borrowbtn);

        // Table of the Book Borrowed
        JPanel bookBorrowedTablePanel = new JPanel(new BorderLayout(10, 10));
        bookBorrowedTablePanel.setBackground(Color.WHITE);
        bookBorrowedTablePanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2), 
                "Book Borrowed Table: ", 0, 0, new Font("Arial", Font.BOLD, 14), new Color(70, 130, 180)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        borrowedBooksTable.setModel(new DefaultTableModel(new Object[]{"Id", "Title", "Days"}, 0));
        borrowedBooksTable.setFont(new Font("Arial", Font.PLAIN, 12));
        borrowedBooksTable.setRowHeight(25);
        borrowedBooksTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        borrowedBooksTable.getTableHeader().setBackground(new Color(70, 130, 180));
        borrowedBooksTable.getTableHeader().setForeground(Color.WHITE);
        borrowedBooksTable.setSelectionBackground(new Color(173, 216, 230));
        borrowedBooksTable.setGridColor(new Color(220, 220, 220));

        // Making your table responsive and scrollable
        JScrollPane scrollBooksPane = new JScrollPane(borrowedBooksTable);
        scrollBooksPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        bookBorrowedTablePanel.add(scrollBooksPane, BorderLayout.CENTER);

        
        // Table of the Book Audit
        JPanel bookBorrowedAuditTabelPanel = new JPanel(new BorderLayout(10, 10));
        bookBorrowedAuditTabelPanel.setBackground(Color.WHITE);
        bookBorrowedAuditTabelPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(BorderFactory.createLineBorder(new Color(70, 130, 180), 2), 
                "Audit Logs Table: ", 0, 0, new Font("Arial", Font.BOLD, 14), new Color(70, 130, 180)),
            new EmptyBorder(15, 15, 15, 15)
        ));

        borrowedBooksAuditTable.setModel(new DefaultTableModel(new Object[]{"Id", "TimeStamp", "Book Title"}, 0));
        borrowedBooksAuditTable.setFont(new Font("Arial", Font.PLAIN, 12));
        borrowedBooksAuditTable.setRowHeight(25);
        borrowedBooksAuditTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        borrowedBooksAuditTable.getTableHeader().setBackground(new Color(70, 130, 180));
        borrowedBooksAuditTable.getTableHeader().setForeground(Color.WHITE);
        borrowedBooksAuditTable.setSelectionBackground(new Color(173, 216, 230));
        borrowedBooksAuditTable.setGridColor(new Color(220, 220, 220));

        JScrollPane scrollAuditPane = new JScrollPane(borrowedBooksAuditTable);
        scrollAuditPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200)));
        bookBorrowedAuditTabelPanel.add(scrollAuditPane, BorderLayout.CENTER);

        booksBorrowedLbl.setFont(new Font("Arial", Font.BOLD, 18));
        
        // Adding the items to the main Panel 
        main.add(borrowBookPanel, BorderLayout.NORTH);
        main.add(bookBorrowedTablePanel, BorderLayout.CENTER);
        main.add(booksBorrowedLbl, BorderLayout.SOUTH);
        main.add(bookBorrowedAuditTabelPanel, BorderLayout.EAST);

        // Adding the MAin Panel to the Borrowing Form
        add(main);

        // Loading Data from the database
        LoadData();
    }

    private void LoadData(){

        // This is to store the Fetched Data in an Array
        ArrayList<Books> books = new ArrayList<>();
        ArrayList<AuditLogs> audits = new ArrayList<>();

        String booksQuery = "SELECT title, days, id From books Where studentId=?";
        String auditQuery = "SELECT id, TimeLogged, bookTitle From audit_log Where studentId=?";

        try (Connection con = DbConfig.getConnection()) {
            PreparedStatement booksPs = con.prepareStatement(booksQuery);
            PreparedStatement auditPs = con.prepareStatement(auditQuery);

            // Passing in the studentID as the Logged in user id 
            booksPs.setInt(1,loggedInUser.id);
            auditPs.setInt(1,loggedInUser.id);

            ResultSet booksRS = booksPs.executeQuery();
            ResultSet auditRS = auditPs.executeQuery();


            // Looping throught the results for books and adding them to the array
            while (booksRS.next()) { 
                String title = booksRS.getString("title");
                int bookId = booksRS.getInt("id");
                int days = booksRS.getInt("days");

                books.add(new Books(bookId, title, days));
            }

            // Looping throught the results of audits and adding them to the array
            while (auditRS.next()) {
                int auditId = auditRS.getInt("id");
                LocalDateTime timeLogged = auditRS.getTimestamp("TimeLogged").toLocalDateTime();
                String bookTitle = auditRS.getString("bookTitle");

                audits.add(new AuditLogs(auditId, timeLogged, bookTitle));
            }

            // Adding the Data in the table
            DefaultTableModel booksModel = (DefaultTableModel) borrowedBooksTable.getModel();
            booksModel.setRowCount(0);
            for (Books r : books) {
                booksModel.addRow(new Object[]{r.id, r.title, r.days});
            }

            // Adding the Data in the table
            DefaultTableModel auditModel = (DefaultTableModel) borrowedBooksAuditTable.getModel();
            auditModel.setRowCount(0);
            for (AuditLogs r : audits) {
                auditModel.addRow(new Object[]{r.id, r.timestamp, r.title});
            }

            // Calling the Count function
            getCountForBookedBooks();
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Load Error: " + ex.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this,"Unexpected Error: "+e.getMessage());
        }
    }

    private void getCountForBookedBooks(){
        try (Connection con = DbConfig.getConnection();
            CallableStatement stm =  con.prepareCall("{? = call GetHowManyBooks(?)}")
        ) {
            // this is register the output / results
            stm.registerOutParameter(1, java.sql.Types.NUMERIC);

            // Setting This Function Parameter
            stm.setInt(2, loggedInUser.id);

            stm.execute();

            // this Storing the Output
            int count = stm.getInt(1);

            // to update the borrowed Book Count Label
            booksBorrowedLbl.setText("Books Borrowed: "+count);
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this, "Count error: " + ex.getMessage());
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Unexpected error: " + e.getMessage());
        }
    }

    private void submitBorrowingRequest(){
        // Retrieve the inputs
        String bookTitle = bookTitleTxt.getText().trim();
        String daysBorrowed = daysTxt.getText().trim();

        // Validate is they are filled (Not Empty)
        if(bookTitle.isEmpty() || daysBorrowed.isEmpty()){
            JOptionPane.showMessageDialog(this,"All Fields Must be filled");
            return;
        }

        try (Connection con = DbConfig.getConnection();
            PreparedStatement stm = con.prepareStatement("INSERT INTO books (title, days, studentId) VALUES (?,?,?)");
        ) {
            stm.setString(1,bookTitle);
            // Integer.parseInt(daysBorrowed) => Turn text into Number
            stm.setInt(2,Integer.parseInt(daysBorrowed));
            stm.setInt(3,loggedInUser.id);

            int rowsAffected = stm.executeUpdate();
            if(rowsAffected > 0){
                JOptionPane.showMessageDialog(this, "Borrowed Successful");
                bookTitleTxt.setText("");
                daysTxt.setText("");
                LoadData();
            }else{
                JOptionPane.showMessageDialog(this, "Failed to Insert Borrowed Books");
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Failed to Insert Borrowed Books: "+e.getMessage());
        }
    }
}
