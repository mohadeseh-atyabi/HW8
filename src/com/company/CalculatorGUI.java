package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

import static java.awt.event.ActionEvent.ALT_MASK;

/**
 * It is just a sample of a calculator.
 */
public class CalculatorGUI extends JFrame {
    JFrame calcFrame;
    JTextField engInput;
    JPanel engMain;
    JMenuBar menuBar;

    public CalculatorGUI() {
        calcFrame = new JFrame();
        calcFrame.setTitle("AUT Calculator");
        calcFrame.setSize(450, 450);
        calcFrame.setLocation(500 , 200);
        calcFrame.setLayout(null);
        calcFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Add tabs
        engineeringCalc();
        addMenu();

        calcFrame.setVisible(true);
    }

    /**
     * Adds menu to the calculator frame
     */
    public void addMenu(){
        menuBar = new JMenuBar();

        JMenu mainMenu = new JMenu("menu");
        mainMenu.setMnemonic('M');

        //Copy item
        JMenuItem copy = new JMenuItem("Copy");
        copy.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(engMain, "The text is copied." , "Copy" ,
                        JOptionPane.INFORMATION_MESSAGE);
            }
        });
        copy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,InputEvent.ALT_DOWN_MASK));
        copy.setMnemonic(KeyEvent.VK_C);
        mainMenu.add(copy);

        //Exit item
        JMenuItem exit = new JMenuItem("Exit");
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E,InputEvent.ALT_DOWN_MASK));
        exit.setMnemonic(KeyEvent.VK_E);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JOptionPane.showMessageDialog(engMain, "You will exit the program.", "Exit",
                        JOptionPane.WARNING_MESSAGE);
                System.exit(0);
            }
        });
        mainMenu.add(exit);

        menuBar.add(mainMenu);
        calcFrame.setJMenuBar(menuBar);
    }

    /**
     * Sets the engineering calculator.
     */
    public void engineeringCalc() {
        engMain = new JPanel();
        engMain.setSize(350, 350);
        engMain.setLocation(40, 30);
        engMain.setLayout(new BorderLayout());

        JPanel eng = new JPanel();
        eng.setLayout(new GridLayout(6, 4));

        ButtonHandler handler = new ButtonHandler();

        String[] buttons = {"9", "8", "7", "6", "5", "4", "3", "2", "1", "0", "pi", "e", ".",
                "+", "-", "/", "*", "%", "sin/cos", "tan/cot", "exp/log", "shift", "C", "="};

        //Sets the buttons
        for (String str : buttons) {
            JButton btn = new JButton();
            btn.setText(str);
            btn.setToolTipText("" + str);
            btn.addActionListener(handler);
            eng.add(btn);
        }

        engMain.add(eng);

        //Text
        engInput = new JTextField();
        engInput.setToolTipText("Enter the operation with tow operands: ");
        engInput.setFont(new Font("Arial", Font.BOLD, 14));

        KeyHandler keyHandler = new KeyHandler();
        engInput.addKeyListener(keyHandler);

        //Scroll
        JScrollPane scroll = new JScrollPane(engInput);
        scroll.setPreferredSize(new Dimension(200, 80));

        engMain.add(engInput, BorderLayout.NORTH);

        calcFrame.add(engMain);

    }

    /**
     * Inner class to handle buttons
     */
    private class ButtonHandler implements ActionListener{
        double result;
        String text = "";
        String operator = "";
        String operand = "";
        boolean shift = false;
        boolean first = true;

        @Override
        public void actionPerformed(ActionEvent e) {

            String command = e.getActionCommand();
            switch (command) {
                case "=" -> {
                    textResult();
                    findResult();
                    engInput.setText("" + result);
                    operand = "";
                    operator = "";
                }
                case "C" -> {
                    engInput.setText("");
                    text = "";
                    operator = "";
                    operand = "";
                    result = 0;
                    first = true;
                }
                case "+" -> {
                    textResult();
                    operator = "+";
                    engInput.setText(engInput.getText() + "+");
                }
                case "-" -> {

                    if (operand.equals("") || !text.equals("")){
                        operand = "";
                        operand += "-";
                        engInput.setText(engInput.getText() + "-");
                    }

                    else {
                        textResult();
                        operator = "-";
                        engInput.setText(engInput.getText() + "-");
                    }
                }
                case "*" -> {
                    textResult();
                    operator = "*";
                    engInput.setText(engInput.getText() + "x");
                }
                case "%" -> {
                    textResult();
                    operator = "%";
                    engInput.setText(engInput.getText() + "%");
                }
                case "/" -> {
                    textResult();
                    operator = "/";
                    engInput.setText(engInput.getText() + "/");
                }
                case "." -> {
                    operand += ".";
                    engInput.setText(engInput.getText() + ".");
                }
                case "shift" -> shift = true;
                case "sin/cos" -> {
                    if (!shift){
                        text = "sin";
                        engInput.setText(engInput.getText() + "sin");
                    }else {
                        text = "cos";
                        engInput.setText(engInput.getText() + "cos");
                        shift = false;
                    }
                }
                case "tan/cot" -> {
                    if (!shift){
                        text = "tan";
                        engInput.setText(engInput.getText() + "tan");
                    }else {
                        text = "cot";
                        engInput.setText(engInput.getText() + "cot");
                        shift = false;
                    }
                }
                case "exp/log" -> {
                    if (!shift){
                        text = "exp";
                        engInput.setText(engInput.getText() + "exp");
                    }else {
                        text = "log";
                        engInput.setText(engInput.getText() + "log");
                        shift = false;
                    }
                }
                default -> {
                    //If the operand is null it does not work
                    if (!operand.equals("")) {
                        //If it is the first number to add, it would be the initial amount for result
                        if (first && !operator.equals("") && !operand.equals("-")) {
                            result = Double.parseDouble(operand);
                            first = false;
                            operand = "";
                        }
                    }

                    if (command.equals("pi")){
                        operand += Math.PI;
                    }else if (command.equals("e")){
                        operand += Math.E;
                    }else {
                        operand += command;
                    }
                    engInput.setText(engInput.getText() + command);
                }
            }
        }

        /**
         * Finds the result based on the operator.
         */
        public void findResult(){
            switch (operator){
                case "+" -> result += Double.parseDouble(operand);
                case "-" -> result -= Double.parseDouble(operand);
                case "*" -> result *= Double.parseDouble(operand);
                case "/" -> result /= Double.parseDouble(operand);
                case "%" -> result %= Double.parseDouble(operand);
                default -> result += Double.parseDouble(operand);
            }
        }

        /**
         * Sets the result according to the entries.
         */
        public void textResult(){
            if (!text.equals("")){
                double res;

                switch (text){
                    case "sin" -> {
                        res = Double.parseDouble(operand);
                        operand = String.valueOf(Math.sin(res));
                        text = "";
                    }
                    case "cos" -> {
                        res = Double.parseDouble(operand);
                        operand = String.valueOf(Math.cos(res));
                        text = "";
                    }
                    case "tan" -> {
                        res = Double.parseDouble(operand);
                        operand = String.valueOf(Math.tan(res));
                        text = "";
                    }
                    case "cot" -> {
                        res = Double.parseDouble(operand);
                        operand = String.valueOf(1/(Math.tan(res)));
                        text = "";
                    }
                    case "exp" -> {
                        res = Double.parseDouble(operand);
                        operand = String.valueOf(Math.exp(res));
                        text = "";
                    }
                    case "log" -> {
                        res = Double.parseDouble(operand);
                        operand = String.valueOf(Math.log10(res));
                        text = "";
                    }
                }
            }
        }
    }

    /**
     * Inner class to handle keyboard
     */
    private class KeyHandler extends KeyAdapter{
        double result;
        String operand1 = "";
        String operand2 = "";
        String text = "";
        String operator = "";
        boolean firstOperand = true;
        double toUse;

        @Override
        public void keyPressed(KeyEvent e) {

            if(e.getKeyCode() != KeyEvent.VK_ENTER) {

                if (e.getKeyChar() == 'P' || e.getKeyChar() == 'E'){
                    switch (e.getKeyChar()){
                        case 'P' -> {
                            if (operand1.equals("") || operand1.equals("-")) {
                                operand1 += String.valueOf(Math.PI);
                            } else {
                                operand2 += String.valueOf(Math.PI);
                            }
                        }
                        case 'E' -> {
                            if (operand1.equals("") || operand1.equals("-")) {
                                operand1 += String.valueOf(Math.E);
                            } else {
                                operand2 += String.valueOf(Math.E);
                            }
                        }
                    }
                }
                else if (e.getKeyChar() >= '0' && e.getKeyChar() <= '9' || e.getKeyChar() == '.') {
                    if (operator.equals("")) {
                        operand1 += e.getKeyChar();
                    } else {
                        operand2 += e.getKeyChar();
                    }
                } else if (e.getKeyChar() == '-') {
                    if (operand1.equals("")) {
                        operand1 += e.getKeyChar();
                    } else if (operand2.equals("") && !operator.equals("")) {
                        operand2 += e.getKeyChar();
                    } else {
                        operator += e.getKeyChar();
                    }
                }else if (e.getKeyChar() == 'C'){
                    engInput.setText("");
                    operand1 = "";
                    operand2 = "";
                    operator = "";
                    text = "";
                    result = 0;
                } else if (e.getKeyChar() == '+' || e.getKeyChar() == '*' || e.getKeyChar() == '/' || e.getKeyChar() == '%') {
                    operator += e.getKeyChar();
                } else if (e.getKeyChar() >= 'a' && e.getKeyChar() <= 'z') {
                    text += e.getKeyChar();
                    firstOperand = operand1.equals("");
                }
            }
            else {

                if (!text.equals("")) {

                    switch (text) {
                        case "sin" -> {
                            if (firstOperand) {
                                result = Math.sin(Double.parseDouble(operand1));
                                if (!operand2.equals(""))
                                    toUse = Double.parseDouble(operand2);
                            } else {
                                result = Double.parseDouble(operand1);
                                toUse = Math.sin(Double.parseDouble(operand2));
                            }
                        }
                        case "cos" -> {
                            if (firstOperand) {
                                result = Math.cos(Double.parseDouble(operand1));
                                if (!operand2.equals(""))
                                    toUse = Double.parseDouble(operand2);
                            } else {
                                result = Double.parseDouble(operand1);
                                toUse = Math.cos(Double.parseDouble(operand2));
                            }
                        }
                        case "tan" -> {
                            if (firstOperand) {
                                result = Math.tan(Double.parseDouble(operand1));
                                if (!operand2.equals(""))
                                    toUse = Double.parseDouble(operand2);
                            } else {
                                result = Double.parseDouble(operand1);
                                toUse = Math.tan(Double.parseDouble(operand2));
                            }
                        }
                        case "cot" -> {
                            if (firstOperand) {
                                result = 1 / (Math.tan(Double.parseDouble(operand1)));
                                if (!operand2.equals(""))
                                    toUse = Double.parseDouble(operand2);
                            } else {
                                result = Double.parseDouble(operand1);
                                toUse = 1 / (Math.tan(Double.parseDouble(operand2)));
                            }
                        }
                        case "log" -> {
                            if (firstOperand) {
                                result = Math.log10(Double.parseDouble(operand1));
                                if (!operand2.equals(""))
                                    toUse = Double.parseDouble(operand2);
                            } else {
                                result = Double.parseDouble(operand1);
                                toUse = Math.log10(Double.parseDouble(operand2));
                            }
                        }
                        case "exp" -> {
                            if (firstOperand) {
                                result = Math.exp(Double.parseDouble(operand1));
                                if (!operand2.equals(""))
                                    toUse = Double.parseDouble(operand2);
                            } else {
                                result = Double.parseDouble(operand1);
                                toUse = Math.exp(Double.parseDouble(operand2));
                            }
                        }
                    }
                } else {
                    if (!operand1.equals("")) {
                        result = Double.parseDouble(operand1);
                        if (!operand2.equals(""))
                            toUse = Double.parseDouble(operand2);
                    }
                }

                switch (operator) {
                    case "+" -> result += toUse;
                    case "-" -> result -= toUse;
                    case "*" -> result *= toUse;
                    case "/" -> result /= toUse;
                    case "%" -> result %= toUse;
                }
                engInput.setText(String.valueOf(result));
                operand1 = String.valueOf(result);
                operator = "";
                operand2 = "";
                text = "";
            }
        }
    }

}
