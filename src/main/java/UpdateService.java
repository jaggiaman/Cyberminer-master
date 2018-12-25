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
    name = "UpdateService",
    urlPatterns = {"/update"}
)
public class UpdateService extends HttpServlet {
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
			
			//Update the line access frequency
			
			String updateLineQuery =
					"UPDATE line " +
					"SET access_frequency = access_frequency + 1 " +
					"WHERE id = " + lineId + ";";
			
			dbHelper.connect();
			
			dbHelper.executeUpdateQuery(updateLineQuery);
			
			dbHelper.disconnect();
			
			//Return the response
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
		    response.getWriter().write("success");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}