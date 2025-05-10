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

@WebServlet("/LogoutServlet")
public class LogoutServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username"); // Get username from request parameter

        if (username != null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees", "root", "bhanuteja");
                
                // Query to get user_id associated with the username
                String userIdQuery = "SELECT id FROM users WHERE username = ?";
                PreparedStatement userIdStmt = conn.prepareStatement(userIdQuery);
                userIdStmt.setString(1, username);
                ResultSet rs = userIdStmt.executeQuery();

                int userId = -1; // Initialize userId
                if (rs.next()) {
                    userId = rs.getInt("id"); // Get the user ID
                }

                if (userId != -1) {
                    // Update the logout time in attendance record
                    String updateQuery = "UPDATE attendance SET logout_time = ? WHERE user_id = ? AND date = ?";
                    PreparedStatement updateStmt = conn.prepareStatement(updateQuery);
                    updateStmt.setTimestamp(1, new Timestamp(System.currentTimeMillis())); // Current logout time
                    updateStmt.setInt(2, userId);
                    updateStmt.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Today's date
                    updateStmt.executeUpdate();
                }

                response.sendRedirect("loginpage.jsp"); // Redirect to login page
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("error.html");
            }
        } else {
            response.sendRedirect("error.html");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
