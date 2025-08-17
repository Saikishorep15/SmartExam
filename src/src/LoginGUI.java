import javax.swing.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginGUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel statusLabel;

    public LoginGUI() {
        setTitle("SmartExam - Login");
        setSize(400, 250);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);

        JLabel lblUser = new JLabel("Username:");
        lblUser.setBounds(50, 50, 100, 25);
        add(lblUser);

        usernameField = new JTextField();
        usernameField.setBounds(150, 50, 180, 25);
        add(usernameField);

        JLabel lblPass = new JLabel("Password:");
        lblPass.setBounds(50, 90, 100, 25);
        add(lblPass);

        passwordField = new JPasswordField();
        passwordField.setBounds(150, 90, 180, 25);
        add(passwordField);

        loginButton = new JButton("Login");
        loginButton.setBounds(150, 130, 100, 30);
        add(loginButton);

        statusLabel = new JLabel("");
        statusLabel.setBounds(50, 170, 300, 25);
        add(statusLabel);

        loginButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        setVisible(true);
    }

    private void login() {
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        try (Connection conn = DBConnection.getConnection()) {
            PreparedStatement ps = conn.prepareStatement(
                "SELECT * FROM users WHERE username=? AND password=?"
            );
            ps.setString(1, username);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                statusLabel.setText("✅ Login successful!");
                dispose(); // close login window
                new SmartExamGUI(); // open main app
            } else {
                statusLabel.setText("❌ Invalid username or password");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            statusLabel.setText("Error: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new LoginGUI();
    }
}



CREATE TABLE IF NOT EXISTS users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) UNIQUE,
    password VARCHAR(50) NOT NULL
);

-- Insert default admin user
INSERT INTO users (username, password) VALUES ('admin', 'admin123');

