package SpiderUtils.SpiderData;

import SpiderUtils.CommonSpiderKnowledge;
import SpiderUtils.SpiderContant;
import SpiderUtils.SpiderUtils;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.IOException;
import java.util.Date;

/**
 * Created by 123 on 2017/2/23.
 */
public class TextGame {
    public static void main(String [] args) throws XpathSyntaxErrorException, IOException {

        //
        System.setProperty("webdriver.chrome.driver", SpiderContant.chromeWindowsPath );
        WebDriver driver= new ChromeDriver();
        driver.get("http://syt.4399.cn/game/hot/1.html");
        WebElement web = driver.findElement(By.xpath("/html"));
        String html = web.getAttribute("outerHTML");
        Document doc = Jsoup.parse(html);
        JXDocument js=new JXDocument(doc);
        System.out.println(js.sel("//ul[@class='fgl_gamehot_ul j-gamehover clearfix']/li/a/@href"));


//        JXDocument document=new JXDocument(doc);
//           System.out.println(document.sel(""));

//        try {
//            CommonSpiderKnowledge.ergodicUrl("spider40407CY", 109, "no");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        //String html = "<html><head><title>开源中国社区</title></head>"
          //      + "<body><p>这里是 jsoup 项目的相关文章</p></body></html>";
//        String cookie="";
//        Connection con=Jsoup.connect("http://www.cnblogs.com/ry123/p/3682749.html");
//        con.header("Accept", "text/html, application/xhtml+xml, */*");
//        con.header("Content-Type", "application/x-www-form-urlencoded");
//        con.header("User-Agent", "Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; WOW64; Trident/5.0))");
//        con.header("Cookie", cookie);
//        Document doc=con.get();
//        System.out.println();
//       //Document doc= Jsoup.parse(html);
//        System.out.println(doc.title());



         //   CommonSpiderKnowledge.ergodicUrl("spiderDYW", 0, "no");







    }
}
