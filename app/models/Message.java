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
public class Message extends Model{
	
	@Id
	public Long messageid;

	public Long taskid;

	public Long senderid;

	public Long receiverid;

	public int mskind;

	public String text;

	public Date timestamp;
	

	public static Finder<Long, Message> find = new Finder<Long, Message>(Long.class, Message.class);
	
    @Override
    public String toString(){
		String rst = "message: " + messageid + " taskid: " + taskid + " senderid: " + senderid + " receiverid: " + receiverid + " mskind: " + mskind + " text " + text + " timestamp " + timestamp;
		return rst;
    }

		
}
