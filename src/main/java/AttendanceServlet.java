import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/AttendanceServlet")
public class AttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get session to retrieve user ID
        HttpSession session = request.getSession();
        Integer userId = (Integer) session.getAttribute("userId"); // Assuming userId is stored in session

        if (userId == null) {
            response.sendRedirect("loginpage.jsp"); // Redirect to login if userId is not found
            return;
        }

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection to the MySQL database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees", "root", "bhanuteja");
            
            // Check if an attendance record already exists for today
            String checkQuery = "SELECT id FROM attendance WHERE user_id = ? AND date = CURDATE()";
            PreparedStatement checkStmt = conn.prepareStatement(checkQuery);
            checkStmt.setInt(1, userId);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // If attendance exists, update the login time
                int attendanceId = rs.getInt("id");
                String updateQuery = "UPDATE attendance SET login_time = ? WHERE id = ?";
                PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                updateStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis())); // Update with current time
                updateStmt.setInt(2, attendanceId);
                updateStmt.executeUpdate(); // Execute update
            } else {
                // If no attendance exists, insert a new record
                String insertQuery = "INSERT INTO attendance (user_id, login_time, date) VALUES (?, ?, ?)";
                PreparedStatement insertStmt = conn.prepareStatement(insertQuery);
                insertStmt.setInt(1, userId); // Set user ID
                insertStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Set login time
                insertStmt.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Set today's date
                insertStmt.executeUpdate(); // Execute insert
            }

            // Close the connection
            conn.close();
            // Redirect to a dashboard or success page
            response.sendRedirect("dashboard.html");
        } catch (Exception e) {
            e.printStackTrace();
            // Redirect to an error page if an exception occurs
            response.sendRedirect("error_update.html");
        }
    }
}
