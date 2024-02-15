import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList; // Add this line to import ArrayList

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/DiabetesAppDatabase";
    private static final String USER = "root";
    private static final String PASSWORD = "147258369@Mm";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASSWORD);
    }

    public static void signUp(String username, String password, int age, double fastingGlucose, double glycohemoglobin)
            throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Users (username, password, age, fasting_glucose, glycohemoglobin) VALUES (?, ?, ?, ?, ?)")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            preparedStatement.setInt(3, age);
            preparedStatement.setDouble(4, fastingGlucose);
            preparedStatement.setDouble(5, glycohemoglobin);
            preparedStatement.executeUpdate();
        }
    }

    public static boolean authenticateUser(String username, String password) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT * FROM Users WHERE username = ? AND password = ?")) {
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    // ... (existing code)

    public static void updateUserData(String username, int age, double fastingGlucose, double glycohemoglobin)
            throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE Users SET age = ?, fasting_glucose = ?, glycohemoglobin = ? WHERE username = ?")) {
            preparedStatement.setInt(1, age);
            preparedStatement.setDouble(2, fastingGlucose);
            preparedStatement.setDouble(3, glycohemoglobin);
            preparedStatement.setString(4, username);
            preparedStatement.executeUpdate();
        }
    }

    public static UserData retrieveUserData(String username) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT age, fasting_glucose, glycohemoglobin FROM Users WHERE username = ?")) {
            preparedStatement.setString(1, username);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                int age = resultSet.getInt("age");
                double fastingGlucose = resultSet.getDouble("fasting_glucose");
                double glycohemoglobin = resultSet.getDouble("glycohemoglobin");

                return new UserData(age, fastingGlucose, glycohemoglobin);
            }
        }

        return null; // Return null if user not found
    }

    public static class UserData {
        private final int age;
        private final double fastingGlucose;
        private final double glycohemoglobin;

        public UserData(int age, double fastingGlucose, double glycohemoglobin) {
            this.age = age;
            this.fastingGlucose = fastingGlucose;
            this.glycohemoglobin = glycohemoglobin;
        }

        public int getAge() {
            return age;
        }

        public double getFastingGlucose() {
            return fastingGlucose;
        }

        public double getGlycohemoglobin() {
            return glycohemoglobin;
        }
    }

    public static void addFood(String foodName, double carbGrams) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "INSERT INTO Food (food_name, carb_grams) VALUES (?, ?)")) {
            preparedStatement.setString(1, foodName);
            preparedStatement.setDouble(2, carbGrams);
            preparedStatement.executeUpdate();
        }
    }

    public static FoodData retrieveFoodData(String foodName) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT carb_grams FROM Food WHERE food_name = ?")) {
            preparedStatement.setString(1, foodName);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {
                double carbGrams = resultSet.getDouble("carb_grams");
                return new FoodData(foodName, carbGrams);
            }
        }

        return null; // Return null if food not found
    }

    public static class FoodData {
        private final String foodName;
        private final double carbGrams;

        public FoodData(String foodName, double carbGrams) {
            this.foodName = foodName;
            this.carbGrams = carbGrams;
        }

        public String getFoodName() {
            return foodName;
        }

        public double getCarbGrams() {
            return carbGrams;
        }
    }
    public static void updateFoodData(String foodName, double newCarbGrams) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE Food SET carb_grams = ? WHERE food_name = ?")) {
            preparedStatement.setDouble(1, newCarbGrams);
            preparedStatement.setString(2, foodName);
            preparedStatement.executeUpdate();
        }
    }

    public static void deleteFood(String foodName) throws SQLException {
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "DELETE FROM Food WHERE food_name = ?")) {
            preparedStatement.setString(1, foodName);
            preparedStatement.executeUpdate();
        }
    }

    public static List<FoodData> retrieveAllFoodData() throws SQLException {
        List<FoodData> foodList = new ArrayList<>();
        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "SELECT food_name, carb_grams FROM Food")) {
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                String foodName = resultSet.getString("food_name");
                double carbGrams = resultSet.getDouble("carb_grams");
                foodList.add(new FoodData(foodName, carbGrams));
            }
        }

        System.out.println("Retrieved Food Data: " + foodList); // Print retrieved data for debugging

        return foodList;
    }
}
