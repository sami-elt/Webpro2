package Servlets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "kurser", urlPatterns = "/kurser")
public class KursServlet extends HttpServlet {

    String space =
            "style = 'text-decoration: none; margin-left: auto; margin-right: auto; padding: 10px; color: #e6e6e6; font-size: 2rem;'";
    String top = "<head><title> Kurser </title></head>"
            + "<body style = 'background-color: #cccec9; height:100%; width: 100%; margin: 0px;'>"
            + "<div style = 'text-align: center; background-color: #333333; height: 60px; top: 0px;'>"
            + "<a href= \"http://localhost:9090\" " + space + "> Hem </a>"
            + "<a href= \"studenter\" " + space + "> Studenter </a>"
            + "<a href= \"narvaro\" " + space + "> Närvaro </a>"
            + "<a href= \"updatestudenter \" " + space + "> Uppdatera Student </a>"
            + "</div>"
            +"<h1 style = 'text-align: center; font-style: italic; font-size: 3rem'>Kurser</h1>"
            + "<table style = 'margin-left: auto; margin-right: auto; border: 1px solid black; background-color: #57864b;'>"
            + "<tr><th>id</th><th>YHP</th><th>Namn</th><th>Beskrivning</th></tr>";
    String bot = "</table>"
            + "<h2 style = 'text-align: center; font-style: italic; font-size: 1.3rem'> Lägg till kurs</h2>"
            + "</body>"
            + "</html>";
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showData(req, resp);
        showCourseForm(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        showData(req, resp);
        updateCourse(req, resp);
        showCourseForm(req, resp);
    }
    //show inputform so you can add new kurser
    private void showCourseForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        out.println(
                "<br>"
                        + "<div style='border:black solid; width:200px; padding:5px display:block; margin-left:auto; margin-right:auto; margin-top:5px; margin-bottom:5px;'>"
                        + "<form style='margin:5px;' action=/kurser method=POST>"
                        + "            <label for=YHP>YHP:</label>"
                        + "            <input type=number id=YHP name=YHP required><br><br>"
                        + "             <label for=namn>Namn</label>"
                        + "            <input type=text id=namn name=namn required><br><br>"
                        + "             <label for=beskrivning>Beskrivning:</label>"
                        + "            <input type=text id=beskrivning name=beskrivning required><br><br>"
                        + "            <input type=submit value=Submit>"
                        + "        </form>"
                        + "</div>"
                        + "<br>"
        );
    }
    //show table from kurser
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

    //method to add new courses to kurser
    private void updateCourse (HttpServletRequest req, HttpServletResponse resp) throws IOException{
        PrintWriter out = resp.getWriter();

        boolean errorMsg = false;

        String yhp = req.getParameter("YHP");
        String namn = req.getParameter("namn");
        String beskrivning = req.getParameter("beskrivning");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //PORT and DbName should be changed

            //user with select and insert option
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gritacademy",
                    "sami", "");
            Statement stmt = con.createStatement();
            //TABLENAME should be changed
            PreparedStatement ps = con.prepareStatement("INSERT INTO kurser (YHP, namn, beskrivning) VALUES (?, ?, ?)");

            ps.setString(1, yhp);
            ps.setString(2, namn);
            ps.setString(3, beskrivning);
            ps.executeUpdate();

            con.close();
        } catch (Exception e) {
            errorMsg = true;
            System.out.println(e);
        }
        if (errorMsg){
            out.println("<h3 style ='text-align: center; font-style: italic; font-size: 1rem' >Kursen finns redan!</h3>");
        } else
        { resp.sendRedirect("/kurser");}
    }
}
