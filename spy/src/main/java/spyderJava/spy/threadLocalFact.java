package spyderJava.spy;
import java.util.*;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
public class threadLocalFact {
	//得到原始线程本地变量的方法
	public ThreadLocal<WebClient> chrome;
	public threadLocalFact() {
		this.chrome=new ThreadLocal<WebClient>();
	}
}
