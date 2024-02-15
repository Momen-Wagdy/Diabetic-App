import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class AllFood extends JFrame {
    private JTable table;
    private DiabetesApp diabetesApp;

    public AllFood(DiabetesApp diabetesApp) {
        this.diabetesApp = diabetesApp;

        setTitle("All Food Items");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);

        // Create a table model
        DefaultTableModel model = new DefaultTableModel();
        model.addColumn("Food");
        model.addColumn("Grams");
        model.addColumn("Carbs");

        // Read data from the CSV file
        try (BufferedReader reader = new BufferedReader(new FileReader("nutrients_csvfile.csv"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                model.addRow(new Object[]{data[0], data[2], data[8]});
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Create a table with the model
        table = new JTable(model);

        // Set up the frame layout
        JScrollPane scrollPane = new JScrollPane(table);
        getContentPane().add(scrollPane, BorderLayout.CENTER);

        // Display the frame
        setLocationRelativeTo(null);

        // Add an action listener to handle food selection
        table.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow != -1) {
                    String foodName = (String) table.getValueAt(selectedRow, 0);
                    String carbs = (String) table.getValueAt(selectedRow, 2);
                    diabetesApp.setSelectedFood(foodName, carbs);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new AllFood(null));
    }
}
