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
public class RegisterPanel extends JPanel {
    private final UserManagementSystem parent;
    private JTextField regUsernameField;
    private JPasswordField regPasswordField;
	private JButton registerButton;
	private JButton loginButton;

    public RegisterPanel(UserManagementSystem parent) {
        this.parent = parent;
        setPreferredSize(new Dimension(414, 896));
        setLayout(null);

        regUsernameField = new JTextField(20);
        regUsernameField.setForeground(new Color(255, 255, 255));
        regUsernameField.setFont(new Font("Montserrat", Font.PLAIN, 22));
        regUsernameField.setOpaque(false);
        regUsernameField.setBorder(null);
        regUsernameField.setBounds(79, 511, 261, 38);
        add(regUsernameField);

        regPasswordField = new JPasswordField(20);
        regPasswordField.setBorder(null);
        regPasswordField.setOpaque(false);
        regPasswordField.setForeground(new Color(255, 255, 255));
        regPasswordField.setFont(new Font("Montserrat", Font.PLAIN, 22));
        regPasswordField.setBounds(79, 604, 261, 34);
        add(regPasswordField);

        registerButton = new JButton("");
        registerButton.setBorder(null);
        registerButton.setContentAreaFilled(false);
        registerButton.setOpaque(false);
        registerButton.setBounds(79, 703, 254, 46);
        add(registerButton);
        
        loginButton = new JButton("");
        loginButton.setContentAreaFilled(false);
        loginButton.setOpaque(false);
        loginButton.setBorder(null);
        loginButton.setBounds(143, 427, 129, 17);
        add(loginButton);
        
        JLabel lblRegisterBackground = new JLabel("");
        lblRegisterBackground.setBounds(0, 0, 414, 896);
        lblRegisterBackground.setIcon(new ImageIcon(this.getClass().getResource("/images/Register.png")));
        add(lblRegisterBackground);

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
        
        registerButton.addActionListener(e -> {
			try {
				register();
			} catch (ClassNotFoundException e1) {
				e1.printStackTrace();
			}
		});
        
        loginButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                // Change cursor to hand cursor
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                // Change cursor back to default
                setCursor(Cursor.getDefaultCursor());
            }
        });
        
        loginButton.addActionListener(e -> parent.showLoginPanel());
    }

    private void register() throws ClassNotFoundException {
    	Connection connection = null;
    	PreparedStatement usersStatement = null;
    	ResultSet resultSet = null;
    	
        String username = regUsernameField.getText();
        String password = new String(regPasswordField.getPassword());

        String url = "jdbc:mysql://localhost:3306/company_db";
		String dbUsername = "root";
		String dbPassword = "";
		
		Message message = new Message();
		message.setDimensions(300, 120);
        try {
        	Class.forName("com.mysql.cj.jdbc.Driver");
			connection = DriverManager.getConnection(url, dbUsername, dbPassword);

			String sql = "INSERT INTO tbl_management_user (username, userPassword) VALUES(? ,?)";
			usersStatement = connection.prepareStatement(sql);
	        usersStatement.setString(1, username);
	        usersStatement.setString(2, password);

	        if(username.isEmpty() || password.isEmpty()) {
	        	message.setTitle("REGISTRATION STATUS");
	        	message.setMessage("Must fill in all of the fields!");
	        	GlassPanePopup.showPopup(message);
	        } else {
	        	usersStatement.executeUpdate();
	        	message.setTitle("REGISTRATION STATUS");
	        	message.setMessage("Registered Succesfully!");
	        	GlassPanePopup.showPopup(message);
		        parent.showLoginPanel();
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
        
        regUsernameField.setText("");
        regPasswordField.setText("");
    }
}
