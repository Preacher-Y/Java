package assin1;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

public class Main {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/booking_db";
        String username = "root";
        String password = "";

        JFrame frame = new JFrame("Hotel Booking Form");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(500, 650);

        JPanel panel = new JPanel(new GridLayout(15, 2, 5, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JLabel clientIdLabel = new JLabel("Client ID :");
        JLabel firstNameLabel = new JLabel("First Name :");
        JLabel lastNameLabel = new JLabel("Last Name :");
        JLabel mobilePhoneLabel = new JLabel("Mobile Phone :");
        JLabel rooTypLabel = new JLabel("Room Type :");
        JLabel roomRateLabel = new JLabel("Room Rate :");
        JLabel checkInLabel = new JLabel("Check In Date :");
        JLabel checkOutLabel = new JLabel("Check Out Date :");
        JLabel numberNightLabel = new JLabel("Number Of Night :");
        JLabel taxLabel = new JLabel("Tax :");
        JLabel discountLabel = new JLabel("Discount :");
        JLabel totalLabel = new JLabel("Total Price:");

        JTextField clientIdField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField mobilePhoneField = new JTextField();

        JComboBox<String> roomTypeComboBox = new JComboBox<>();
        roomTypeComboBox.addItem("Single");
        roomTypeComboBox.addItem("Double");
        roomTypeComboBox.addItem("Suite");

        JLabel roomRateField = new JLabel();

        SpinnerDateModel modelIn = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner checkInSpinner = new JSpinner(modelIn);
        JSpinner.DateEditor editorIn = new JSpinner.DateEditor(checkInSpinner, "yyyy/MM/dd");
        checkInSpinner.setEditor(editorIn);

        SpinnerDateModel modelOut = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        JSpinner checkOutSpinner = new JSpinner(modelOut);
        JSpinner.DateEditor editorOut = new JSpinner.DateEditor(checkOutSpinner, "yyyy/MM/dd");
        checkOutSpinner.setEditor(editorOut);

        JLabel numberNightField = new JLabel("0");
        JLabel taxField = new JLabel("0.00");
        JLabel discountField = new JLabel("0.00");
        JLabel totalField = new JLabel("0.00");

        JButton saveButton = new JButton("Save");
        saveButton.setSize(100, 50);

        panel.add(clientIdLabel); panel.add(clientIdField);
        panel.add(firstNameLabel); panel.add(firstNameField);
        panel.add(lastNameLabel); panel.add(lastNameField);
        panel.add(mobilePhoneLabel); panel.add(mobilePhoneField);
        panel.add(rooTypLabel); panel.add(roomTypeComboBox);
        panel.add(roomRateLabel); panel.add(roomRateField);
        panel.add(checkInLabel); panel.add(checkInSpinner);
        panel.add(checkOutLabel); panel.add(checkOutSpinner);
        panel.add(numberNightLabel); panel.add(numberNightField);
        panel.add(taxLabel); panel.add(taxField);
        panel.add(discountLabel); panel.add(discountField);
        panel.add(totalLabel); panel.add(totalField);
        panel.add(saveButton);

        frame.add(panel);
        frame.setVisible(true);

        clientIdField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (!clientIdField.getText().trim().isEmpty() && !clientIdField.getText().trim().matches("\\d+")) {
                    JOptionPane.showMessageDialog(frame, "CLIENT ID must be numeric.");
                    clientIdField.requestFocus();
                }
            }
        });

        firstNameField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (!firstNameField.getText().trim().isEmpty() && !firstNameField.getText().trim().matches("[a-zA-Z]+")) {
                    JOptionPane.showMessageDialog(frame, "FIRST NAME must contain only letters.");
                    firstNameField.requestFocus();
                }
            }
        });

        lastNameField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (!lastNameField.getText().trim().isEmpty() && !lastNameField.getText().trim().matches("[a-zA-Z]+")) {
                    JOptionPane.showMessageDialog(frame, "LAST NAME must contain only letters.");
                    lastNameField.requestFocus();
                }
            }
        });

        mobilePhoneField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                if (!mobilePhoneField.getText().trim().isEmpty() && !mobilePhoneField.getText().trim().matches("07\\d{8}")) {
                    JOptionPane.showMessageDialog(frame, "MOBILE PHONE must follow format 07########.");
                    mobilePhoneField.requestFocus();
                }
            }
        });

        roomTypeComboBox.addActionListener(e -> {
            String type = (String) roomTypeComboBox.getSelectedItem();
            switch (type) {
                case "Single": roomRateField.setText("70000"); break;
                case "Double": roomRateField.setText("120000"); break;
                case "Suite": roomRateField.setText("190000"); break;
                default: roomRateField.setText(""); break;
            }
            calculateAndPopulate(checkInSpinner, checkOutSpinner, roomRateField, numberNightField, taxField, discountField, totalField, frame);
        });

        ChangeListener recalcListener = new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                calculateAndPopulate(checkInSpinner, checkOutSpinner, roomRateField, numberNightField, taxField, discountField, totalField, frame);
            }
        };
        checkInSpinner.addChangeListener(recalcListener);
        checkOutSpinner.addChangeListener(recalcListener);

        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (clientIdField.getText().isEmpty() || firstNameField.getText().isEmpty() ||
                        lastNameField.getText().isEmpty() || mobilePhoneField.getText().isEmpty() ||
                        roomRateField.getText().isEmpty() || numberNightField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "Please complete all required fields and ensure calculations are done.");
                    return;
                }

                try {
                    int ClientID = Integer.parseInt(clientIdField.getText());
                    String FirstName = firstNameField.getText();
                    String LastName = lastNameField.getText();
                    String MobilePhone = mobilePhoneField.getText();
                    String RoomType = roomTypeComboBox.getSelectedItem().toString();
                    Double RoomRate = Double.parseDouble(roomRateField.getText());

                    Date utilCheckIn = (Date) checkInSpinner.getValue();
                    Date utilCheckOut = (Date) checkOutSpinner.getValue();
                    java.sql.Date sqlCheckIn = new java.sql.Date(utilCheckIn.getTime());
                    java.sql.Date sqlCheckOut = new java.sql.Date(utilCheckOut.getTime());

                    int NumberOfNight = Integer.parseInt(numberNightField.getText());
                    double Tax = Double.parseDouble(taxField.getText());
                    double Discount = Double.parseDouble(discountField.getText());
                    double TotalPrice = Double.parseDouble(totalField.getText());

                    Class.forName("com.mysql.cj.jdbc.Driver");
                    Connection con = DriverManager.getConnection(url, username, password);

                    String sql = "INSERT INTO BOOKING_DATA (CLIENT_ID, FIRST_NAME, LAST_NAME, MOBILE_PHONE, ROOM_TYPE, ROOM_RATE, CHECK_IN_DATE, CHECK_OUT_DATE, NUMBER_OF_NIGHTS, TAX, DISCOUNT, TOTAL_PRICE) VALUES (?,?,?,?,?,?,?,?,?,?,?,?)";
                    PreparedStatement stm = con.prepareStatement(sql);

                    stm.setInt(1, ClientID);
                    stm.setString(2, FirstName);
                    stm.setString(3, LastName);
                    stm.setString(4, MobilePhone);
                    stm.setString(5, RoomType);
                    stm.setDouble(6, RoomRate);
                    stm.setDate(7, sqlCheckIn);
                    stm.setDate(8, sqlCheckOut);
                    stm.setInt(9, NumberOfNight);
                    stm.setDouble(10, Tax);
                    stm.setDouble(11, Discount);
                    stm.setDouble(12, TotalPrice);

                    int rowsAffected = stm.executeUpdate();

                    if (rowsAffected > 0) {
                        JOptionPane.showMessageDialog(null, "Data inserted successfully!");
                        clientIdField.setText("");
                        firstNameField.setText("");
                        lastNameField.setText("");
                        mobilePhoneField.setText("");
                        roomTypeComboBox.setSelectedIndex(0);
                        roomRateField.setText("");
                        checkInSpinner.setValue(new Date());
                        checkOutSpinner.setValue(new Date());
                        numberNightField.setText("0");
                        taxField.setText("0.00");
                        discountField.setText("0.00");
                        totalField.setText("0.00");
                    } else {
                        JOptionPane.showMessageDialog(null, "Failed to insert data.");
                    }

                    con.close();
                } catch (Exception err) {
                    err.printStackTrace();
                    JOptionPane.showMessageDialog(frame, "Error saving booking: " + err.getMessage());
                }
            }
        });
    }

    private static void calculateAndPopulate(JSpinner checkInSpinner, JSpinner checkOutSpinner, JLabel roomRateField, JLabel numberNightField, JLabel taxField, JLabel discountField, JLabel totalField, Component parent) {
        try {
            
            Date checkInUtil = (Date) checkInSpinner.getValue();
            Date checkOutUtil = (Date) checkOutSpinner.getValue();

            LocalDate checkIn = checkInUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            LocalDate checkOut = checkOutUtil.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

            if (!checkOut.isAfter(checkIn)) {
                numberNightField.setText("0");
                taxField.setText("0.00");
                discountField.setText("0.00");
                totalField.setText("0.00");
                return;
            }

            long nights = ChronoUnit.DAYS.between(checkIn, checkOut);
            numberNightField.setText(String.valueOf(nights));

            String rateText = roomRateField.getText();
            if (rateText == null || rateText.isEmpty()) {
                taxField.setText("0.00");
                discountField.setText("0.00");
                totalField.setText("0.00");
                return;
            }

            double rate = Double.parseDouble(rateText);
            double tax = rate * nights * 0.05;
            double discount = (nights >= 7) ? rate * nights * 0.10 : 0;
            double total = rate * nights + tax - discount;

            taxField.setText(String.format("%.2f", tax));
            discountField.setText(String.format("%.2f", discount));
            totalField.setText(String.format("%.2f", total));

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(parent, "Error calculating booking details: " + ex.getMessage());
        }
    }
}
