package zs.library.model;

public class User {
	private int userId;
	private String email;
	
	public User() {
		// TODO Auto-generated constructor stub
	}
	
	public User(String email) {
		// TODO Auto-generated constructor stub
		this.email = email;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
