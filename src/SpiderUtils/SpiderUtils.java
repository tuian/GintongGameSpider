package SpiderUtils;

import JavaBean.*;
import cn.wanghaomiao.xpath.exception.XpathSyntaxErrorException;
import cn.wanghaomiao.xpath.model.JXDocument;
import cn.wanghaomiao.xpath.model.JXNode;
import dao.impl.BasPersonInfoImpl;
import dao.impl.BugDataImpl;
import dao.impl.PerKnowledgeImpl;
import dao.impl.BasKnowledgeInfoDaoImpl;
import org.apache.commons.lang3.StringUtils;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.jsoup.Jsoup;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;

import javax.xml.parsers.ParserConfigurationException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by 孙鸿宇 on 2017/2/17.
 */
public class SpiderUtils {

    private static BugDataImpl bugDataimpl = new BugDataImpl();
    public static BaseKnowLedge baseKnowledge=new BaseKnowLedge();
    private static SAXReader sax=new SAXReader();
    private static List<BasKnowledgeInfo> basKnowledgeInfoList =new ArrayList<BasKnowledgeInfo>();
    private static List<BasPersonInfo> basPersonInfoList=new ArrayList<BasPersonInfo>();
    private static List<PerKnowledge> perKnowledgeList=new ArrayList<PerKnowledge>();
    private static BasKnowledgeInfoDaoImpl proknowimpl = new BasKnowledgeInfoDaoImpl();
    private static BasPersonInfoImpl basperimpl = new BasPersonInfoImpl();
    private static PerKnowledgeImpl perknowimpl = new PerKnowledgeImpl();
    private static SimpleDateFormat simpleDateFormatchange=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private static int a=1;

    public static class FormatEexception extends Exception
    {
        public FormatEexception(String msg)
        {
            super(msg);
        }
    }

    public static void getDocument(String flag,String url) throws FormatEexception {
        if (flag.equals("windows")) {
            System.setProperty("webdriver.chrome.driver",SpiderContant.chromeWindowsPath );
            baseKnowledge.setDriver(new ChromeDriver());
            baseKnowledge.getDriver().get(url);
        }else if(flag.equals("linux")){
            System.setProperty("phantomjs.binary.path",SpiderContant.phantomjsLinuxPath );
            baseKnowledge.setDriver(new PhantomJSDriver());
            baseKnowledge.getDriver().get(url);
        }else{
            throw new FormatEexception("You have to choose windows or linux");
        }
    }


    public static List<BasKnowledgeInfo> getData(OrganizeConfigure organizeConfigure,String orgflag) throws XpathSyntaxErrorException, ParseException, InterruptedException, FormatEexception, BasKnowledgeInfoDaoImpl.FormatEexception {
        List<BasKnowledgeInfo> test=new ArrayList<BasKnowledgeInfo>();
        for(int i=1;i>0;i++) {
            Thread.sleep(2000);
            if(organizeConfigure.getPage()!=0) {
                for (int pag = 1; pag < organizeConfigure.getPage(); pag++) {
                    JavascriptExecutor executornext = (JavascriptExecutor) baseKnowledge.getDriver();
                    executornext.executeScript(organizeConfigure.getNext().getText());
                }
            }
            JavascriptExecutor executorRoller = (JavascriptExecutor) baseKnowledge.getDriver();
            executorRoller.executeScript("$(window).scrollTop(30000)");

            baseKnowledge.setWebElement(baseKnowledge.getDriver().findElement(By.xpath("/html")));
            JXDocument jxDocument =new JXDocument(Jsoup.parse(baseKnowledge.getWebElement().getAttribute("outerHTML")));
            if(organizeConfigure.getMore() == null) {
                System.out.println("如果您想抓取更多页面， 请在配置文件中配置<more>标签");
            }
            if(organizeConfigure.getMore() != null && StringUtils.isNotEmpty(organizeConfigure.getMore().getText())){
                for(int more=1;more>0;more++) {
                    JavascriptExecutor executormore = (JavascriptExecutor) baseKnowledge.getDriver();
                    executormore.executeScript(organizeConfigure.getMore().getText());
                    Thread.sleep(1000);
                    baseKnowledge.setWebElement(baseKnowledge.getDriver().findElement(By.xpath("/html")));
                    JXDocument jxDocumentnow =new JXDocument(Jsoup.parse(baseKnowledge.getWebElement().getAttribute("outerHTML")));
                    if(jxDocumentnow.selOne(organizeConfigure.getMoreflag().getText())==null){
                        break;
                    }
                }
            }
            String author=null;
            String title=null;
            String cover=null;
            String ptimetest=null;
            String ptime=null;
            String authorurl=null;
            String Mosaic=null;
            String tag=null;
            String type = null;
            int fg=0;
            JavascriptExecutor executor = (JavascriptExecutor) baseKnowledge.getDriver();
            executor.executeScript("window.open('" + "https://www.baidu.com/" + "')");
            String handle = baseKnowledge.getDriver().getWindowHandle();
            for (String handles : baseKnowledge.getDriver().getWindowHandles()) {
                if (handles.equals(handle)) {
                    continue;
                }
                baseKnowledge.getDriver().switchTo().window(handles);
            }
            List<Object> childlist = jxDocument.sel(organizeConfigure.getChildLink().getText());


            List<Object> authorlist=null;
            List<Object> titlelist=null;
            List<Object> coverlist=null;
            List<Object> ptimetestlist=null;
            List<Object> ptimelist=new ArrayList<Object>();
            List<Object> authorurllist=null;
            List<Object> tagslist=null;
            List<Object> typeslist=null;

            if(StringUtils.isNotEmpty(organizeConfigure.getTagi().getText())) {
                if (jxDocument.selN(organizeConfigure.getTagi().getText()).size() > 0){
                    tagslist = jxDocument.sel(organizeConfigure.getTagi().getText());
                }
            }
            if(StringUtils.isNotEmpty(organizeConfigure.getTypei().getText())) {
                if (jxDocument.selN(organizeConfigure.getTypei().getText()).size() > 0){
                    typeslist = jxDocument.sel(organizeConfigure.getTypei().getText());
                }
            }
            if(StringUtils.isNotEmpty(organizeConfigure.getAuthorurli().getText())) {
                if (jxDocument.selN(organizeConfigure.getAuthorurli().getText()).size() > 0) {
                    authorurllist = jxDocument.sel(organizeConfigure.getAuthorurli().getText());
                }
            }
            if(StringUtils.isNotEmpty(organizeConfigure.getAuthori().getText())) {
                if (jxDocument.selN(organizeConfigure.getAuthori().getText()).size() > 0) {
                    authorlist = jxDocument.sel(organizeConfigure.getAuthori().getText());
                }
            }
            if(StringUtils.isNotEmpty(organizeConfigure.getTitlei().getText())) {
                if (jxDocument.selN(organizeConfigure.getTitlei().getText()).size() > 0) {
                    titlelist = jxDocument.sel(organizeConfigure.getTitlei().getText());
                }
            }
            if(StringUtils.isNotEmpty(organizeConfigure.getCoveri().getText())) {
                if (jxDocument.selN(organizeConfigure.getCoveri().getText()).size() > 0) {
                    coverlist = jxDocument.sel(organizeConfigure.getCoveri().getText());
                }
            }
            if(StringUtils.isNotEmpty(organizeConfigure.getPtimei().getText())) {
                if (jxDocument.selN(organizeConfigure.getPtimei().getText()).size() > 0) {
                    ptimetestlist = jxDocument.sel(organizeConfigure.getPtimei().getText());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(organizeConfigure.getPtimei().attributeValue("timeFormat"));
                    for (int size = 0; size < ptimetestlist.size(); size++) {
                        Date date = simpleDateFormat.parse((String) ptimetestlist.get(size).toString().replaceAll("\\D", " ").trim());
                        ptimelist.add(simpleDateFormatchange.format(date));
                    }
                }
            }


            for (Object obj : childlist) {
                try {
                    String main = null;
                    String child=null;
                    if(StringUtils.isNotEmpty(organizeConfigure.getChildLink().attributeValue("Mosaic"))) {
                        if(obj.toString().substring(0,4).equals("http")) {
                            child = (String) obj;
                        }else{
                            child=organizeConfigure.getChildLink().attributeValue("Mosaic") + obj;
                        }
                    }else{
                        child= (String) obj;
                    }
                    baseKnowledge.getDriver().get((String) child);
                    Thread.sleep(1000);
                    baseKnowledge.setWebElement(baseKnowledge.getDriver().findElement(By.xpath("/html")));
                    JXDocument jxDocumentChild = new JXDocument(Jsoup.parse(baseKnowledge.getWebElement().getAttribute("outerHTML")));
                    String puuid = UUID.randomUUID().toString();
                    String kuuid = UUID.randomUUID().toString();


                    if(StringUtils.isNotEmpty(organizeConfigure.getAuthori().getText())) {
                        if (jxDocument.selN(organizeConfigure.getAuthori().getText()).size() <= 1) {
                            author = (String) jxDocumentChild.selOne(organizeConfigure.getAuthori().getText()).toString().replace("作者：", "").replace("频道作者：","");
                        } else {
                            author = (String) authorlist.get(fg).toString().replace("作者：","").replace("频道作者：","");
                        }
                    }else{
                        author=null;
                    }
                    if(StringUtils.isNotEmpty(organizeConfigure.getAuthorurli().getText())) {
                        if (jxDocument.selN(organizeConfigure.getAuthorurli().getText()).size() <= 1) {
                            authorurl = (String) jxDocumentChild.selOne(organizeConfigure.getAuthorurli().getText());
                        } else {
                            authorurl = (String) authorurllist.get(fg);
                        }
                    }else{
                        authorurl= (String) child;
                    }
                    if(StringUtils.isNotEmpty(organizeConfigure.getTitlei().getText())) {
                        if (jxDocument.selN(organizeConfigure.getTitlei().getText()).size() <= 1) {
                            title = (String) jxDocumentChild.selOne(organizeConfigure.getTitlei().getText());
                        } else {
                            title = (String) titlelist.get(fg);
                        }
                    }else{
                        title=null;
                    }
                    if(StringUtils.isNotEmpty(organizeConfigure.getCoveri().getText())) {
                        if (jxDocument.selN(organizeConfigure.getCoveri().getText()).size() <= 1) {
                            if(StringUtils.isNotEmpty(organizeConfigure.getCoveri().attributeValue("Mosaic"))) {
                                cover = (String) organizeConfigure.getCoveri().attributeValue("Mosaic")+jxDocumentChild.selOne(organizeConfigure.getCoveri().getText());
                            }else{
                                cover=null;
                            }
                        } else {
                            if(StringUtils.isNotEmpty((String) coverlist.get(fg))) {
                                cover = (String) coverlist.get(fg) ;
                            }else{
                                cover=null;
                            }
                        }
                    }else{
                        cover=null;
                    }
                    if(StringUtils.isNotEmpty(organizeConfigure.getPtimei().getText())) {
                        if (jxDocument.selN(organizeConfigure.getPtimei().getText()).size() <= 1) {
                            ptimetest = jxDocumentChild.selOne(organizeConfigure.getPtimei().getText()).toString().replaceAll("\\D", " ").trim();
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(organizeConfigure.getPtimei().attributeValue("timeFormat"));
                            Date date = simpleDateFormat.parse(ptimetest);
                            ptime = simpleDateFormatchange.format(date);
                        } else {
                            ptime = (String) ptimelist.get(fg);
                        }
                    }else{
                        ptime=null;
                    }

                    String source = organizeConfigure.getSourcei().getText();

                    System.out.println(author);
                    System.out.println(title);
                    System.out.println(cover);
                    System.out.println(ptime);
                    System.out.println(source);

                    if(StringUtils.isNotEmpty(organizeConfigure.getTypei().getText())) {
                        if (jxDocument.selN(organizeConfigure.getTypei().getText()).size() <= 1) {
                            List<Object> typelist = jxDocumentChild.sel(organizeConfigure.getTypei().getText());
                            for (Object objtype : typelist) {
                                type = (type + "," + objtype).replace("null,", "");
                            }
                        } else {
                            type = (type + "," + typeslist.get(fg)).replace("null,", "").replace(" ", ",");
                        }
                    }else{
                        type=null;
                    }

                    if(StringUtils.isNotEmpty(organizeConfigure.getTagi().getText())) {
                        if (jxDocument.selN(organizeConfigure.getTagi().getText()).size() <= 1) {
                            List<Object> taglist = jxDocumentChild.sel(organizeConfigure.getTagi().getText());
                            for (Object objtag : taglist) {
                                tag = (tag + "," + objtag).replace("null,", "");
                            }
                        } else {

                            tag = (tag + "," + tagslist.get(fg)).replace("null,", "");

                        }
                    }else{
                        tag=null;
                    }
                    if(StringUtils.isNotEmpty(organizeConfigure.getChildnextflagi().getText())) {
                        for(int x=1;x>0;x++) {
                            baseKnowledge.setWebElement(baseKnowledge.getDriver().findElement(By.xpath("/html")));
                            JXDocument jxDocumentmain =new JXDocument(Jsoup.parse(baseKnowledge.getWebElement().getAttribute("outerHTML")));
                            List<JXNode> mainlist = jxDocumentmain.selN(organizeConfigure.getMaini().getText());
                            for (JXNode objmain : mainlist) {
                                if (StringUtils.isNotEmpty(objmain.getElement().text())) {
                                    main = (main + "\r\n<p>" + objmain.getElement().text() + "</p>").replace("null\r\n", "").replace(Jsoup.parse("&nbsp;").text(), "");
                                }
                                if (objmain.sel(organizeConfigure.getMainipic().getText()).size() > 0) {
                                    if(StringUtils.isNotEmpty(organizeConfigure.getMainipic().attributeValue("Mosaic"))){
                                        main=(main + "\r\n<img src=\"" + organizeConfigure.getMainipic().attributeValue("Mosaic")+objmain.sel(organizeConfigure.getMainipic().getText()).get(0) + "\">");
                                    }else {
                                        main = (main + "\r\n<img src=\"" + objmain.sel(organizeConfigure.getMainipic().getText()).get(0) + "\">");
                                    }
                                }
                            }
                            if(jxDocumentmain.selOne(organizeConfigure.getChildnextflagi().getText())==null){
                                break;
                            }else if(StringUtils.isEmpty(jxDocumentmain.selOne(organizeConfigure.getChildnextflagi().getText()).toString())){
                                break;
                            }
                            JavascriptExecutor executorChildnext = (JavascriptExecutor) baseKnowledge.getDriver();
                            executorChildnext.executeScript(organizeConfigure.getChildnexti().getText());
                        }
                    }else{
                        List<JXNode> mainlist = jxDocumentChild.selN(organizeConfigure.getMaini().getText());
                        for (JXNode objmain : mainlist) {
                            if (StringUtils.isNotEmpty(objmain.getElement().text())) {
                                main = (main + "\r\n<p>" + objmain.getElement().text() + "</p>").replace("null\r\n", "").replace(Jsoup.parse("&nbsp;").text(), "");
                            }
                            if (objmain.sel(organizeConfigure.getMainipic().getText()).size() > 0) {
                                if(StringUtils.isNotEmpty(organizeConfigure.getMainipic().attributeValue("Mosaic"))){
                                    main=(main + "\r\n<img src=\"" + organizeConfigure.getMainipic().attributeValue("Mosaic")+objmain.sel(organizeConfigure.getMainipic().getText()).get(0) + "\">");
                                }else {
                                    main = (main + "\r\n<img src=\"" + objmain.sel(organizeConfigure.getMainipic().getText()).get(0) + "\">");
                                }
                            }
                        }
                    }


                    System.out.println(main);
                    System.out.println(tag);
                    System.out.println(type);
                    dataClean(title, ptime, type, cover, tag, author, main, puuid, kuuid, (String) child, source, authorurl);
                    System.out.println(a + "+" + i);
                    a++;
                    fg++;
                    System.out.println("---------------------------------");
                }catch (Exception e){
                    e.printStackTrace();
                }
            }


            test=storeToDatebase(organizeConfigure.getFlag(), author,orgflag);

            String handle2 = baseKnowledge.getDriver().getWindowHandle();
            baseKnowledge.getDriver().close();
            Thread.sleep(2000);
            for (String handles : baseKnowledge.getDriver().getWindowHandles()) {
                if (handles.equals(handle2)) {
                    continue;
                }
                baseKnowledge.getDriver().switchTo().window(handles);
            }

            if(StringUtils.isEmpty(organizeConfigure.getNextflagi().getText())){
                break;
            }else if(jxDocument.selOne(organizeConfigure.getNextflagi().getText())==null){
                break;
            }else if(StringUtils.isEmpty(jxDocument.selOne(organizeConfigure.getNextflagi().getText()).toString())){
                break;
            }
            JavascriptExecutor executornext = (JavascriptExecutor) baseKnowledge.getDriver();
            executornext.executeScript(organizeConfigure.getNext().getText());
            String handle3 = baseKnowledge.getDriver().getWindowHandle();
            for (String handles : baseKnowledge.getDriver().getWindowHandles()) {
                if (handles.equals(handle3)) {
                    continue;
                }else {
                    baseKnowledge.getDriver().close();
                    baseKnowledge.getDriver().switchTo().window(handles);
                }
            }
        }
        return test;
    }


    /**
     * 解析配置文件
     * @param flag
     * @param element
     * @param page
     * @param orgflag
     * @throws FormatEexception
     * @throws DocumentException
     * @throws ParserConfigurationException
     * @throws XpathSyntaxErrorException
     * @throws IOException
     * @throws dao.impl.BasKnowledgeInfoDaoImpl.FormatEexception
     * @throws InterruptedException
     * @throws ParseException
     */
    public static void getElements(String flag,String element,int page,String orgflag) throws FormatEexception, DocumentException, ParserConfigurationException, XpathSyntaxErrorException, IOException, BasKnowledgeInfoDaoImpl.FormatEexception, InterruptedException, ParseException {
        InputStream inputStream=new FileInputStream(SpiderUtils.class.getClassLoader().getResource("SpiderUtils/BasKnowledgePattern.xml").getFile());
        baseKnowledge.setDocsax(sax.read(inputStream));
        baseKnowledge.setRoot(baseKnowledge.getDocsax().getRootElement());//获取根元素
        Element childElement = baseKnowledge.getRoot().element(element);
        Element classifiedLink=childElement.element("urls");
        List<Element> classifiedlist=classifiedLink.elements();
        Element childLink=childElement.element("childLink");
        Element next=childElement.element("next");
        Element nextflagi=childElement.element("nextflag");

        Element authori = childElement.element("author");
        Element titlei = childElement.element("title");
        Element coveri = childElement.element("cover");
        Element tagi = childElement.element("tag");
        Element maini = childElement.element("main");
        Element mainipic = childElement.element("mainpic");
        Element ptimei = childElement.element("ptime");
        Element typei = childElement.element("type");
        Element sourcei = childElement.element("source");
        Element authorurli = childElement.element("authorurl");
        Element childnexti=childElement.element("childnext");
        Element childnextflagi=childElement.element("childnextflag");
        Element morei=childElement.element("more");
        Element moreflag=childElement.element("moreflag");

        OrganizeConfigure organizeConfigure=new OrganizeConfigure();
        organizeConfigure.setPage(page);
        organizeConfigure.setMore(morei);
        organizeConfigure.setMoreflag(moreflag);
        organizeConfigure.setChildLink(childLink);
        organizeConfigure.setNext(next);
        organizeConfigure.setNextflagi(nextflagi);
        organizeConfigure.setAuthori(authori);
        organizeConfigure.setTitlei(titlei);
        organizeConfigure.setCoveri(coveri);
        organizeConfigure.setTagi(tagi);
        organizeConfigure.setMaini(maini);
        organizeConfigure.setMainipic(mainipic);
        organizeConfigure.setPtimei(ptimei);
        organizeConfigure.setTypei(typei);
        organizeConfigure.setSourcei(sourcei);
        organizeConfigure.setAuthorurli(authorurli);
        organizeConfigure.setChildnexti(childnexti);
        organizeConfigure.setChildnextflagi(childnextflagi);
        organizeConfigure.setFlag(flag);

        /**
         * TODO 抽离代码
         */

        int a=1;
        for(Element ele:classifiedlist){
            SpiderUtils.getDocument(flag, ele.getText().trim());
            getData(organizeConfigure,orgflag);
            inputStream.close();
            baseKnowledge.getDriver().close();
        }
        inputStream.close();
        baseKnowledge.getDriver().close();
        System.exit(0);
    }

    public static void getElementsAdd(String flag,int page,String orgflag) throws BasKnowledgeInfoDaoImpl.FormatEexception, FormatEexception, InterruptedException, XpathSyntaxErrorException, ParseException, FileNotFoundException, DocumentException {
        baseKnowledge.setInputStream(new FileInputStream(SpiderUtils.class.getClassLoader().getResource("SpiderUtils/BasKnowledgePattern.xml").getFile()));
        baseKnowledge.setDocsax(sax.read(baseKnowledge.getInputStream()));
        baseKnowledge.setRoot(baseKnowledge.getDocsax().getRootElement());//获取根元素
        List<Element> childElements = baseKnowledge.getRoot().elements("spider");
        for(Element childElement:childElements) {
            Element classifiedLink=childElement.element("urls");
            List<Element> classifiedlist=classifiedLink.elements();
            Element childLink=childElement.element("childLink");
            Element next=childElement.element("next");
            Element nextflagi=childElement.element("nextflag");

            Element authori = childElement.element("author");
            Element titlei = childElement.element("title");
            Element coveri = childElement.element("cover");
            Element tagi = childElement.element("tag");
            Element maini = childElement.element("main");
            Element mainipic = childElement.element("mainpic");
            Element ptimei = childElement.element("ptime");
            Element typei = childElement.element("type");
            Element sourcei = childElement.element("source");
            Element authorurli = childElement.element("authorurl");
            Element childnexti=childElement.element("childnext");
            Element childnextflagi=childElement.element("childnextflag");
            Element morei=childElement.element("more");
            Element moreflag=childElement.element("moreflag");

            OrganizeConfigure organizeConfigure=new OrganizeConfigure();
            organizeConfigure.setPage(page);
            organizeConfigure.setMore(morei);
            organizeConfigure.setMoreflag(moreflag);
            organizeConfigure.setChildLink(childLink);
            organizeConfigure.setNext(next);
            organizeConfigure.setNextflagi(nextflagi);
            organizeConfigure.setAuthori(authori);
            organizeConfigure.setTitlei(titlei);
            organizeConfigure.setCoveri(coveri);
            organizeConfigure.setTagi(tagi);
            organizeConfigure.setMaini(maini);
            organizeConfigure.setMainipic(mainipic);
            organizeConfigure.setPtimei(ptimei);
            organizeConfigure.setTypei(typei);
            organizeConfigure.setSourcei(sourcei);
            organizeConfigure.setAuthorurli(authorurli);
            organizeConfigure.setChildnexti(childnexti);
            organizeConfigure.setChildnextflagi(childnextflagi);
            organizeConfigure.setFlag(flag);
            for (Element ele : classifiedlist) {
                SpiderUtils.getDocument(flag, ele.getText().trim());
                getData(organizeConfigure,orgflag);
            }
        }
    }



    public static void dataClean(String title,String ptime,String type,String cover,String tag,String author,String main,String puuid,String kuuid,String url,String source,String authorurl) throws BasKnowledgeInfoDaoImpl.FormatEexception, FormatEexception {
        BasKnowledgeInfo basKnowledgeInfo = new BasKnowledgeInfo();
        basKnowledgeInfo.setTitle(title);
        basKnowledgeInfo.setPtime(ptime);
        basKnowledgeInfo.setType(type);
        basKnowledgeInfo.setCover(cover);
        basKnowledgeInfo.setTag(tag);
        basKnowledgeInfo.setAuthor(author);
        basKnowledgeInfo.setMain(main);
        basKnowledgeInfo.setUrl(url);
        basKnowledgeInfo.setSource(source);
        basKnowledgeInfo.setUuid(kuuid);
        basKnowledgeInfoList.add(basKnowledgeInfo);

        if(StringUtils.isNotEmpty(author)) {
            PerKnowledge perKnow = new PerKnowledge();
            perKnow.setName(author);
            perKnow.setKname(title);
            perKnow.setRtype("原作者");
            perKnow.setPuuid(puuid);
            perKnow.setKuuid(kuuid);
            perKnow.setSource(source);
            perKnowledgeList.add(perKnow);

            BasPersonInfo basPerson = new BasPersonInfo();
            basPerson.setUuid(puuid);
            basPerson.setName(author);
            basPerson.setSource(source);
            basPerson.setUrl(authorurl);
            basPersonInfoList.add(basPerson);
        }

    }

    public static List storeToDatebase(String flag,String author,String orgflag) throws FormatEexception, BasKnowledgeInfoDaoImpl.FormatEexception, ParseException {
        List<BasKnowledgeInfo> basKnowledgeInfoListq =new ArrayList<BasKnowledgeInfo>();
        if (flag.equals("windows")) {
            Map map = proknowimpl.insertBatchAutoDedup(basKnowledgeInfoList, basPersonInfoList, perKnowledgeList);
            basKnowledgeInfoListq = (List<BasKnowledgeInfo>) map.get(5);
            if (((List<Integer>) map.get(4)).get(0) != 0&&StringUtils.isNotEmpty(author)) {
                basperimpl.insertBatch((List<BasPersonInfo>) map.get(1));
                perknowimpl.insertBatch((List<PerKnowledge>) map.get(3));
            }
            if(orgflag.equals("no")) {
                basKnowledgeInfoList.clear();
            }
            basPersonInfoList.clear();
            perKnowledgeList.clear();
        } else if (flag.equals("linux")) {
            Map map = proknowimpl.insertBatchAutoDedup(basKnowledgeInfoList, basPersonInfoList, perKnowledgeList);
            if (((List<String>) map.get(2)).get(0).equals("false")) {
                System.exit(0);
            }
            if (((List<Integer>) map.get(4)).get(0) != 0&&StringUtils.isNotEmpty(author)) {
                basperimpl.insertBatch(basPersonInfoList);
                perknowimpl.insertBatch(perKnowledgeList);
            }
            basKnowledgeInfoList.clear();
            basPersonInfoList.clear();
            perKnowledgeList.clear();
        } else {
            throw new FormatEexception("You have to choose windows or linux");
        }
        return basKnowledgeInfoListq;
    }

    public static void storeBugdata(String key,String value,String uuid){
        BugData bugData=new BugData();
        bugData.setKey(key);
        bugData.setValue(value);
        bugData.setUuid(uuid);
        bugDataimpl.insert(bugData);
    }

}
