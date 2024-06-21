import javax.swing.*;

import raven.glasspanepopup.GlassPanePopup;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@SuppressWarnings("serial")
public class LoginPanel extends JPanel {
	
    private final UserManagementSystem parent;
    private JTextField usernameField;
    private JPasswordField passwordField;
	private JButton loginButton;
	private JButton registerButton;

    public LoginPanel(UserManagementSystem parent) {
        this.parent = parent;
        setPreferredSize(new Dimension(414, 896));
        setLayout(null);

        usernameField = new JTextField(20);
        usernameField.setForeground(new Color(255, 255, 255));
        usernameField.setFont(new Font("Montserrat", Font.PLAIN, 22));
        usernameField.setOpaque(false);
        usernameField.setBorder(null);
        usernameField.setBounds(79, 519, 237, 34);
        add(usernameField);

        passwordField = new JPasswordField(20);
        passwordField.setBorder(null);
        passwordField.setOpaque(false);
        passwordField.setForeground(new Color(255, 255, 255));
        passwordField.setFont(new Font("Montserrat", Font.PLAIN, 22));
        passwordField.setBounds(79, 618, 237, 34);
        add(passwordField);

        loginButton = new JButton("");
        loginButton.setContentAreaFilled(false);
        loginButton.setOpaque(false);
        loginButton.setBorder(null);
        loginButton.setBounds(77, 704, 230, 40);
        add(loginButton);

        registerButton = new JButton("");
        registerButton.setContentAreaFilled(false);
        registerButton.setBorder(null);
        registerButton.setOpaque(false);
        registerButton.setBounds(77, 766, 230, 40);
        add(registerButton);
        
        JLabel lblLoginBackground = new JLabel("");
        lblLoginBackground.setBounds(0, 0, 414, 896);
        lblLoginBackground.setIcon(new ImageIcon(this.getClass().getResource("/images/Login.png")));
        add(lblLoginBackground);

        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });
        
        loginButton.addActionListener(e -> {
			try {
				login();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		});
        
        registerButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });
        
        
        registerButton.addActionListener(e -> parent.showRegisterPanel());
    }

    private void login() throws ClassNotFoundException {
    	Connection connection = null;
    	PreparedStatement usersStatement = null;
    	ResultSet resultSet = null;
    	
        String username = usernameField.getText();
        String password = new String(passwordField.getPassword());

        String url = "jdbc:mysql://localhost:3306/company_db";
		String dbUsername = "root";
		String dbPassword = "";
		
		Message message = new Message();
		message.setDimensions(300, 120);
        try {
        	
        	Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, dbUsername, dbPassword);
        	
			String sql = "SELECT * FROM tbl_management_user WHERE username = ? AND userPassword = ?";
			usersStatement = connection.prepareStatement(sql);
	        usersStatement.setString(1, username);
	        usersStatement.setString(2, password);

	        resultSet = usersStatement.executeQuery();

	        if (resultSet.next()) {
	        	message.setTitle("LOGIN STATUS");
	        	message.setMessage("Login Succesful!");
	        	GlassPanePopup.showPopup(message);
	            parent.showSummaryPanel();
	        } else {
	        	message.setTitle("LOGIN STATUS");
	        	message.setMessage("Invalid username or password.");
	        	GlassPanePopup.showPopup(message);
	        }
        	
        } catch (SQLException e) {
        	
        	message.setTitle("ERROR");
        	message.setMessage(e.getMessage());
        	GlassPanePopup.showPopup(message);
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (usersStatement != null) usersStatement.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
            	message.setTitle("ERROR");
            	message.setMessage(e.getMessage());
            	GlassPanePopup.showPopup(message);
            }
        }
        
        usernameField.setText("");
        passwordField.setText("");
        
    }
}
