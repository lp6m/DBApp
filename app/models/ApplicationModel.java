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
public class ApplicationModel extends Model{

    public Long taskid;

	public Long applicantid;

	public static Finder<Long, ApplicationModel> find = new Finder<Long, ApplicationModel>(Long.class, ApplicationModel.class);

    @Override
    public String toString(){
		String rst = "taskid" + taskid + " appicantid: " + applicantid;
		return rst;
    }

		
}
