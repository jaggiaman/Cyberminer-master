import java.io.BufferedReader;
import java.io.IOException;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import KWIC.KWIC;
import KWIC.Line;

@SuppressWarnings("serial")
@WebServlet(
    name = "AddService",
    urlPatterns = {"/add"}
)
public class AddService extends HttpServlet {
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		try {
			//Create instance of DatabaseHelper
			
			DatabaseHelper dbHelper = new DatabaseHelper();
			
			//Parse the input
			
			JSONObject requestObj = null;
			StringBuffer stringBuffer = new StringBuffer();
			
			BufferedReader reader = request.getReader();
			String bufferline = null;
			
		    while ((bufferline = reader.readLine()) != null)
		    	stringBuffer.append(bufferline);

		    requestObj = new JSONObject(stringBuffer.toString());
			
			String descriptor = requestObj.getString("descriptor");
			String url = requestObj.getString("url");
			int payment = requestObj.getInt("payment");
			
			//Generate the Circular Shifts
			
			Line line = new Line(descriptor, url);
			KWIC kwic = new KWIC(line);
			kwic.setup();
			
			ArrayList<Line> sortedLines = kwic.getAlphabetizer().getSortedLines();
			
			if(sortedLines.get(0).getDescriptor().equals("")) {
			    response.sendError(HttpServletResponse.SC_NOT_FOUND, "Invalid Descriptor!");
			    return;
			}
			
			//Insert the line to the database
			
			String insertLineQuery =
					"INSERT INTO line " + 
					"(url, descriptor, payment) " + 
					"VALUES ('" + url + "', '" + descriptor + "', " + payment + ");";
			
			dbHelper.connect();
            ResultSet insertLineResSet = dbHelper.executeInsertQueryAndReturnKey(insertLineQuery);
			
            insertLineResSet.next();
			
			int lineId = insertLineResSet.getInt(1);
			
			dbHelper.disconnect();
			
			//Insert the Circular Shifts to the database
			
			for(int i = 0; i < sortedLines.size(); i++) {
				String insertShiftQuery = 
						"INSERT INTO shift " +
						"VALUES (" + lineId + ", '" + sortedLines.get(i).getDescriptor() + "');";
				
				dbHelper.connect();
				
				dbHelper.executeUpdateQuery(insertShiftQuery);
				
				dbHelper.disconnect();
			}
			
			//Return the response
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
		    response.getWriter().write("Web Page Added!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}