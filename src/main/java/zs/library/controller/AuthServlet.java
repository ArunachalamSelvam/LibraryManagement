package zs.library.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import zs.library.model.User;
import zs.library.service.UserServiceImpl;
import zs.library.utils.JwtDecoder;

import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;

import org.json.JSONObject;

import com.fasterxml.jackson.core.JsonProcessingException;

/**
 * Servlet implementation class AuthServlet
 */
public class AuthServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    private static final UserServiceImpl USER_SERVICE = UserServiceImpl.getInstance(); 
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AuthServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String authCode = request.getParameter("code"); // Get authorization code from URL
		int bookId = Integer.parseInt(request.getParameter("state").toString());
//		int bookId=1;
		System.out.println("BookId : " + bookId);
		
        if (authCode != null) {
            response.setContentType("application/json"); // Set response type as JSON
            response.setCharacterEncoding("UTF-8");

            try {
                // Define API endpoint
                String url = "https://accounts.zoho.com/oauth/v2/token";

                // Construct request body parameters
                String requestBody = "client_id=" + URLEncoder.encode("1000.88UX8X1EAZZPQVR5F9T2MZUCWOBK8U", StandardCharsets.UTF_8)
                        + "&client_secret=" + URLEncoder.encode("c961d71a822a0ca57aacb850fda7818c5a18ee8787", StandardCharsets.UTF_8)
                        + "&grant_type=authorization_code"
                        + "&redirect_uri=" + URLEncoder.encode("http://localhost:8080/LibraryManagement/AuthServlet", StandardCharsets.UTF_8)
                        + "&code=" + URLEncoder.encode(authCode, StandardCharsets.UTF_8);

                // Create HttpClient instance
                HttpClient client = HttpClient.newHttpClient();

                // Create POST request with headers
                HttpRequest requestApi = HttpRequest.newBuilder()
                        .uri(URI.create(url))
                        .header("Content-Type", "application/x-www-form-urlencoded")
                        .POST(BodyPublishers.ofString(requestBody))
                        .build();

                // Send request and receive response
                HttpResponse<String> responseApi = client.send(requestApi, HttpResponse.BodyHandlers.ofString());

                // Print response
                System.out.println("Response Code: " + responseApi.statusCode());
                System.out.println("Response Body: " + responseApi.body());

                JSONObject jsonResponse = new JSONObject(responseApi.body());

                // Extract `id_token` (JWT) and `access_token`
                String accessToken = jsonResponse.optString("access_token", "No access token found");
                String idToken = jsonResponse.optString("id_token", null); // JWT Token

                if (idToken != null) {
                    try {
                        // Decode the JWT payload
                        String decodedPayload = JwtDecoder.decodeJwt(idToken);
                        System.out.println(decodedPayload);
                        JSONObject userJson = new JSONObject(decodedPayload);
                        
                        String email = userJson.optString("email", null);
                        
                        HttpSession session = request.getSession();
                        
                        User existingUser = USER_SERVICE.getUserByEmail(email);
                        
                        if(existingUser==null) {
                        	User user = new User(email);
                        	User savedUser = USER_SERVICE.addUser(user);
                        	
                            session.setAttribute("userId", savedUser.getUserId());
                        	
                        }else {
                        	session.setAttribute("userId", existingUser.getUserId());

                        }
                        
                        session.setAttribute("email", email);
//                        response.getWriter().write(decodedPayload);
                        
                        response.sendRedirect("http://localhost:8080/LibraryManagement/bookPage.html?bookId=" + bookId);

                    } catch (JsonProcessingException jwtException) {
                        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                        response.getWriter().write("{\"error\": \"Invalid JWT format or decoding failed\"}");
                    }
                } else {
                    response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                    response.getWriter().write("{\"error\": \"ID Token not found in response\"}");
                }

            } catch (Exception e) {
                e.printStackTrace();
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"An error occurred while processing the request\"}");
            }

        } else {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.getWriter().write("{\"error\": \"Authorization Code not found\"}");
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
