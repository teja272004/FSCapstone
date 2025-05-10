import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/FetchAttendanceServlet")
public class FetchAttendanceServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            // Load MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection to the MySQL database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees", "root", "bhanuteja");

            // Prepare SQL query to fetch all attendance records
            String query = "SELECT user_id, login_time, date, logout_time, attendance_count FROM attendance"; // Fetch all records including logout_time and attendance_count
            stmt = conn.prepareStatement(query);
            rs = stmt.executeQuery();
            
            // Set the response type to HTML
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();

            // Start building the HTML response
            out.println("<!DOCTYPE html>");
            out.println("<html lang='en'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Attendance Records</title>");
            out.println("<link href='https://cdn.jsdelivr.net/npm/tailwindcss@2.2.19/dist/tailwind.min.css' rel='stylesheet'>"); // Updated link
            out.println("</head>");
            out.println("<body class='bg-gray-900 text-white'>");
            out.println("<div class='container mx-auto p-5'>");
            out.println("<h1 class='text-4xl font-bold mb-8'>All Attendance Records</h1>");
            out.println("<table class='min-w-full border-collapse border border-gray-700'>"); // Table border
            out.println("<thead>");
            out.println("<tr class='bg-green-800'>"); // Header background color
            out.println("<th class='border border-gray-600 p-4 text-left'>User ID</th>"); // Add User ID or Name
            out.println("<th class='border border-gray-600 p-4 text-left'>Login Time</th>");
            out.println("<th class='border border-gray-600 p-4 text-left'>Date</th>");
            out.println("<th class='border border-gray-600 p-4 text-left'>Logout Time</th>"); // Added logout_time
            out.println("<th class='border border-gray-600 p-4 text-left'>Attendance Count</th>"); // Added attendance_count
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");

            // Iterate through the result set and display attendance records
            while (rs.next()) {
                int userId = rs.getInt("user_id"); // Fetch user ID from the record
                Timestamp loginTime = rs.getTimestamp("login_time");
                java.sql.Date date = rs.getDate("date");
                Timestamp logoutTime = rs.getTimestamp("logout_time"); // Fetch logout_time
                int attendanceCount = rs.getInt("attendance_count"); // Fetch attendance_count

                out.println("<tr class='bg-gray-800 hover:bg-gray-700'>"); // Row background color and hover effect
                out.println("<td class='border border-gray-600 p-4 text-center'>" + userId + "</td>"); // Display User ID
                out.println("<td class='border border-gray-600 p-4 text-center'>" + loginTime + "</td>");
                out.println("<td class='border border-gray-600 p-4 text-center'>" + date + "</td>");
                out.println("<td class='border border-gray-600 p-4 text-center'>" + (logoutTime != null ? logoutTime : "Not logged out") + "</td>"); // Display logout_time
                out.println("<td class='border border-gray-600 p-4 text-center'>" + attendanceCount + "</td>"); // Display attendance_count
                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("<button onclick=\"window.location.href='dashboard.html'\" class='bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded mt-4'>Back to Dashboard</button>");
            out.println("</div>");
            out.println("</body>");
            out.println("</html>");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error_update.html"); // Redirect to an error page if an exception occurs
        } finally {
            // Close resources
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
