package zs.library.service;

import java.sql.SQLException;

import zs.library.model.Book;

public interface BookService {
	Book addBook(Book book) throws SQLException, ClassNotFoundException;
	boolean updateBookStatus(int bookId) throws ClassNotFoundException, SQLException;
	boolean getBookStatus(int bookId) throws SQLException, ClassNotFoundException ;
	Book getBook(int bookId) throws SQLException, ClassNotFoundException;
	Book getBookByName(String bookName) throws SQLException, ClassNotFoundException;
		
}
