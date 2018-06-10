package com.njuwebclass.csplus.Crawler;

import org.apache.http.Consts;
import org.apache.http.NameValuePair;
import org.apache.http.client.CookieStore;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Crawler {
    private static String loginURL = "http://cslabcms.nju.edu.cn/login/index.php";
    private static String testURL = "http://cslabcms.nju.edu.cn/local/pyclasses/mycalendar.php?my=1";

    private static CloseableHttpClient httpClient = null;
    private static HttpClientContext context = null;
    private static CookieStore cookieStore = null;
    private static RequestConfig globalConfig = null;
    private static CloseableHttpResponse res = null;

    static {
        // 全局请求设置
        globalConfig = RequestConfig.custom().setCookieSpec(CookieSpecs.STANDARD).build();
        cookieStore = new BasicCookieStore();
        context = HttpClientContext.create();
        context.setCookieStore(cookieStore);

        // httpClient
        httpClient= HttpClients.custom()
                .setDefaultRequestConfig(globalConfig)
                .setDefaultCookieStore(cookieStore)
                .build();
    }

    private static List<Info> getAllClass() throws Exception{
        List<Info> ans = new ArrayList<>();
        String mainPageUrl = "http://cslabcms.nju.edu.cn/local/pyclasses/mycalendar.php?my=1";

        HttpGet getMainPage = new HttpGet(mainPageUrl);
        res = httpClient.execute(getMainPage,context);
        String mainPage = EntityUtils.toString(res.getEntity());
        res.close();

        Document mainDoc = Jsoup.parse(mainPage);
        Elements ele = mainDoc.getElementsByClass("fheader");
        for(Element s:ele){
            //System.out.println(s.text());
            //System.out.println(s.attr("aria-controls").split("_")[2]);
            ans.add(new Info(s.text(),"http://cslabcms.nju.edu.cn/course/view.php?id="+s.attr("aria-controls").split("_")[2]));
        }
        return ans;
    }

    private static void showInfoList(List<Info> infoList){
        for(Info info : infoList){
            info.show();
        }
    }

    private static void showDDLInfo(List<DDLInfo> ddlInfo){
        for(DDLInfo d:ddlInfo) d.show();
    }

    private static void showInfoUpdate(List<InfoUpdate> infoUpdates){
        for(InfoUpdate infoUpdate: infoUpdates) infoUpdate.show();
    }

    private static List<String> getAllTaskURL(String url) throws Exception{
        HttpGet getMainPage = new HttpGet(url);
        res = httpClient.execute(getMainPage,context);
        String urlPage = EntityUtils.toString(res.getEntity());
        res.close();

        String pattern = "http://cslabcms\\.nju\\.edu\\.cn/mod/assign/view\\.php\\?id=.*?(?=\")";
        List<String> taskUrl = matchStringByRE(urlPage,pattern);

     //   System.out.println(taskUrl);
        return taskUrl;
    }

    private static List<String> getAllTaskName(String url) throws Exception{
        HttpGet URL = new HttpGet(url);
        res = httpClient.execute(URL,context);
        String urlPage = EntityUtils.toString(res.getEntity());
        res.close();

        String pattern = "(?<=<i class=\"fa fa-file-text-o fa-lg\" style=\"color: #6091ba;\"></i> <span class=\"instancename\">).*?(?=<)";
        List<String> taskName = matchStringByRE(urlPage,pattern);

        return taskName;
    }

    private static List<String> getAllResName(String url) throws  Exception{
        HttpGet URL = new HttpGet(url);
        res = httpClient.execute(URL,context);
        String urlPage = EntityUtils.toString(res.getEntity());
        res.close();

        String pattern = "(?<=<i class=\"fa fa-file fa-lg\" style=\"color: #6091ba;\"></i> <span class=\"instancename\">).*?(?=<)";
        List<String> resName = matchStringByRE(urlPage,pattern);

        return resName;
    }

    public static boolean login(String username,String password) throws Exception{
        HttpGet getLogin = new HttpGet(loginURL);
        httpClient.execute(getLogin,context);   //获取常规Cookie

        //构造post数据
        List<NameValuePair> valuePairs = new LinkedList<NameValuePair>();
        valuePairs.add(new BasicNameValuePair("username",username));
        valuePairs.add(new BasicNameValuePair("password",password));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(valuePairs, Consts.UTF_8);
        entity.setContentType("application/x-www-form-urlencoded");

        HttpPost postLogin = new HttpPost(loginURL);
        postLogin.setEntity(entity);
        res = httpClient.execute(postLogin,context);    //获取该用户名密码下的Cookie
        res.close();  // 这里不可少，不知道为什么

        HttpGet test = new HttpGet(testURL);
        res = httpClient.execute(test,context);
        String content = EntityUtils.toString(res.getEntity());
        res.close();

        if(content!=null&&content.indexOf("<label for=\"username\">用户名</label>")==-1){
            return true;
        }
        return false;
    }

    public static List<InfoUpdate> getUpdateInfo(String filename) throws Exception{
        List<InfoUpdate> ans = new ArrayList<>();

        // 读取文件内容，获取之前访问时动态
        List<InfoUpdate> beforeInfo = new ArrayList<>();
        File file = new File(filename);
        if(!file.exists()) file.createNewFile();

        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        while ((line=reader.readLine())!=null){
            String[] tmp = line.split("---");
            beforeInfo.add(new InfoUpdate(tmp[0],tmp[1],tmp[2]));
        }

        // 获取当前动态
        List<Info> allClass = getAllClass();
        for(Info Class: allClass){
            List<String> allTask = getAllTaskName(Class.getUrl());
            List<String> allRes = getAllResName(Class.getUrl());

            for(String task:allTask){
                InfoUpdate infoUpdate= new InfoUpdate(Class.getName(),task,"作业");
                if(!beforeInfo.contains(infoUpdate)) ans.add(infoUpdate);
            }

            for (String res : allRes){
                InfoUpdate infoUpdate= new InfoUpdate(Class.getName(),res,"资源");
                if(!beforeInfo.contains(infoUpdate)) ans.add(infoUpdate);
            }
        }

        //把当前信息保存到文件
        Writer out = new FileWriter(file);
        for(InfoUpdate infoUpdate:beforeInfo){
            out.write(infoUpdate.getClassName()+"---"+infoUpdate.getHomework()+"---"+infoUpdate.getType()+"\n");
        }
        for(InfoUpdate infoUpdate:ans){
            out.write(infoUpdate.getClassName()+"---"+infoUpdate.getHomework()+"---"+infoUpdate.getType()+"\n");
        }
        out.close();

        return ans;
    }

    public static List<DDLInfo> getDDLInfo(Date currentDate)throws Exception{
        List<DDLInfo> ans = new ArrayList<>();

        List<Info> allClass = getAllClass();

        for(Info Class:allClass){
            List<String> allTask = getAllTaskURL(Class.getUrl());
            for(String task: allTask){
                HttpGet getTask = new HttpGet(task);
                res = httpClient.execute(getTask,context);
                String urlPage = EntityUtils.toString(res.getEntity());
                res.close();

                Document doc = Jsoup.parse(urlPage);
                Elements ele = doc.getElementsByTag("a");
                String ddlName =null;
                for(Element e: ele){
                    if(e.attr("title").equals("作业")){
                        ddlName =e.text();
                        break;
                    }
                }
            //    System.out.println(ddlName);

                // 确定ddl时间
                String pattern = "(?<=提交截止时间</td>\n<td class=\"cell c1 lastcol\" style=\"\">).*?(?=<)";
                List<String> ddlTimeInString = matchStringByRE(urlPage,pattern);
                DateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 E HH:mm");
                Date date = dateFormat.parse(ddlTimeInString.get(0));
                if(date.after(currentDate))
                    ans.add(new DDLInfo(Class.getName(),ddlName,date));
            }
        }

        Collections.sort(ans);
        return ans;
    }

    private static List<String> matchStringByRE(String str, String pattern){
        List<String> ans = new ArrayList<>();
        Pattern p = Pattern.compile(pattern);
        Matcher m = p.matcher(str);
        while(m.find()){
            ans.add(m.group());
        }
        return ans;
    }

}
