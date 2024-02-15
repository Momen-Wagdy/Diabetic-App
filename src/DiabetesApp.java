import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class DiabetesApp extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField ageField;
    private JTextField fastingGlucoseField;
    private JTextField glycohemoglobinField;
    private JTextField carbGramsField;
    private JTextField preMealGlucoseField;
    private JTextArea resultArea;
    private JTextField selectedFoodField;

    private InsulinCalculator insulinCalculator;

    public DiabetesApp() {
        setTitle("Diabetes Management App");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        insulinCalculator = new InsulinCalculator();

        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(8, 2, 5, 5));
        addComponents(inputPanel);

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        addButtons(buttonPanel);

        JPanel resultPanel = new JPanel(new BorderLayout());
        resultArea = new JTextArea(5, 20);
        resultArea.setEditable(false);
        resultPanel.add(new JScrollPane(resultArea), BorderLayout.CENTER);

        mainPanel.add(inputPanel, BorderLayout.NORTH);
        mainPanel.add(buttonPanel, BorderLayout.CENTER);
        mainPanel.add(resultPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }

    private void addComponents(JPanel panel) {
        // Add components to the panel
        // You can customize the layout according to your needs

        // Example: ageField, fastingGlucoseField, glycohemoglobinField, carbGramsField, preMealGlucoseField, resultArea

        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        ageField = new JTextField(15);
        fastingGlucoseField = new JTextField(15);
        glycohemoglobinField = new JTextField(15);
        carbGramsField = new JTextField(15);
        preMealGlucoseField = new JTextField(15);
        selectedFoodField = new JTextField(15);
        selectedFoodField.setEditable(false);

        panel.add(new JLabel("Username:"));
        panel.add(usernameField);
        panel.add(new JLabel("Password:"));
        panel.add(passwordField);
        panel.add(new JLabel("Age:"));
        panel.add(ageField);
        panel.add(new JLabel("Fasting Glucose:"));
        panel.add(fastingGlucoseField);
        panel.add(new JLabel("Glycohemoglobin:"));
        panel.add(glycohemoglobinField);
        panel.add(new JLabel("Carb Grams:"));
        panel.add(carbGramsField);
        panel.add(new JLabel("Pre-meal Glucose:"));
        panel.add(preMealGlucoseField);
        panel.add(new JLabel("Selected Food:"));
        panel.add(selectedFoodField);
    }

    private void addButtons(JPanel panel) {
        JButton calculateButton = new JButton("Calculate Insulin");
        calculateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                calculateInsulin();
            }
        });

        JButton signupButton = new JButton("Signup");
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                signUp();
            }
        });

        JButton loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                login();
            }
        });

        JButton updateButton = new JButton("Update");
        updateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateUserData();
            }
        });

        JButton selectFoodButton = new JButton("Select Food");
        selectFoodButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFoodSelection();
            }
        });

        panel.add(signupButton);
        panel.add(loginButton);
        panel.add(updateButton);
        panel.add(selectFoodButton);
        panel.add(calculateButton);
    }

    private void signUp() {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());
            int age = Integer.parseInt(ageField.getText());
            double fastingGlucose = Double.parseDouble(fastingGlucoseField.getText());
            double glycohemoglobin = Double.parseDouble(glycohemoglobinField.getText());

            DatabaseHandler.signUp(username, password, age, fastingGlucose, glycohemoglobin);
            resultArea.setText("Signup successful!");
        } catch (NumberFormatException | SQLException ex) {
            resultArea.setText("Error during signup. Please check your input and try again.");
            ex.printStackTrace(); // Handle the exception properly in a real application
        }
    }

    private void login() {
        try {
            String username = usernameField.getText();
            String password = new String(passwordField.getPassword());

            boolean isAuthenticated = DatabaseHandler.authenticateUser(username, password);
            if (isAuthenticated) {
                resultArea.setText("Login successful!");

                // You can load the user data here for further calculations or updates
                DatabaseHandler.UserData userData = DatabaseHandler.retrieveUserData(username);
                if (userData != null) {
                    ageField.setText(String.valueOf(userData.getAge()));
                    fastingGlucoseField.setText(String.valueOf(userData.getFastingGlucose()));
                    glycohemoglobinField.setText(String.valueOf(userData.getGlycohemoglobin()));
                }
            } else {
                resultArea.setText("Invalid username or password.");
            }
        } catch (SQLException ex) {
            resultArea.setText("Error during login. Please try again.");
            ex.printStackTrace(); // Handle the exception properly in a real application
        }
    }

    private void updateUserData() {
        try {
            String username = usernameField.getText();
            int age = Integer.parseInt(ageField.getText());
            double fastingGlucose = Double.parseDouble(fastingGlucoseField.getText());
            double glycohemoglobin = Double.parseDouble(glycohemoglobinField.getText());

            DatabaseHandler.updateUserData(username, age, fastingGlucose, glycohemoglobin);
            resultArea.setText("Update successful!");
        } catch (NumberFormatException | SQLException ex) {
            resultArea.setText("Error during update. Please check your input and try again.");
            ex.printStackTrace(); // Handle the exception properly in a real application
        }
    }

    private void openFoodSelection() {
        AllFood allFood = new AllFood(this);
        allFood.setVisible(true);
    }

    private void calculateInsulin() {
        try {
            int age = Integer.parseInt(ageField.getText());
            double fastingGlucose = Double.parseDouble(fastingGlucoseField.getText());
            double glycohemoglobin = Double.parseDouble(glycohemoglobinField.getText());
            double carbGrams = Double.parseDouble(carbGramsField.getText());
            double preMealGlucose = Double.parseDouble(preMealGlucoseField.getText());

            DiabetesProfile profile = new DiabetesProfile(age, fastingGlucose, glycohemoglobin);
            double insulinDose = insulinCalculator.calculateInsulinForMeal(carbGrams, preMealGlucose);

            resultArea.setText("Insulin Dose: " + insulinDose + " units");
        } catch (NumberFormatException ex) {
            resultArea.setText("Invalid input. Please enter numeric values.");
        }
    }

    public void setSelectedFood(String foodName, String carbs) {
        selectedFoodField.setText(foodName + " - Carbs: " + carbs + " grams");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new DiabetesApp().setVisible(true);
            }
        });
    }
}
