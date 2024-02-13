package Servlets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name="/narvaro" , urlPatterns = "/narvaro")
public class AttendenceServlet extends HttpServlet {
    String space =
            "style = 'text-decoration: none; margin-left: auto; margin-right: auto; padding: 10px; color: #e6e6e6; font-size: 2rem;'";
    String top = "<head><title> Närvaro </title></head>"
            + "<body style = 'background-color: #cccec9; height:100%; width: 100%; margin: 0px;'>"
            + "<div style = 'text-align: center; background-color: #333333; height: 60px; top: 0px;'>"
            + "<a href=\"http://localhost:9090\" " + space + "> Hem </a>"
            + "<a href= \"studenter\" " + space + "> Studenter </a>"
            + "<a href= \"kurser\" " + space + "> Kurser </a>"
            + "<a href= \"updatestudenter\" " + space + "> Uppdatera Student </a>"
            + "</div>"
            + "<h1 style = 'text-align: center; font-style: italic; font-size: 3rem'>Närvaro</h1>"
            + "<table style = 'margin-left: auto; margin-right: auto; border: 1px solid black; background-color: #57864b;'>"
            + "<tr><th>id</th><th>Namn</th><th>Efternamn</th><th>YHP</th><th>Namn</th><th>Beskrivning</th></tr>";
    String bot = "</table>"
            + "</body>"
            + "</html>";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showData(req, resp);
        showDataStudent(req, resp);
        showDataCourses(req, resp);
        showForm(req, resp);

    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        showData(req, resp);
        showDataStudent(req, resp);
        showDataCourses(req, resp);
        updateAll(req, resp);
        showForm(req, resp);
    }
    //shows the joined table with kurser and studenter
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
            ResultSet rs = stmt.executeQuery("select s.id, s.Fname, s.Lname, k.YHP, k.namn, k.beskrivning FROM studenter s  INNER JOIN närvaro n ON s.id = n.student_id INNER JOIN kurser k ON k.id = n.kurs_id ORDER BY Fname;");
            while (rs.next()) {

                out.println("<tr>");
                out.println("<td>" + rs.getString(1) + "</td>");
                out.println("<td>" + rs.getString(2) + "</td>");
                out.println("<td>" + rs.getString(3) + "</td>");
                out.println("<td>" + rs.getString(4) + "</td>");
                out.println("<td>" + rs.getString(5) + "</td>");
                out.println("<td>" + rs.getString(6) + "</td>");
                out.println("</tr>");
            }
            con.close();

            out.println(bot);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    //show the studenter table
    private void showDataStudent (HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/HTML");
        PrintWriter out = resp.getWriter();
        out.println("<h1 style = 'text-align: center; font-style: italic; font-size: 1.5rem'>Studenter</h1>");
        out.println("<table style = 'margin-left: auto; margin-right: auto; border: 1px solid black; background-color: #57864b;'>" +
                "<tr><th>id</th><th>Namn</th><th>Efternamn</th><th>Ort</th><th>Intressen</th></tr>");

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
            con.close();
            out.println(bot);

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    //show the kurser table
    private void showDataCourses (HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/HTML");
        PrintWriter out = resp.getWriter();
        out.println("<h1 style = 'text-align: center; font-style: italic; font-size: 1.5rem'>kurser</h1>");
        out.println("<table style = 'margin-left: auto; margin-right: auto; border: 1px solid black; background-color: #57864b;'><tr><th>id</th><th>YHP</th><th>Namn</th><th>Beskrivning</th></tr>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //PORT and DbName should be changed

            //user with select and insert option
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gritacademy",
                    "sami", "");
            Statement stmt = con.createStatement();
            //TABLENAME should be changed
            ResultSet rs = stmt.executeQuery("select * from kurser;");

            while (rs.next()) {
                out.println("<tr>");
                out.println("<td>" + rs.getInt(1) + "</td>");
                out.println("<td>" + rs.getString(2) + "</td>");
                out.println("<td>" + rs.getString(3) + "</td>");
                out.println("<td>" + rs.getString(4) + "</td>");
                out.println("</tr>");

            }

            out.println(bot);
            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }
    //Show inputform so you can choose id for students and kurser
    private void showForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println(
                "<br>"
                        + "<div style='border:black solid; width:200px; padding:5px display:block; margin-left:auto; margin-right:auto; margin-top:5px; margin-bottom:5px;'>"
                        + "<form style='margin:5px;' action=/narvaro method=POST>"
                        + "            <label for=sID>Student ID:</label>"
                        + "            <input type=number id=sID name=sID><br><br>"
                        + "             <label for=cID>Kurs ID:</label>"
                        + "            <input type=number id=cID name=cID><br><br>"
                        + "            <input type=submit value=Submit>"
                        + "        </form>"
                        + "</div>"
                        + "<br>"
        );
    }

    //Method for inserting values into närvaro from student and kurser
    private void updateAll (HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter out = resp.getWriter();

        //getting parameters from string student and kurser
        String sID = req.getParameter("sID");
        String cID = req.getParameter("cID");

        boolean errorMsg = false;


        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //PORT and DbName should be changed

            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gritacademy",
                    "sami", "");
            Statement stmt = con.createStatement();
            //TABLENAME should be changed
            PreparedStatement ps = con.prepareStatement("INSERT INTO närvaro (student_id, kurs_id) VALUES (?, ?)");

            ps.setString(1, sID);
            ps.setString(2, cID);

            ps.executeUpdate();

            con.close();
        } catch (Exception e) {
            errorMsg = true;
            System.out.println(e);
        }
        if (errorMsg){
            out.println("<h3 style ='text-align: center; font-style: italic; font-size: 1rem' >Student finns redan i kursen! Prova någon annan</h3>");
        } else
        { resp.sendRedirect("/narvaro");}
    }
}
