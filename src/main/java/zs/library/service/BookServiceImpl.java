package zs.library.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import zs.library.db.DbManager;
import zs.library.model.Book;
import zs.library.utils.BookStatus;

public class BookServiceImpl implements BookService{
    private final DbManager DB_MANAGER = DbManager.getInstance();
    
    private static BookServiceImpl instance = null;
   

	private BookServiceImpl() {
		
	}
	
	public static BookServiceImpl getInstance() {
		if(instance == null) {
			instance = new BookServiceImpl();
		}
		
		return instance;
	}
	
	

	@Override
	public Book addBook(Book book) throws SQLException, ClassNotFoundException {
		String query = "insert into book (isbn_no,book_name,author,status) values (?,?,?,?)";
		
		try (Connection con = DB_MANAGER.createConnection();
	             PreparedStatement pstmt = con.prepareStatement(query,PreparedStatement.RETURN_GENERATED_KEYS)) {
			pstmt.setString(1, book.getIsbnNo());
			pstmt.setString(2, book.getBookName());
			pstmt.setString(3, book.getAuthor());
			pstmt.setString(4, book.getStatus().name());
			
			 int affectedRows = pstmt.executeUpdate(); // Execute the INSERT statement

		        if (affectedRows > 0) {
		            try (ResultSet rs = pstmt.getGeneratedKeys()) { // Get generated keys
		                if (rs.next()) {
		                    int bookId = rs.getInt(1); // Retrieve the auto-generated key
		                    book.setBookId(bookId);
		                }
		            }
		        }
			
			
		}
		return book;
	}
	
	@Override
	public Book getBook(int bookId) throws SQLException, ClassNotFoundException {
	    String query = "SELECT * FROM book WHERE book_id = ?";
	    Book book = null;

	    try (Connection con = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = con.prepareStatement(query)) {
	        pstmt.setInt(1, bookId);
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            book = new Book();
	            book.setBookId(rs.getInt("book_id"));
	            book.setIsbnNo(rs.getString("isbn_no"));
	            book.setBookName(rs.getString("book_name"));
	            book.setAuthor(rs.getString("author"));
	            book.setStatus(BookStatus.valueOf(rs.getString("status"))); // Assuming BookStatus is an Enum
	        }
	    }
	    return book;
	}


	@Override
	public boolean updateBookStatus(int bookId) throws ClassNotFoundException, SQLException {
		String query = "update book set status=? where book_id=?";
		boolean status = getBookStatus(bookId);
		String bookStatus;
		if(status) {
			bookStatus = BookStatus.BORROWED.name();
		}else {
			bookStatus = BookStatus.AVAILABLE.name();

		}
		try (Connection con = DB_MANAGER.createConnection();
	             PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setString(1, bookStatus);
			pstmt.setInt(2, bookId);
			
			int rowsUpdated = pstmt.executeUpdate();
			
			if(rowsUpdated>0) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean getBookStatus(int bookId) throws SQLException, ClassNotFoundException {
		String query = "select status from book where book_id = ?";
		String status=null;
		try (Connection con = DB_MANAGER.createConnection();
	             PreparedStatement pstmt = con.prepareStatement(query)) {
			pstmt.setInt(1, bookId);
			
			ResultSet rs = pstmt.executeQuery();
			
			if(rs.next()) {
				status = rs.getString("status");
			}
		}
		
		if(status!=null && status.equals("AVAILABLE")) {
			return true;
		}
		
		return false;
	}

}
