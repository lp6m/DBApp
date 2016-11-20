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
public class RegisteredCategory extends Model{

   
	@Required
    public Long userid;

	@Required
	public String category;

    @Override
    public String toString(){
		String rst = "userid: " + userid + " category: " + category;
		return rst;
    }
	public static Finder<Long, RegisteredCategory > find = new Finder<Long, RegisteredCategory>(Long.class, RegisteredCategory.class);

		
}
