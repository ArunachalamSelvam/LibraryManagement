package zs.library.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import zs.library.exceptions.InvalidEmailException;
import zs.library.externalApis.CliqApiManager;
import zs.library.model.Book;
import zs.library.service.BookService;
import zs.library.service.BookServiceImpl;
import zs.library.utils.QrCodeGenerator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.apache.hc.core5.http.ParseException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.zxing.WriterException;

/**
 * Servlet implementation class BookServlet
 */
public class BookServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final ObjectMapper objectMapper = new ObjectMapper();
	private final CliqApiManager CLIQ_API = CliqApiManager.getInstance();

	
	private final BookService BOOK_SERVICE = BookServiceImpl.getInstance();
	 
	private final QrCodeGenerator QR_CODE_GENERATOR = new QrCodeGenerator();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public BookServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
//		response.getWriter().append("Served at: ").append(request.getContextPath());
		
		String action = request.getParameter("action");
		
		switch(action) {
		case "viewBook":{
			getBook(request, response);
			break;
		}
		
		case "getBookByName":{
			getBookByName(request, response);
		}
		}
	}

	private void getBook(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
		response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        int bookId = Integer.parseInt(request.getParameter("bookId"));

        try {
            Book book = BOOK_SERVICE.getBook(bookId);
            
            if (book != null) {
                String jsonResponse = objectMapper.writeValueAsString(book);
                response.getWriter().write(jsonResponse);
            } else {
                response.setStatus(HttpServletResponse.SC_NOT_FOUND);
                response.getWriter().write("{\"message\":\"Book not found\"}");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().write("{\"message\":\"Error retrieving book\"}");
        }
	}
	
	private void getBookByName(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");

	    String bookName = request.getParameter("bookName");

	    if (bookName == null || bookName.trim().isEmpty()) {
	        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	        response.getWriter().write("{\"message\":\"Book name is required\"}");
	        return;
	    }

	    try {
	        Book book = BOOK_SERVICE.getBookByName(bookName); 

	        if (book != null) {
	            String jsonResponse = objectMapper.writeValueAsString(book);
	            response.getWriter().write(jsonResponse);
	        } else {
	            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
	            response.getWriter().write("{\"message\":\"Book not found\"}");
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("{\"message\":\"Error retrieving book\"}");
	    }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		String action = request.getParameter("action");
		
		switch(action) {
		case "updateBookStatus" : {
			updateBookStatus(request, response);
			break;
		}
		case "addBook" : {
			addBook(request, response);
			break;
		}
		
		}
		
		
	}
	
	private void addBook(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException{
		 Book book = objectMapper.readValue(request.getReader(), Book.class);
		 
		 try {
			Book savedBook = BOOK_SERVICE.addBook(book);
			response.setContentType("application/json");
            objectMapper.writeValue(response.getOutputStream(), savedBook);
		} catch (ClassNotFoundException | SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Failed to add book.");

			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		 
		 
	}
	
	private void updateBookStatus(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    int bookId = Integer.parseInt(request.getParameter("bookId"));
	    response.setContentType("application/json");
	    response.setCharacterEncoding("UTF-8");
	    HttpSession session = request.getSession(false);
	    String email = session.getAttribute("email").toString();
	    try {
	        boolean isUpdated = BOOK_SERVICE.updateBookStatus(bookId);
	        boolean bookStatus = BOOK_SERVICE.getBookStatus(bookId);
	        Book book = BOOK_SERVICE.getBook(bookId);
	        String status = bookStatus ? "AVAILABLE" : "BORROWED";

	        if (isUpdated) {
	            String jsonResponse = String.format("{\"status\":\"%s\", \"isUpdated\":true}", status);
	            if(status.equals("BORROWED")) {
	            	 CLIQ_API.sendMessageToZohoCliq(email, "Hi!"+email.split("@")[0]+", You are borrowed " + book.getBookName());
	            }else {
	            	 CLIQ_API.sendMessageToZohoCliq(email, "Hi!"+email.split("@")[0]+", You are returned " + book.getBookName());

	            }
	           
	            response.getWriter().write(jsonResponse);
	        } else {
	            response.getWriter().write("{\"isUpdated\":false}");
	        }
	    } catch (ClassNotFoundException | SQLException | ParseException | InvalidEmailException | InterruptedException e) {
	        e.printStackTrace();
	        response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
	        response.getWriter().write("{\"error\": \"An error occurred while updating book status.\"}");
	    }
	}


}
