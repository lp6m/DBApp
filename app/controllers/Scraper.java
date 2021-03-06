package controllers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.*;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

    // コンストラクタでURLを指定するので、private
    
    private Scraper() {
    };

    protected Document document = null;
    public Scraper(String url) throws IOException {
        this.document = Jsoup.connect(url).get();
		String temp = document.html().replace("<br>", "$$$"); //$$$ instead <br>
		document = Jsoup.parse(temp);
    }

    public ArrayList<String> getElementsByClass(String className) {
        ArrayList<String> list = new ArrayList<>();
        Elements elements = document.getElementsByClass(className);
        for (Element element : elements) {
            list.add(element.text());
        }
        return list;
    }

	public ArrayList<String> getJobInfo(){
		ArrayList<String> list = new ArrayList<>();
		Element nowrap = document.getElementsByClass("summary cw-table").get(0);
        Element table = nowrap.select("table").get(0);
		Elements rows = table.select("tr");

		for (int i = 0; i < rows.size(); i++) {
			Element row = rows.get(i);
			Elements cols = row.select("td");
			list.add(cols.text());
		}
		return list;
	}

	public Date getJobBoshuKaishi(){
		try{
			List<String> list = getJobInfo();
			String str = list.get(0);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			return sdf.parse(str);
		}catch(ParseException e){
			return null;
		}
		
	}

	public Date getJobBoshuShuryo(){
		try{
			List<String> list = getJobInfo();
			String str = list.get(1);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
			return sdf.parse(str);
		}catch(ParseException e){
			return null;
		}
	}

	public Boolean IsNum(String str){
		Boolean b = false;
		for(int i = 0; i < 10; i++){
			if(str.equals(String.valueOf(i))) b = true;
		}
		return b;
	}
	public int getContractMoney(){
		List<Integer> moneys = new ArrayList<Integer>();
		String str = getJobInfo().get(3);
		String[] strarray = str.split("");//1文字ずつに切り出す

		String nowmoney = "";
		for(int i = 0; i < strarray.length; i++){
			if(strarray[i].equals("円")){
				int m = Integer.parseInt(nowmoney);
				moneys.add(m);
				nowmoney = "";
			}else if(IsNum(strarray[i])){
				nowmoney += strarray[i];
			}
		}
		//最後に平均をとって出す
		int sum = 0;
		for(int i = 0; i < moneys.size(); i++) sum += moneys.get(i);
		
		if(moneys.size() == 0) return 0;
		return sum / moneys.size();
	}

	public String getJobTitle(){
		Element titlecontainer = document.getElementsByClass("title_container title_detail").get(0);
		Element title = titlecontainer.select("h1").get(0);
		return title.text();
	}

	public String getIntroduction(){
		return getElementsByClass("description").get(0).replace("$$$", "\n").toString();
		/*return getElementsByClass("post_block").get(0).replace("$$$", "\n").toString();*/
	}

	public List<String> getJobPages(){
		List<String> rst = new ArrayList<String>();
		Elements lis = document.getElementsByClass("jobs_lists jobs_lists_simple").select("li");
		System.out.println(lis.size());
		for(int i = 0; i < lis.size(); i++){
			Elements urls = lis.get(i).select("a");
			if(urls.size()>0){
				String link = urls.get(0).attr("href");
				if(link.matches(".*" + "/public/jobs/" + ".*")){
						rst.add(link);
				}
			}
		}
		return rst;
	}
}
