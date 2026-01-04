import java.awt.*;
import java.awt.event.*;
import java.util.Arrays;
import javax.swing.*;
import javax.swing.border.LineBorder;

public class Calculator {
    int boardWidth = 360;
    int boardHeight = 540;

    Color customPaleGreen = new Color(120, 197, 142);
    Color customDarkGreen = new Color(0, 88, 0);
    Color customWhite = new Color(255, 255, 255);
    Color customPale = new Color(255, 170, 115);
    Color customDarkerGreen = new Color(0, 72, 70);
    Color customExtremeDarkGreen = new Color(0, 52, 0);

    String[] buttonValues = {
        "AC", "+/-", "%", "÷",
        "7", "8", "9", "×",
        "4", "5", "6", "-",
        "1", "2", "3", "+",
        "0", ".", "√", "="
    };
    String[] rightSymbols = {"÷", "×", "-", "+", "="};
    String[] topSymbols = {"AC", "+/-", "%"};

    JFrame frame = new JFrame("calculator");
    JLabel displayLabel = new JLabel();
    JPanel displayPanel = new JPanel();
    JPanel buttonsPanel = new JPanel();

    String A = "0";
    String operator = null;
    String B = null;

    public Calculator() {
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        displayLabel.setBackground(customDarkGreen);
        displayLabel.setForeground(customPale);
        displayLabel.setFont(new Font("Arial", Font.PLAIN, 80));
        displayLabel.setHorizontalAlignment(SwingConstants.RIGHT);
        displayLabel.setText("0");
        displayLabel.setOpaque(true);

        displayPanel.setLayout(new BorderLayout());
        displayPanel.add(displayLabel, BorderLayout.CENTER);
        frame.add(displayPanel, BorderLayout.NORTH);

        buttonsPanel.setLayout(new GridLayout(5, 4));
        buttonsPanel.setBackground(customDarkerGreen);
        frame.add(buttonsPanel, BorderLayout.CENTER);

        for (int i = 0; i < buttonValues.length; i++) {
            JButton button = new JButton();
            String buttonValue = buttonValues[i];

            button.setFont(new Font("Arial", Font.PLAIN, 30));
            button.setText(buttonValue);
            button.setFocusable(false);
            button.setBorder(new LineBorder(customDarkGreen));

            if (Arrays.asList(topSymbols).contains(buttonValue)) {
                button.setBackground(customPaleGreen);
                button.setForeground(customWhite);
            } else if (Arrays.asList(rightSymbols).contains(buttonValue)) {
                button.setBackground(customDarkerGreen);
                button.setForeground(customPale);
            } else {
                button.setBackground(customExtremeDarkGreen);
                button.setForeground(customWhite);
            }

            buttonsPanel.add(button);

            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton b = (JButton) e.getSource();
                    handleInput(b.getText());
                }
            });
        }

        setupKeyBindings();
        frame.setVisible(true);
    }

    void handleInput(String buttonValue) {
        if (Arrays.asList(rightSymbols).contains(buttonValue)) {
            if (buttonValue.equals("=")) {
                if (operator == null) return;

                B = displayLabel.getText();
                double numA = Double.parseDouble(A);
                double numB = Double.parseDouble(B);

                if (operator.equals("+")) {
                    displayLabel.setText(removeZeroDecimal(numA + numB));
                } else if (operator.equals("-")) {
                    displayLabel.setText(removeZeroDecimal(numA - numB));
                } else if (operator.equals("×")) {
                    displayLabel.setText(removeZeroDecimal(numA * numB));
                } else if (operator.equals("÷")) {
                    displayLabel.setText(removeZeroDecimal(numA / numB));
                }

                clearAll();
            } else if ("+-×÷".contains(buttonValue)) {
                if (operator == null) {
                    A = displayLabel.getText();
                    displayLabel.setText("0");
                    B = "0";
                }
                operator = buttonValue;
            }
            return;
        }

        if (Arrays.asList(topSymbols).contains(buttonValue)) {
            if (buttonValue.equals("AC")) {
                clearAll();
                displayLabel.setText("0");
            } else if (buttonValue.equals("+/-")) {
                if (displayLabel.getText().equals("Error")) return;
                double numDisplay = Double.parseDouble(displayLabel.getText());
                numDisplay *= -1;
                displayLabel.setText(removeZeroDecimal(numDisplay));
            } else if (buttonValue.equals("%")) {
                if (displayLabel.getText().equals("Error")) return;
                double numDisplay = Double.parseDouble(displayLabel.getText());
                numDisplay /= 100.0;
                displayLabel.setText(removeZeroDecimal(numDisplay));
            }
            return;
        }

        if (buttonValue.equals(".")) {
            if (displayLabel.getText().equals("Error")) {
                displayLabel.setText("0");
            }
            if (!displayLabel.getText().contains(".")) {
                displayLabel.setText(displayLabel.getText() + ".");
            }
            return;
        }

        if ("0987654321".contains(buttonValue)) {
            if (displayLabel.getText().equals("Error")) {
                displayLabel.setText("0");
            }
            if (displayLabel.getText().equals("0")) {
                displayLabel.setText(buttonValue);
            } else {
                displayLabel.setText(displayLabel.getText() + buttonValue);
            }
            return;
        }

        if (buttonValue.equals("√")) {
            if (displayLabel.getText().equals("Error")) return;

            double value = Double.parseDouble(displayLabel.getText());
            if (value < 0) {
                displayLabel.setText("Error");
            } else {
                double result = Math.sqrt(value);
                displayLabel.setText(removeZeroDecimal(result));
            }
        }
    }

    void setupKeyBindings() {
        JRootPane root = frame.getRootPane();
        InputMap im = root.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = root.getActionMap();

        for (int i = 0; i <= 9; i++) {
            String digit = String.valueOf(i);
            im.put(KeyStroke.getKeyStroke(digit), "digit_" + digit);
            am.put("digit_" + digit, new AbstractAction() {
                public void actionPerformed(ActionEvent e) {
                    handleInput(digit);
                }
            });
        }

        im.put(KeyStroke.getKeyStroke('.'), "dot");
        am.put("dot", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                handleInput(".");
            }
        });

        im.put(KeyStroke.getKeyStroke('+'), "plus");
        am.put("plus", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                handleInput("+");
            }
        });

        im.put(KeyStroke.getKeyStroke('-'), "minus");
        am.put("minus", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                handleInput("-");
            }
        });

        im.put(KeyStroke.getKeyStroke('*'), "times");
        am.put("times", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                handleInput("×");
            }
        });

        im.put(KeyStroke.getKeyStroke('/'), "divide");
        am.put("divide", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                handleInput("÷");
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "equals");
        am.put("equals", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                handleInput("=");
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), "ac");
        am.put("ac", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                handleInput("AC");
            }
        });

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0), "backspace");
        am.put("backspace", new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                handleBackspace();
            }
        });
    }

    void handleBackspace() {
        String text = displayLabel.getText();

        if (text.equals("Error")) {
            displayLabel.setText("0");
            return;
        }

        if (text.length() > 1) {
            displayLabel.setText(text.substring(0, text.length() - 1));
        } else {
            displayLabel.setText("0");
        }
    }

    void clearAll() {
        A = "0";
        operator = null;
        B = null;
    }

    String removeZeroDecimal(double numDisplay) {
        if (Math.abs(numDisplay - Math.round(numDisplay)) < 1e-10) {
            return Long.toString(Math.round(numDisplay));
        }
        return Double.toString(numDisplay);
    }

    public static void main(String[] args) {
        new Calculator();
    }
}
