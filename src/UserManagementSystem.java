import javax.swing.*;

import raven.glasspanepopup.GlassPanePopup;

import java.awt.*;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class UserManagementSystem extends JFrame {

    CardLayout cardLayout;
    JPanel mainPanel;
    ArrayList<User> users;
    private LoginPanel loginPanel;
    private RegisterPanel registerPanel;
    InfoPanel infoPanel;
    SummaryPanel summaryPanel;

    public UserManagementSystem() {
    	GlassPanePopup.install(this);
        setTitle("AscendWare DBMS");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);
        getContentPane().add(mainPanel);

        users = new ArrayList<>();

        loginPanel = new LoginPanel(this);
        registerPanel = new RegisterPanel(this);
        infoPanel = new InfoPanel(this);
        summaryPanel = new SummaryPanel(this);

        mainPanel.add(loginPanel, "login");
        mainPanel.add(registerPanel, "register");
        mainPanel.add(infoPanel, "info");
        mainPanel.add(summaryPanel, "summary");

        cardLayout.show(mainPanel, "login");
    }

    public void showLoginPanel() {
    	adjustFrameSize(loginPanel);
        cardLayout.show(mainPanel, "login");
    }
    
    public void showRegisterPanel() {
    	adjustFrameSize(registerPanel);
        cardLayout.show(mainPanel, "register");
    }

    public void showInfoPanel() {
    	adjustFrameSize(infoPanel);
        cardLayout.show(mainPanel, "info");
    }

    public void showSummaryPanel() {
    	adjustFrameSize(summaryPanel);
        cardLayout.show(mainPanel, "summary");
        summaryPanel.searchField.setText("Search...");
        summaryPanel.searchButton.doClick();
    }

    private void adjustFrameSize(JPanel panel) {
        Dimension preferredSize = panel.getPreferredSize();
        setSize(preferredSize.width, preferredSize.height);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            UserManagementSystem frame = new UserManagementSystem();
            frame.pack();
            frame.setVisible(true);
            frame.setSize(414, 896);  
        });
    }
}
