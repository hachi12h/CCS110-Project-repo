import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.border.EmptyBorder;

@SuppressWarnings("serial")
public class InfoPanel extends JPanel {
    private final UserManagementSystem parent;
    JTextField lastNameField;
    JTextField firstNameField;
    JTextField middleNameField;
    JTextField ageField;
    JTextField addressField;
    JTextField emailField;
    JTextField taskField;
    JButton submitButton;
    JComboBox<String> departmentComboBox;
    JComboBox<String> projectComboBox;
    
    private final Map<String, String[]> departmentProjectsMap = new HashMap<>();
    private JButton backButton;
    
    public InfoPanel(UserManagementSystem parent) {
        this.parent = parent;

        initializeDepartmentProjectsMap();
        setPreferredSize(new Dimension(414, 932));
        setLayout(null);

        lastNameField = new JTextField(20);
        lastNameField.setBorder(null);
        lastNameField.setOpaque(false);
        lastNameField.setForeground(new Color(255, 255, 255));
        lastNameField.setFont(new Font("Montserrat", Font.PLAIN, 16));
        lastNameField.setBounds(48, 230, 318, 29);
        add(lastNameField);

        firstNameField = new JTextField(20);
        firstNameField.setFont(new Font("Montserrat", Font.PLAIN, 16));
        firstNameField.setForeground(new Color(255, 255, 255));
        firstNameField.setOpaque(false);
        firstNameField.setBorder(null);
        firstNameField.setBounds(48, 297, 318, 29);
        add(firstNameField);

        middleNameField = new JTextField(20);
        middleNameField.setBorder(null);
        middleNameField.setOpaque(false);
        middleNameField.setForeground(new Color(255, 255, 255));
        middleNameField.setFont(new Font("Montserrat", Font.PLAIN, 16));
        middleNameField.setBounds(48, 364, 318, 29);
        add(middleNameField);

        ageField = new JTextField(20);
        ageField.setOpaque(false);
        ageField.setForeground(new Color(255, 255, 255));
        ageField.setFont(new Font("Montserrat", Font.PLAIN, 16));
        ageField.setBorder(null);
        ageField.setBounds(48, 428, 318, 29);
        add(ageField);

        addressField = new JTextField(20);
        addressField.setForeground(new Color(255, 255, 255));
        addressField.setBorder(null);
        addressField.setFont(new Font("Montserrat", Font.PLAIN, 16));
        addressField.setOpaque(false);
        addressField.setBounds(48, 498, 318, 29);
        add(addressField);

        emailField = new JTextField(20);
        emailField.setOpaque(false);
        emailField.setForeground(new Color(255, 255, 255));
        emailField.setFont(new Font("Montserrat", Font.PLAIN, 16));
        emailField.setBorder(null);
        emailField.setBounds(48, 569, 318, 29);
        add(emailField);

        DefaultComboBoxModel<String> departmentModel = new DefaultComboBoxModel<>(new String[]{"Marketing", "Finance", "IT", "Production", "Maintenance"});
        departmentComboBox = new JComboBox<>(departmentModel);
        departmentComboBox.setBorder(new EmptyBorder(0, 0, 0, 0));
        departmentComboBox.setForeground(new Color(0, 0, 0));
        departmentComboBox.setBackground(new Color(255, 255, 255));
        departmentComboBox.setFont(new Font("Montserrat", Font.PLAIN, 16));
        departmentComboBox.setBounds(36, 639, 340, 34);
        add(departmentComboBox);
        
        projectComboBox = new JComboBox<>();
        projectComboBox.setBorder(new EmptyBorder(0, 0, 0, 0));
        projectComboBox.setFont(new Font("Montserrat", Font.PLAIN, 16));
        projectComboBox.setBounds(36, 708, 340, 34);
        add(projectComboBox);
        
        taskField = new JTextField(20);
        taskField.setOpaque(false);
        taskField.setForeground(new Color(255, 255, 255));
        taskField.setFont(new Font("Montserrat", Font.PLAIN, 16));
        taskField.setBorder(null);
        taskField.setBounds(48, 780, 318, 29);
        add(taskField);
        
        submitButton = new JButton("");
        submitButton.setContentAreaFilled(false);
        submitButton.setBorder(null);
        submitButton.setFont(new Font("Tahoma", Font.PLAIN, 16));
        submitButton.setOpaque(false);
        submitButton.setBounds(126, 835, 160, 39);
        add(submitButton);
       
        JLabel lblInformationBackground = new JLabel("");
        lblInformationBackground.setBounds(0, 0, 414, 896);
        lblInformationBackground.setIcon(new ImageIcon(this.getClass().getResource("/images/Information.png")));
        add(lblInformationBackground);
        
        backButton = new JButton("");
        backButton.setContentAreaFilled(false);
        backButton.setBorder(null);
        backButton.setOpaque(false);
        backButton.setBounds(10, 24, 38, 39);
        add(backButton);

        submitButton.addActionListener(e -> submitInfo());
        
        submitButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });
        
        backButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });

        backButton.addActionListener(e -> parent.showSummaryPanel());
        departmentComboBox.addActionListener(e -> updateProjectComboBox());
        updateProjectComboBox();
    }

    private void initializeDepartmentProjectsMap() {
        departmentProjectsMap.put("Marketing", new String[]{"Market Research", "Campaign Management", "SEO Optimization"});
        departmentProjectsMap.put("Finance", new String[]{"Accounting", "Budgeting", "Investment Analysis"});
        departmentProjectsMap.put("IT", new String[]{"Software Development", "Network Security", "Database Management"});
        departmentProjectsMap.put("Production", new String[]{"Manufacturing", "Quality Control", "Logistics"});
        departmentProjectsMap.put("Maintenance", new String[]{"Equipment Repair", "Routine Inspection", "Facility Management"});
    }

    private void updateProjectComboBox() {
        String selectedDepartment = (String) departmentComboBox.getSelectedItem();
        projectComboBox.removeAllItems();
        if (selectedDepartment != null && departmentProjectsMap.containsKey(selectedDepartment)) {
            for (String project : departmentProjectsMap.get(selectedDepartment)) {
                projectComboBox.addItem(project);
            }
        }
    }

    private void submitInfo() {
        String lastName = lastNameField.getText();
        String firstName = firstNameField.getText();
        String middleName = middleNameField.getText();
        String age = ageField.getText();
        String address = addressField.getText();
        String email = emailField.getText();
        String department = (String) departmentComboBox.getSelectedItem();
        String project = (String) projectComboBox.getSelectedItem();
        String task = taskField.getText();

        User user = new User(lastName, firstName, middleName, age, address, email, department, project, task);
        parent.users.add(user);

        parent.summaryPanel.addUserToTable(user);
        parent.showSummaryPanel();
    }

    public void clearInfoFields() {
        lastNameField.setText("");
        firstNameField.setText("");
        middleNameField.setText("");
        ageField.setText("");
        addressField.setText("");
        emailField.setText("");
        departmentComboBox.setSelectedIndex(0);
        updateProjectComboBox();
        taskField.setText("");
    }
    
    public void setUserToFields(User user) {
        lastNameField.setText(user.lastName);
        firstNameField.setText(user.firstName);
        middleNameField.setText(user.middleName);
        ageField.setText(user.age);
        addressField.setText(user.address);
        emailField.setText(user.email);
        departmentComboBox.setSelectedItem(user.department);
        updateProjectComboBox();
        projectComboBox.setSelectedItem(user.project);
        taskField.setText(user.task);
    }
    
    public User getUserFromFields() {
        return new User(
            lastNameField.getText(),
            firstNameField.getText(),
            middleNameField.getText(),
            ageField.getText(),
            addressField.getText(),
            emailField.getText(),
            (String) departmentComboBox.getSelectedItem(),
            (String) projectComboBox.getSelectedItem(),
            taskField.getText()
        );
    }
}
