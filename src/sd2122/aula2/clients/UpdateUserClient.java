package sd2122.aula2.clients;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.glassfish.jersey.client.ClientConfig;
import sd2122.aula2.api.User;
import sd2122.aula2.api.service.RestUsers;

import java.io.IOException;

public class UpdateUserClient {
	
	public static void main(String[] args) throws IOException {
		
		if (args.length != 6) {
			System.err.println("Use: java sd2122.aula2.clients.UpdateUserClient url userId oldpwd fullName email password");
			return;
		}
		
		String serverUrl = args[0];
		String userId = args[1];
		String oldpwd = args[2];
		String fullName = args[3];
		String email = args[4];
		String password = args[5];
		
		User u = new User(userId, fullName, email, password);
		
		System.out.println("Sending request to server.");
		
		ClientConfig config = new ClientConfig();
		Client client = ClientBuilder.newClient(config);
		
		WebTarget target = client.target(serverUrl).path(RestUsers.PATH);
		
		Response r = target.path(userId)
				.queryParam(RestUsers.PASSWORD, oldpwd)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(u, MediaType.APPLICATION_JSON));
		
		if (r.getStatus() == Response.Status.OK.getStatusCode() && r.hasEntity()) {
			System.out.println("Success:");
			User user = r.readEntity(User.class);
			System.out.println("User : " + u);
		}
		else
			System.out.println("Error, HTTP error status: " + r.getStatus());
		
	}
	
}
