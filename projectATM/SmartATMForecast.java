import java.sql.*;
import java.util.Scanner;

public class SmartATMForecast {
    public static void main(String[] args) {
        // ✅ Declare DB credentials inside main
        String url = "jdbc:mysql://localhost:3306/atm_system?useSSL=false&serverTimezone=UTC";
        String user = "root";
        String password = "Mk31122004";  // Replace with your MySQL password

        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter current ATM balance: ₹");
        double currentBalance = scanner.nextDouble();

        try (Connection conn = DriverManager.getConnection(url, user, password)) {
            String query = "SELECT SUM(amount_withdrawn) AS total, COUNT(*) AS days " +
                           "FROM atm_transactions WHERE transaction_date >= CURDATE() - INTERVAL 7 DAY";

            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                double totalWithdrawn = rs.getDouble("total");
                int days = rs.getInt("days");

                if (days == 0) {
                    System.out.println("No data available for the last 7 days.");
                    return;
                }

                double averageDailyWithdraw = totalWithdrawn / days;

                System.out.println("\n📊 Forecast Report:");
                System.out.println("Average Daily Withdrawal: ₹" + averageDailyWithdraw);
                System.out.println("Current ATM Balance: ₹" + currentBalance);

                if (averageDailyWithdraw > currentBalance) {
                    System.out.println("\n🔴 Replenishment Required.");
                } else {
                    System.out.println("\n🟢 No Immediate Replenishment Needed.");
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        scanner.close();
    }
}
