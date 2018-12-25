import java.io.BufferedReader;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

@SuppressWarnings("serial")
@WebServlet(
    name = "DeleteService",
    urlPatterns = {"/delete"}
)
public class DeleteService extends HttpServlet {
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
			
			int lineId = requestObj.getInt("lineId");
			
			//Delete the shifts from the database
			
			String deleteShiftsQuery =
					"DELETE FROM shift " + 
					"WHERE line_id = " + lineId + ";";
			
			dbHelper.connect();
			
			dbHelper.executeUpdateQuery(deleteShiftsQuery);
			
			dbHelper.disconnect();
			
			//Delete the line from the database
			
			String deleteLineQuery =
					"DELETE FROM line " + 
					"WHERE id = " + lineId + ";";
			
			dbHelper.connect();
			
			dbHelper.executeUpdateQuery(deleteLineQuery);
			
			dbHelper.disconnect();
			
			//Return the response
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
		    response.getWriter().write("Web Page Deleted!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}