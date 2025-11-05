package Midterm;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.awt.event.FocusEvent;
import java.awt.event.FocusAdapter;

public class midterm {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Midterm"); 
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400,500);
        
        JPanel panel = new JPanel(new GridLayout(9,2,10,10));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 20, 50, 20));
        
        // JLables
        JLabel InvoiceNumber = new JLabel("Invoice Number");
        JLabel ProductName = new JLabel("Product Name");
        JLabel Quantity = new JLabel("Quantity");
        JLabel UnitPrice = new JLabel("Unit Price");
        JLabel SubTotal = new JLabel("SubTotal");
        JLabel Discount = new JLabel("Discount");
        JLabel GrandTotal = new JLabel("Grand Total");
        JLabel CashPaid = new JLabel("Cash Paid");
        JLabel Balance = new JLabel("Balance");

        // Text Fields
        JTextField InvoiceNumberField = new JTextField();
        JTextField ProductNameField = new JTextField();
        JTextField QuantityField = new JTextField();
        JTextField UnitPriceField = new JTextField();
        JLabel SubTotalField = new JLabel("0");
        JLabel DiscountField = new JLabel("0.0");
        JLabel GrandTotalField = new JLabel("0.0");
        JTextField CashPaidField = new JTextField();
        JLabel BalanceField = new JLabel("0.0");

        // adding the Components to the Panel
        panel.add(InvoiceNumber);panel.add(InvoiceNumberField);
        panel.add(ProductName); panel.add(ProductNameField);
        panel.add(Quantity); panel.add(QuantityField);
        panel.add(UnitPrice); panel.add(UnitPriceField);
        panel.add(SubTotal); panel.add(SubTotalField);
        panel.add(Discount); panel.add(DiscountField);
        panel.add(GrandTotal); panel.add(GrandTotalField);
        panel.add(CashPaid); panel.add(CashPaidField);
        panel.add(Balance); panel.add(BalanceField);

        UnitPriceField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                try {
                    double quantity = Double.parseDouble(QuantityField.getText());
                    double unitPrice = Double.parseDouble(UnitPriceField.getText());

                    if (quantity <= 0 || unitPrice <= 0) {
                        JOptionPane.showMessageDialog(frame, "Quantity and Unit Price must be greater than 0.");
                        return;
                    }

                    double subtotal = quantity * unitPrice;
                    double discount = (subtotal >= 50000) ? subtotal * 0.10 : 0;
                    double grandTotal = subtotal - discount;

                    SubTotalField.setText(String.format("%.2f", subtotal));
                    DiscountField.setText(String.format("%.2f", discount));
                    GrandTotalField.setText(String.format("%.2f", grandTotal));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter valid numeric values for Quantity and Unit Price.");
                }
            }
        });

        CashPaidField.addFocusListener(new FocusAdapter() {
            public void focusLost(FocusEvent e) {
                try {
                    double grandTotal = Double.parseDouble(GrandTotalField.getText());
                    double cashPaid = Double.parseDouble(CashPaidField.getText());

                    if (cashPaid < grandTotal) {
                        JOptionPane.showMessageDialog(frame, "Cash Paid must be greater than or equal to Grand Total.");
                        return;
                    }

                    double balance = cashPaid - grandTotal;
                    BalanceField.setText(String.format("%.2f", balance));
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(frame, "Please enter a valid numeric value for Cash Paid.");
                }
            }
        });


        
        frame.add(panel);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
