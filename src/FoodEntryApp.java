import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class FoodEntryApp {
    public static void main(String[] args) {
        JFrame frame = new JFrame("Food Entry App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        JPanel panel = new JPanel();
        frame.add(panel);
        placeComponents(panel);

        frame.setVisible(true);
    }

    private static void placeComponents(JPanel panel) {
        panel.setLayout(null);

        JLabel foodLabel = new JLabel("Food:");
        foodLabel.setBounds(10, 20, 80, 25);
        panel.add(foodLabel);

        JTextField foodText = new JTextField(20);
        foodText.setBounds(100, 20, 165, 25);
        panel.add(foodText);

        JLabel carbLabel = new JLabel("Carb Grams:");
        carbLabel.setBounds(10, 50, 80, 25);
        panel.add(carbLabel);

        JTextField carbText = new JTextField(20);
        carbText.setBounds(100, 50, 165, 25);
        panel.add(carbText);

        JButton addButton = new JButton("Add Food");
        addButton.setBounds(10, 80, 150, 25);
        panel.add(addButton);

        addButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    String foodName = foodText.getText();
                    double carbGrams = Double.parseDouble(carbText.getText());
                    DatabaseHandler.addFood(foodName, carbGrams);
                    JOptionPane.showMessageDialog(panel, "Food added successfully!");
                } catch (NumberFormatException | SQLException ex) {
                    JOptionPane.showMessageDialog(panel, "Error adding food: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
