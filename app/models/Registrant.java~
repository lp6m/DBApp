package models;
import java.util.Date;
import java.util.List;

import javax.persistence.*;
import javax.validation.*;

import com.avaje.ebean.annotation.*;

import play.db.ebean.*;
import play.data.validation.*;
import play.data.validation.Constraints.*;

@Entity
public class Registrant extends Model{
 	
    @Id
    public Long id;

	@Required
	public String username;
	
	@Required
	public String password;
	
	@Required(message = "名前を入力してください")
	public String screenname;

	@Required
	public int prefecture;

	@Required
	public int age;

	@Required(message = "自己紹介を入力してください")
	public String introduction;
 
    public static Finder<Long, Registrant > find = new Finder<Long, Registrant>(Long.class, Registrant.class);

    @Override
    public String toString(){
		String rst = "id: " + id + " username: " + username + " prefecture " + prefecture + "age: " + age + "introduction : " +  introduction;
		return rst;
    }

		
}
