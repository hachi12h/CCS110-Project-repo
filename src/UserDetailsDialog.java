import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class UserDetailsDialog {

    private final UserManagementSystem parent;

    public UserDetailsDialog(UserManagementSystem parent) {
        this.parent = parent;
    }

    public void generateUserDetails(User user) {
        showUserDetailsDialog(user);
    }

    /**
     * @wbp.parser.entryPoint
     */
    private void showUserDetailsDialog(User user) {
        JDialog dialog = new JDialog(parent, "User Details", true);
        dialog.setSize(950, 500);
        dialog.setUndecorated(true);
        dialog.setLocationRelativeTo(parent);

        final Point[] mouseDownCompCoords = new Point[1];

        dialog.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                mouseDownCompCoords[0] = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                mouseDownCompCoords[0] = null;
            }
        });

        dialog.addMouseMotionListener(new MouseAdapter() {
            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                dialog.setLocation(currCoords.x - mouseDownCompCoords[0].x, currCoords.y - mouseDownCompCoords[0].y);
            }
        });
        
        
        JPanel detailsPanel = new JPanel();
        detailsPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        detailsPanel.setLayout(null);
        
        JLabel lblLastName = new JLabel(user.lastName);
        lblLastName.setForeground(new Color(255, 255, 255));
        lblLastName.setFont(new Font("Montserrat", Font.PLAIN, 18));
        lblLastName.setBounds(404, 84, 176, 35);
        detailsPanel.add(lblLastName);
        
        JLabel lblFirstName = new JLabel(user.firstName);
        lblFirstName.setFont(new Font("Montserrat", Font.PLAIN, 18));
        lblFirstName.setForeground(new Color(255, 255, 255));
        lblFirstName.setBounds(590, 84, 176, 35);
        detailsPanel.add(lblFirstName);
        
        JLabel lblMiddleName = new JLabel(user.middleName);
        lblMiddleName.setForeground(new Color(255, 255, 255));
        lblMiddleName.setFont(new Font("Montserrat", Font.PLAIN, 18));
        lblMiddleName.setBounds(776, 86, 170, 35);
        detailsPanel.add(lblMiddleName);
        
        JLabel lblAge = new JLabel(user.age);
        lblAge.setForeground(new Color(255, 255, 255));
        lblAge.setFont(new Font("Montserrat", Font.PLAIN, 18));
        lblAge.setBounds(456, 136, 122, 35);
        detailsPanel.add(lblAge);
        
        JLabel lblAddress = new JLabel(user.address);
        lblAddress.setForeground(new Color(255, 255, 255));
        lblAddress.setFont(new Font("Montserrat", Font.PLAIN, 16));
        lblAddress.setBounds(509, 180, 441, 35);
        detailsPanel.add(lblAddress);
        
        JLabel lblEmail = new JLabel(user.email);
        lblEmail.setForeground(new Color(255, 255, 255));
        lblEmail.setFont(new Font("Montserrat", Font.PLAIN, 18));
        lblEmail.setBounds(476, 221, 462, 35);
        detailsPanel.add(lblEmail);
        
        JLabel lblDepartment = new JLabel(user.department);
        lblDepartment.setForeground(new Color(255, 255, 255));
        lblDepartment.setFont(new Font("Montserrat", Font.PLAIN, 18));
        lblDepartment.setBounds(551, 263, 387, 35);
        detailsPanel.add(lblDepartment);
        
        JLabel lblProject = new JLabel(user.project);
        lblProject.setForeground(new Color(255, 255, 255));
        lblProject.setFont(new Font("Montserrat", Font.PLAIN, 18));
        lblProject.setBounds(505, 305, 433, 35);
        detailsPanel.add(lblProject);
        
        JTextArea lblTask = new JTextArea(user.task);
        lblTask.setForeground(new Color(255, 255, 255));
        lblTask.setFont(new Font("Montserrat", Font.PLAIN, 15));
        lblTask.setBounds(404, 379, 483, 93);
        lblTask.setLineWrap(true);
        lblTask.setWrapStyleWord(true);
        lblTask.setOpaque(false);
        lblTask.setEditable(false);
        lblTask.setBorder(null);
        detailsPanel.add(lblTask);

        dialog.getContentPane().add(detailsPanel, BorderLayout.CENTER);

        JButton closeButton = new JButton("");
        closeButton.setContentAreaFilled(false);
        closeButton.setBorder(null);
        closeButton.setOpaque(false);
        closeButton.setLocation(888, 10);
        closeButton.setSize(38, 35);
        closeButton.addActionListener(e -> dialog.dispose());
        detailsPanel.add(closeButton);
        
        closeButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                closeButton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                closeButton.setCursor(Cursor.getDefaultCursor());
            }
        });
        
        JLabel lblGenerateBackground = new JLabel("");
        lblGenerateBackground.setBounds(0, 0, 950, 500);
        detailsPanel.add(lblGenerateBackground);
        lblGenerateBackground.setIcon(new ImageIcon(this.getClass().getResource("/images/GenerateReport.png")));
        dialog.setVisible(true);
    }
}
