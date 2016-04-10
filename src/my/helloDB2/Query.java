package my.helloDB2;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.sql.*;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet implementation class Query
 */
@WebServlet("/Query")
public class Query extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private static Connection db2Conn;
	private static Statement stmt;
	
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Query() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String query = request.getParameter("query");
		ResultSet rs = null;
		ResultSetMetaData rsmd = null;
		int numCols;
		ArrayList<String> colNames = new ArrayList<String>();
		try {
    		rs = stmt.executeQuery(query);
    		rsmd = rs.getMetaData();
    		numCols = rsmd.getColumnCount();
    		for (int i = 1; i <= numCols; i++) {
    			colNames.add(rsmd.getColumnName(i));
    		}
    		PrintWriter out = response.getWriter();
    		out.println("<table><thead><tr>");
    		for (String name : colNames) {
    			out.println("<td>"+name+"</td>");
    		}
    		out.println("</tr></thead>");
    		out.println("<tbody>");
    		while(rs.next()) {
	    		out.println("<tr>");
	    		for (int i = 1; i <= numCols; i++) {
	    		out.println("<td>"+rs.getString(i)+"</td>");
	    		}
	    		out.println("</tr>");
    		}
    		out.println("</tbody></table>");
		} catch (SQLException e) {
			e.printStackTrace();
		}
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

	public void init(ServletConfig config) throws ServletException {
		String DB_USER = "DTU11";
		String DB_PASSWORD = "FAGP2016";
		try {
			Class.forName("com.ibm.db2.jcc.DB2Driver");
			db2Conn = DriverManager.getConnection
			("jdbc:db2://192.86.32.54:5040/DALLASB:"
			+ "user=" + DB_USER
			+ ";" + "password=" + DB_PASSWORD + ";");
			stmt = db2Conn.createStatement();
			System.out.println("connection and statement created");
		} catch (SQLException | ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public void destroy() {
		try {
			db2Conn.close();
			stmt.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}
