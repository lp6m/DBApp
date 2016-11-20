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
public class Task extends Model{
	
    @Id
    public Long taskid;

	public Long employerid;
	
	public Long workerid;

	@Required
	@Column(columnDefinition = "TEXT")
	public String title;

	@Required
	@Column(columnDefinition = "TEXT")
	public String introduction;

	@Required
	public int contractmoney;

	
	public int status;

	@Required
	public String category;

	@CreatedTimestamp
	public Date boshu_kaishi;

	@Required
	public Date boshu_shuryo;

	public Date keiyaku_kakutei;

	public Date nouhin_kanryo;

	public static Finder<Long, Task > find = new Finder<Long, Task>(Long.class, Task.class);

	@Override
    public String toString(){
		String rst = "taskid: " + taskid + " workerid: " + workerid + " title: " + title + "introduction: " + introduction + " contractmoney:" + contractmoney + " status: " + status + " category: " + category + " boshu_kaishi:" + boshu_kaishi + " boshu_shuryo: " + boshu_shuryo + " keiyaku_kakutei: " + keiyaku_kakutei + "nouhin_kanryo: " + nouhin_kanryo;
		return rst;
    }

		
}
