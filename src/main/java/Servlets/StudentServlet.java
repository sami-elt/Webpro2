package Servlets;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;


@WebServlet(name="studenter", urlPatterns = "/studenter")
public class StudentServlet extends HttpServlet {
    String space =
            "style = 'text-decoration: none; margin-left: auto; margin-right: auto; padding: 10px; color: #e6e6e6; font-size: 2rem;'";
    String top =
            "<head><title> stundent </title></head>"
            + "<body style = 'background-color: #cccec9; height:100%; width: 100%; margin: 0px;'>"
            + "<div style = 'text-align: center; background-color: #333333; height: 60px; top: 0px;'>"
            + "<a href=\"http://localhost:9090\" " + space + "> Hem </a>"
            + "<a href=\"kurser\" " + space + "> Kurser </a>"
            + "<a href=\"narvaro\" "  + space + "> Närvaro </a>"
            + "<a href= \"updatestudenter\" "  + space + "> Uppdatera Student </a>"
            + "</div>"
            + "<h1 style = 'text-align: center; font-style: italic; font-size: 3rem'>Studenter</h1>"
            + "<table style = 'margin-left: auto; margin-right: auto; border: 1px solid black; background-color: #57864b;'>"
            + "<tr><th>id</th><th>Namn</th><th>Efternamn</th><th>Ort</th><th>Intressen</th></tr>";

    String bot = "</table>"
            + "<h2 style = 'text-align: center; font-style: italic; font-size: 1.3rem'> Sök efter student</h2>"
            + "</body>"
            + "</html>";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        showData(req, resp);
        showForm(req, resp);
    }
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws IOException, ServletException {
        showStudent(req, resp);
        showForm(req, resp);
    }

    //find a specific person in the studenter table
    private void showStudent(HttpServletRequest req, HttpServletResponse resp) throws IOException{
        resp.setContentType("text/HTML");
        PrintWriter out = resp.getWriter();
        //Change tablehead to fit the new header 
        out.println(
                "<head><title> stundent </title></head>"
                + "<body style = 'background-color: #cccec9; height:100%; width: 100%; margin: 0px;'>"
                + "<div style = 'text-align: center; background-color: #333333; height: 60px; top: 0px;'>"
                + "<a href=\"http://localhost:9090\" " + space + "> Hem </a>"
                + "<a href=\"kurser\" " + space + "> Kurser </a>"
                + "<a href=\"narvaro\" "  + space + "> Närvaro </a>"
                + "<a href= \"updatestudenter\" "  + space + "> Uppdatera Student </a>"
                + "</div>"
                + "<h1 style = 'text-align: center; font-style: italic; font-size: 3rem'>Studenter</h1>"
                + "<table style = 'margin-left: auto; margin-right: auto; border: 1px solid black; background-color: #57864b;'>"
                + "<tr><th>id</th><th>Namn</th><th>Efternamn</th><th>YHP</th><th>Kurs</th></tr>");

        //boolean so i can toggle message if anything goes wrong and print out what it is
        boolean errorMsg = false;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //PORT and DbName should be changed

            //user with only select option
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gritacademy",
                    "sams", "");
            Statement stmt = con.createStatement();
            //TABLENAME should be changed
            ResultSet rs = stmt.executeQuery(
                    "SELECT s.id, s.Fname, s.Lname, k.YHP, k.namn, k.beskrivning FROM studenter s INNER JOIN närvaro n ON s.id = n.student_id INNER JOIN kurser k ON k.id = n.kurs_id WHERE Fname ='"
                            + req.getParameter("fname")+ "'AND Lname = '" + req.getParameter("lname") + "';");

            while (rs.next()) {
                //goes to true so the message will hide
                errorMsg = true;

                out.println("<tr>");
                out.println("<td>" + rs.getInt(1) + "</td>");
                out.println("<td>" + rs.getString(2) + "</td>");
                out.println("<td>" + rs.getString(3) + "</td>");
                out.println("<td>" + rs.getString(4) + "</td>");
                out.println("<td>" + rs.getString(5) + "</td>");
                out.println("</tr>");

            }

            out.println(bot);
            //sends out message if you cant find the person in table = there is no resultset.next
            if (!errorMsg){
                out.println("<h3 style ='text-align: center; font-style: italic; font-size: 1rem'> Kunde inte hitta student</h3>");
            }

            con.close();

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    //show table of studenter
    private void showData (HttpServletRequest req, HttpServletResponse resp) throws IOException {

        resp.setContentType("text/HTML");
        PrintWriter out = resp.getWriter();
        out.println(top);

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            //PORT and DbName should be changed

            //user with only select option
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/gritacademy",
                    "sams", "");
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

    //input form so you can search for a specific person in studenter table
    private void showForm(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter out = resp.getWriter();
        String fname = req.getParameter("fname")==null?"":req.getParameter("fname");
        String lname = req.getParameter("lname")==null?"":req.getParameter("lname");

        out.println(
                "<br>"
                        + "<div style='border:black solid; width:200px; padding:5px display:block; margin-left:auto; margin-right:auto; margin-top:5px; margin-bottom:5px;'>"
                        + "<form style='margin:5px;' action=/studenter method=POST>"
                        + "            <label for=fname>First Name:</label>"
                        + "            <input type=text id=fname name=fname required value=" +fname+"><br><br>"
                        + "             <label for=fname>Last Name:</label>"
                        + "            <input type=text id=lname name=lname required value=" +lname+"><br><br>"
                        + "            <input type=submit value=Submit>"
                        + "            <button type=button id=reset onclick=location.href='/studenter'> reset </button>"
                        + "        </form>"
                        + "</div>"
                        + "<br>"
        );
    }
}
