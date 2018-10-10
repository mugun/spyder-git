package spyderJava.spy;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.FileLockInterruptionException;
import java.nio.channels.OverlappingFileLockException;
import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import com.gargoylesoftware.htmlunit.*;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import spyderJava.spy.spyderMulti;
public class crawl {
	private String baseUrl;
	//主要的url
	private int page;
	//需要爬去的页数
	//private String tag;
	//用来爬去的标记
	//private int jump;
	//是否需要跳转
	private String charset;
	//字符集
	private String filename;
	//用于存储的文件名

	private static int MAXIMUM_POOL_SIZE = 4;
	private static int CORE_POOL_SIZE = 2;

	public String pattern="(http|ftp|https):\\/\\/[\\w\\-_]+(\\.[\\w\\-_]+)+([\\w\\-\\.,@?^=%&amp;:/~\\+#]*[\\w\\-\\@?^=%&amp;/~\\+#])?";
	public crawl(String baseUrl,int page,String charset,String filename) {
		this.baseUrl=baseUrl;
		this.page=page;
		//this.tag=tag;
		//this.jump=jump;
		this.charset=charset;
		this.filename=filename;
	}
	public BlockingQueue<String> spyderFront(String bodyFrame) throws IOException{

		BlockingQueue<String> jumpUrl=new LinkedBlockingQueue<String>();
		final WebClient chrome=new WebClient(BrowserVersion.CHROME);
		chrome.getOptions().setThrowExceptionOnScriptError(false);
		chrome.getOptions().setThrowExceptionOnFailingStatusCode(false);
		chrome.getOptions().setCssEnabled(false);
		chrome.getOptions().setJavaScriptEnabled(false);
		chrome.setAjaxController(new NicelyResynchronizingAjaxController());
		for(int pageNow=1;pageNow<=this.page;pageNow++) {
			String baseUrl=this.baseUrl+"page="+pageNow+this.charset;
			//得到得到当前需要查找的URL
			/*Document doc=Jsoup.connect(baseUrl).get();
			List<Element> sourUrl=doc.select(bodyFrame);
			for(int i=0;i<sourUrl.size();i++) {
				//遍历锁选择出来的内容
				Elements elee=sourUrl.get(i).select("a");
				jumpUrl.add(elee.attr("abs:href"));
			}*///被banUA了emmmm
			HtmlPage page=chrome.getPage(baseUrl);
			chrome.waitForBackgroundJavaScript(1000);
			String pageXml=page.asXml();
			Document doc=Jsoup.parse(pageXml);
//			List<Element> sourUrl=doc.select(bodyFrame);
			List<Element> sourUrl=doc.getElementsByClass(bodyFrame);
			for(int i=0;i<sourUrl.size();i++) {
				//遍历锁选择出来的内容
				Elements elee=sourUrl.get(i).select("a");
				if(!elee.isEmpty()) {
					jumpUrl.add("https://www.sogou.com" + elee.get(0).attr("href"));
				}
			}
		}
		return jumpUrl;
	}
	
	public List<String> spyderMain(Queue<String> jumpUrl,String tag) throws IOException{
		List<String> result=new ArrayList<String>();
		final WebClient chrome=new WebClient(BrowserVersion.CHROME);
		chrome.getOptions().setThrowExceptionOnScriptError(false);
		chrome.getOptions().setThrowExceptionOnFailingStatusCode(false);
		chrome.getOptions().setCssEnabled(false);
		chrome.getOptions().setJavaScriptEnabled(true);
		chrome.setAjaxController(new NicelyResynchronizingAjaxController());
		jumpUrl.poll();
		while(!jumpUrl.isEmpty()) {
			
			HtmlPage page=chrome.getPage(jumpUrl.poll());
			chrome.waitForBackgroundJavaScript(1000);
			String pageXml=page.asXml();
			Document doc=Jsoup.parse(pageXml);
			String Head=doc.select("head").text()+"\n";
			//获取标题
			Elements ele=doc.select(tag);
			List<Element> mainEle=ele.select("p");
			for(int i=0;i<mainEle.size();i++) {
				Head=Head+mainEle.get(i).text()+"\n";
			}
			result.add(Head);
		}
		return result;
	}
	public void SpyMainMulti(BlockingQueue<String> jumpUrl,String tag) throws IOException {
		//关于多线程的方法
		BlockingQueue<String> temp=new LinkedBlockingQueue<String>();

//		jumpUrl.poll();
		ThreadPoolExecutor exec = new ThreadPoolExecutor(CORE_POOL_SIZE,MAXIMUM_POOL_SIZE,2000,TimeUnit.MILLISECONDS, new ArrayBlockingQueue<Runnable>(100),new ThreadPoolExecutor.CallerRunsPolicy());
		initTempFiles();
		while(!jumpUrl.isEmpty()) {
		//for(int i=0;i<10;i++) {
			exec.execute(new spyderMulti(jumpUrl,tag, this.filename));
			//如果需要测试，请把jumpUrl改成temp并将上面的注释打开
		}
		exec.shutdown();
		//fw.close();
	}
	
	public void printRes(List<String> result)throws IOException{
		FileWriter fw=new FileWriter(this.filename);
		for(int i=0;i<result.size();i++) {
			fw.write(result.get(i));
		}
		fw.close();
	}

	private void initTempFiles() {
		for(int i = 0; i< MAXIMUM_POOL_SIZE; i++) {
			try {
				new File(this.filename + "" + i).createNewFile();
			}
			catch (Exception e) {
				e.printStackTrace();
				System.exit(233);
			}
		}
	}

	public static String getFilenameNotLocked(String filename) {
		for(int i = 0; i< MAXIMUM_POOL_SIZE; i++) {
			try {
				FileChannel channel = new RandomAccessFile(filename + "" + i, "rw").getChannel();
				FileLock lock = channel.tryLock();
				if(lock != null) {
					lock.release();
					return filename + "" + i;
				}
			}
			catch (FileNotFoundException e) {
				try {
					new File(filename + "" + i).createNewFile();
				}
				catch (IOException error) {
					System.out.println(error.getMessage());
				}
			}
			catch (IOException e) {
				System.out.println(e.getMessage());
			}
			catch (OverlappingFileLockException e) {
				System.out.println(e.getMessage());
			}
		}

		return null;
	}
}
