package Servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


@WebServlet(name="Updatestudenter", urlPatterns = "/updatestudenter")
public class UpdateStudentServlet extends HttpServlet {
    String space =
            "style = 'text-decoration: none; margin-left: auto; margin-right: auto; padding: 10px; color: #e6e6e6; font-size: 2rem;'";
    String top =
            "<head><title> Uppdatera Studenter</title></head>"
            + "<body style = 'background-color: #cccec9; height:100%; width: 100%; margin: 0px;'>"
            + "<div style = 'text-align: center; background-color: #333333; height: 60px; top: 0px;'>"
            + "<a href=\"http://localhost:9090\" " + space + "> Hem </a>"
            + "<a href= \"studenter\" "  + space + "> Studenter </a>"
            + "<a href=\"kurser\" " + space + "> Kurser </a>"
            + "<a href=\"narvaro\" "  + space + "> Närvaro </a>"
            + "</div>"
            + "<h1 style = 'text-align: center; font-style: italic; font-size: 3rem'>Studenter</h1>"
            + "<table style = 'margin-left: auto; margin-right: auto; border: 1px solid black; background-color: #57864b;'>"
            + "<tr><th>id</th><th>Namn</th><th>Efternamn</th><th>Ort</th><th>intressen</th></tr>";

    String bot =
            "</table>"
            + "<h2 style = 'text-align: center; font-style: italic; font-size: 1.3rem'> Lägg till student</h2>"
            + "</body>"
            + "</html>";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showData(req, resp);
    showForm(req, resp);

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    showData(req, resp);
    showForm(req, resp);
    updateStudent(req, resp);
    }

    //inputform so you can insert information into studenter with fname and lname required
    private void showForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println(
                "<br>"
                        + "<div style='border:black solid; width:200px; padding:5px display:block; margin-left:auto; margin-right:auto; margin-top:5px; margin-bottom:5px;'>"
                        + "<form style='margin:5px;' action=/updatestudenter method=POST>"
                        + "            <label for=fname>First Name:</label>"
                        + "            <input type=text id=fname name=fname required><br><br>"
                        + "             <label for=fname>Last name:</label>"
                        + "            <input type=text id=lname name=lname required><br><br>"
                        + "             <label for=ort>Ort:</label>"
                        + "            <input type=text id=ort name=ort><br><br>"
                        + "             <label for=intressen>Intressen:</label>"
                        + "            <input type=text id=intressen name=intressen><br><br>"
                        + "            <input type=submit value=Submit>"
                        + "        </form>"
                        + "</div>"
                        + "<br>"
        );
    }

    //table to show all the data from studenter
    private void showData (HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/HTML");
        PrintWriter out = resp.getWriter();
        out.println(top);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //PORT and DbName should be changed

            //user with select and insert option
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gritacademy",
                    "sami", "");
            Statement stmt = con.createStatement();
            //TABLENAME should be changed
            ResultSet rs = stmt.executeQuery("select * from studenter;");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt(1) + "</td>");
                out.println("<td>" + rs.getString(2) + "</td>");
                out.println("<td>" + rs.getString(3) + "</td>");
                out.println("<td>" + rs.getString(4) + "</td>");
                out.println("<td>" + rs.getString(5) + "</td>");
                out.println("</tr>");

            }

            out.println(bot);
            con.close();


        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //method to insert new studenter into stundenter table
    private void updateStudent (HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter out = resp.getWriter();
        String fName = req.getParameter("fname");
        String lName = req.getParameter("lname");
        String ort = req.getParameter("ort");
        String intressen = req.getParameter("intressen");

        boolean errorMsg = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //PORT and DbName should be changed

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gritacademy",
                    "sami", "");
            Statement stmt = con.createStatement();
            //TABLENAME should be changed
            PreparedStatement ps = con.prepareStatement("INSERT INTO studenter (fname, lname, ort, intressen) VALUES (?, ?, ?, ?)");

            ps.setString(1, fName);
            ps.setString(2, lName);
            ps.setString(3, ort);
            ps.setString(4, intressen);
            ps.executeUpdate();

            con.close();
        } catch (Exception e) {
            errorMsg = true;
            System.out.println(e);
        }
        if (errorMsg){
            out.println("<h3 style ='text-align: center; font-style: italic; font-size: 1rem' > Kunde inte lägga till student!</h3>");
        } else
        { resp.sendRedirect("/updatestudenter");}
    }
}
