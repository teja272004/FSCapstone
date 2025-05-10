import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/UpdateEmployeeServlet")
public class UpdateEmployeeServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String job = request.getParameter("job");
        String salaryStr = request.getParameter("salary");
        
        Double salary = null;
        if (salaryStr != null && !salaryStr.isEmpty()) {
            salary = Double.parseDouble(salaryStr);
        }

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/employees", "root", "bhanuteja");

            StringBuilder sql = new StringBuilder("UPDATE users SET ");
            boolean isFirst = true;

            if (username != null && !username.isEmpty()) {
                sql.append("username = ?");
                isFirst = false;
            }
            if (password != null && !password.isEmpty()) {
                if (!isFirst) sql.append(", ");
                sql.append("password = ?");
                isFirst = false;
            }
            if (name != null && !name.isEmpty()) {
                if (!isFirst) sql.append(", ");
                sql.append("name = ?");
                isFirst = false;
            }
            if (email != null && !email.isEmpty()) {
                if (!isFirst) sql.append(", ");
                sql.append("email = ?");
                isFirst = false;
            }
            if (phone != null && !phone.isEmpty()) {
                if (!isFirst) sql.append(", ");
                sql.append("phone = ?");
                isFirst = false;
            }
            if (job != null && !job.isEmpty()) {
                if (!isFirst) sql.append(", ");
                sql.append("job = ?");
                isFirst = false;
            }
            if (salary != null) {
                if (!isFirst) sql.append(", ");
                sql.append("salary = ?");
            }

            sql.append(" WHERE id = ?");

            pstmt = conn.prepareStatement(sql.toString());

            int paramIndex = 1;

            if (username != null && !username.isEmpty()) pstmt.setString(paramIndex++, username);
            if (password != null && !password.isEmpty()) pstmt.setString(paramIndex++, password);
            if (name != null && !name.isEmpty()) pstmt.setString(paramIndex++, name);
            if (email != null && !email.isEmpty()) pstmt.setString(paramIndex++, email);
            if (phone != null && !phone.isEmpty()) pstmt.setString(paramIndex++, phone);
            if (job != null && !job.isEmpty()) pstmt.setString(paramIndex++, job);
            if (salary != null) pstmt.setDouble(paramIndex++, salary);

            pstmt.setInt(paramIndex, id);

            int rowsUpdated = pstmt.executeUpdate();

            if (rowsUpdated > 0) {
                response.sendRedirect("view_employee.html");
            } else {
                response.sendRedirect("error_update.html");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("error_update.html");
        } finally {
            try {
                if (pstmt != null) pstmt.close();
                if (conn != null) conn.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
