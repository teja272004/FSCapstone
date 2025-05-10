import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/AddEmployeeServlet")
public class AddEmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Get parameters from request
            String idParam = request.getParameter("id");
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String name = request.getParameter("name");
            String email = request.getParameter("email");
            String phone = request.getParameter("phone");
            String job = request.getParameter("job");
            String salaryParam = request.getParameter("salary");

            // Check for null or empty values
            if (idParam == null || idParam.isEmpty() || username == null || username.isEmpty() ||
                password == null || password.isEmpty() || name == null || name.isEmpty() ||
                email == null || email.isEmpty() || phone == null || phone.isEmpty() ||
                job == null || job.isEmpty() || salaryParam == null || salaryParam.isEmpty()) {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, "All fields are required.");
                return;
            }

            // Parse ID and Salary
            int id = Integer.parseInt(idParam);
            double salary = Double.parseDouble(salaryParam);

            // Database connection
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees", "root", "bhanuteja");

            // Prepare SQL query
            String query = "INSERT INTO users (id, username, password, name, email, phone, job, salary) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement stmt = conn.prepareStatement(query);
            stmt.setInt(1, id);
            stmt.setString(2, username);
            stmt.setString(3, password);
            stmt.setString(4, name);
            stmt.setString(5, email);
            stmt.setString(6, phone);
            stmt.setString(7, job);
            stmt.setDouble(8, salary);

            // Execute the update
            stmt.executeUpdate();
            conn.close();

            // Redirect to success page
            response.sendRedirect("view_employee.html");
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID and Salary must be valid numbers.");
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "An error occurred while processing your request.");
        }
    }
}
