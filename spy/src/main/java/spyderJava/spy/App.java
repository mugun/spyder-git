package spyderJava.spy;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.io.FileWriter;
import java.io.IOException;
import spyderJava.spy.crawl;
import spyderJava.spy.fileCombiner;
/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )throws IOException{
    	//需要进行爬虫的基本的url
    	String baseUrl="https://www.sogou.com/sogou?site=news.qq.com&query=%E5%85%BB%E8%80%81%E6%8E%A8%E8%8D%90&pid=sogou-wsse-b58ac8403eb9cf17-0004&duppid=1&idx=f&";
    	//需要爬的页数
    	//String pageTem=args[1];
    	//int page=Integer.valueOf(pageTem);
    	int page=1;
    	//需要进行需要进行提取的文章标签
    	//String tag=args[2];
    	//程序是否有url 跳转？有1无0
    	//String jump=args[3];
    	//需要保存到文件名字
    	String fileName="F:/workspace/spy/temp/result.txt";
    	String tempPath="F:/workspace/spy/temp/";
    	String outputPath="F:/workspace/spy/Output/result.txt";
    	String charset="&ie=utf8";
    	fileCombiner fcb=new fileCombiner(tempPath,outputPath);
    	fcb.deleteFile();
        crawl spy=new crawl(baseUrl,page,charset,fileName);
        BlockingQueue que=spy.spyderFront("h3");
        //List lis=spy.spyderMain(que, "#ArticleCnt");
       // spy.printRes(lis);
        spy.SpyMainMulti(que, "#ArticleCnt");
        
        //System.exit(0);
        //fileCombiner fcb=new fileCombiner(tempPath,outputPath);
        fcb.combiner();
    }

}
