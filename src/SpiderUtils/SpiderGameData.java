package SpiderUtils;

import SpiderUtils.SpiderUtils;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import dao.impl.ProKnowledgeImpl;
import org.dom4j.DocumentException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.text.ParseException;

/**
 * Created by 123 on 2017/2/23.
 */
public class SpiderGameData {
    public static void main(String [] args){
        try {
              CommonSpiderKnowledge.ergodicUrl("spiderYXTL", 0, "no");
//            CommonSpiderKnowledge.ergodicUrl("spider18183", 0, "no");
//            CommonSpiderKnowledge.ergodicUrl("spiderCDW", 0, "no");
//            CommonSpiderKnowledge.ergodicUrl("spiderPc25g", 0, "no");
              //CommonSpiderKnowledge.ergodicUrl("spiderYXG", 0, "no");

//            CommonSpiderKnowledge.ergodicUrl("spiderDYW", 0, "no");
//            CommonSpiderKnowledge.ergodicUrl("spiderQZW", 0, "no");
//            CommonSpiderKnowledge.ergodicUrl("spiderYJJ", 0, "no");
//
//            CommonSpiderKnowledge.ergodicUrl("spiderAW", 0, "no");


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
