package forms;

import play.data.validation.*;
import play.db.ebean.*;
import java.security.NoSuchAlgorithmException;
import models.*;

public class LoginForm {

	@Constraints.Required
	private String username;
	@Constraints.Required
	private String password;

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	public String validate() throws NoSuchAlgorithmException{
		if(authenticate(username,password)==null){
			return "Invalid user or password";
		}
		return null;
	}
	public static Registrant authenticate(String username, String password) throws java.security.NoSuchAlgorithmException{
		Model.Finder<Long, Registrant> find = new Model.Finder<Long, Registrant>(Long.class, Registrant.class);
		return find.where().eq("username", username).eq("password", password).findUnique();
	}

}
