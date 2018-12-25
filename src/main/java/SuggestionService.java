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

@SuppressWarnings("serial")
@WebServlet(
    name = "SuggestionService",
    urlPatterns = {"/suggest"}
)
public class SuggestionService extends HttpServlet {
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
			
			String searchTerm = requestObj.getString("searchTxt");
			int num = requestObj.getInt("num");
			int counter = requestObj.getInt("counter");
			
			//Get the lines
			
			ArrayList<String> suggestions = new ArrayList<String>();
			
			String suggestionsQuery = getSuggestionsQuery(searchTerm, num);

			dbHelper.connect();
			ResultSet suggestionsResSet = dbHelper.executeSelectQuery(suggestionsQuery);
			
			while (suggestionsResSet.next())
			{
				String suggestion = suggestionsResSet.getString("descriptor");
				suggestions.add(suggestion);
			}
			
			dbHelper.disconnect();
			
			//Create the json response object
			
			String suggestionsString = "";
			for(int i = 0; i < suggestions.size(); i++) {
				suggestionsString += "{ \"suggestion\": \"" + suggestions.get(i) + "\" }";
				
				if(i < suggestions.size() - 1) {
					suggestionsString += ",";
				}
			}
			suggestionsString = "[" + suggestionsString + "]";
			
			String result =  "{ \"suggestions\": " + suggestionsString + ", \"counter\": " + counter + " }";
			
			//Return the response
			
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
		    response.getWriter().write(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getSuggestionsQuery(String searchTerm, int num) {
		String suggestionsQuery = 
				"SELECT descriptor " + 
				"FROM line " + 
				"WHERE descriptor LIKE '" + searchTerm + "%' " +
				"ORDER BY descriptor ASC " +
				"LIMIT 0, " + num + ";";
		
		return suggestionsQuery;
	}
}