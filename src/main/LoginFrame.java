package main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class LoginFrame extends JFrame {
    private final AuthService authService = new AuthService();

    private final JTextField usernameField = new JTextField();
    private final JPasswordField passwordField = new JPasswordField();

    public LoginFrame() {
        setTitle("Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setContentPane(buildContent());
        pack();
        setMinimumSize(getSize());
        setResizable(false);
        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                SwingUtilities.invokeLater(() -> usernameField.requestFocusInWindow());
            }
        });
    }

    private JPanel buildContent() {
        JPanel root = new JPanel(new BorderLayout(0, 18));
        root.setBorder(BorderFactory.createEmptyBorder(24, 24, 24, 24));
        root.setBackground(new Color(236, 236, 236));

        JLabel title = new JLabel("Inventory System Login", SwingConstants.CENTER);
        title.setFont(new Font("Segoe UI", Font.BOLD, 22));

        JLabel subtitle = new JLabel("", SwingConstants.CENTER);
        subtitle.setFont(new Font("Segoe UI", Font.PLAIN, 14));

        JPanel header = new JPanel(new GridLayout(2, 1, 0, 6));
        header.setOpaque(false);
        header.add(title);
        header.add(subtitle);

        JPanel form = new JPanel(new GridLayout(4, 1, 0, 10));
        form.setOpaque(false);
        usernameField.setColumns(18);
        usernameField.setEditable(true);
        usernameField.setEnabled(true);
        passwordField.setColumns(18);
        passwordField.setEditable(true);
        passwordField.setEnabled(true);

        form.add(createFieldPanel("Username", usernameField));
        form.add(createFieldPanel("Password", passwordField));

        JButton loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginButton.setBackground(new Color(82, 109, 130));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        loginButton.addActionListener(e -> attemptLogin());

        JLabel hint = new JLabel("Username: admin    Password: admin123", SwingConstants.CENTER);
        hint.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        hint.setForeground(new Color(90, 90, 90));

        form.add(loginButton);
        form.add(hint);

        root.add(header, BorderLayout.NORTH);
        root.add(form, BorderLayout.CENTER);
        getRootPane().setDefaultButton(loginButton);

        return root;
    }

    private JPanel createFieldPanel(String labelText, javax.swing.JComponent field) {
        JPanel panel = new JPanel(new BorderLayout(0, 6));
        panel.setOpaque(false);

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Segoe UI", Font.BOLD, 13));
        panel.add(label, BorderLayout.NORTH);
        panel.add(field, BorderLayout.CENTER);

        return panel;
    }

    private void attemptLogin() {
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());

        AuthResult result = authService.authenticate(username, password);
        if (result.isAuthenticated()) {
            new Main(result.getFullName()).setVisible(true);
            dispose();
            return;
        }

        UIManager.getLookAndFeel().provideErrorFeedback(this);
        JOptionPane.showMessageDialog(
                this,
                result.getMessage(),
                "Login Failed",
                JOptionPane.ERROR_MESSAGE
        );
        passwordField.setText("");
        passwordField.requestFocusInWindow();
    }
}
