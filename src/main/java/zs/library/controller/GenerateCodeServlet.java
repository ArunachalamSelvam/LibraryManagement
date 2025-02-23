package zs.library.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * Servlet implementation class GenerateCodeServlet
 */
public class GenerateCodeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GenerateCodeServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		try {
            // Construct the URL
            String authUrl = "https://accounts.zoho.com/oauth/v2/auth"
                    + "?response_type=code"
                    + "&client_id=1000.88UX8X1EAZZPQVR5F9T2MZUCWOBK8U"
                    + "&scope=email"
                    + "&redirect_uri=http://localhost:8080/LibraryManagement/AuthServlet";

            // Create HttpClient instance
//            HttpClient client = HttpClient.newHttpClient();
//
//            // Create GET request
//            HttpRequest requestApi = HttpRequest.newBuilder()
//                    .uri(URI.create(url))
//                    .GET()
//                    .build();
//
//            // Send request and receive response
//            HttpResponse<String> responseApi = client.send(requestApi, HttpResponse.BodyHandlers.ofString());
//
//            // Print response body
//            System.out.println("Response Code: " + responseApi.statusCode());
//            System.out.println("Response Body: " + responseApi.body());
            
            response.sendRedirect(authUrl);
		}
		catch(Exception e) {
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

}
