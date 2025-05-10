import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Get username and password from form input
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        // Debugging output
        System.out.println("Attempting login for Username: " + username + ", Password: " + password);

        Connection conn = null;
        PreparedStatement stmt = null;
        PreparedStatement attendanceStmt = null;
        ResultSet rs = null;

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection to the MySQL database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees?useSSL=false", "root", "bhanuteja");

            // Check for admin credentials first
            if ("admin".equals(username) && "password123".equals(password)) {
                System.out.println("Login successful for admin: " + username);
                HttpSession session = request.getSession();
                session.setAttribute("username", username);
                response.sendRedirect("manage.html"); // Redirect to manage.html
            } else {
                // Prepare SQL query to check username and password for other users
                String query = "SELECT id FROM users WHERE username = ? AND password = ?";
                stmt = conn.prepareStatement(query);
                stmt.setString(1, username);
                stmt.setString(2, password);
                rs = stmt.executeQuery();

                // Check if user exists
                if (rs.next()) {
                    // If login is successful, create a session and insert/update attendance record
                    int userId = rs.getInt("id"); // Get user ID from the result set
                    System.out.println("Login successful for user: " + username);
                    HttpSession session = request.getSession();
                    session.setAttribute("username", username);
                    
                    // Prepare to check today's attendance
                    String date = new SimpleDateFormat("yyyy-MM-dd").format(new java.util.Date());
                    String attendanceQuery = "INSERT INTO attendance (user_id, login_time, date) " +
                                             "VALUES (?, ?, ?) " +
                                             "ON DUPLICATE KEY UPDATE login_time = VALUES(login_time), attendance_count = attendance_count + 1";

                    attendanceStmt = conn.prepareStatement(attendanceQuery);
                    attendanceStmt.setInt(1, userId); // Use the fetched user ID
                    attendanceStmt.setTimestamp(2, new Timestamp(System.currentTimeMillis())); // Current login time
                    attendanceStmt.setString(3, date);
                    attendanceStmt.executeUpdate(); // Execute attendance insert/update
                    
                    response.sendRedirect("dashboard.jsp"); // Redirect to dashboard.jsp
                } else {
                    // If login fails, redirect back to login page with error
                    System.out.println("Login failed for user: " + username);
                    response.sendRedirect("error_update.html");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error_update.html");
        } finally {
            // Close resources in the finally block to avoid memory leaks
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (attendanceStmt != null) attendanceStmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
