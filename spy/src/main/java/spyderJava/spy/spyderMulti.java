package spyderJava.spy;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.http.conn.HttpHostConnectException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.concurrent.*;
import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import static java.lang.Thread.sleep;


class spyderMulti implements Runnable{
	private WebClient chrome;
	private BlockingQueue<String> url;
	private String tag;
	//private FileWriter fw;
	private static int num=0;
	private String filePath;
	public  spyderMulti(BlockingQueue<String> url,String tag,String filePath) {
		//this.chrome=chrome;
		this.url=url;
		this.tag=tag;
		//this.fw=fw;
		this.filePath=filePath;
	}

	public void run() {
		while(true) {
			if(crawl.getFilenameNotLocked(filePath) != null) {
//				System.out.println(crawl.getFilenameNotLocked(filePath));
				break;
			}
			else {
				try {
					sleep(1000);
				} catch (Exception e){
					e.printStackTrace();
				}
			}
		}
		if(!this.url.isEmpty()) {
			try {
				WebClient chrome=new WebClient(BrowserVersion.CHROME);
				chrome.getOptions().setThrowExceptionOnScriptError(false);
				chrome.getOptions().setThrowExceptionOnFailingStatusCode(false);
				chrome.getOptions().setCssEnabled(false);
				chrome.getOptions().setJavaScriptEnabled(false);
				chrome.setAjaxController(new NicelyResynchronizingAjaxController());
//				String filePath=this.filePath+spyderMulti.num;
//				spyderMulti.num++;
				//File fw1=new File(filePath);
//				FileWriter fw=new FileWriter(filePath);
				FileWriter fw=new FileWriter(crawl.getFilenameNotLocked(filePath), true);
				String url=this.url.poll();
				System.out.println(url);
				HtmlPage page = null;
				page = chrome.getPage(url);
				chrome.waitForBackgroundJavaScript(1000);
				String pageXml=page.asXml();
				Document doc=Jsoup.parse(pageXml);
				String Head=doc.select("head").text()+"\n";
				//获取标题
				Elements ele=doc.select(this.tag);
				List<Element> mainEle=ele.select("p");
				for(int i=0;i<mainEle.size();i++) {
					Head=Head+mainEle.get(i).text()+"\n";
				}
				//writeFile(Head,this.fw);
				fw.write(Head);
				fw.close();
				chrome.close();
			} catch (FailingHttpStatusCodeException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			} catch (MalformedURLException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(e.getMessage());
			} catch (Exception e ){
				System.out.println(e.getMessage());
			}
			
		}
	}
}
