package controllers;

import java.io.IOException; 
import controllers.Scraper;
import play.*;
import play.data.*;
import static play.data.Form.*;
import com.avaje.ebean.ExpressionList;
import play.mvc.*;
import models.*;
import forms.*;
import java.util.*;
import play.data.validation.Constraints.Required;
import play.db.ebean.*;
import java.util.Random;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import org.apache.commons.lang3.RandomStringUtils;
import java.util.Date;
import java.text.SimpleDateFormat;
import views.html.*;
import java.text.ParseException;
import java.sql.*;

import java.net.URLDecoder;
import java.net.URLEncoder;
import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import com.avaje.ebean.SqlRow;

public class Application extends Controller {

	public static Long userid = -1L;
	public static String username = "";
	public static boolean IsLogined(){
		return (Application.username != "");
	}
	public static void DoLogOut(){
		session().clear();
		Application.username = "";
		Application.userid = -1L;
	}
    public static Result index() {
		List<Registrant> reg = Registrant.find.all();
		List<RegisteredCategory> regcategory = RegisteredCategory.find.all();
		List<Task> task = Task.find.all();
		List<String> st =Groups.data;
		List<ApplicationModel> app = ApplicationModel.find.all();
		return ok(index.render("DataBase Sample", reg, regcategory, task,st,app));
    }
	public static Result login(){
		Form<LoginForm> loginForm = new Form<LoginForm>(LoginForm.class);
        return ok(login.render(loginForm));
	}
	public static Result authenticate(){
        Form<LoginForm> loginForm = form(LoginForm.class).bindFromRequest();
        if (loginForm.hasErrors()) {
            return badRequest(login.render(loginForm));
        } else {
        	session().clear();
            session("username", loginForm.get().getUsername());
            String returnUrl = ctx().session().get("returnUrl");
            if(returnUrl == null || returnUrl.equals("") || returnUrl.equals(routes.Application.login().absoluteURL(request()))) {
                returnUrl = "/user";
				setUserName();
            }
            return redirect(returnUrl);
        }
    }
	
	public static void setUserName(){
		Application.username = session("username");
		Application.userid = GetUserIdFromUsername(Application.username);
	}

	public static Long GetUserIdFromUsername(String uname){
		if(uname == null || uname == "") return -1L;
		Registrant rst = Registrant.find.where().eq("username",uname).findList().get(0);
		if(rst == null) return -1L;
		return rst.id;
 	}
	public static Result userpage(){
		if(IsLogined()){
			Registrant r = Registrant.find.byId(Application.userid);
			return ok(user.render("ユーザーページ",r));
		}else{
			Form<LoginForm> f = new Form<LoginForm>(LoginForm.class);
			return ok(login.render(f));
		}
	}
	// Registrant Action
	public static Result AddUser(){
        Form<Registrant> f = new Form<Registrant>(Registrant.class);
        return ok(adduser.render("ユーザー登録", f));
    }

    public static Result CreateUser(){
        Form<Registrant> f = new Form<Registrant>(Registrant.class).bindFromRequest();
        if (!f.hasErrors()){
            Registrant data = f.get();
            data.save();
            return redirect("/");
        }else{
            return badRequest(adduser.render("ERROR", f));
        }
    }

	public static Result AddCategory(){
		if("GET".equals(request().method())){
			//GET
			Form<RegisteredCategory> f = new Form<RegisteredCategory>(RegisteredCategory.class);
			return ok(addcategory.render("",f));
		}else{

			//POST
			Map<String,String[]> f = request().body().asFormUrlEncoded();
			String[] useridinput = f.get("userid");
			String[] categoryinput = f.get("category");
			Registrant user = Registrant.find.byId(Long.parseLong(useridinput[0]));
			if(user != null){
				RegisteredCategory data = new RegisteredCategory();
				data.userid = Long.parseLong(useridinput[0]);
				data.category = categoryinput[0];
				data.save();
				return redirect("/");
			}else{
				return badRequest(addcategory.render("ERROR", new Form<RegisteredCategory>(RegisteredCategory.class)));
			}
			
		}
	}
	
	public static Result AddTask(){
		if("GET".equals(request().method())){
			if(IsLogined()){
				Form<Task> f = new Form<Task>(Task.class);
				return ok(addtask.render("タスク追加ページ",f,Groups.data));
			}else{
				Form<LoginForm> f = new Form<LoginForm>(LoginForm.class);
				return ok(login.render(f));
			}
		}else{
			Form<Task> f = new Form<Task>(Task.class).bindFromRequest();
			if(!f.hasErrors()){
				Task data = f.get();
				data.employerid = Application.userid;
				data.save();
				return redirect("/");
			}else{
				return badRequest(addtask.render("ERROR", f,Groups.data));
			}
		}
	}
	public static class TaskSearchForm{
		public String category = "";
		public String searchstr = "";
	}
	public static Result TaskList() {
		Form<TaskSearchForm> f = new Form<TaskSearchForm>(TaskSearchForm.class).bindFromRequest();
		if(!f.hasErrors() && f.get().category != ""){
			List<Task> rst = Task.find.where().eq("category",f.get().category).findList();
			return ok(tasklist.render("検索結果:",rst,Groups.data,new Form<TaskSearchForm>(TaskSearchForm.class)));
		}else if(f.get().searchstr != ""){
			String s = f.get().searchstr.toLowerCase();
			List<Task> tasks = Task.find.all();
			List<Task> rst = new ArrayList<Task>();
			for(int i = 0; i < tasks.size(); i++){
				Task t = tasks.get(i);
				if((t.introduction.toLowerCase().matches(".*" + s + ".*"))|| (t.title.toLowerCase().matches(".*" + s + ".*"))){
					rst.add(t);
				}
			}
			return ok(tasklist.render("検索結果:",rst,Groups.data, new Form<TaskSearchForm>(TaskSearchForm.class)));
		}else{
			List<Task> task = Task.find.all(); 
			return ok(tasklist.render("仕事一覧",task,Groups.data,new Form<TaskSearchForm>(TaskSearchForm.class)));
		}
    }
	public static Date RandomDate(Random rnd){
		try{
			Date today = new Date();
			int month = rnd.nextInt(12) + 1;
			String monthstr = String.valueOf(month);
			if(month < 10) monthstr = "0" + monthstr;
			int day = rnd.nextInt(30) + 1;
			String daystr = String.valueOf(day);
			if(day < 10) daystr = "0" + daystr;
			String certainDay = "2016-" + monthstr + "-" + daystr;
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			return sdf.parse(certainDay);
		}catch(ParseException e){
			return null;
		}
	}
	public static Result maketask(){
		Random rnd = new Random();
		for(int i = 0; i < 100; i++){
			Task t = new Task();
			t.employerid = 1L;
			t.title = RandomStringUtils.randomAlphabetic(10);
			t.introduction = RandomStringUtils.randomAlphabetic(100);
			t.contractmoney = rnd.nextInt(30000);
			t.status = rnd.nextInt(3)+1;
			t.category = Groups.data.get(rnd.nextInt(Groups.data.size()));
			t.boshu_shuryo = RandomDate(rnd);
			t.save();
		}
		return redirect("/tasklist");
	}
	static public Result jobpage(int jobnum){
		List<Task> tasklist = Task.find.where().eq("taskid",jobnum).findList();
	    Task task;
		if(tasklist.size() == 0) task = null;
		else task = tasklist.get(0);

		/*自分の発注した仕事の場合は応募画面を表示しない*/
		Boolean canapplication = true;
		if(IsLogined() && task.employerid == Application.userid){
			canapplication = false;
		}
		/*すでに応募している場合も応募画面を表示しない*/
		if(IsLogined()){
			List<ApplicationModel> apps = ApplicationModel.find.where().eq("applicantid",Application.userid).findList();
			for(int i = 0; i < apps.size(); i++) if(apps.get(i).taskid == jobnum) canapplication = false;
		}
		return ok(jobpage.render(task,new Form<AppForm>(AppForm.class),canapplication));
	}
	public static class AppForm{
		Long jobnum;
		String message;
	}
	static public Result edituser(){
		Registrant me = Registrant.find.byId(Application.userid);
		Form<Registrant> f = new Form<Registrant>(Registrant.class).bindFromRequest();
		if(me != null){
			f = new Form<Registrant>(Registrant.class).fill(me);
			return ok(edituser.render(f));
		}else{
			return redirect("/user");
		}
	}
	static public Result updateuser(){
		Form<Registrant> f = new Form<Registrant>(Registrant.class).bindFromRequest();
		System.out.println(f);
		if(!f.hasErrors()){
			Registrant reg = f.get();
			reg.id = Application.userid;
			reg.update();
			return redirect("/user");
		}else{
			return ok(edituser.render(f));
		}
	}
	static public Result application(){
		Map<String,String[]> f = request().body().asFormUrlEncoded();
		String message = f.get("message")[0];
		if(IsLogined()){
			Task task = Task.find.where().eq("taskid",Long.parseLong(f.get("taskid")[0])).findList().get(0);
			ApplicationModel app = new ApplicationModel();
			app.taskid = task.taskid;
			app.applicantid = Application.userid;
			app.save();
			return redirect("/message");
		}else{
			return redirect("/login");
		}
	}
	static public Result logout(){
		DoLogOut();
		return ok(logout.render());
	}
	static class MessageForm{
		public int mskind;
		public String message;
	}
	static class AdoptionForm{
		public Long taskid;
		public Long workerid;
	}
	static public Result adoption(){
		Form<AdoptionForm> f =  new Form<AdoptionForm>(AdoptionForm.class).bindFromRequest();
		Task t = Task.find.where().eq("taskid",f.get().taskid).findList().get(0);
		t.status = 2;
		return redirect("/orderlist");
		
	}
	static public Result orderlist(){
	    if(IsLogined()){
			List<Task> myordertask = Task.find.where().eq("employerid",String.valueOf(Application.userid)).findList();
			List<List<Registrant>> myapplicants = new ArrayList<List<Registrant> >();
			//ApplicationModelとの検索を行い,応募者の一覧を出力する
			/*			try{*/
				/*String sql = "select Task.title, Registrant.username from Task natural join Registrant where Task.employerid=" + String.valueOf(Application.userid);
				List<SqlRow> rst = Ebean.createSqlQuery(sql).findList();
				System.out.println(rst);
				for(int i = 0; i < rst.size(); i++){
					Clob c = (Clob)rst.get(i).get("title");
					String a =  c.getSubString(1,(int)c.length());
					System.out.println(a);
					System.out.println(rst.get(i).get("username"));
					}*/
			/*			}catch(SQLException e){
			}*/
			
			for(int i = 0+0; i < myordertask.size(); i++){
				Task t = myordertask.get(i);
				List <ApplicationModel> applicantoft = ApplicationModel.find.where().eq("taskid",t.taskid).findList(); //ApplicationModel.find().where.eq("taskid",t.taskid).findList();
				List <Registrant> regs_of_t = new ArrayList<Registrant>();
				for(int j = 0; j < applicantoft.size(); j++){
					ApplicationModel app = applicantoft.get(j);
					Registrant r = Registrant.find.where().eq("id", app.applicantid).findList().get(0);
					regs_of_t.add(r);
				}
				myapplicants.add(regs_of_t);
			}
			return ok(orderlist.render(myordertask,myapplicants));
		}else{
			List<Task> task = Task.find.all();
			return ok(tasklist.render("ログインしてください",task,Groups.data,new Form<TaskSearchForm>(TaskSearchForm.class)));
		}
	}
	static public Result applist(){
		 if(IsLogined()){
			 System.out.println(Application.username);
			 List<ApplicationModel> apps = ApplicationModel.find.where().eq("applicantid",Application.userid).findList();
			 List<Task> myapptasks = new ArrayList<Task>();
			 for(int i = 0; i < apps.size(); i++){
				 ApplicationModel app = apps.get(i);
				 Task t = Task.find.where().eq("taskid",app.taskid).findList().get(0);
				 myapptasks.add(t);
			 }
			return ok(applist.render(myapptasks));
		}else{
			List<Task> task = Task.find.all();
			return ok(tasklist.render("ログインしてください",task,Groups.data,new Form<TaskSearchForm>(TaskSearchForm.class)));
		}
	}
	static public Result deleteme(){
		Registrant me = Registrant.find.where().eq("id",Application.userid).findList().get(0);
		me.delete();
		DoLogOut();
		return redirect("/logout");
	}
	static public List<Task> GetAppTasks(){
		List<ApplicationModel> apps = ApplicationModel.find.where().eq("applicantid",Application.userid).findList();
	    List<Task> rst = new ArrayList<Task>();
		for(int i = 0; i < apps.size(); i++){
			Task t = Task.find.where().eq("taskid",apps.get(i).taskid).findList().get(0);
			rst.add(t);
		}
		return rst;
	}
	static public Result message(){
		List<Task> ordertasks = Task.find.where().eq("employerid",Application.userid).findList();
		List<Task> apptasks = GetAppTasks();
		List<Task> tasks = new ArrayList<Task>();
		List<Boolean> isorder = new ArrayList<Boolean>(); /*そのTaskで自分は発注者か*/
		for(int i = 0; i < ordertasks.size(); i++) isorder.add(true);
		for(int i = 0; i < apptasks.size(); i++) isorder.add(false);
		tasks.addAll(ordertasks);
		tasks.addAll(apptasks);
		List<List<Message>> messages= new ArrayList<List<Message>>(); /*仕事別*/
		for(int i = 0; i < tasks.size(); i++){
			Task t = tasks.get(i);
			List<Message> ms = Message.find.where().eq("taskid",t.taskid).findList();
			messages.add(ms);
		}
		return ok(message.render(tasks,messages,isorder));
		
	}
	static public Result scraping(){
		try{
			for(int i = 0; i < Groups.urls.size(); i++){
				Scraper joblistpage = new Scraper("https://crowdworks.jp/public/jobs/group/"+Groups.urls.get(i)+"?payment_type=fixed_price");
				List<String> jobs = joblistpage.getJobPages();
				for(String joburl : jobs){
					try{
						System.out.println(joburl);
						Scraper scraper = new Scraper("https://crowdworks.jp"+joburl);
						Date boshu_kaishi = scraper.getJobBoshuKaishi();
						Date boshu_shuryo = scraper.getJobBoshuShuryo();
						int contractmoney = scraper.getContractMoney();
						String introduction = scraper.getIntroduction();
						String jobtitle = scraper.getJobTitle();

						Task task = new Task();
						task.employerid = 1L;
						task.title = jobtitle;
						task.introduction = introduction;
						task.contractmoney = contractmoney;
						task.status = 1;
						task.category = Groups.data.get(i);
						task.boshu_kaishi = boshu_kaishi;
						task.boshu_shuryo = boshu_shuryo;
						task.save();
					}catch(IOException e){
					}
				}
			}
		}catch(IOException e){
			System.out.println("something wrong");
		}
		return redirect("/tasklist");
	}
}
