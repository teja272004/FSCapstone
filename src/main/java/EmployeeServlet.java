import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import Manage.Employee;

@WebServlet("/EmployeeServlet")
public class EmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/plain"); // Setting content type to plain text
        PrintWriter out = response.getWriter();

        ArrayList<Employee> employeeList = new ArrayList<>();

        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Establish connection to the MySQL database
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees", "root", "bhanuteja");

            // Prepare SQL query to fetch all employees
            String query = "SELECT * FROM users"; // Change "users" to your actual employee table name
            PreparedStatement stmt = conn.prepareStatement(query);
            ResultSet rs = stmt.executeQuery();

            // Fetching data from ResultSet and adding it to the list
            while (rs.next()) {
                Employee emp = new Employee();
                emp.setId(rs.getInt("id")); // Adjust the column name as necessary
                emp.setName(rs.getString("name")); // Adjust the column name as necessary
                emp.setEmail(rs.getString("email")); // Adjust the column name as necessary
                emp.setPhone(rs.getString("phone")); // Adjust the column name as necessary
                emp.setJob(rs.getString("job")); // Adjust the column name as necessary
                emp.setSalary(rs.getDouble("salary")); // Adjust the column name as necessary
                emp.setUsername(rs.getString("username")); // Adjust the column name as necessary
                emp.setPassword(rs.getString("password")); // Adjust the column name as necessary
                
                employeeList.add(emp);
            }

            // Closing the database connection
            conn.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("Error fetching employee data");
            return;
        }

        // Outputting employee details as plain text
        for (Employee emp : employeeList) {
            out.println("ID: " + emp.getId() + 
                        ", Name: " + emp.getName() + 
                        ", Email: " + emp.getEmail() + 
                        ", Phone: " + emp.getPhone() + 
                        ", Job: " + emp.getJob() + 
                        ", Salary: " + emp.getSalary() + 
                        ", Username: " + emp.getUsername() + 
                        ", Password: " + emp.getPassword());
        }
    }
}
