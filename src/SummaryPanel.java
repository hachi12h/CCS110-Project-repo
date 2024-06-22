import javax.swing.*;
import javax.swing.table.*;

import raven.glasspanepopup.GlassPanePopup;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;

@SuppressWarnings("serial")
public class SummaryPanel extends JPanel {
    private final UserManagementSystem parent;
    DefaultTableModel summaryTableModel;
    JTable summaryTable;
    JTextField searchField;
    JButton searchButton;
    
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/company_db";
    private final String dbUsername = "root";
    private final String dbPassword = "";
	private JButton addButton;
	private JButton updateButton;
	private JButton deleteButton;
	private JButton generateButton;
    
    public SummaryPanel(UserManagementSystem parent) {
        this.parent = parent;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, dbUsername, dbPassword);
        } catch (Exception e) {
            System.out.println(e);
        }
        
        setPreferredSize(new Dimension(966, 622));
        setLayout(null);

        summaryTableModel = new DefaultTableModel(new String[]{"Last Name", "First Name", "Middle Name", "Age", "Address", "Email", "Department", "Project", "Task"}, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        summaryTable = new JTable(summaryTableModel);
        summaryTable.setBorder(null);
        summaryTable.setBackground(new Color(255, 255, 255));
        summaryTable.setGridColor(new Color(255, 128, 128));
        summaryTable.setForeground(new Color(0, 0, 0));
        summaryTable.setFont(new Font("Montserrat", Font.PLAIN, 12));
        summaryTable.setShowGrid(false);
        summaryTable.setIntercellSpacing(new Dimension(0, 0));
        summaryTable.setOpaque(false);
        summaryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        customizedTableHeader();
        summaryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
        
        CustomScrollPane scrollPane = new CustomScrollPane(summaryTable);
        scrollPane.setBorder(null);
        scrollPane.setOpaque(false);
        scrollPane.getViewport().setOpaque(false);
        scrollPane.setViewportBorder(null);
        scrollPane.setBorder(null);
        scrollPane.setBounds(16, 49, 915, 473);
        
        ScrollBar sb = new ScrollBar();
        sb.setOrientation(JScrollBar.HORIZONTAL);
        
        scrollPane.setHorizontalScrollBar(sb);
        scrollPane.setVerticalScrollBar(new ScrollBar());
        scrollPane.setBackground(new Color(255, 255, 255));

        add(scrollPane);

        loadDataFromDatabase();

        addButton = new JButton("");
        addButton.setBounds(159, 537, 134, 32);
        add(addButton);
        addButton.setBorderPainted(false);
        addButton.setContentAreaFilled(false);
        addButton.setOpaque(false);

        updateButton = new JButton("");
        updateButton.setBounds(325, 537, 134, 32);
        add(updateButton);
        updateButton.setOpaque(false);
        updateButton.setContentAreaFilled(false);
        updateButton.setBorderPainted(false);

        deleteButton = new JButton("");
        deleteButton.setBounds(492, 537, 134, 32);
        add(deleteButton);
        deleteButton.setOpaque(false);
        deleteButton.setBorderPainted(false);
        deleteButton.setContentAreaFilled(false);

        generateButton = new JButton("");
        generateButton.setBounds(658, 537, 134, 32);
        add(generateButton);
        generateButton.setOpaque(false);
        generateButton.setContentAreaFilled(false);
        generateButton.setBorderPainted(false);

        searchButton = new JButton("");
        searchButton.setBounds(650, 10, 27, 22);
        searchButton.setContentAreaFilled(false);
        searchButton.setBorderPainted(false);
        searchButton.setOpaque(false);
        add(searchButton);

        searchField = new JTextField(20);
        searchField.setForeground(Color.WHITE);
        searchField.setFont(new Font("Montserrat", Font.PLAIN, 16));
        searchField.setText("Search...");
        searchField.setBounds(307, 10, 335, 21);
        searchField.setBorder(null);
        searchField.setOpaque(false);
        add(searchField);
        
        searchField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchField.getText().equals("Search...")) {
                    searchField.setText("");
                    searchField.setForeground(Color.WHITE);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchField.getText().isEmpty()) {
                    searchField.setForeground(Color.WHITE);
                    searchField.setText("Search...");
                }
            }
        });
        
        JButton backButton = new JButton("");
        backButton.setContentAreaFilled(false);
        backButton.setOpaque(false);
        backButton.setBorder(null);
        backButton.setBounds(10, 10, 21, 21);
        add(backButton);
        
        JLabel lblSummaryBackground = new JLabel("");
        lblSummaryBackground.setBounds(0, 0, 950, 590);
        lblSummaryBackground.setIcon(new ImageIcon(this.getClass().getResource("/images/Summary.png")));
        add(lblSummaryBackground);

        backButton.addActionListener(e -> parent.showLoginPanel());
        searchButton.addActionListener(e -> search());
        generateButton.addActionListener(e -> generateUserDetails());
        deleteButton.addActionListener(e -> deleteUser());
        updateButton.addActionListener(e -> updateUser());
        addButton.addActionListener(e -> addUser());
        
        addHandCursorListener(addButton);
        addHandCursorListener(deleteButton);
        addHandCursorListener(updateButton);
        addHandCursorListener(generateButton);
        addHandCursorListener(searchButton);
        addHandCursorListener(backButton);
    }
    
    private void addHandCursorListener(JButton button) {
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                setCursor(Cursor.getDefaultCursor());
            }
        });
    }
    
    private void loadDataFromDatabase() {
        summaryTableModel.setRowCount(0);
        parent.users.clear();
        
        String employeeSql = "SELECT e.lastName, e.firstName, e.middleName, e.age, e.address, e.email, " +
                             "d.departmentName, p.projectName, e.task " +
                             "FROM tbl_Employee e " +
                             "JOIN tbl_department d ON e.departmentID = d.departmentID " +
                             "JOIN tbl_project p ON e.projectID = p.projectID";
        try (PreparedStatement statement = connection.prepareStatement(employeeSql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                User user = new User(
                        resultSet.getString("lastName"),
                        resultSet.getString("firstName"),
                        resultSet.getString("middleName"),
                        resultSet.getString("age"),
                        resultSet.getString("address"),
                        resultSet.getString("email"),
                        resultSet.getString("departmentName"),
                        resultSet.getString("projectName"),
                        resultSet.getString("task")
                );
                parent.users.add(user);
                addUserToTable(user);
            }
        } catch (SQLException e) {
        	Message message = new Message();
        	message.setTitle("ERROR");
        	message.setMessage(e.getMessage());
        	GlassPanePopup.showPopup(message);
        }
    }


    private void addUser() {
        parent.infoPanel.clearInfoFields();
        parent.showInfoPanel();

        for (ActionListener al : parent.infoPanel.submitButton.getActionListeners()) {
            parent.infoPanel.submitButton.removeActionListener(al);
        }

        parent.infoPanel.submitButton.addActionListener(e -> {
            User user = parent.infoPanel.getUserFromFields();
            addUserToDatabase(user);
            addUserToTable(user);
            parent.showSummaryPanel();
            loadDataFromDatabase();
        });
    }

    private void addUserToDatabase(User user) {
        int departmentID = getDepartmentID(user.department);
        int projectID = getProjectID(user.project);

        if (departmentID == -1 || projectID == -1) {
        	Message message = new Message();
        	message.setTitle("ERROR");
        	message.setMessage("Please select a project.");
        	GlassPanePopup.showPopup(message);
            return;
        }

        String employeeSql = "INSERT INTO tbl_Employee (lastName, firstName, middleName, age, address, email, departmentID, projectID, task) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String taskSql = "INSERT INTO tbl_task (taskName, projectID) VALUES (?, ?)";

        try (PreparedStatement employeeStatement = connection.prepareStatement(employeeSql);
             PreparedStatement taskStatement = connection.prepareStatement(taskSql)) {

            employeeStatement.setString(1, user.lastName);
            employeeStatement.setString(2, user.firstName);
            employeeStatement.setString(3, user.middleName);
            employeeStatement.setString(4, user.age);
            employeeStatement.setString(5, user.address);
            employeeStatement.setString(6, user.email);
            employeeStatement.setInt(7, departmentID);
            employeeStatement.setInt(8, projectID);
            employeeStatement.setString(9, user.task);
            employeeStatement.executeUpdate();

            taskStatement.setString(1, user.task);
            taskStatement.setInt(2, projectID);
            taskStatement.executeUpdate();
            
            Message message = new Message();
        	message.setTitle("INFORMATION ENTRY");
        	message.setMessage("Information added succesfully to the database.");
        	GlassPanePopup.showPopup(message);
            parent.showSummaryPanel();

        } catch (SQLException e) {
        	Message message = new Message();
        	message.setTitle("ERROR");
        	message.setMessage(e.getMessage());
        	GlassPanePopup.showPopup(message);
        }
    }

    
    public void addUserToTable(User user) {
        summaryTableModel.addRow(new Object[]{user.lastName, user.firstName, user.middleName, user.age, user.address, user.email, user.department, user.project, user.task});
    }
    
    private int getDepartmentID(String departmentName) {
        String sql = "SELECT departmentID FROM tbl_department WHERE departmentName = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, departmentName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("departmentID");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            Message message = new Message();
        	message.setTitle("ERROR");
        	message.setMessage(e.getMessage());
        	GlassPanePopup.showPopup(message);
        }
        return -1;
    }

    private int getProjectID(String projectName) {
        String sql = "SELECT projectID FROM tbl_project WHERE projectName = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, projectName);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("projectID");
            }
        } catch (SQLException e) {
        	Message message = new Message();
        	message.setTitle("ERROR");
        	message.setMessage(e.getMessage());
        	GlassPanePopup.showPopup(message);
        }
        return -1;
    }


    private void deleteUser() {
        int selectedRow = summaryTable.convertRowIndexToModel(summaryTable.getSelectedRow());
        if (selectedRow >= 0) {
            User user = retrieveOriginalIndexFromFilteredTable(selectedRow);
            int userId = getUserIdByDetails(user.email, user.lastName, user.firstName, user.middleName);

            if (userId != -1) {
                int taskId = getTaskIdByTaskNameAndUserId(user.task, userId);

                if (taskId != -1) {
                    deleteUserTasksByUserId(userId);
                    deleteUserFromDatabase(userId);

                    parent.users.remove(selectedRow);
                    summaryTableModel.removeRow(selectedRow);
                    searchField.setText("Search...");
                    search();

                    Message message = new Message();
                    message.setTitle("SELECTED ENTRY");
                    message.setMessage("Successfully deleted the selected employee and their associated tasks.");
                    GlassPanePopup.showPopup(message);
                } else {
                    Message message = new Message();
                    message.setTitle("ERROR");
                    message.setMessage("Task not found or does not match the user.");
                    GlassPanePopup.showPopup(message);
                }
            } else {
                Message message = new Message();
                message.setTitle("ERROR");
                message.setMessage("User not found.");
                GlassPanePopup.showPopup(message);
            }
        } else {
            Message message = new Message();
            message.setTitle("ROW SELECTION");
            message.setMessage("Select a row to delete.");
            GlassPanePopup.showPopup(message);
        }
    }

    private void deleteUserFromDatabase(int userId) {
        String sql = "DELETE FROM tbl_Employee WHERE employeeID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            Message message = new Message();
            message.setTitle("ERROR");
            message.setMessage(e.getMessage());
            GlassPanePopup.showPopup(message);
        }
    }

    private void deleteUserTasksByUserId(int userId) {
        String sql = "DELETE FROM tbl_Task WHERE taskID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, userId);
            statement.executeUpdate();
        } catch (SQLException e) {
            Message message = new Message();
            message.setTitle("ERROR");
            message.setMessage(e.getMessage());
            GlassPanePopup.showPopup(message);
        }
    }

    private int getUserIdByDetails(String email, String lastName, String firstName, String middleName) {
        String sql = "SELECT employeeID FROM tbl_Employee WHERE email = ? AND lastName = ? AND firstName = ? AND middleName = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            statement.setString(2, lastName);
            statement.setString(3, firstName);
            statement.setString(4, middleName);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("employeeID");
                }
            }
        } catch (SQLException e) {
            Message message = new Message();
            message.setTitle("ERROR");
            message.setMessage(e.getMessage());
            GlassPanePopup.showPopup(message);
        }
        return -1; 
    }

    private int getTaskIdByTaskNameAndUserId(String taskName, int userId) {
        String sql = "SELECT taskID FROM tbl_Task WHERE taskName = ? AND taskID = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, taskName);
            statement.setInt(2, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt("taskID");
                }
            }
        } catch (SQLException e) {
            Message message = new Message();
            message.setTitle("ERROR");
            message.setMessage(e.getMessage());
            GlassPanePopup.showPopup(message);
        }
        return -1;
    }

    
    private void updateUser() {
    	int selectedRow = summaryTable.convertRowIndexToModel(summaryTable.getSelectedRow());
        if (selectedRow >= 0) {
        	User user = retrieveOriginalIndexFromFilteredTable(selectedRow);
            parent.infoPanel.setUserToFields(user);

            for (ActionListener al : parent.infoPanel.submitButton.getActionListeners()) {
                parent.infoPanel.submitButton.removeActionListener(al);
            }

            parent.infoPanel.submitButton.addActionListener(e -> {
                User updatedUser = parent.infoPanel.getUserFromFields();
                parent.users.set(selectedRow, updatedUser);
                updateUserInDatabase(user, updatedUser);
                updateUserInTable(selectedRow, updatedUser);
                parent.showSummaryPanel();
            });

            parent.showInfoPanel();
            searchField.setText("Search...");
            search();
        } else {
        	Message message = new Message();
        	message.setTitle("ROW SELECTION");
        	message.setMessage("Select a row to update.");
        	GlassPanePopup.showPopup(message);
        }
    }
    
    private void updateUserInDatabase(User user, User updatedUser) {
        int oldDepartmentID = getDepartmentID(user.department);
        int oldProjectID = getProjectID(user.project);
        int newDepartmentID = getDepartmentID(updatedUser.department);
        int newProjectID = getProjectID(updatedUser.project);

        if (oldDepartmentID == -1 || oldProjectID == -1 || newDepartmentID == -1 || newProjectID == -1) {
            JOptionPane.showMessageDialog(parent, "Invalid department or project");
            return;
        }

        String employeeSql = "UPDATE tbl_Employee SET lastName = ?, firstName = ?, middleName = ?, age = ?, address = ?, email = ?, departmentID = ?, projectID = ?, task = ? "
                           + "WHERE lastName = ? AND firstName = ? AND middleName = ? AND age = ? AND address = ? AND email = ? AND departmentID = ? AND projectID = ? AND task = ?";
        String taskSql = "UPDATE tbl_task SET taskName = ?, projectID = ? WHERE taskName = ? AND projectID = ?";

        try (PreparedStatement employeeStatement = connection.prepareStatement(employeeSql);
             PreparedStatement taskStatement = connection.prepareStatement(taskSql)) {

            employeeStatement.setString(1, updatedUser.lastName);
            employeeStatement.setString(2, updatedUser.firstName);
            employeeStatement.setString(3, updatedUser.middleName);
            employeeStatement.setString(4, updatedUser.age);
            employeeStatement.setString(5, updatedUser.address);
            employeeStatement.setString(6, updatedUser.email);
            employeeStatement.setInt(7, newDepartmentID);
            employeeStatement.setInt(8, newProjectID);
            employeeStatement.setString(9, updatedUser.task);

            employeeStatement.setString(10, user.lastName);
            employeeStatement.setString(11, user.firstName);
            employeeStatement.setString(12, user.middleName);
            employeeStatement.setString(13, user.age);
            employeeStatement.setString(14, user.address);
            employeeStatement.setString(15, user.email);
            employeeStatement.setInt(16, oldDepartmentID);
            employeeStatement.setInt(17, oldProjectID);
            employeeStatement.setString(18, user.task);

            employeeStatement.executeUpdate();

            taskStatement.setString(1, updatedUser.task);
            taskStatement.setInt(2, newProjectID);
            taskStatement.setString(3, user.task);
            taskStatement.setInt(4, oldProjectID);

            taskStatement.executeUpdate();
            Message message = new Message();
        	message.setTitle("SELECTED ENTRY");
        	message.setMessage("Successfully updated the selected datas.");
        	GlassPanePopup.showPopup(message);
            
        } catch (SQLException e) {
        	Message message = new Message();
        	message.setTitle("ERROR");
        	message.setMessage(e.getMessage());
        	GlassPanePopup.showPopup(message);
        }
    }

    
    private void updateUserInTable(int row, User user) {
        summaryTableModel.setValueAt(user.lastName, row, 0);
        summaryTableModel.setValueAt(user.firstName, row, 1);
        summaryTableModel.setValueAt(user.middleName, row, 2);
        summaryTableModel.setValueAt(user.age, row, 3);
        summaryTableModel.setValueAt(user.address, row, 4);
        summaryTableModel.setValueAt(user.email, row, 5);
        summaryTableModel.setValueAt(user.department, row, 6);
        summaryTableModel.setValueAt(user.project, row, 7);
        summaryTableModel.setValueAt(user.task, row, 8);
    }

    private void generateUserDetails() {
    	
        int selectedRow = summaryTable.convertRowIndexToModel(summaryTable.getSelectedRow());

        if (selectedRow >= 0) {
            User user = retrieveOriginalIndexFromFilteredTable(selectedRow);
            UserDetailsDialog userDetailsDialog = new UserDetailsDialog(parent);
            userDetailsDialog.generateUserDetails(user);
        } else {
            Message message = new Message();
            message.setTitle("ROW SELECTION");
            message.setMessage("Please select a user to generate details.");
            GlassPanePopup.showPopup(message);
        }
    }
    
    private User retrieveOriginalIndexFromFilteredTable(int selectedRow) {
        if (selectedRow >= 0) {
            int modelRow = selectedRow;

            String lastName = summaryTable.getModel().getValueAt(modelRow, 0).toString();
            String firstName = summaryTable.getModel().getValueAt(modelRow, 1).toString();
            String middleName = summaryTable.getModel().getValueAt(modelRow, 2).toString();
            String age = summaryTable.getModel().getValueAt(modelRow, 3).toString();
            String address = summaryTable.getModel().getValueAt(modelRow, 4).toString();
            String email = summaryTable.getModel().getValueAt(modelRow, 5).toString();
            String department = summaryTable.getModel().getValueAt(modelRow, 6).toString();
            String project = summaryTable.getModel().getValueAt(modelRow, 7).toString();
            String task = summaryTable.getModel().getValueAt(modelRow, 8).toString();

            int originalIndex = findOriginalIndex(lastName, firstName, middleName, age, address, email, department, project, task);
            if (originalIndex != -1) {
                User user = parent.users.get(originalIndex);
                return user;
            } 
        }
        return null;
    }

    private int findOriginalIndex(String lastName, String firstName, String middleName,
                                  String age, String address, String email,
                                  String department, String project, String task) {
        for (int i = 0; i < parent.users.size(); i++) {
            User user = parent.users.get(i);
            if (user.lastName.equalsIgnoreCase(lastName) &&
                user.firstName.equalsIgnoreCase(firstName) &&
                user.middleName.equalsIgnoreCase(middleName) &&
                user.age.equalsIgnoreCase(age) &&
                user.address.equalsIgnoreCase(address) &&
                user.email.equalsIgnoreCase(email) &&
                user.department.equalsIgnoreCase(department) &&
                user.project.equalsIgnoreCase(project) &&
                user.task.equalsIgnoreCase(task)) {
                return i; 
            }
        }
        return -1; 
    }


    private void search() {
        String query = searchField.getText().trim().toLowerCase();

        if (!query.isEmpty() && !query.equalsIgnoreCase("search...")) {
        	
            DefaultTableModel filteredModel = new DefaultTableModel(new String[]{"Last Name", "First Name", "Middle Name", "Age", "Address", "Email", "Department", "Project", "Task"}, 0) {
                @Override
                public boolean isCellEditable(int row, int column) {
                    return false;
                }
            };

            String employeeSql = "SELECT e.lastName, e.firstName, e.middleName, e.age, e.address, e.email, " +
                                 "d.departmentName, p.projectName, e.task " +
                                 "FROM tbl_Employee e " +
                                 "JOIN tbl_department d ON e.departmentID = d.departmentID " +
                                 "JOIN tbl_project p ON e.projectID = p.projectID " +
                                 "WHERE LOWER(e.lastName) LIKE ? OR LOWER(e.firstName) LIKE ? OR LOWER(e.middleName) LIKE ? " +
                                 "OR LOWER(e.age) LIKE ? OR LOWER(e.address) LIKE ? OR LOWER(e.email) LIKE ? " +
                                 "OR LOWER(d.departmentName) LIKE ? OR LOWER(p.projectName) LIKE ? OR LOWER(e.task) LIKE ?";

            try (PreparedStatement statement = connection.prepareStatement(employeeSql)) {
                for (int i = 1; i <= 9; i++) {
                    statement.setString(i, "%" + query + "%");
                }
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    String lastName = resultSet.getString("lastName");
                    String firstName = resultSet.getString("firstName");
                    String middleName = resultSet.getString("middleName");
                    String age = resultSet.getString("age");
                    String address = resultSet.getString("address");
                    String email = resultSet.getString("email");
                    String departmentName = resultSet.getString("departmentName");
                    String projectName = resultSet.getString("projectName");
                    String task = resultSet.getString("task");

                    filteredModel.addRow(new Object[]{lastName, firstName, middleName, age, address, email, departmentName, projectName, task});
                }
            } catch (SQLException e) {
                e.printStackTrace();
                Message message = new Message();
                message.setTitle("ERROR");
                message.setMessage(e.getMessage());
                GlassPanePopup.showPopup(message);
                return;
            }
            summaryTable.setModel(filteredModel);

        } else {
            loadDataFromDatabase();
            summaryTable.setModel(summaryTableModel);
        }
        searchField.setText("Search...");
        customizedTableHeader();
    }
    
    private void customizedTableHeader() {
    	TableCellRenderer headerRenderer = new TableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                                                           boolean isSelected, boolean hasFocus, int row, int column) {
                JLabel label = new JLabel(value.toString());
                label.setHorizontalAlignment(SwingConstants.CENTER);
                label.setFont(new Font("Montserrat", Font.PLAIN, 14));
                label.setOpaque(true);
                label.setBackground(null);
                label.setForeground(Color.BLACK);
                return label;
            }
        };

        JTableHeader header = summaryTable.getTableHeader();
        header.setDefaultRenderer(headerRenderer);
        header.setReorderingAllowed(false);

        TableColumnModel columnModel = summaryTable.getColumnModel();
        columnModel.getColumn(0).setPreferredWidth(125);  
        columnModel.getColumn(1).setPreferredWidth(125); 
        columnModel.getColumn(2).setPreferredWidth(125);
        columnModel.getColumn(3).setPreferredWidth(50);
        columnModel.getColumn(4).setPreferredWidth(150);
        columnModel.getColumn(5).setPreferredWidth(175);
        columnModel.getColumn(6).setPreferredWidth(125);
        columnModel.getColumn(7).setPreferredWidth(150);
        columnModel.getColumn(8).setPreferredWidth(250);
    }
}
