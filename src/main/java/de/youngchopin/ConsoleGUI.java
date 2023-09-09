package de.youngchopin;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.OutputStream;
import java.io.PrintStream;

public class ConsoleGUI extends JFrame {

    private JTextArea textArea;
    private JTextField inputField;


    public ConsoleGUI() {
        setTitle("GUI mit Konsole");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);

        textArea = new JTextArea();
        textArea.setEditable(false);

        inputField = new JTextField();
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String inputText = inputField.getText();
                commands(inputText);
                inputField.setText("");
            }
        });

        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(new JScrollPane(textArea), BorderLayout.CENTER);
        getContentPane().add(inputField, BorderLayout.SOUTH);

        // Erstellen Sie eine benutzerdefinierte OutputStream-Implementierung
        OutputStream outputStream = new OutputStream() {
            @Override
            public void write(int b) {
                SwingUtilities.invokeLater(() -> textArea.append(String.valueOf((char) b)));
            }
        };

        System.setOut(new PrintStream(outputStream));
        System.setErr(new PrintStream(outputStream));
    }

    public void writeToConsole(String text) {
        textArea.append(text + "\n");
    }
    public void commands(String command) {
        switch (command) {
            case "help" -> {
             writeToConsole("All Commands:");
             writeToConsole("shutdown - Exits the Program");
             writeToConsole("stalk - Gives you all messages on all Servers");
             writeToConsole("clear - clears this console");
            }
            case "shutdown" -> System.exit(0);
            case "stalk" -> {
                Listener.stalk = !Listener.stalk;
                writeToConsole("Stalking Mode was set to " + Listener.stalk);
            }
            case "clear" -> textArea.setText("");
            default -> writeToConsole("Sorry command wasnt found");

        }
    }
}
