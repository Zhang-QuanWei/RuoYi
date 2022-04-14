package com.ruoyi.common.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class JsoupUtils {

    public static String USER_AGENT = "User-Agent";
    public static String USER_AGENT_VALUE = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/100.0.4896.60 Safari/537.36";

    /**
     * 判断爬取网址是否正常
     * @param url
     * @return
     */
    public static int isConnection(String url){

        int status = 404;
        if (StringUtils.isEmpty(url)){
            return status;
        }


        try {
            URL novelUrl = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) novelUrl.openConnection();
            connection.setUseCaches(false);
            //设置超时时间
            connection.setConnectTimeout(3000);
            status = connection.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return status;

    }

    /**
     * 获取当前页面的DOM对象
     * @param url
     * @return
     */
    public static Document getDoc(String url){
        Document document = null;
        //模拟浏览器获取dom对象
        try {
            document = Jsoup.connect(url)
                    .header(USER_AGENT, USER_AGENT_VALUE)
                    .timeout(5000)
                    .get();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return document;
    }

    /**
     * 切割笔趣阁小说信息开头标签
     * @return
     */
    public static String subStrStart(String str){
        if (StringUtils.isNotEmpty(str)){
            return str.substring(str.indexOf("：")+1);
        }

        return null;
    }

    /**
     * 切割笔趣阁小说简介字符串
     * @param str
     * @return
     */
    public static String subStrFull(String str){

        if (StringUtils.isNotEmpty(str)){

            String temp = str.substring(3,str.indexOf("作者："));
            if (temp.indexOf("展开>> ") != (-1)){
                //存在展开按钮
                str = temp.substring(0,temp.length()-5);
                return str;
            }
            //不存在“展开”按钮
            return temp;

        }

        return null;
    }

    /**
     * 切割小说章节链接
     * @param bookUrl
     * @param bookChapterHref
     * @return
     */
    public static String sunStringHref(String bookUrl, String bookChapterHref) {

        if (StringUtils.isEmpty(bookUrl) || StringUtils.isEmpty(bookChapterHref)){
            return null;
        }

        return bookUrl.substring(0, bookUrl.length() - 10) + bookChapterHref;

    }

    /**
     * 测试方法
     * @param args
     */
    public static void main(String[] args) {
        Document document;

        {
            try {
                document = Jsoup.connect("https://www.shuquge.com/category/1_1.html").get();

                System.out.println(document.select("span.s2 a").text());
                Elements elements = document.select("span.s2 a");
                for (Element element : elements){
                    System.out.println(element.attr("href"));
                }


            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
