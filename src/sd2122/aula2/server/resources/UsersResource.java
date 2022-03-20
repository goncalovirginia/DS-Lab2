package sd2122.aula2.server.resources;

import jakarta.inject.Singleton;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import sd2122.aula2.api.User;
import sd2122.aula2.api.service.RestUsers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@Singleton
public class UsersResource implements RestUsers {
	
	private static final Logger Log = Logger.getLogger(UsersResource.class.getName());
	
	private final Map<String, User> users;
	
	public UsersResource() {
		users = new HashMap<>();
	}
	
	@Override
	public String createUser(User user) {
		Log.info("createUser : " + user);
		
		// Check if user data is valid
		if (user.getUserId() == null || user.getPassword() == null || user.getFullName() == null || user.getEmail() == null) {
			Log.info("User object invalid.");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		
		// Check if userId already exists
		if (users.containsKey(user.getUserId())) {
			Log.info("User already exists.");
			throw new WebApplicationException(Status.CONFLICT);
		}
		
		//Add the user to the map of users
		users.put(user.getUserId(), user);
		return user.getUserId();
	}
	
	
	@Override
	public User getUser(String userId, String password) {
		Log.info("getUser : user = " + userId + "; pwd = " + password);
		
		// Check if user is valid
		if (userId == null || password == null) {
			Log.info("UserId or password null.");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		
		User user = users.get(userId);
		
		// Check if user exists 
		if (user == null) {
			Log.info("User does not exist.");
			throw new WebApplicationException(Status.NOT_FOUND);
		}
		
		//Check if the password is correct
		if (!user.getPassword().equals(password)) {
			Log.info("Password is incorrect.");
			throw new WebApplicationException(Status.FORBIDDEN);
		}
		
		return user;
	}
	
	
	@Override
	public User updateUser(String userId, String password, User updatedUser) {
		Log.info("updateUser : user = " + userId + "; pwd = " + password + " ; user = " + updatedUser);
		
		User user = getUser(userId, password);
		
		//Check if the updated user is null
		if (updatedUser == null) {
			Log.info("Updated user null.");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		
		if (updatedUser.getPassword() != null) {
			user.setPassword(updatedUser.getPassword());
		}
		if (updatedUser.getEmail() != null) {
			user.setEmail(updatedUser.getEmail());
		}
		if (updatedUser.getFullName() != null) {
			user.setFullName(updatedUser.getFullName());
		}
		
		return user;
	}
	
	
	@Override
	public User deleteUser(String userId, String password) {
		Log.info("deleteUser : user = " + userId + "; pwd = " + password);
		return users.remove(getUser(userId, password).getUserId());
	}
	
	
	@Override
	public List<User> searchUsers(String pattern) {
		Log.info("searchUsers : pattern = " + pattern);
		
		//Check if the pattern is null or empty
		if (pattern == null) {
			Log.info("Pattern null.");
			throw new WebApplicationException(Status.BAD_REQUEST);
		}
		
		if (pattern.equals("")) {
			return users.values().stream().toList();
		}
		
		List<User> userList = new LinkedList<>();
		String patternLowerCase = pattern.toLowerCase();
		
		for (String userId : users.keySet()) {
			if (userId.toLowerCase().contains(patternLowerCase)) {
				User user = users.get(userId);
				userList.add(new User(user.getUserId(), user.getFullName(), user.getEmail(), ""));
			}
		}
		
		return userList;
	}
	
}
