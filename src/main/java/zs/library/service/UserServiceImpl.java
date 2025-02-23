package zs.library.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import zs.library.db.DbManager;
import zs.library.model.User;

public class UserServiceImpl implements UserService {
    private final DbManager DB_MANAGER = DbManager.getInstance();

    private UserServiceImpl() {}
    
    private static UserServiceImpl instance = null;
    
    public static UserServiceImpl getInstance() {
    	if(instance == null) {
    		instance = new UserServiceImpl();
    	}
    	
    	return instance;
    }
    
	@Override
	public User addUser(User user) throws ClassNotFoundException {
	    String sql = "INSERT INTO users (email) VALUES (?)";

	    try (Connection conn = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {

	        pstmt.setString(1, user.getEmail());
	        int affectedRows = pstmt.executeUpdate(); // Execute update instead of query

	        if (affectedRows > 0) {
	            try (ResultSet rs = pstmt.getGeneratedKeys()) { // Retrieve generated keys
	                if (rs.next()) {
	                    user.setUserId(rs.getInt("user_id")); // Get generated user_id
	                }
	            }
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        return null;
	    }

	    return user;
	}

	
	@Override
	public boolean isUserExists(String email) throws ClassNotFoundException {
	    String sql = "SELECT COUNT(*) FROM users WHERE email = ?"; 

	    try (Connection conn = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, email);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                return rs.getInt(1) > 0; // Return true if count > 0
	            }
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false; // Return false if an error occurs or user doesn't exist
	}
	
	
	@Override
	public User getUserByEmail(String email) throws ClassNotFoundException {
		String sql = "SELECT user_id, email FROM users WHERE email = ?";

	    User user = null;

	    try (Connection conn = DB_MANAGER.createConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setString(1, email);

	        try (ResultSet rs = pstmt.executeQuery()) {
	            if (rs.next()) {
	                user = new User();
	                user.setUserId(rs.getInt("user_id"));
	                user.setEmail(rs.getString("email"));
	            }
	            
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return user; // Returns user if found, otherwise null
	}


}
