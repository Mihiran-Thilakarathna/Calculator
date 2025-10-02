package org.example;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

// Main class for the Calculator application. It extends JFrame to create a window
// and implements ActionListener to handle button clicks.
public class Calculator extends JFrame implements ActionListener {

    // --- Instance Variables ---
    // These variables store the state of the calculator.

    private JTextField display;          // The text field that shows numbers and results.
    private String operator = "";        // Stores the last operator clicked (+, -, *, /).
    private double firstNumber = 0;      // Stores the first number in a calculation.
    private boolean startNewNumber = true; // A flag to check if the next digit should start a new number or append.

    public Calculator() {
        // --- UI Setup for the main window (the JFrame) ---
        setTitle("Calculator");
        setSize(360, 500);               // Fixed size to prevent shrinking
        setLocationRelativeTo(null);     // Center on screen
        setResizable(false);             // Prevent resizing
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- Setup for the calculator's display screen ---
        display = new JTextField("0");
        display.setFont(new Font("SansSerif", Font.PLAIN, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setEditable(false);
        display.setBackground(Color.WHITE);
        display.setForeground(Color.BLACK);
        display.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        add(display, BorderLayout.NORTH);

        // --- Button Creation ---

        // An array to hold the text for all calculator buttons in order.
        String[] buttons = {
                "C", "±", "%", "÷",
                "√", "7", "8", "9", "×",
                "4", "5", "6", "−",
                "1", "2", "3", "+",
                "0", ".", "="
        };

        // Create a panel to hold all the buttons in a grid layout.
        JPanel panel = new JPanel(new GridLayout(6, 4, 10, 10));
        panel.setBackground(new Color(240, 240, 240));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        // Loop through the button texts to create each button.
        for (String text : buttons) {
            JButton btn = new JButton(text);
            btn.setFont(new Font("SansSerif", Font.BOLD, 20));
            btn.setFocusPainted(false);
            btn.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

            // Apply different colors based on the button type (operator, equals, number).
            if ("C±%÷×−+√".contains(text)) {
                btn.setBackground(new Color(173, 216, 230)); // Style for function buttons
            } else if (text.equals("=")) {
                btn.setBackground(new Color(0, 120, 215));   // Style for the equals button
                btn.setForeground(Color.WHITE);
            } else {
                btn.setBackground(Color.WHITE);              // Style for number buttons
            }

            btn.addActionListener(this); // Make the calculator listen for a click on this button
            panel.add(btn);
        }

        add(panel, BorderLayout.CENTER);
        setVisible(true); // Make the window visible
    }

    // This method is called whenever any button is clicked.
    public void actionPerformed(ActionEvent e) {
        // Get the text from the button that was clicked (e.g., "7", "+", "=").
        String cmd = e.getActionCommand();

        // --- Logic for Number Buttons (0-9) ---
        if ("0123456789".contains(cmd)) {
            if (startNewNumber) {
                display.setText(cmd); // Start a new number on the display
                startNewNumber = false;
            } else {
                display.setText(display.getText() + cmd); // Add the digit to the current number
            }
            // --- Logic for the Decimal Point Button (.) ---
        } else if (cmd.equals(".")) {
            if (!display.getText().contains(".")) {
                display.setText(display.getText() + "."); // Add a decimal point if one doesn't exist
            }
            // --- Logic for the Clear Button (C) ---
        } else if (cmd.equals("C")) {
            display.setText("0"); // Reset display
            operator = "";        // Reset operator
            firstNumber = 0;      // Reset first number
            startNewNumber = true; // Ready for a new number
            // --- Logic for the Plus/Minus Button (±) ---
        } else if (cmd.equals("±")) {
            double val = Double.parseDouble(display.getText());
            display.setText(String.valueOf(-val)); // Make the current number negative or positive
            // --- Logic for the Percentage Button (%) ---
        } else if (cmd.equals("%")) {
            double val = Double.parseDouble(display.getText());
            display.setText(String.valueOf(val / 100)); // Calculate percentage
            // --- Logic for the Square Root Button (√) ---
        } else if (cmd.equals("√")) {
            double val = Double.parseDouble(display.getText());
            if (val >= 0) {
                display.setText(String.valueOf(Math.sqrt(val))); // Calculate square root
            } else {
                display.setText("Error"); // Cannot find sqrt of a negative number
            }
            startNewNumber = true;
            // --- Logic for Operator Buttons (+, −, ×, ÷) ---
        } else if ("÷×−+".contains(cmd)) {
            operator = cmd; // Store the operator
            firstNumber = Double.parseDouble(display.getText()); // Store the current display value as the first number
            startNewNumber = true; // Prepare for the second number to be typed
            // --- Logic for the Equals Button (=) ---
        } else if (cmd.equals("=")) {
            double secondNumber = Double.parseDouble(display.getText());
            double result = 0;

            // Perform the calculation based on the stored operator.
            result = switch (operator) {
                case "+" -> firstNumber + secondNumber;
                case "−" -> firstNumber - secondNumber;
                case "×" -> firstNumber * secondNumber;
                case "÷" -> firstNumber / secondNumber;
                default -> secondNumber; // If no operator was pressed, the result is the second number
            };
            display.setText(String.valueOf(result)); // Show the result
            startNewNumber = true; // Prepare for a new calculation
        }
    }

    // The main entry point of the application.
    public static void main(String[] args) {
        // Use invokeLater to ensure that the UI is created on the Event Dispatch Thread (EDT),
        // which is the standard and safe way to start a Swing application.
        SwingUtilities.invokeLater(Calculator::new);
    }
}