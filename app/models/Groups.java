package models;
import java.util.Date;
import java.util.List;
import java.util.Arrays;
import javax.persistence.*;
import javax.validation.*;

import com.avaje.ebean.annotation.*;

import play.db.ebean.*;
import play.data.validation.*;
import play.data.validation.Constraints.*;

public class Groups{
	static public List<String> data = Arrays.asList("システム開発","ホームページ制作・Webデザイン", "アプリ・スマートフォン開発", "ハードウェア開発", "デザイン作成", "記事作成", "ネーミング・アイデア", "3D-CG制作", "音楽・音響・BGM", "翻訳・通訳サービス");

	static public List<String> urls = Arrays.asList("development","web_products","software_development","hardware_development","design","writing","idea","3dcg","sounds","translation_and_interpretation");
}
