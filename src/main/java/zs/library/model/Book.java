package zs.library.model;

import zs.library.utils.BookStatus;

public class Book {
	private int bookId;
	private String isbnNo;
	private String bookName;
	private String author;
	private BookStatus status;
	
	
	public int getBookId() {
		return bookId;
	}
	public void setBookId(int bookId) {
		this.bookId = bookId;
	}
	public String getIsbnNo() {
		return isbnNo;
	}
	public void setIsbnNo(String isbnNo) {
		this.isbnNo = isbnNo;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public BookStatus getStatus() {
		return status;
	}
	public void setStatus(BookStatus status) {
		this.status = status;
	}
	
	
}
