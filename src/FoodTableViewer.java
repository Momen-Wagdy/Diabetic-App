import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class FoodTableViewer extends JFrame {
    private JTable foodTable;
    private DefaultTableModel tableModel;

    public FoodTableViewer() {
        setTitle("Food Table Viewer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500, 300);

        // Explicitly set the column names
        String[] columnNames = {"Food Name", "Carb Grams"};
        tableModel = new DefaultTableModel(columnNames, 0);

        foodTable = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(foodTable);
        add(scrollPane, BorderLayout.CENTER);

        JButton addButton = new JButton("Add Food");
        JButton editButton = new JButton("Edit Food");
        JButton deleteButton = new JButton("Delete Food");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(addButton);
        buttonPanel.add(editButton);
        buttonPanel.add(deleteButton);

        add(buttonPanel, BorderLayout.SOUTH);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addFood();
            }
        });

        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editFood();
            }
        });

        deleteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteFood();
            }
        });

        loadFoodData();
    }

    private void loadFoodData() {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // Clear existing data
                    tableModel.setRowCount(0);

                    // Retrieve food data from the database and populate the table
                    List<DatabaseHandler.FoodData> foodDataList = DatabaseHandler.retrieveAllFoodData();
                    for (DatabaseHandler.FoodData foodData : foodDataList) {
                        Object[] rowData = {foodData.getFoodName(), foodData.getCarbGrams()};
                        tableModel.addRow(rowData);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    // Handle the exception appropriately (show a message, log, etc.)
                }
            }
        });
    }



    private void addFood() {
        String foodName = JOptionPane.showInputDialog("Enter Food Name:");
        if (foodName != null && !foodName.isEmpty()) {
            try {
                double carbGrams = Double.parseDouble(JOptionPane.showInputDialog("Enter Carb Grams:"));
                DatabaseHandler.addFood(foodName, carbGrams);
                loadFoodData();
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number for Carb Grams.");
            }
        }
    }

    private void editFood() {
        int selectedRow = foodTable.getSelectedRow();
        if (selectedRow != -1) {
            String foodName = (String) foodTable.getValueAt(selectedRow, 0);
            double currentCarbGrams = (double) foodTable.getValueAt(selectedRow, 1);

            try {
                double newCarbGrams = Double.parseDouble(JOptionPane.showInputDialog("Enter New Carb Grams:", currentCarbGrams));
                DatabaseHandler.updateFoodData(foodName, newCarbGrams);
                loadFoodData();
            } catch (NumberFormatException | SQLException e) {
                e.printStackTrace();
                JOptionPane.showMessageDialog(this, "Invalid input. Please enter a valid number for Carb Grams.");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to edit.");
        }
    }

    private void deleteFood() {
        int selectedRow = foodTable.getSelectedRow();
        if (selectedRow != -1) {
            String foodName = (String) foodTable.getValueAt(selectedRow, 0);
            try {
                DatabaseHandler.deleteFood(foodName);
                loadFoodData();
            } catch (SQLException e) {
                e.printStackTrace();
                // Handle the exception appropriately (show a message, log, etc.)
            }
        } else {
            JOptionPane.showMessageDialog(this, "Please select a row to delete.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                FoodTableViewer viewer = new FoodTableViewer();
                viewer.setVisible(true);
            }
        });
    }


}
